package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;
import org.example.database.UserDAO;
import org.example.session.SessionManager;

import java.net.URL;
import java.util.ResourceBundle;

public class LoginController implements Initializable {

    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private Label errorLabel;
    @FXML private ImageView logoImageView;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        // Load the logo PNG from the resources folder
        try {
            Image logo = new Image(getClass().getResourceAsStream("/images/logo.png"));
            logoImageView.setImage(logo);
        } catch (Exception e) {
            System.err.println("Error loading logo: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        String username = usernameField.getText().trim();
        String password = passwordField.getText().trim();

        if (username.isEmpty() || password.isEmpty()) {
            errorLabel.setText("Please enter username and password.");
            return;
        }

        int userId = UserDAO.loginUser(username, password);

        if (userId != -1) {

            SessionManager.setCurrentUserId(userId);
            SessionManager.setCurrentUsername(username);

            String role = UserDAO.getUserRole(userId);

            if (role.equalsIgnoreCase("ADMIN")) {
                goToAdminDashboard();
            } else {
                goToMovies();
            }

        } else {
            errorLabel.setText("Invalid username or password.");
        }
    }

    @FXML
    private void goToRegister() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/register.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToMovies() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/cinema.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void goToAdminDashboard() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/admin_dashboard.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 900, 650));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}