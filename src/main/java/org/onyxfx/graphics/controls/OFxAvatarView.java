
/*
 * Copyright (c) [2025] [MuhammedTJ]
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.onyxfx.graphics.controls;

import javafx.beans.property.*;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;

import java.util.Random;
import java.util.prefs.Preferences;

/**
 *
 * @ONYX-FX
 *
 * `OFxAvatarView` is a customizable avatar component for JavaFX.
 * It displays an image clipped in a circular shape with an optional colored background.
 * The background color is randomly generated on first use and stored in user preferences
 * to persist between application runs.
 *
 * @author MuhammedTJ
 * @version 1.0
 * @since 2025
 *
 * <p>Features:</p>
 * <ul>
 *   <li>Resizable via `size` property</li>
 *   <li>Optional manual `fitWidth` and `fitHeight`</li>
 *   <li>Clipped image inside a circle</li>
 *   <li>Persistent random color background</li>
 * </ul>
 */

public class OFxAvatarView extends StackPane {

    /** Random generator for default color */
    private static final Random RANDOM = new Random();

    /** Java Preferences API to persist the avatar color */
    private static final Preferences PREFS = Preferences.userNodeForPackage(OFxAvatarView.class);

    /** Key to store/retrieve background color */
    private static final String COLOR_PREF_KEY = "ofx-avatar-color";

    /** The avatar image shown in the circle */
    private final ObjectProperty<Image> image = new SimpleObjectProperty<>();

    /** Size of the avatar (width and height are equal) */
    private final DoubleProperty size = new SimpleDoubleProperty(40);

    /** Background color for the circular area */
    private final ObjectProperty<Color> backgroundColor = new SimpleObjectProperty<>(loadOrGenerateColor());

    /** Optional manual image fit width */
    private final DoubleProperty fitWidth = new SimpleDoubleProperty();

    /** Optional manual image fit height */
    private final DoubleProperty fitHeight = new SimpleDoubleProperty();

    /** Circular clip shape */
    private final Circle clip = new Circle();

    /** Circular background node */
    private final Circle backgroundCircle = new Circle();

    /** ImageView for displaying the avatar */
    private final ImageView imageView = new ImageView();

    /** Default constructor */
    public OFxAvatarView() {
        initialize();
    }

    /**
     * Constructs the avatar view with a given image.
     * @param avatarImage the image to show
     */
    public OFxAvatarView(Image avatarImage) {
        setImage(avatarImage);
        initialize();
    }

    /** Initializes layout, bindings, and visuals */
    private void initialize() {
        getStyleClass().add("ofx-avatar-view");
        setAlignment(Pos.CENTER);

        // Circular clip setup
        clip.radiusProperty().bind(size.divide(2));
        clip.centerXProperty().bind(size.divide(2));
        clip.centerYProperty().bind(size.divide(2));

        // Background circle configuration
        backgroundCircle.radiusProperty().bind(size.divide(2));
        backgroundCircle.fillProperty().bind(backgroundColor);

        // ImageView settings
        imageView.imageProperty().bind(image);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setClip(clip);

        getChildren().addAll(backgroundCircle, imageView);

        // Listen for size changes
        size.addListener((obs, oldVal, newVal) -> {
            setPrefSize(newVal.doubleValue(), newVal.doubleValue());
            if (!fitWidthProperty().isBound()) {
                imageView.setFitWidth(newVal.doubleValue() * 0.6);
            }
            if (!fitHeightProperty().isBound()) {
                imageView.setFitHeight(newVal.doubleValue() * 0.6);
            }
        });

        // Initial preferred size
        setPrefSize(size.get(), size.get());

        // Bind manual fit width/height if set
        fitWidth.addListener((obs, oldVal, newVal) -> imageView.setFitWidth(newVal.doubleValue()));
        fitHeight.addListener((obs, oldVal, newVal) -> imageView.setFitHeight(newVal.doubleValue()));

        // Initialize fit dimensions
        if (!fitWidth.isBound()) {
            imageView.setFitWidth(size.get() * 0.6);
        }
        if (!fitHeight.isBound()) {
            imageView.setFitHeight(size.get() * 0.6);
        }
    }

    /**
     * Loads the saved color from preferences or generates a new one.
     * @return a persistent or new background color
     */
    public static Color loadOrGenerateColor() {
        String saved = PREFS.get(COLOR_PREF_KEY, null);
        if (saved != null) {
            try {
                return Color.web(saved);
            } catch (IllegalArgumentException ignored) {}
        }
        Color generated = Color.hsb(RANDOM.nextDouble() * 360, 0.6, 0.85);
        PREFS.put(COLOR_PREF_KEY, toHex(generated));
        return generated;
    }

    /**
     * Converts a JavaFX {@link Color} into a hex string.
     * @param color the color to convert
     * @return a hex representation like "#FFAA33"
     */
    public static String toHex(Color color) {
        return String.format("#%02X%02X%02X",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255));
    }

    // ---------- Getters / Setters / Properties ----------

    public Image getImage() { return image.get(); }
    public void setImage(Image value) { image.set(value); }
    public ObjectProperty<Image> imageProperty() { return image; }

    public double getSize() { return size.get(); }
    public void setSize(double value) { size.set(value); }
    public DoubleProperty sizeProperty() { return size; }

    public Color getBackgroundColor() { return backgroundColor.get(); }
    public void setBackgroundColor(Color color) {
        backgroundColor.set(color);
        PREFS.put(COLOR_PREF_KEY, toHex(color));
    }
    public ObjectProperty<Color> backgroundColorProperty() { return backgroundColor; }

    public double getFitWidth() { return fitWidth.get(); }
    public void setFitWidth(double value) { fitWidth.set(value); }
    public DoubleProperty fitWidthProperty() { return fitWidth; }

    public double getFitHeight() { return fitHeight.get(); }
    public void setFitHeight(double value) { fitHeight.set(value); }
    public DoubleProperty fitHeightProperty() { return fitHeight; }
}