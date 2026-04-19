package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import org.example.booking.BookingManager;
import org.example.database.BookingDAO;
import org.example.session.SessionManager;

import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

public class SeatsController implements Initializable {

    @FXML private GridPane seatGrid;
    @FXML private Label movieTitleLabel;
    @FXML private Label selectedSeatsLabel;
    @FXML private Label totalPriceLabel;
    @FXML private Button confirmBtn;

    private List<String> selectedSeats = new ArrayList<>();
    private List<String> bookedSeats = new ArrayList<>();
    private double pricePerSeat = 12.50;

    private static final int ROWS = 6;
    private static final int COLS = 8;
    private static final String[] ROW_LABELS = {"A","B","C","D","E","F"};

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        int movieId = SessionManager.getCurrentMovieId();
        bookedSeats = BookingDAO.getBookedSeats(movieId);

        if (movieTitleLabel != null) {
            movieTitleLabel.setText(SessionManager.getCurrentMovieName() + " - Select Your Seats");
        }
        buildSeatGrid();
    }

    private void buildSeatGrid() {
        for (int row = 0; row < ROWS; row++) {
            for (int col = 0; col < COLS; col++) {
                String seatId = ROW_LABELS[row] + (col + 1);
                Button seatBtn = new Button(seatId);
                seatBtn.setMinSize(55, 40);
                seatBtn.setMaxSize(55, 40);

                if (bookedSeats.contains(seatId)) {
                    seatBtn.setStyle("-fx-background-color: #555555; -fx-text-fill: #888888; " +
                            "-fx-background-radius: 5; -fx-font-size: 10;");
                    seatBtn.setDisable(true);
                } else {
                    seatBtn.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                            "-fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 10;");
                    seatBtn.setOnAction(e -> toggleSeat(seatBtn, seatId));
                }
                seatGrid.add(seatBtn, col, row);
            }
        }
    }

    private void toggleSeat(Button btn, String seatId) {
        if (selectedSeats.contains(seatId)) {
            selectedSeats.remove(seatId);
            btn.setStyle("-fx-background-color: #16213e; -fx-text-fill: white; " +
                    "-fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 10;");
        } else {
            selectedSeats.add(seatId);
            btn.setStyle("-fx-background-color: #c9a84c; -fx-text-fill: white; " +
                    "-fx-background-radius: 5; -fx-cursor: hand; -fx-font-size: 10;");
        }
        updateSummary();
    }

    private void updateSummary() {
        if (selectedSeats.isEmpty()) {
            selectedSeatsLabel.setText("Selected Seats: None");
            totalPriceLabel.setText("Total: $0.00");
        } else {
            selectedSeatsLabel.setText("Selected Seats: " + String.join(", ", selectedSeats));
            double total = selectedSeats.size() * pricePerSeat;
            totalPriceLabel.setText(String.format("Total: $%.2f", total));
        }
    }

    @FXML
    private void handleConfirm() {
        if (selectedSeats.isEmpty()) {
            showAlert("No Seats Selected", "Please select at least one seat before confirming.");
            return;
        }

        int userId   = SessionManager.getCurrentUserId();
        int movieId  = SessionManager.getCurrentMovieId();
        int cinemaId = SessionManager.getCurrentCinemaId();

        // Delegate to BookingManager – handles validation, double-booking, DB errors
        BookingManager.BookingResult result =
                BookingManager.confirmBooking(userId, movieId, cinemaId, selectedSeats);

        switch (result.status) {
            case SUCCESS:
                goToConfirmation();
                break;

            case SEAT_TAKEN:
                showAlert("Seat Unavailable", result.message);
                // Refresh seat grid so the taken seat shows as disabled
                seatGrid.getChildren().clear();
                selectedSeats.clear();
                updateSummary();
                bookedSeats = BookingDAO.getBookedSeats(movieId);
                buildSeatGrid();
                break;

            case INVALID_INPUT:
                showAlert("Invalid Input", result.message);
                break;

            case DB_ERROR:
                showAlert("Database Error", result.message + "\nPlease try again or restart the app.");
                break;

            default:
                showAlert("Unexpected Error", "Something went wrong. Please try again.");
        }
    }

    private void goToConfirmation() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/confirmation.fxml"));
            Stage stage = (Stage) seatGrid.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));

            ConfirmationController controller = loader.getController();
            controller.setBookingDetails(
                    SessionManager.getCurrentMovieName(),
                    SessionManager.getCurrentCinemaName(),
                    selectedSeats,
                    selectedSeats.size() * pricePerSeat
            );
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    @FXML
    private void goBack() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/movies.fxml"));
            Stage stage = (Stage) seatGrid.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void refreshSeats() {
        int movieId = SessionManager.getCurrentMovieId();

        selectedSeats.clear();
        seatGrid.getChildren().clear();

        bookedSeats = BookingDAO.getBookedSeats(movieId);

        updateSummary();
        buildSeatGrid();
    }
}