package de.ddkfm.demoapp;

import com.aquafx_project.demo.JavaFXDialog;
import javafx.application.Application;
import javafx.embed.swing.SwingNode;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.swing.*;

public class HelloWorld extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        Button button = new Button("Hello World");
        button.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Hallo Welt");
            alert.showAndWait();
        });
        root.setCenter(button);
        /*
        SwingNode swingNode = new SwingNode();
        JButton jButton = new JButton("JButton");
        swingNode.setContent(jButton);
        root.setBottom(swingNode);
        */
        primaryStage.setTitle("Hello World");
        Scene scene = new Scene(root, 400, 400);
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
