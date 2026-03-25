package org.example.controllers;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import org.example.database.UserDAO;

public class RegisterController {

    @FXML private TextField fullNameField;
    @FXML private TextField emailField;
    @FXML private TextField usernameField;
    @FXML private PasswordField passwordField;
    @FXML private PasswordField confirmPasswordField;
    @FXML private Label messageLabel;

    @FXML
    private void handleRegister() {
        String fullName        = fullNameField.getText().trim();
        String email           = emailField.getText().trim();
        String username        = usernameField.getText().trim();
        String password        = passwordField.getText().trim();
        String confirmPassword = confirmPasswordField.getText().trim();

        if (fullName.isEmpty() || email.isEmpty() || username.isEmpty() || password.isEmpty()) {
            messageLabel.setText("Please fill in all fields.");
            messageLabel.setStyle("-fx-text-fill: #c9a84c;");
            return;
        }

        if (!email.contains("@")) {
            messageLabel.setText("Please enter a valid email.");
            messageLabel.setStyle("-fx-text-fill: #c9a84c;");
            return;
        }

        if (password.length() < 6) {
            messageLabel.setText("Password must be at least 6 characters.");
            messageLabel.setStyle("-fx-text-fill: #c9a84c;");
            return;
        }

        if (!password.equals(confirmPassword)) {
            messageLabel.setText("Passwords do not match.");
            messageLabel.setStyle("-fx-text-fill: #c9a84c;");
            return;
        }

        // Check if username already exists
        if (UserDAO.usernameExists(username)) {
            messageLabel.setText("Username already taken. Choose another.");
            messageLabel.setStyle("-fx-text-fill: #c9a84c;");
            return;
        }

        // Save to database
        boolean success = UserDAO.registerUser(fullName, email, username, password);

        if (success) {
            messageLabel.setStyle("-fx-text-fill: #00ff88;");
            messageLabel.setText("Account created! Redirecting to login...");

            new Thread(() -> {
                try {
                    Thread.sleep(1500);
                    javafx.application.Platform.runLater(this::goToLogin);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }).start();
        } else {
            messageLabel.setText("Registration failed. Email may already be in use.");
            messageLabel.setStyle("-fx-text-fill: #c9a84c;");
        }
    }

    @FXML
    private void goToLogin() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/views/login.fxml"));
            Stage stage = (Stage) usernameField.getScene().getWindow();
            stage.setScene(new Scene(loader.load(), 800, 600));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}