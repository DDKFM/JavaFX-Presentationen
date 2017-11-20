package de.ddkfm.css.demo;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ResourceBundle;

public class CSSDemoController implements Initializable{

    @FXML
    private TextArea taCSS;

    @FXML
    private Button btApplyStyle;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btApplyStyle.setOnAction(event -> {
            Scene scene = btApplyStyle.getScene();
            String cssContent = taCSS.getText();
            try {
                Path tmpFile = Files.createTempFile("javafx_css_demo", "css");
                Files.write(tmpFile, cssContent.getBytes());
                scene.getStylesheets().clear();
                scene.getStylesheets().add(tmpFile.toUri().toString());
            } catch (IOException e) {
                e.printStackTrace();
            }
        });
    }
}
