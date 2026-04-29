package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.database.MovieDAO;

import java.io.IOException;
import java.util.List;

public class DeleteMovieController {

    @FXML private ListView<String> movieListView;
    @FXML private Label statusLabel;

    private List<String[]> movieData;

    @FXML
    public void initialize() {
        loadMovies();
    }

    private void loadMovies() {
        movieListView.getItems().clear();

        movieData = MovieDAO.getAllMovies();

        if (movieData == null || movieData.isEmpty()) {
            movieListView.getItems().add("No movies found.");
            return;
        }

        for (String[] row : movieData) {
            String display = row[1] + " | " + row[2] + " | " + row[3] + " | $" + row[4];
            movieListView.getItems().add(display);
        }
    }

    @FXML
    private void handleDelete() {
        int selectedIndex = movieListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex < 0 || movieData == null || movieData.isEmpty()) {
            showStatus("Please select a movie first.", false);
            return;
        }

        String[] selected = movieData.get(selectedIndex);
        int movieId = Integer.parseInt(selected[0]);
        String movieName = selected[1];

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/confirm_delete.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));

            ConfirmDeleteController controller = loader.getController();

            controller.setData(movieId, movieName, this::loadMovies);

            stage.setTitle("Confirm Delete");
            stage.show();

        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleBack() {
        navigate("/views/admin_dashboard.fxml");
    }

    private void showStatus(String msg, boolean ok) {
        statusLabel.setText(msg);
        statusLabel.setStyle(ok
                ? "-fx-text-fill: #4caf82;"
                : "-fx-text-fill: #e05555;");
    }

    private void navigate(String path) {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(path));
            Scene scene = new Scene(loader.load());
            Stage stage = (Stage) movieListView.getScene().getWindow();
            stage.setScene(scene);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}