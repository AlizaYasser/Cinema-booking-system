package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.stage.Stage;
import org.example.database.MovieDAO;

public class ConfirmDeleteController {

    @FXML private Label messageLabel;

    private int movieId;
    private Runnable onSuccess; // callback to refresh list

    // called from DeleteMovieController
    public void setData(int movieId, String movieName, Runnable onSuccess) {
        this.movieId = movieId;
        this.onSuccess = onSuccess;
        messageLabel.setText("Are you sure you want to delete:\n\"" + movieName + "\"?");
    }

    @FXML
    private void handleDelete() {
        boolean success = MovieDAO.deleteMovie(movieId);

        if (success && onSuccess != null) {
            onSuccess.run(); // refresh list in parent
        }

        close();
    }

    @FXML
    private void handleCancel() {
        close();
    }

    private void close() {
        Stage stage = (Stage) messageLabel.getScene().getWindow();
        stage.close();
    }
}