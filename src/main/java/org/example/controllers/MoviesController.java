package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.example.database.MovieDAO;
import org.example.session.SessionManager;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;

public class MoviesController implements Initializable {

    @FXML private GridPane moviesGrid;
    @FXML private Label welcomeLabel;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        if (welcomeLabel != null) {
            welcomeLabel.setText("Movies at " + SessionManager.getCurrentCinemaName());
        }
        loadMoviesFromDB();
    }

    private void loadMoviesFromDB() {
        int cinemaId = SessionManager.getCurrentCinemaId();
        List<String[]> movies = MovieDAO.getMoviesByCinema(cinemaId);

        moviesGrid.getChildren().clear();
        int col = 0, row = 0;

        if (movies.isEmpty()) {
            Label noMovies = new Label("No movies available for this cinema.");
            noMovies.setStyle("-fx-text-fill: white; -fx-font-size: 16;");
            moviesGrid.add(noMovies, 0, 0);
            return;
        }

        for (String[] movie : movies) {
            VBox card = createMovieCard(movie);
            moviesGrid.add(card, col, row);
            col++;
            if (col == 3) { col = 0; row++; }
        }
    }

    private VBox createMovieCard(String[] movie) {
        // movie[0]=id, movie[1]=name, movie[2]=date, movie[3]=time, movie[4]=price
        VBox card = new VBox(8);
        card.setAlignment(Pos.TOP_CENTER);
        card.setPrefWidth(210);
        card.setPadding(new Insets(15));
        card.setStyle("-fx-background-color: #0e1017; -fx-cursor: hand; " +
                "-fx-border-color: #2a2d3a; -fx-border-width: 1;");

        Label title = new Label(movie[1]);
        title.setStyle("-fx-font-size: 14; -fx-text-fill: #ffffff; -fx-font-weight: bold;");
        title.setWrapText(true);

        Label showtime = new Label(movie[2] + " | " + movie[3]);
        showtime.setStyle("-fx-font-size: 11; -fx-text-fill: #a0a8c0;");

        Label price = new Label("$ " + movie[4]);
        price.setStyle("-fx-font-size: 11; -fx-text-fill: #c9a84c;");

        Button bookBtn = new Button("BOOK SEATS");
        bookBtn.setPrefWidth(180);
        bookBtn.setStyle("-fx-background-color: #c9a84c; -fx-text-fill: #111318; " +
                "-fx-font-size: 11; -fx-font-weight: bold; -fx-padding: 9 0; " +
                "-fx-cursor: hand; -fx-background-radius: 0;");
        bookBtn.setOnAction(e -> goToSeats(movie));

        card.getChildren().addAll(title, showtime, price, bookBtn);

        card.setOnMouseEntered(e -> card.setStyle("-fx-background-color: #0e1017; -fx-cursor: hand; " +
                "-fx-border-color: #c9a84c; -fx-border-width: 1; -fx-padding: 15;"));
        card.setOnMouseExited(e -> card.setStyle("-fx-background-color: #0e1017; -fx-cursor: hand; " +
                "-fx-border-color: #2a2d3a; -fx-border-width: 1; -fx-padding: 15;"));

        return card;
    }

    private void goToSeats(String[] movie) {
        // Save selected movie to session
        SessionManager.setCurrentMovieId(Integer.parseInt(movie[0]));
        SessionManager.setCurrentMovieName(movie[1]);

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/seats.fxml"));
            Stage stage = (Stage) moviesGrid.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleMyBookings() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/dashboard.fxml"));
            Stage stage = (Stage) moviesGrid.getScene().getWindow();
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
            Stage stage = (Stage) moviesGrid.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}