package org.onyxfx.graphics.layout;

import javafx.animation.RotateTransition;
import javafx.application.Platform;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.css.*;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.ContentDisplay;
import javafx.scene.control.Label;
import javafx.scene.control.TitledPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.util.Duration;

import java.net.URL;
import java.util.List;

public class OFxExpandablePane extends TitledPane {

    private final BooleanProperty underlineVisible = new SimpleBooleanProperty(true);
    private final ObjectProperty<Image> arrowImage = new SimpleObjectProperty<>();

    private final DoubleProperty arrowIconWidth = new SimpleDoubleProperty(this, "arrowIconWidth", 10);
    private final DoubleProperty arrowIconHeight = new SimpleDoubleProperty(this, "arrowIconHeight", 10);

    private final StackPane arrowContainer = new StackPane();
    private final ImageView arrowIcon = new ImageView();
    private final Label titleLabel = new Label(); // bring back label

    private final HBox textContainer = new HBox();
    private final HBox iconContainer = new HBox();
    private final HBox titleBox = new HBox();

    public OFxExpandablePane() {
        super();
        initialize(); // <- re-use the same init logic
        applyDefaultStyleClasses();
    }

    public OFxExpandablePane(String title, Node content) {
        this();
        setText(title);
        setContent(content);
        applyDefaultStyleClasses();
    }

    private void initialize() {
        titleLabel.textProperty().bind(textProperty());
        titleLabel.fontProperty().bind(fontProperty());
        titleLabel.setDisable(false);

        // Bind title label color to the same value SceneBuilder assigns to -fx-text-fill
        sceneProperty().addListener((obs, oldScene, newScene) -> {
            if (newScene != null) {
                Platform.runLater(() -> {
                    // Extract -fx-text-fill from JavaFX CSS engine
                    getCssMetaData().stream().filter(meta -> meta.getProperty().equals("-fx-text-fill")).findFirst().ifPresent(meta -> {
                        @SuppressWarnings("unchecked") StyleableProperty<Paint> styleable = (StyleableProperty<Paint>) ((CssMetaData<Styleable, ?>) meta).getStyleableProperty(this);
                        Paint fill = styleable.getValue();
                        titleLabel.setTextFill(fill);
                    });
                });
            }
        });

        arrowIcon.fitWidthProperty().bind(arrowIconWidth);
        arrowIcon.fitHeightProperty().bind(arrowIconHeight);
        arrowImage.addListener((obs, oldImg, newImg) -> {
            if (newImg != null) {
                arrowIcon.setImage(newImg);
            }
        });

        arrowContainer.getChildren().add(arrowIcon);
        arrowContainer.setAlignment(Pos.CENTER);
        arrowContainer.getStyleClass().add("arrow-icon");

        iconContainer.getChildren().add(arrowContainer);
        iconContainer.setAlignment(Pos.CENTER_RIGHT);
        HBox.setHgrow(iconContainer, Priority.ALWAYS);

        textContainer.getChildren().add(titleLabel);
        textContainer.setAlignment(Pos.CENTER_LEFT);
        HBox.setHgrow(textContainer, Priority.ALWAYS);

        titleBox.getChildren().setAll(textContainer, iconContainer);
        titleBox.setAlignment(Pos.CENTER);
        titleBox.setMaxWidth(Double.MAX_VALUE);

        setGraphic(titleBox);
        setContentDisplay(ContentDisplay.GRAPHIC_ONLY);
        setAnimated(true);

        expandedProperty().addListener((obs, wasExpanded, isNowExpanded) -> animateArrow(isNowExpanded));
    }

    private Paint getTextFillFromParentOrTheme() {
        Node parent = getParent();
        while (parent != null) {
            if (parent instanceof Label label) {
                return label.getTextFill();
            }
            parent = parent.getParent();
        }
        return Color.BLACK;
    }

    private void animateArrow(boolean expand) {
        RotateTransition rotate = new RotateTransition(Duration.millis(100), arrowIcon);
        rotate.setFromAngle(expand ? 0 : 90);
        rotate.setToAngle(expand ? 90 : 0);
        rotate.play();
    }

    @Override
    public String getUserAgentStylesheet() {
        URL css = getClass().getResource("/css/ExpandablePane.css");
        return css != null ? css.toExternalForm() : null;
    }

    private void applyDefaultStyleClasses() {
        if (!getStyleClass().contains("expandable-list-section")) {
            getStyleClass().add("expandable-list-section");
        }
        if (isUnderlineVisible()) {
            getStyleClass().add("underline-visible");
        }

        underlineVisible.addListener((obs, oldVal, newVal) -> {
            if (newVal) {
                if (!getStyleClass().contains("underline-visible")) {
                    getStyleClass().add("underline-visible");
                }
            } else {
                getStyleClass().remove("underline-visible");
            }
        });
    }

    private static boolean isInSceneBuilder() {
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

    public double getArrowIconWidth() {
        return arrowIconWidth.get();
    }

    public void setArrowIconWidth(double width) {
        this.arrowIconWidth.set(width);
    }

    public DoubleProperty arrowIconWidthProperty() {
        return arrowIconWidth;
    }

    public double getArrowIconHeight() {
        return arrowIconHeight.get();
    }

    public void setArrowIconHeight(double height) {
        this.arrowIconHeight.set(height);
    }

    public DoubleProperty arrowIconHeightProperty() {
        return arrowIconHeight;
    }

    public ObjectProperty<Image> arrowImageProperty() {
        return arrowImage;
    }
}