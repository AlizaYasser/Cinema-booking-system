package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.media.*;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class TitleController implements Initializable {

    @FXML private MediaView mediaView;
    @FXML private Button continueBtn;

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        try {
            URL videoUrl = getClass().getResource("/media/intro.mp4");
            Media media = new Media(videoUrl.toExternalForm());
            MediaPlayer player = new MediaPlayer(media);
            mediaView.setMediaPlayer(player);
            player.setCycleCount(MediaPlayer.INDEFINITE); // loops
            player.play();
        } catch (Exception e) {
            System.out.println("Video not found, skipping.");
        }

        // Show continue button after 2 seconds
        new Thread(() -> {
            try {
                Thread.sleep(2000);
                javafx.application.Platform.runLater(() -> continueBtn.setVisible(true));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }).start();
    }

    @FXML
    private void handleContinue() {
        try {
            // Stop video
            if (mediaView.getMediaPlayer() != null) {
                mediaView.getMediaPlayer().stop();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Stage stage = (Stage) continueBtn.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}