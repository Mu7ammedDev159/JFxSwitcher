package org.onyxfx.graphics.layout;

import javafx.animation.RotateTransition;
import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.util.Duration;

public class OFxExpandablePane extends TitledPane {

    private final BooleanProperty underlineVisible = new SimpleBooleanProperty(true);
    private final ObjectProperty<Image> arrowImage = new SimpleObjectProperty<>();
    private final StackPane arrowContainer = new StackPane();
    private final ImageView arrowIcon = new ImageView();
    private final Label titleLabel = new Label();
    private final HBox titleBox = new HBox();

    public OFxExpandablePane() {
        super();
        initialize(); // <- re-use the same init logic
        applyRuntimeStyles();
    }

    private void initialize() {
//        getStylesheets().add(getClass().getResource("css/ExpandablePane.css").toExternalForm());
//        this.getStyleClass().add("expandable-list-section");

        arrowContainer.getChildren().add(arrowIcon);
        arrowContainer.setAlignment(Pos.CENTER);

        // This sets the label text to track the actual TitledPane text
        titleLabel.textProperty().bind(textProperty());

        // We show the text + arrow icon
        titleBox.setAlignment(Pos.CENTER_LEFT);
        titleBox.setSpacing(8);
        titleBox.getChildren().setAll(arrowContainer, titleLabel);

        // Let the whole box be the graphic, but keep text visible inside it
        setGraphic(titleBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setAnimated(true);

        arrowIcon.setFitWidth(10);
        arrowIcon.setFitHeight(10);

        underlineVisible.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                getStyleClass().add("underline-visible");
            } else {
                getStyleClass().remove("underline-visible");
            }
        });

        arrowImage.addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                arrowIcon.setImage(newImg);
            }
        });

        expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> animateArrow(isNowExpanded));

        // Initial state
        if (arrowImage.get() != null) {
            arrowIcon.setImage(arrowImage.get());
        }
        if (underlineVisible.get()) {
            getStyleClass().add("underline-visible");
        }
    }

    private void animateArrow(boolean expand) {
        RotateTransition rotate = new RotateTransition(Duration.millis(200), arrowIcon);
        rotate.setFromAngle(expand ? 0 : 90);
        rotate.setToAngle(expand ? 90 : 0);
        rotate.play();
    }

    private void applyRuntimeStyles() {
        if (!isInSceneBuilder()) {
            getStyleClass().add("expandable-list-section");

            try {
                String css = getClass().getResource("/css/ExpandablePane.css").toExternalForm();
                getStylesheets().add(css);
            } catch (Exception e) {
                System.err.println("Failed to load ExpandablePane CSS: " + e.getMessage());
            }
        }
    }

    private boolean isInSceneBuilder() {
        return System.getProperty("java.class.path").toLowerCase().contains("scenebuilder");
    }

    public boolean isUnderlineVisible() {
        return underlineVisible.get();
    }

    public void setUnderlineVisible(boolean value) {
        underlineVisible.set(value);
    }

    public BooleanProperty underlineVisibleProperty() {
        return underlineVisible;
    }

    public Image getArrowImage() {
        return arrowImage.get();
    }

    public void setArrowImage(Image image) {
        this.arrowImage.set(image);
    }

    public ObjectProperty<Image> arrowImageProperty() {
        return arrowImage;
    }
}