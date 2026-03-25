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

/**
 * DashboardController – Person 3
 * Shows the logged-in user's full booking history fetched from the database.
 * Provides navigation back to the cinema selection screen or logout.
 */
public class DashboardController implements Initializable {

    // ------------------------------------------------------------------ //
    //  TableView row model
    // ------------------------------------------------------------------ //
    public static class BookingRow {
        private final String movie;
        private final String cinema;
        private final String seat;
        private final String showtime;

        public BookingRow(String movie, String cinema, String seat, String showtime) {
            this.movie    = movie;
            this.cinema   = cinema;
            this.seat     = seat;
            this.showtime = showtime;
        }

        public String getMovie()    { return movie; }
        public String getCinema()   { return cinema; }
        public String getSeat()     { return seat; }
        public String getShowtime() { return showtime; }
    }

    // ------------------------------------------------------------------ //
    //  FXML bindings
    // ------------------------------------------------------------------ //
    @FXML private Label welcomeLabel;
    @FXML private Label bookingCountLabel;
    @FXML private Label errorLabel;

    @FXML private TableView<BookingRow>       bookingTable;
    @FXML private TableColumn<BookingRow, String> colMovie;
    @FXML private TableColumn<BookingRow, String> colCinema;
    @FXML private TableColumn<BookingRow, String> colSeat;
    @FXML private TableColumn<BookingRow, String> colShowtime;

    // ------------------------------------------------------------------ //
    //  Initialise
    // ------------------------------------------------------------------ //
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        // Wire columns to BookingRow properties
        colMovie.setCellValueFactory(new PropertyValueFactory<>("movie"));
        colCinema.setCellValueFactory(new PropertyValueFactory<>("cinema"));
        colSeat.setCellValueFactory(new PropertyValueFactory<>("seat"));
        colShowtime.setCellValueFactory(new PropertyValueFactory<>("showtime"));

        // Welcome message
        String username = SessionManager.getCurrentUsername();
        welcomeLabel.setText("Welcome, " + username + "!");

        // Load and display booking history
        loadBookingHistory();
    }

    // ------------------------------------------------------------------ //
    //  Load history from DB with error handling
    // ------------------------------------------------------------------ //
    private void loadBookingHistory() {
        int userId = SessionManager.getCurrentUserId();

        if (userId <= 0) {
            showError("Session error: user not found. Please log in again.");
            return;
        }

        try {
            List<String[]> history = BookingDAO.getBookingHistory(userId);

            if (history == null) {
                showError("Could not retrieve booking history. Database may be unavailable.");
                return;
            }

            ObservableList<BookingRow> rows = FXCollections.observableArrayList();

            for (String[] row : history) {
                // row = [movie_name, cinema_name, seat_number, showtime]
                if (row.length < 4) continue;
                rows.add(new BookingRow(row[0], row[1], row[2], row[3]));
            }

            bookingTable.setItems(rows);

            int count = rows.size();
            bookingCountLabel.setText(
                    count == 0
                    ? "You have no bookings yet."
                    : "Total bookings: " + count);

            errorLabel.setVisible(false);

        } catch (Exception e) {
            System.err.println("[DashboardController] Error loading history: " + e.getMessage());
            showError("Failed to load booking history: " + e.getMessage());
        }
    }

    private void showError(String msg) {
        errorLabel.setText("⚠ " + msg);
        errorLabel.setVisible(true);
        bookingCountLabel.setText("Could not load bookings.");
    }

    // ------------------------------------------------------------------ //
    //  Navigation
    // ------------------------------------------------------------------ //
    @FXML
    private void goToCinema() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cinema.fxml"));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            showError("Navigation error: " + e.getMessage());
            e.printStackTrace();
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
