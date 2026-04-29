package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import org.example.database.BookingDAO;
import org.example.session.SessionManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class DashboardController implements Initializable {


    public static class BookingRow {

        private final int bookingId;
        private final String movie;
        private final String cinema;
        private final String seat;
        private final String showtime;

        public BookingRow(int bookingId, String movie, String cinema, String seat, String showtime) {
            this.bookingId = bookingId;
            this.movie = movie;
            this.cinema = cinema;
            this.seat = seat;
            this.showtime = showtime;
        }

        public int getBookingId() { return bookingId; }
        public String getMovie() { return movie; }
        public String getCinema() { return cinema; }
        public String getSeat() { return seat; }
        public String getShowtime() { return showtime; }
    }

    // FXML
    @FXML private Label welcomeLabel;
    @FXML private Label bookingCountLabel;
    @FXML private Label errorLabel;

    @FXML private TableView<BookingRow> bookingTable;
    @FXML private TableColumn<BookingRow, String> colMovie;
    @FXML private TableColumn<BookingRow, String> colCinema;
    @FXML private TableColumn<BookingRow, String> colSeat;
    @FXML private TableColumn<BookingRow, String> colShowtime;

    @Override
    public void initialize(URL url, ResourceBundle rb) {

        colMovie.setCellValueFactory(new PropertyValueFactory<>("movie"));
        colCinema.setCellValueFactory(new PropertyValueFactory<>("cinema"));
        colSeat.setCellValueFactory(new PropertyValueFactory<>("seat"));
        colShowtime.setCellValueFactory(new PropertyValueFactory<>("showtime"));

        welcomeLabel.setText("Welcome, " + SessionManager.getCurrentUsername());

        loadBookingHistory();
    }

    // ─────────────────────────────────────────────
    private void loadBookingHistory() {

        int userId = SessionManager.getCurrentUserId();

        try {
            List<String[]> history = BookingDAO.getBookingHistory(userId);

            ObservableList<BookingRow> rows = FXCollections.observableArrayList();

            for (String[] row : history) {
                rows.add(new BookingRow(
                        Integer.parseInt(row[0]), // booking_id
                        row[1], // movie
                        row[2], // cinema
                        row[3], // seat
                        row[4]  // showtime
                ));
            }

            bookingTable.setItems(rows);

            bookingCountLabel.setText(
                    rows.isEmpty()
                            ? "You have no bookings yet."
                            : "Total bookings: " + rows.size()
            );

        } catch (Exception e) {
            showError("Failed to load bookings: " + e.getMessage());
        }
    }

    // ─────────────────────────────────────────────
    @FXML
    private void handleCancelBooking() {

        BookingRow selected = bookingTable.getSelectionModel().getSelectedItem();

        if (selected == null) {
            showError("Please select a booking first.");
            return;
        }

        boolean success = BookingDAO.cancelBooking(selected.getBookingId());

        if (success) {
            loadBookingHistory();

            // optional message
            bookingCountLabel.setText("Booking cancelled successfully.");
        } else {
            showError("Failed to cancel booking.");
        }
    }

    // ─────────────────────────────────────────────
    private void showError(String msg) {
        errorLabel.setText("⚠ " + msg);
        errorLabel.setVisible(true);
    }

    // ─────────────────────────────────────────────
    @FXML
    private void goToCinema() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cinema.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));

            // optional: force refresh next screen if needed
        } catch (Exception e) {
            showError(e.getMessage());
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleRefresh() {
        loadBookingHistory();
    }
}