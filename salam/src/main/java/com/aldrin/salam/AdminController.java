/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.aldrin.salam;

import com.aldrin.salam.model.DemandesAdhesion;


/**
 *
 * @author YOGA
 */

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.IOException;
import java.io.InputStream;
import java.sql.*;
import java.util.Properties;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

public class AdminController {
    @FXML
    private TableView<DemandesAdhesion> membershipRequestsTable;
    @FXML
    private TableColumn<DemandesAdhesion, Integer> idColumn;
    @FXML
    private TableColumn<DemandesAdhesion, String> nomColumn;
    @FXML
    private TableColumn<DemandesAdhesion, String> prenomColumn;
    @FXML
    private TableColumn<DemandesAdhesion, String> usernameColumn;
    @FXML
    private TableColumn<DemandesAdhesion, Date> dateDemandeColumn;

    private ObservableList<DemandesAdhesion> membershipRequestsData;
    @FXML
    private TextField nomField;
    @FXML
    private TextField prenomField;
    @FXML
    private TextField usernameField;
    @FXML
    private TextField passwordField;
    @FXML
    private TextField roleField;

    @FXML
    private void initialize() {
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id_demande"));
        nomColumn.setCellValueFactory(new PropertyValueFactory<>("nom"));
        prenomColumn.setCellValueFactory(new PropertyValueFactory<>("prenom"));
        usernameColumn.setCellValueFactory(new PropertyValueFactory<>("username"));
        dateDemandeColumn.setCellValueFactory(new PropertyValueFactory<>("date_demande"));

        membershipRequestsData = FXCollections.observableArrayList();
        membershipRequestsTable.setItems(membershipRequestsData);

        loadMembershipRequests();
    }
    @FXML
    private void handleAddMember(ActionEvent event) {
        String nom = nomField.getText();
        String prenom = prenomField.getText();
        String username = usernameField.getText();
        String password = passwordField.getText();
        String role = roleField.getText();

        if (addMember(nom, prenom, username, password, role)) {
            showAlert("Member Added", "The member has been added successfully.");
        } else {
            showAlert("Addition Failed", "An error occurred while adding the member.");
        }
    }

    @FXML
    private void handleDeleteMember(ActionEvent event) {
        String username = usernameField.getText();

        if (deleteMember(username)) {
            showAlert("Member Deleted", "The member has been deleted successfully.");
        } else {
            showAlert("Deletion Failed", "An error occurred while deleting the member.");
        }
    }

    private boolean addMember(String nom, String prenom, String username, String password, String role) {
        Properties properties = loadDBProperties();
        if (properties == null) {
            System.out.println("Failed to load database properties.");
            return false;
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String pass = properties.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "INSERT INTO Membres (nom, prenom, username, mot_de_passe, date_adhesion, role) VALUES (?, ?, ?, ?, CURRENT_DATE, ?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, nom);
            statement.setString(2, prenom);
            statement.setString(3, username);
            statement.setString(4, password);
            statement.setString(5, role);
System.out.println("Inserting member: " + nom + ", " + prenom + ", " + username + ", " + password + ", " + role); // Log value
            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }

    private boolean deleteMember(String username) {
        Properties properties = loadDBProperties();
        if (properties == null) {
            System.out.println("Failed to load database properties.");
            return false;
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String pass = properties.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = "DELETE FROM Membres WHERE username = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, username);

            int rowsAffected = statement.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    
    
    
    
    
    
private void loadMembershipRequests() {
    Properties properties = loadDBProperties();
    if (properties == null) {
        System.out.println("Failed to load database properties.");
        return;
    }

    String url = properties.getProperty("db.url");
    String user = properties.getProperty("db.username");
    String pass = properties.getProperty("db.password");

    try (Connection conn = DriverManager.getConnection(url, user, pass)) {
        String query = "SELECT * FROM DemandesAdhesion";
        Statement statement = conn.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        while (resultSet.next()) {
            int id = resultSet.getInt("id_demande");
            String nom = resultSet.getString("nom");
            String prenom = resultSet.getString("prenom");
            String username = resultSet.getString("username");
            Date dateDemande = resultSet.getDate("date_demande");

            DemandesAdhesion adhesion = new DemandesAdhesion(id, nom, prenom, username, dateDemande);
            System.out.println("Loaded adhesion: " + adhesion); // Debugging
            membershipRequestsData.add(adhesion);
        }
    } catch (SQLException e) {
        e.printStackTrace();
    }
}
    @FXML
    private void handleApproveRequest(ActionEvent event) {
        DemandesAdhesion selectedRequest = membershipRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            if (processMembershipRequest(selectedRequest.getId_demande(), true)) {
                membershipRequestsData.remove(selectedRequest);
                showAlert("Request Approved", "The membership request has been approved.");
            } else {
                showAlert("Approval Failed", "An error occurred while approving the request.");
            }
        } else {
            showAlert("No Selection", "Please select a request to approve.");
        }
    }

    @FXML
    private void handleRejectRequest(ActionEvent event) {
        DemandesAdhesion selectedRequest = membershipRequestsTable.getSelectionModel().getSelectedItem();
        if (selectedRequest != null) {
            if (processMembershipRequest(selectedRequest.getId_demande(), false)) {
                membershipRequestsData.remove(selectedRequest);
                showAlert("Request Rejected", "The membership request has been rejected.");
            } else {
                showAlert("Rejection Failed", "An error occurred while rejecting the request.");
            }
        } else {
            showAlert("No Selection", "Please select a request to reject.");
        }
    }

    private boolean processMembershipRequest(int requestId, boolean approve) {
        Properties properties = loadDBProperties();
        if (properties == null) {
            System.out.println("Failed to load database properties.");
            return false;
        }

        String url = properties.getProperty("db.url");
        String user = properties.getProperty("db.username");
        String pass = properties.getProperty("db.password");

        try (Connection conn = DriverManager.getConnection(url, user, pass)) {
            String query = approve ? "INSERT INTO Membres (username, mot_de_passe, role) SELECT username, mot_de_passe, 'user' FROM DemandesAdhesion WHERE id_demande = ?" : "DELETE FROM DemandesAdhesion WHERE id_demande = ?";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setInt(1, requestId);

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

    private void showAlert(String title, String message) {
        Alert alert = new Alert(AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    
    @FXML
private void switchToPrimary(ActionEvent event) throws IOException {
    Parent root = FXMLLoader.load(getClass().getResource("primary.fxml"));
    Scene scene = new Scene(root);
    Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
    stage.setScene(scene);
    stage.show();
}
}