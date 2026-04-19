package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import org.example.database.MovieDAO;

import java.io.IOException;

public class AddMovieController {

    @FXML private TextField movieNameField;
    @FXML private TextField cinemaIdField;
    @FXML private TextField showtimeField;
    @FXML private TextField priceField;
    @FXML private Label statusLabel;
    @FXML private TextField dateField;

    @FXML
    private void handleAdd() {
        String name = movieNameField.getText().trim();
        String cinemaStr = cinemaIdField.getText().trim();
        String showtime = showtimeField.getText().trim();
        String date = dateField.getText().trim();
        String priceStr = priceField.getText().trim();

        if (name.isEmpty() || cinemaStr.isEmpty() ||
                date.isEmpty() || showtime.isEmpty() || priceStr.isEmpty())  {
            showStatus("All fields are required.", false);
            return;
        }

        int cinemaId;
        double price;

        try {
            cinemaId = Integer.parseInt(cinemaStr);
        } catch (NumberFormatException e) {
            showStatus("Cinema ID must be a number.", false);
            return;
        }

        try {
            price = Double.parseDouble(priceStr);
        } catch (NumberFormatException e) {
            showStatus("Price must be valid.", false);
            return;
        }

        boolean success = MovieDAO.addMovie(name, cinemaId, date, showtime, price);

        if (success) {
            showStatus("Movie added successfully!", true);
            clearFields();
        } else {
            showStatus("Failed to add movie. Check Cinema ID.", false);
        }
    }

    @FXML
    private void handleBack() {
        navigateTo("/views/admin_dashboard.fxml");
    }

    private void showStatus(String msg, boolean ok) {
        statusLabel.setText(msg);
        statusLabel.setStyle(ok
                ? "-fx-text-fill: #4caf82;"
                : "-fx-text-fill: #e05555;");
    }

    private void clearFields() {
        movieNameField.clear();
        cinemaIdField.clear();
        dateField.clear();
        showtimeField.clear();
        priceField.clear();
    }

    private void navigateTo(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) movieNameField.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}