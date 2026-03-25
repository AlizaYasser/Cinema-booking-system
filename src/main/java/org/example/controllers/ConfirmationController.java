package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.session.SessionManager;

import java.util.List;

public class ConfirmationController {

    @FXML private Label movieLabel;
    @FXML private Label cinemaLabel;
    @FXML private Label seatsLabel;
    @FXML private Label totalLabel;
    @FXML private Label userLabel;

    public void setBookingDetails(String movieName, String cinemaName,
                                  List<String> seats, double total) {
        movieLabel.setText("🎬 Movie:    " + movieName);
        cinemaLabel.setText("🏛️ Cinema:   " + cinemaName);
        seatsLabel.setText("💺 Seats:    " + String.join(", ", seats));
        totalLabel.setText("💰 Total Paid:  $" + String.format("%.2f", total));
        userLabel.setText("👤 Booked by: " + SessionManager.getCurrentUsername());
    }

    @FXML
    private void goToDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Stage stage = (Stage) movieLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void goToMovies() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/movies.fxml"));
            Stage stage = (Stage) movieLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Stage stage = (Stage) movieLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}