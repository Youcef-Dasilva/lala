package com.aldrin.salam;

import java.io.IOException;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.stage.Stage;

public class SecondaryController {

    @FXML
    private Button myButton;
    @FXML
    private ImageView imageView;

    @FXML
    private void switchToPrimary() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("primary.fxml"));
        Parent root = loader.load();
        Stage stage = (Stage) myButton.getScene().getWindow();
        stage.setScene(new Scene(root));
    }

    public void initialize() {
        // Optionally set the image programmatically
        Image image = new Image(getClass().getResourceAsStream("icons8-flèche-gauche-100.png"));
        imageView.setImage(image);
        myButton.setGraphic(imageView);
        myButton.setStyle("-fx-background-color: transparent;"); // Optional: makes button background transparent
    }
}