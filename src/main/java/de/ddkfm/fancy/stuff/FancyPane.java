package de.ddkfm.fancy.stuff;

import javafx.scene.control.Button;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Reflection;
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
                int randomInt = new Random().nextInt(2);
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
                    int intValue = new Random().nextInt(10);
                    BlendMode blendMode = null;
                    switch(intValue) {
                        case 0:
                            blendMode = BlendMode.ADD;
                            break;
                        case 1:
                            blendMode = BlendMode.BLUE;
                            break;
                        case 2:
                            blendMode = BlendMode.COLOR_BURN;
                            break;
                        case 3:
                            blendMode = BlendMode.DARKEN;
                            break;
                        case 4:
                            blendMode = BlendMode.COLOR_DODGE;
                            break;
                        case 5:
                            blendMode = BlendMode.EXCLUSION;
                            break;
                        case 6:
                            blendMode = BlendMode.HARD_LIGHT;
                            break;
                        case 7:
                            blendMode = BlendMode.OVERLAY;
                            break;
                        case 8:
                            blendMode = BlendMode.SOFT_LIGHT;
                            break;
                        case 9:
                            blendMode = BlendMode.LIGHTEN;
                            break;
                    }
                    shape.setBlendMode(blendMode);
                    shapes.add(shape);
                    this.getChildren().add(shape);
                }
            }
        });
    }

    public void setReflection() {
        Reflection reflection = new Reflection();
        reflection.setFraction(0.4);
        this.shapes.forEach((shape) -> shape.setEffect(reflection));
    }

    public void removeReflection() {
        this.shapes.forEach((shape) -> shape.setEffect(null));
    }

    private void setXYValue(Shape shape, double x, double y) {
        if(this.oldXYValues.containsKey(shape)) {
            this.oldXYValues.remove(shape);
        }
        this.oldXYValues.put(shape, new Pair<>(x, y));
    }
}
