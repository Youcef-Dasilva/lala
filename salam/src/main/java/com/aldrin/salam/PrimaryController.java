package com.aldrin.salam;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;

public class PrimaryController {
    @FXML
    private TextField usernameField;
    @FXML
    private PasswordField passwordField;

    @FXML
    private void handleLogin(ActionEvent event) throws IOException {
        String username = usernameField.getText();
        String password = passwordField.getText();

        String role = authenticate(username, password);
        if (role != null) {
            String fxmlFile = determineFXMLFile(role);
            if (fxmlFile != null) {
                loadFXML(event, fxmlFile);
            } else {
                showAlert("Access denied", "You do not have access to this application.");
            }
        } else {
            showAlert("Login failed", "Invalid username or password. Please try again.");
        }
    }

    @FXML
    private void handleRequestMembership(ActionEvent event) {
        String username = usernameField.getText();
        String password = passwordField.getText();

        if (requestMembership(username, password)) {
            showCustomDialog("Request Sent", "Your membership request has been sent.");
        } else {
            showAlert("Request Failed", "An error occurred while sending your membership request.");
        }
    }

    private boolean requestMembership(String username, String password) {
        Properties properties = loadDBProperties();
        if (properties == null) {
            System.out.println("Failed to load database properties.");
            return false;
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String pass = properties.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "INSERT INTO DemandesAdhesion (username, mot_de_passe) VALUES (?, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    private Properties loadDBProperties() {
        Properties properties = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input == null) {
                System.out.println("Sorry, unable to find database.properties");
                return null;
            }
            properties.load(input);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
        return properties;
    }

    private String authenticate(String username, String password) {
        Properties properties = loadDBProperties();
        if (properties == null) {
            System.out.println("Failed to load database properties.");
            return null;
        }

        String role = null;
        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String pass = properties.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "SELECT role FROM Membres WHERE username = ? AND mot_de_passe = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                role = resultSet.getString("role");
            } else {
                System.out.println("No matching user found.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return role;
    }

    private String determineFXMLFile(String role) {
        switch (role) {
            case "admin":
                return "admin";
            case "employé":
                return "employee";
            case "utilisateur":
                return "user";
            default:
                return null;
        }
    }

    private void loadFXML(ActionEvent event, String fxmlFile) throws IOException {
        try {
            Parent root = FXMLLoader.load(getClass().getResource(fxmlFile + ".fxml"));
            Scene scene = new Scene(root);
            Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Error loading the " + fxmlFile + " file");
        }
    }

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.ERROR);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void showCustomDialog(String title, String message) {
        Dialog<ButtonType> dialog = new Dialog<>();
        dialog.setTitle(title);
        dialog.setResizable(true);

        Label label = new Label(message);
        VBox vbox = new VBox(label);
        vbox.setSpacing(10);

        dialog.getDialogPane().setContent(vbox);
        dialog.getDialogPane().getButtonTypes().add(ButtonType.OK);

        dialog.showAndWait();
    }
}