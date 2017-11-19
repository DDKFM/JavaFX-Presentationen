package de.ddkfm.binding;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.stage.Stage;

import javax.swing.*;
import java.time.LocalDate;

public class BindingExample extends Application{
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();
        Scene scene = new Scene(root);

        //create an inital Person named Max Mustermann
        Person person  = new Person("Max Mustermann",
                                    LocalDate.of(1996, 8, 9),
                                    500);

        PersonPane personPane = new PersonPane(person);
        root.setCenter(personPane);

        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
