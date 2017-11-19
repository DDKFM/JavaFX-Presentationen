package de.ddkfm.demoapp;

import com.aquafx_project.AquaFx;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.BorderPane;
import javafx.scene.web.HTMLEditor;
import org.aerofx.AeroFX;

import java.net.URL;
import java.time.LocalDate;
import java.util.ResourceBundle;

public class DesignController implements Initializable {

    @FXML
    private Button btGeneralComponents;

    @FXML
    private ComboBox<String> cbGeneralComponents;

    @FXML
    private HTMLEditor htmlEdit;

    @FXML
    private TextArea taGeneralComponents;

    @FXML
    private TextField textPandB1;

    @FXML
    private Label lblPandB;

    @FXML
    private TextField textPandB2;

    @FXML
    private ProgressBar pbPandB;

    @FXML
    private ProgressIndicator piPandB;

    @FXML
    private RadioButton rbIsWinterTime;

    @FXML
    private CheckBox cbIsLeapJear;

    @FXML
    private DatePicker dpDate;

    @FXML
    private Slider slider;

    @FXML
    private BorderPane borderPane;

    @FXML
    private TabPane tabPane;

    @FXML
    private ComboBox<String> cbStylesheets;

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        btGeneralComponents.setOnAction(event -> {
            cbGeneralComponents.getItems().addAll(
                    "JavaFX",
                    "ist",
                    "toll",
                    "",
                    "",
                    "Immer besser als Swing"
            );
            cbGeneralComponents.getSelectionModel().select(0);
        });
        cbStylesheets.getItems().addAll(
                "Default",
                "AeroFX",
                "AquaFX",
                "Bootstrap"
        );

        dpDate.valueProperty().addListener(new ChangeListener<LocalDate>() {
            @Override
            public void changed(ObservableValue<? extends LocalDate> observable, LocalDate oldValue, LocalDate newValue) {
                cbIsLeapJear.setSelected(newValue.isLeapYear());
                int selectedYear = newValue.getYear();
                boolean isWinterTime = newValue.isAfter(LocalDate.of(selectedYear, 10, 29));
                isWinterTime &= newValue.isBefore(LocalDate.of(selectedYear + 1, 3, 25));
                rbIsWinterTime.setSelected(isWinterTime);
            }
        });
        dpDate.valueProperty().addListener((observable, oldValue, newValue) -> {
            cbIsLeapJear.setSelected(newValue.isLeapYear());
            rbIsWinterTime.setSelected(
                    newValue.isAfter(LocalDate.of(newValue.getYear(), 10, 29)) &&
                    newValue.isBefore(LocalDate.of(newValue.getYear() + 1, 3, 25))
            );
        });

        htmlEdit.setOnKeyReleased((event) -> taGeneralComponents.setText(htmlEdit.getHtmlText()));
        taGeneralComponents.textProperty().addListener(((observable, oldValue, newValue) -> {
            htmlEdit.setHtmlText(newValue);
        }));

        lblPandB.textProperty().bind(textPandB1.textProperty());


        textPandB2.textProperty().addListener((observable, oldValue, newValue) -> {
            System.out.println(newValue);
            int intValue = 0;
            try {
                intValue = Integer.parseInt(newValue);
            } catch (NumberFormatException e) {

            }
            System.out.println("intValue: " + intValue);
            pbPandB.setProgress(intValue / 100f);
            piPandB.setProgress(1 - intValue / 100f);
        });

        lblPandB.scaleXProperty().bind(slider.valueProperty().divide(100));
        lblPandB.scaleYProperty().bind(slider.valueProperty().divide(100));
        textPandB1.scaleXProperty().bind(slider.valueProperty().divide(100));
        textPandB2.scaleXProperty().bind(slider.valueProperty().divide(100));
        pbPandB.scaleXProperty().bind(slider.valueProperty().divide(100));
        piPandB.scaleXProperty().bind(slider.valueProperty().divide(100));

        textPandB1.scaleYProperty().bind(slider.valueProperty().divide(100));
        textPandB2.scaleYProperty().bind(slider.valueProperty().divide(100));
        pbPandB.scaleYProperty().bind(slider.valueProperty().divide(100));
        piPandB.scaleYProperty().bind(slider.valueProperty().divide(100));

        Scene scene = borderPane.getScene();
        cbStylesheets.getSelectionModel().selectedItemProperty().addListener(((observable, oldValue, newValue) -> {
            switch(newValue.toLowerCase()) {
                case "default":
                    borderPane.getStylesheets().clear();
                    break;
                case "aerofx":
                    AeroFX.style();
                    break;
                case "aquafx":
                    AquaFx.style();
                    break;
                case "bootstrap":
                    borderPane.getStylesheets().add(getClass().getResource("bootstrap3.css").toExternalForm());
                    break;
            }
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Das ist eine JavaFX-Meldung");
            alert.setHeaderText("Du hast das Thema " + newValue + " gewählt");
            alert.setContentText("Ich akzeptiere die damit verbundenen Ansprüche auf Schmerzensgeld");
            alert.showAndWait();
        }));
    }
}

