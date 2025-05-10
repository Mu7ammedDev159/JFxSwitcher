
/*
 * Copyright (c) [2025] [Muhammed Joharji]
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

package org.onyxfx.fx.controls;

import javafx.beans.property.*;
import javafx.css.*;
import javafx.css.converter.PaintConverter;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

/**
 * A class that is customizable to support enhanced styling features.
 * It allows dynamic configuration of visual states such as normal, hover, pressed, and focused colors via properties.
 *
 * @author Muhammed Joharji
 * @version 1.0
 * @since 2025
 *
 */

public class OFxButton extends Button {

    private static final StyleablePropertyFactory<OFxButton> FACTORY =
            new StyleablePropertyFactory<>(Button.getClassCssMetaData());

    //@SuppressWarnings("unchecked")
    private final StyleableObjectProperty<Paint> normalColor =
            new StyleableObjectProperty<>(Color.web("#2196f3")) {
                @Override
                public Object getBean() {
                    return OFxButton.this;
                }

                @Override
                public String getName() {
                    return "normalColor";
                }

                @Override
                public CssMetaData<OFxButton, Paint> getCssMetaData() {
                    return StyleableProperties.NORMAL_COLOR;
                }
            };

    @SuppressWarnings("unchecked")
    private final StyleableObjectProperty<Paint> hoverColor =
            new StyleableObjectProperty<>(Color.web("#2196f3")) {
                @Override
                public Object getBean() {
                    return OFxButton.this;
                }

                @Override
                public String getName() {
                    return "hoverColor";
                }

                @Override
                public CssMetaData<OFxButton, Paint> getCssMetaData() {
                    return StyleableProperties.HOVER_COLOR;
                }
            };

    @SuppressWarnings("unchecked")
    private final StyleableObjectProperty<Paint> pressedColor =
            new StyleableObjectProperty<>(Color.web("#2196f3")) {
                @Override
                public Object getBean() {
                    return OFxButton.this;
                }

                @Override
                public String getName() {
                    return "pressedColor";
                }

                @Override
                public CssMetaData<OFxButton, Paint> getCssMetaData() {
                    return StyleableProperties.PRESSED_COLOR;
                }
            };

    @SuppressWarnings("unchecked")
    private final StyleableObjectProperty<Paint> focusedColor =
            new StyleableObjectProperty<>(Color.web("#2196f3")) {
                @Override
                public Object getBean() {
                    return OFxButton.this;
                }

                @Override
                public String getName() {
                    return "focusedColor";
                }

                @Override
                public CssMetaData<OFxButton, Paint> getCssMetaData() {
                    return StyleableProperties.FOCUSED_COLOR;
                }
            };

    private final BooleanProperty focusedByDefault = new SimpleBooleanProperty(false);
    private final ObjectProperty<Image> icon = new SimpleObjectProperty<>();
    private final DoubleProperty cornerRadius = new SimpleDoubleProperty(10.0);

    public OFxButton() {
        super();

        // Apply default stylesheet (ensure the path is valid!)
        //getStylesheets().add(getClass().getResource("/css/default-button.css").toExternalForm());

        getStyleClass().add("ofx-button");

        // React to state changes
        hoverProperty().addListener((obs, oldVal, newVal) -> updateBackgroundColor());
        armedProperty().addListener((obs, oldVal, newVal) -> updateBackgroundColor());
        focusedProperty().addListener((obs, oldVal, newVal) -> updateBackgroundColor());

        // Also react when styleable properties change
        normalColorProperty().addListener((obs, old, val) -> updateBackgroundColor());
        hoverColorProperty().addListener((obs, old, val) -> updateBackgroundColor());
        pressedColorProperty().addListener((obs, old, val) -> updateBackgroundColor());
        focusedColorProperty().addListener((obs, old, val) -> updateBackgroundColor());

        // Set initial background color
        updateBackgroundColor();

        initListeners();
    }

    private void updateBackgroundColor() {
        Paint paint;

        if (isPressed() && getPressedColor() != null) {
            paint = getPressedColor();
        } else if (isHover() && getHoverColor() != null) {
            paint = getHoverColor();
        } else if (isFocused() && getFocusedColor() != null) {
            paint = getFocusedColor();
        } else {
            paint = getNormalColor();
        }

        if (paint instanceof Color) {
            setBackground(new Background(new BackgroundFill((Color) paint, getBackground() != null ? getBackground().getFills().get(0).getRadii() : null, Insets.EMPTY)));
        }
    }

    // Icon handling
    public void setIcon(Image image) {
        this.icon.set(image);
    }

    public Image getIcon() {
        return icon.get();
    }

    public ObjectProperty<Image> iconProperty() {
        return icon;
    }

    public void setFocusedByDefault(boolean isFocused) {
        this.focusedByDefault.set(isFocused);
    }

    public boolean isFocusedByDefault() {
        return focusedByDefault.get();
    }

    public BooleanProperty focusedByDefaultProperty() {
        return focusedByDefault;
    }

    public void setCornerRadius(double radiusValue) {
        this.cornerRadius.set(radiusValue);
    }

    public double getCornerRadius() {
        return cornerRadius.get();
    }

