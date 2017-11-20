package de.ddkfm.fancy.stuff;

import javafx.scene.control.Button;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.util.Pair;

import java.awt.geom.RoundRectangle2D;
import java.util.*;

public class FancyPane extends Pane {

    private List<Shape> shapes = new ArrayList<>();
    private Map<Shape, Pair<Double, Double>> oldXYValues = new HashMap<>();
    public FancyPane() {
        this.setOnMouseClicked(event -> {
            if(event.getClickCount() >= 2) {
                int randomInt = new Random().nextInt(4);
                double randomDouble1 = new Random().nextDouble() * 100;
                double randomDouble2 = new Random().nextDouble() * 100;
                double x = event.getSceneX();
                double y = event.getSceneY();
                Shape shape = null;
                switch (randomInt) {
                    case 0:
                        shape = new Rectangle(x, y, randomDouble1, randomDouble2);
                        break;
                    case 1:
                        shape = new Ellipse(x, y, randomDouble1, randomDouble2);
                        break;
                    case 2:
                        shape = new Arc(x, y, randomDouble1, randomDouble2, 0, 100);
                        break;
                    case 3:
                        shape = new Polygon(randomDouble1, randomDouble2, randomDouble1 - 5, randomDouble2 - 50);
                        break;
                }
                int r = new Random().nextInt(256);
                int g = new Random().nextInt(256);
                int b = new Random().nextInt(256);
                shape.setFill(Color.rgb(r, g, b));
                if (shape != null) {
                    Shape finalShape = shape;
                    shape.setOnMousePressed(e -> {
                        this.setXYValue(finalShape, e.getSceneX(), e.getSceneY());
                    });
                    shape.setOnMouseDragged(e -> {
                        double offsetX = e.getSceneX() - this.oldXYValues.get(finalShape).getKey();
                        double offsetY = e.getSceneY() - this.oldXYValues.get(finalShape).getValue();


                        finalShape.setLayoutX(finalShape.getLayoutX() + offsetX);
                        finalShape.setLayoutY(finalShape.getLayoutY() + offsetY);

                        this.setXYValue(finalShape, e.getSceneX(), e.getSceneY());
                    });
                    shape.setOnZoom(e -> {
                        finalShape.setScaleX(finalShape.getScaleX() * e.getZoomFactor());
                        finalShape.setScaleY(finalShape.getScaleY() * e.getZoomFactor());
                    });
                    shape.setOnRotate(e -> {
                        finalShape.setRotate(finalShape.getRotate() + e.getAngle());
                    });
                    shapes.add(shape);
                    this.getChildren().add(shape);
                }
            }
        });
    }
    private void setXYValue(Shape shape, double x, double y) {
        if(this.oldXYValues.containsKey(shape)) {
            this.oldXYValues.remove(shape);
        }
        this.oldXYValues.put(shape, new Pair<>(x, y));
    }
}
