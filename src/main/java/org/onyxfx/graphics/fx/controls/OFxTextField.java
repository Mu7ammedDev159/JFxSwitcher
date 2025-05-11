
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

package org.onyxfx.graphics.fx.controls;

import javafx.css.*;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.layout.Border;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;


/**
 *
 * @ONYX-FX
 *
 * A TextField class that is customizable to support enhanced styling features.
 * It allows dynamic configuration of visual states such as normal, hover, pressed, and focused colors via properties.
 *
 * @author Muhammed Joharji
 * @version 1.0
 * @since 2025
 *
 */

public class OFxTextField extends TextField {

    // Factory for creating CSS-styleable properties.
    private static final StyleablePropertyFactory<OFxTextField> FACTORY =
            new StyleablePropertyFactory<>(TextField.getClassCssMetaData());

    // Styleable background color in normal state.
    private final StyleableObjectProperty<Paint> normalColor =
            new StyleableObjectProperty<>(Color.web("#2196f3")) {
                @Override
                public Object getBean() { return OFxTextField.this; }
                @Override
                public String getName() { return "normalColor"; }
                @Override
                public CssMetaData<OFxTextField, Paint> getCssMetaData() {
                    return OFxTextField.StyleableProperties.NORMAL_COLOR;
                }
            };

    // Styleable background color in hover state.
    private final StyleableObjectProperty<Paint> hoverColor =
            new StyleableObjectProperty<>(Color.web("#4654c0")) {
                @Override
                public Object getBean() { return OFxTextField.this; }
                @Override
                public String getName() { return "hoverColor"; }
                @Override
                public CssMetaData<OFxTextField, Paint> getCssMetaData() {
                    return OFxTextField.StyleableProperties.HOVER_COLOR;
                }
            };

    // Styleable background color in pressed state.
    private final StyleableObjectProperty<Paint> pressedColor =
            new StyleableObjectProperty<>(Color.web("#3a48a3")) {
                @Override
                public Object getBean() { return OFxTextField.this; }
                @Override
                public String getName() { return "pressedColor"; }
                @Override
                public CssMetaData<OFxTextField, Paint> getCssMetaData() {
                    return OFxTextField.StyleableProperties.PRESSED_COLOR;
                }
            };

    // Styleable background color in focused state.
    private final StyleableObjectProperty<Paint> focusedColor =
            new StyleableObjectProperty<>(Color.web("#2196f3")) {
                @Override
                public Object getBean() { return OFxTextField.this; }
                @Override
                public String getName() { return "focusedColor"; }
                @Override
                public CssMetaData<OFxTextField, Paint> getCssMetaData() {
                    return OFxTextField.StyleableProperties.FOCUSED_COLOR;
                }
            };

    public OFxTextField(){
        super();
        getStyleClass().add("ofx-textField");

        // Listeners for state changes to update appearance
        hoverProperty().addListener((obs, oldVal, newVal) -> updateBackgroundColor());

        // Listeners for property changes
        normalColorProperty().addListener((obs, old, val) -> updateBackgroundColor());
        hoverColorProperty().addListener((obs, old, val) -> updateBackgroundColor());
        pressedColorProperty().addListener((obs, old, val) -> updateBackgroundColor());
        focusedColorProperty().addListener((obs, old, val) -> updateBackgroundColor());

        updateBackgroundColor();
    }

    private void updateBackgroundColor() {
        Paint paint;

        if (isPressed() && getcPressedColor() != null) {
            paint = getcPressedColor();
        } else if (isHover() && getbHoverColor() != null) {
            paint = getbHoverColor();
        } else if (isFocused() && getdFocusedColor() != null) {
            paint = getdFocusedColor();
        } else {
            paint = getaNormalColor();
        }

        if (paint instanceof Color) {
            setBackground(new Background(new BackgroundFill(
                    (Color) paint,
                    getBackground() != null ? getBackground().getFills().get(0).getRadii() : null,
                    Insets.EMPTY
            )));
        }
    }

    public Paint getNormalColor() {
        return normalColor.get();
    }

    public Paint getHoverColor() {
        return hoverColor.get();
    }

    public Paint getPressedColor() {
        return pressedColor.get();
    }

    public Paint getFocusedColor() {
        return focusedColor.get();
    }

    /** @return the styleable normal color. */
    public Paint getaNormalColor() {
        return normalColor.get();
    }

