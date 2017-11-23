package de.ddkfm.fancy.stuff;

import javafx.animation.FadeTransition;
import javafx.event.EventHandler;
import javafx.scene.Cursor;
import javafx.scene.SnapshotParameters;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.Reflection;
import javafx.scene.image.Image;
import javafx.scene.image.WritableImage;
import javafx.scene.input.ClipboardContent;
import javafx.scene.input.Dragboard;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TransferMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.scene.paint.Color;
import javafx.scene.shape.*;
import javafx.scene.text.Font;
import javafx.util.Duration;
import javafx.util.Pair;

import javax.imageio.ImageIO;
import javax.imageio.stream.ImageInputStream;
import java.awt.geom.RoundRectangle2D;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;

public class FancyPane extends Pane {

    private List<Shape> shapes = new ArrayList<>();
    private Map<Shape, Pair<Double, Double>> oldXYValues = new HashMap<>();
    public FancyPane() {
        BorderPane deleteZone = new BorderPane();
        deleteZone.setMinWidth(100);
        deleteZone.setMinHeight(100);
        deleteZone.setStyle("-fx-background-color: lightgreen");

        deleteZone.setLayoutX(0);
        deleteZone.setLayoutY(0);
        Label lblDelete = new Label("DELETE");
        lblDelete.setStyle("-fx-font-size: 20px;-fx-text-fill: white");

        deleteZone.setCenter(lblDelete);
        this.getChildren().add(deleteZone);

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
                        finalShape.setCursor(Cursor.CLOSED_HAND);
                    });
                    shape.setOnMouseReleased(e -> {
                        if(e.getSceneX() <= deleteZone.getLayoutX() + deleteZone.getMinWidth()
                                && e.getSceneY() <= deleteZone.getLayoutY() + deleteZone.getMinHeight()) {
                            FadeTransition fadeTransition = new FadeTransition(Duration.millis(500), finalShape);
                            fadeTransition.setFromValue(1.0);
                            fadeTransition.setToValue(0.0);
                            fadeTransition.onFinishedProperty().set((actionEvent) -> {
                                System.out.println("on Finished");
                                this.getChildren().remove(finalShape);
                                this.shapes.remove(finalShape);
                                deleteZone.setStyle("-fx-background-color: lightgreen");
                            });
                            fadeTransition.play();
                        }
                        finalShape.setCursor(Cursor.DEFAULT);
                    });
                    shape.setOnMouseDragged(e -> {
                        double offsetX = e.getSceneX() - this.oldXYValues.get(finalShape).getKey();
                        double offsetY = e.getSceneY() - this.oldXYValues.get(finalShape).getValue();

                        finalShape.setLayoutX(finalShape.getLayoutX() + offsetX);
                        finalShape.setLayoutY(finalShape.getLayoutY() + offsetY);

                        this.setXYValue(finalShape, e.getSceneX(), e.getSceneY());

                        if(e.getSceneX() <= deleteZone.getLayoutX() + deleteZone.getMinWidth()
                                && e.getSceneY() <= deleteZone.getLayoutY() + deleteZone.getMinHeight()) {
                            deleteZone.setStyle("-fx-background-color: lightcoral");
                        } else {
                            deleteZone.setStyle("-fx-background-color: lightgreen");
                        }
                        e.consume();
                    });
                    shape.setOnZoom(e -> {
                        finalShape.setScaleX(finalShape.getScaleX() * e.getZoomFactor());
                        finalShape.setScaleY(finalShape.getScaleY() * e.getZoomFactor());
                    });
                    shape.setOnRotate(e -> {
                        finalShape.setRotate(finalShape.getRotate() + e.getAngle());
                    });
                    int intValue = new Random().nextInt(10);
                    /*
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
                    shape.setBlendMode(blendMode);*/
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
