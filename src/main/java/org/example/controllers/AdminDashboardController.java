package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.input.MouseEvent; // Added for VBox clicks
import javafx.scene.layout.HBox;
import javafx.scene.layout.Region;
import javafx.stage.Stage;
import org.example.session.SessionManager;
import org.example.database.BookingDAO;

import javafx.scene.layout.VBox;
import javafx.scene.control.Label;
import javafx.geometry.Insets;
import org.example.database.MovieDAO;
import java.util.List;

public class AdminDashboardController {

    @FXML private Label welcomeLabel;
    @FXML private Label totalBookingsLabel;
    @FXML private Label totalRevenueLabel;
    @FXML private VBox recentMoviesBox;

    @FXML
    public void initialize() {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Admin: " + SessionManager.getCurrentUsername());
        }

        int bookings = BookingDAO.getTotalBookings();
        double revenue = BookingDAO.getTotalRevenue();

        totalBookingsLabel.setText(String.valueOf(bookings));
        totalRevenueLabel.setText("$ " + String.format("%.2f", revenue));

        loadRecentMovies();
    }

    private void loadRecentMovies() {
        recentMoviesBox.getChildren().clear();

        List<String[]> movies = MovieDAO.getRecentMovies(5);

        if (movies.isEmpty()) {
            Label empty = new Label("No movies added yet");
            empty.setStyle("-fx-text-fill: #888;");
            recentMoviesBox.getChildren().add(empty);
            return;
        }

        for (String[] m : movies) {

            // m[0]=name, m[1]=showtime, m[2]=price
            HBox row = new HBox(20);
            row.setAlignment(javafx.geometry.Pos.CENTER_LEFT);
            row.setPadding(new Insets(10));
            row.setStyle("-fx-border-color: #2a2d3a; -fx-border-width: 0 0 1 0;");

            Label name = new Label(m[0]);
            name.setStyle("-fx-text-fill: white;");
            name.setPrefWidth(200);

            Label showtime = new Label(m[1]);
            showtime.setStyle("-fx-text-fill: #c9a84c;");

            Label price = new Label("$" + m[2]);
            price.setStyle("-fx-text-fill: #888;");

            Region spacer = new Region();
            HBox.setHgrow(spacer, javafx.scene.layout.Priority.ALWAYS);

            row.getChildren().addAll(name, showtime, spacer, price);

            recentMoviesBox.getChildren().add(row);
        }
    }

    @FXML
    private void handleAddMovie() {
        navigateTo("/views/add_movie.fxml");
    }

    @FXML
    private void handleDeleteMovie() {
        navigateTo("/views/delete_movie.fxml");
    }

    @FXML
    private void handleLogout() {
        SessionManager.logout();
        navigateTo("/views/login.fxml");
    }

    private void navigateTo(String fxmlPath) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(fxmlPath));
            Stage stage = (Stage) welcomeLabel.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            System.err.println("Error navigating to: " + fxmlPath);
            e.printStackTrace();
        }
    }
}