    /** Sets the styleable normal color. */
    public void setaNormalColor(Paint color) {
        this.normalColor.set(color);
    }

    /** @return the styleable normal color property. */
    public StyleableObjectProperty<Paint> normalColorProperty() {
        return normalColor;
    }

    public Paint getbHoverColor() {
        return hoverColor.get();
    }

    public void setbHoverColor(Paint color) {
        this.hoverColor.set(color);
    }

    public StyleableObjectProperty<Paint> hoverColorProperty() {
        return hoverColor;
    }

    public Paint getcPressedColor() {
        return pressedColor.get();
    }

    public void setcPressedColor(Paint color) {
        this.pressedColor.set(color);
    }

    public StyleableObjectProperty<Paint> pressedColorProperty() {
        return pressedColor;
    }

    public Paint getdFocusedColor() {
        return focusedColor.get();
    }

    public void setdFocusedColor(Paint color) {
        this.focusedColor.set(color);
    }

    public StyleableObjectProperty<Paint> focusedColorProperty() {
        return focusedColor;
    }

    // List of all CSS metadata for this control.
    private static final List<CssMetaData<? extends Styleable, ?>> CSS_META_DATA_LIST;
    static {
        List<CssMetaData<? extends Styleable, ?>> list = new ArrayList<>(Button.getClassCssMetaData());
        list.addAll(List.of(
                ((StyleableProperty<?>) new OFxTextField().normalColor).getCssMetaData(),
                ((StyleableProperty<?>) new OFxTextField().hoverColor).getCssMetaData(),
                ((StyleableProperty<?>) new OFxTextField().pressedColor).getCssMetaData(),
                ((StyleableProperty<?>) new OFxTextField().focusedColor).getCssMetaData()
        ));
        CSS_META_DATA_LIST = Collections.unmodifiableList(list);
    }

    /**
     * Returns the list of CSS metadata associated with this control,
     * including the styleable color properties.
     */
    @Override
    public List<CssMetaData<? extends Styleable, ?>> getControlCssMetaData() {
        return CSS_META_DATA_LIST;
    }

    /**
     * Overrides layoutChildren to ensure background color is updated
     * during layout passes.
     */
    @Override
    protected void layoutChildren() {
        super.layoutChildren();
        updateBackgroundColor();
    }

    /**
     * Internal class holding the custom CSS metadata for {@code OFxButton}.
     */
    private static class StyleableProperties {
        private static final CssMetaData<OFxTextField, Paint> NORMAL_COLOR =
                new CssMetaData<>("-ofx-focused-color", StyleConverter.getPaintConverter(), Color.web("#2196f3")) {
                    @Override public boolean isSettable(OFxTextField node) {
                        return !node.normalColor.isBound();
                    }
                    @Override public StyleableProperty<Paint> getStyleableProperty(OFxTextField node) {
                        return node.normalColorProperty();
                    }
                };

        private static final CssMetaData<OFxTextField, Paint> HOVER_COLOR =
                new CssMetaData<>("-ofx-hover-color", StyleConverter.getPaintConverter(), Color.web("#4654c0")) {
                    @Override public boolean isSettable(OFxTextField node) {
                        return !node.hoverColor.isBound();
                    }
                    @Override public StyleableProperty<Paint> getStyleableProperty(OFxTextField node) {
                        return node.hoverColorProperty();
                    }
                };

        private static final CssMetaData<OFxTextField, Paint> PRESSED_COLOR =
                new CssMetaData<>("-ofx-pressed-color", StyleConverter.getPaintConverter(), Color.web("#3a48a3")) {
                    @Override public boolean isSettable(OFxTextField node) {
                        return !node.pressedColor.isBound();
                    }
                    @Override public StyleableProperty<Paint> getStyleableProperty(OFxTextField node) {
                        return node.pressedColorProperty();
                    }
                };

        private static final CssMetaData<OFxTextField, Paint> FOCUSED_COLOR =
                new CssMetaData<>("-ofx-focused-color", StyleConverter.getPaintConverter(), Color.web("#2196f3")) {
                    @Override public boolean isSettable(OFxTextField node) {
                        return !node.focusedColor.isBound();
                    }
                    @Override public StyleableProperty<Paint> getStyleableProperty(OFxTextField node) {
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
