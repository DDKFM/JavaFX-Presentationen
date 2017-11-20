package de.ddkfm.charts;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Scene;
import javafx.scene.chart.*;
import javafx.scene.control.Button;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import javax.json.*;
import javax.json.stream.JsonParser;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;
import java.time.LocalDate;
import java.util.*;

public class ChartDemo extends Application {
    @Override
    public void start(Stage primaryStage) throws Exception {
        PieChart pieChart = new PieChart();

        CategoryAxis xAxis = new CategoryAxis();
        NumberAxis yAxis = new NumberAxis();
        BarChart<String,Number> barChart = new BarChart<String,Number>(xAxis,yAxis);

        BorderPane root = new BorderPane();

        BorderPane buttonPane = new BorderPane();
        Button buttonBarChart = new Button("Show Barchart");
        buttonBarChart.setOnAction(event -> root.setCenter(barChart));
        Button buttonPieChart = new Button("Show PieChart");
        buttonPieChart.setOnAction(event -> root.setCenter(pieChart));
        Button buttonRefresh = new Button("Refresh Data");
        buttonRefresh.setOnAction(event -> {
            this.changeData(pieChart);
            this.changeData(barChart);
        });
        buttonRefresh.fire();

        buttonPane.setLeft(buttonBarChart);
        buttonPane.setCenter(buttonRefresh);
        buttonPane.setRight(buttonPieChart);

        root.setCenter(barChart);
        root.setBottom(buttonPane);
        Scene scene = new Scene(root);
        primaryStage.setScene(scene);
        primaryStage.show();
    }
    private void changeData(PieChart pieChart) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        Map<String, Double> fetchedData = this.fetchData();
        fetchedData.forEach((key, value) -> data.add(new PieChart.Data(key, value)));
        pieChart.setData(data);
    }
    private void changeData(BarChart<String, Number> pieChart) {
        ObservableList<PieChart.Data> data = FXCollections.observableArrayList();

        Map<String, Double> fetchedData = this.fetchData();
        XYChart.Series<String, Number> series1 = new XYChart.Series<>();
        fetchedData.forEach((key, value) -> series1.getData().add(new XYChart.Data<>(key, value)));
        pieChart.getData().clear();
        pieChart.getData().add(series1);
    }
    private Map<String, Double> fetchData() {
        String accessToken = "";
        LocalDate now = LocalDate.now();
        String dateString = now.getYear() + "-" + now.getMonthValue() + "-" + now.getDayOfMonth();
        String query = "created:>=" + dateString;
        InputStream inputStream = null;
        try {
            URL url = new URL("https://api.github.com/search/repositories" +
                    "?access_token=" + accessToken +
                    "&q=" + query +
                    "&per_page=100" +
                    "&sort=desc" +
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
        return languages;
    }
    public static void main(String[] args) {
        launch(args);
    }
}
