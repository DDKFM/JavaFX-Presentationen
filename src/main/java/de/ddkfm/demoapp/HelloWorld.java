package de.ddkfm.demoapp;

import com.aquafx_project.demo.JavaFXDialog;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

public class HelloWorld extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        Button button = new Button("Hello World");
        button.setOnAction(event -> {//new cool java lamdas (Java 8, 2014)
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setContentText("Hallo Welt");
            alert.showAndWait();
        });
        root.setCenter(button);

        primaryStage.setTitle("Hello World");
        primaryStage.setScene(new Scene(root, 400, 400));
        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
