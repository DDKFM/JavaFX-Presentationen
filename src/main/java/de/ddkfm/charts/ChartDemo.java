package de.ddkfm.charts;

import com.aquafx_project.AquaFx;
import javafx.application.Application;
import javafx.application.Platform;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.json.*;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

public class ChartDemo extends Application {

    private Map<String, Double> fetchedData = new TreeMap<>();
    private StringProperty lastRefresh = new SimpleStringProperty();

    private PieChart pieChart = new PieChart();
    private CategoryAxis xAxis = new CategoryAxis();
    private NumberAxis yAxis = new NumberAxis();
    private BarChart<String,Number> barChart = new BarChart<String,Number>(xAxis,yAxis);

    @Override
    public void start(Stage primaryStage) throws Exception {
        BorderPane root = new BorderPane();

        BorderPane buttonPane = new BorderPane();
        Button buttonBarChart = new Button("Show Barchart");
        buttonBarChart.setOnAction(event -> root.setCenter(barChart));
        Button buttonPieChart = new Button("Show PieChart");
        buttonPieChart.setOnAction(event -> root.setCenter(pieChart));

        buttonPane.setLeft(buttonBarChart);
        buttonPane.setRight(buttonPieChart);
        root.setBottom(buttonPane);

        fetchedData = fetchData();

        Timer timer = new Timer(true);
        TimerTask task = new TimerTask() {
            @Override
            public void run() {
                fetchedData = fetchData();
            }
        };
        timer.schedule(task, 0, 10000);

        root.setCenter(barChart);

        Label lbl = new Label("Letzte Aktualisierung: ");
        lbl.textProperty().bind(this.lastRefresh);
        root.setTop(lbl);

        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void changeData(PieChart pieChart) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();
        fetchedData.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toList())
                .forEach((entry) -> data.add(new PieChart.Data(entry.getKey(), entry.getValue())));
        pieChart.setData(data);
    }
    private void changeData(BarChart<String, Number> pieChart) {
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        fetchedData.entrySet()
                .stream()
                .sorted(Map.Entry.comparingByValue(Collections.reverseOrder()))
                .collect(Collectors.toList())
                .forEach((entry) -> series1.getData().add(new XYChart.Data<>(entry.getKey(), entry.getValue())));
        pieChart.getData().clear();
        pieChart.getData().add(series1);
    }
    private Map<String, Double> fetchData() {
        String accessToken = "548de33a5fb666f3f3dd737a4f065ff9ae829332";
        LocalDate now = LocalDate.now();
        String dateString = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
        String query = "created:>=" + dateString;
        InputStream inputStream = null;
        try {
            URL url = new URL("https://api.github.com/search/repositories" +
                    "?access_token=" + accessToken +
                    "&q=" + query +
                    "&per_page=100" +
                    "&sort=updated" +
                    "&order=desc" +
                    "&fork=false");
            URLConnection conn = url.openConnection();
            inputStream = conn.getInputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        JsonReader reader = Json.createReader(inputStream);
        JsonObject response = reader.readObject();
        JsonArray items = response.getJsonArray("items");
        Map<String, Double> languages = new TreeMap<>();
        for(int i = 0 ; i < items.size() ; i++) {
            JsonObject item = items.getJsonObject(i);
            if(item.containsKey("language")) {
                try {
                    String language = item.get("language").toString();
                    if (languages.containsKey(language))
                        languages.put(language, languages.get(language) + 1);
                    else
                        languages.put(language, 1.0);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        Date currentDate = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        Platform.runLater(() -> {
            this.lastRefresh.set("Letzte Aktualisierung: " + sdf.format(currentDate));
            this.changeData(pieChart);
            this.changeData(barChart);
        });
        return languages;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
