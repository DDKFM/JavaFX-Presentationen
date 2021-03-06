package de.ddkfm.fancy.stuff;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class FancyExample extends Application {

    private Map<Integer, FancyPane> panes = new TreeMap<>();
    private int currentIndex = 0;
    private boolean withReflection = false;
    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        MenuBar menuBar = new MenuBar();
        Menu menu = new Menu("Anzeige");
        MenuItem miFullscreen = new MenuItem("Vollbild starten");
        miFullscreen.setOnAction(event -> {
            if(primaryStage.isFullScreen()) {
                primaryStage.setFullScreen(false);
            } else {
                primaryStage.setFullScreen(true);
                miFullscreen.setText("Vollbild beenden");
            }
        });
        miFullscreen.setAccelerator(new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN));

        MenuItem miReflection = new MenuItem("Mit Reflection-Effekt");
        miReflection.setOnAction(event -> {
            if(withReflection)
                this.panes.get(currentIndex).removeReflection();
            else
                this.panes.get(currentIndex).setReflection();
            withReflection = !withReflection;
        });

        menu.getItems().addAll(miFullscreen, miReflection);
        menuBar.getMenus().add(menu);
        root.setTop(menuBar);
        FancyPane pane = new FancyPane();
        panes.put(0, pane);
        root.setCenter(pane);

        root.setOnSwipeRight(event -> {
            if(currentIndex == panes.size() - 1) {
                FancyPane newPane = new FancyPane();
                panes.put(currentIndex + 1, newPane);
                currentIndex++;
                root.setCenter(newPane);
            } else {
                currentIndex++;
                root.setCenter(panes.get(currentIndex));
            }
        });
        root.setOnSwipeLeft(event -> {
            if(currentIndex > 0) {
                currentIndex--;
                root.setCenter(panes.get(currentIndex));
            }
        });
        Scene scene = new Scene(root, 500, 500);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    public static void main(String[] args) {
        launch(args);
    }
}