    public DoubleProperty cornerRadiusProperty() {
        return cornerRadius;
    }

    public Paint getNormalColor() {
        return normalColor.get();
    }

    public void setNormalColor(Paint color) {
        this.normalColor.set(color);
    }

    public StyleableObjectProperty<Paint> normalColorProperty() {
        return normalColor;
    }

    public Paint getHoverColor() {
        return hoverColor.get();
    }

    public void setHoverColor(Paint color) {
        this.hoverColor.set(color);
    }

    public StyleableObjectProperty<Paint> hoverColorProperty() {
        return hoverColor;
    }

    public Paint getPressedColor() {
        return pressedColor.get();
    }

    public void setPressedColor(Paint color) {
        this.pressedColor.set(color);
    }

    public StyleableObjectProperty<Paint> pressedColorProperty() {
        return pressedColor;
    }

    public Paint getFocusedColor() {
        return focusedColor.get();
    }

    public void setFocusedColor(Paint color) {
        this.focusedColor.set(color);
    }

    public StyleableObjectProperty<Paint> focusedColorProperty() {
        return focusedColor;
    }

    private void initListeners() {
        focusedByDefault.addListener((obs, oldVal, newVal) -> {
            if (newVal) requestFocus();
        });

        icon.addListener((obs, old, img) -> {
            if (img != null) {
                setGraphic(new ImageView(img));
            }
        });

        cornerRadius.addListener((obs, old, val) -> {
            setStyle("-fx-background-radius: " + val + "; -fx-border-radius: " + val + ";");
        });

    }

    private String toRgbaString(Color color) {
        return String.format("rgba(%d,%d,%d,%.2f)",
                (int) (color.getRed() * 255),
                (int) (color.getGreen() * 255),
                (int) (color.getBlue() * 255),
                color.getOpacity());
    }

    // Required by CSS engine
    private static final List<CssMetaData<? extends Styleable, ?>> CSS_META_DATA_LIST;
    static {
        List<CssMetaData<? extends Styleable, ?>> list = new ArrayList<>(Button.getClassCssMetaData());
        list.addAll(List.of(
                ((StyleableProperty<?>) new OFxButton().normalColor).getCssMetaData(),
                ((StyleableProperty<?>) new OFxButton().hoverColor).getCssMetaData(),
                ((StyleableProperty<?>) new OFxButton().pressedColor).getCssMetaData(),
                ((StyleableProperty<?>) new OFxButton().focusedColor).getCssMetaData()
        ));
        CSS_META_DATA_LIST = Collections.unmodifiableList(list);
    }

    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return CSS_META_DATA_LIST;
    }

    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        updateBackgroundColor(); // Re-apply on layout
    }

    private static class StyleableProperties {
        private static final CssMetaData<OFxButton, Paint> NORMAL_COLOR =
                new CssMetaData<>("-ofx-normal-color", StyleConverter.getPaintConverter(), Color.web("#2196f3")) {

                    @Override
                    public boolean isSettable(OFxButton node) {
                        return !node.normalColor.isBound();
                    }

                    @Override
                    public StyleableProperty<Paint> getStyleableProperty(OFxButton node) {
                        return node.normalColorProperty();
                    }
                };

        private static final CssMetaData<OFxButton, Paint> HOVER_COLOR =
                new CssMetaData<>("-ofx-hover-color", StyleConverter.getPaintConverter(), Color.web("#2196f3")) {

                    @Override
                    public boolean isSettable(OFxButton node) {
                        return !node.hoverColor.isBound();
                    }

                    @Override
                    public StyleableProperty<Paint> getStyleableProperty(OFxButton node) {
                        return node.hoverColorProperty();
                    }
                };

        private static final CssMetaData<OFxButton, Paint> PRESSED_COLOR =
                new CssMetaData<>("-ofx-pressed-color", StyleConverter.getPaintConverter(), Color.web("#2196f3")) {

                    @Override
                    public boolean isSettable(OFxButton node) {
                        return !node.pressedColor.isBound();
                    }

                    @Override
                    public StyleableProperty<Paint> getStyleableProperty(OFxButton node) {
                        return node.pressedColorProperty();
                    }
                };

        private static final CssMetaData<OFxButton, Paint> FOCUSED_COLOR =
                new CssMetaData<>("-ofx-focused-color", StyleConverter.getPaintConverter(), Color.web("#2196f3")) {

                    @Override
                    public boolean isSettable(OFxButton node) {
                        return !node.focusedColor.isBound();
                    }

                    @Override
                    public StyleableProperty<Paint> getStyleableProperty(OFxButton node) {
                        return node.focusedColorProperty();
                    }
                };
        private static final List<CssMetaData<? extends Styleable, ?>> STYLEABLES;
        static {
            final List<CssMetaData<? extends Styleable, ?>> styleables =
                    new ArrayList<>(Button.getClassCssMetaData());
            styleables.add(NORMAL_COLOR);
            styleables.add(HOVER_COLOR);
            styleables.add(PRESSED_COLOR);
            styleables.add(FOCUSED_COLOR);
            STYLEABLES = Collections.unmodifiableList(styleables);
        }
    }
}

