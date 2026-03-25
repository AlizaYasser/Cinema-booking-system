package org.example.controllers;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.ListView;
import javafx.stage.Stage;
import org.example.database.CinemaDAO;
import org.example.session.SessionManager;

import java.util.List;

public class CinemaController {

    @FXML private ListView<String> cinemaListView;

    // Store cinema data so we can get the ID later
    private List<String[]> cinemaData;

    @FXML
    public void initialize() {
        cinemaData = CinemaDAO.getAllCinemas();
        ObservableList<String> cinemaNames = FXCollections.observableArrayList();

        for (String[] cinema : cinemaData) {
            // Show "CinemaName - Location"
            cinemaNames.add(cinema[1] + " - " + cinema[2]);
        }
        cinemaListView.setItems(cinemaNames);

    }

    @FXML
    private void handleCinemaSelect() {
        int selectedIndex = cinemaListView.getSelectionModel().getSelectedIndex();

        if (selectedIndex == -1) return; // nothing selected

        String[] selectedCinema = cinemaData.get(selectedIndex);
        int cinemaId = Integer.parseInt(selectedCinema[0]);
        String cinemaName = selectedCinema[1];

        // Save to session so movies screen knows which cinema
        SessionManager.setCurrentCinemaId(cinemaId);
        SessionManager.setCurrentCinemaName(cinemaName);

        goToMovies();
    }

    private void goToMovies() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/movies.fxml"));
            Stage stage = (Stage) cinemaListView.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}