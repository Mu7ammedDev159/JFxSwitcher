
/*
 * Copyright (c) [2025] [@MuhammedTJ]
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

package org.onyxfx.graphics.layout;

import javafx.animation.RotateTransition;
import javafx.beans.binding.Bindings;
import javafx.beans.property.*;
import javafx.collections.ObservableList;
import javafx.css.PseudoClass;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Label;
import javafx.scene.effect.Blend;
import javafx.scene.effect.BlendMode;
import javafx.scene.effect.ColorInput;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.shape.Rectangle;
import javafx.scene.text.Font;
import javafx.util.Duration;

/**
 *
 * @ONYX-FX
 *
 * `OFxExpandablePane` is a custom expandable pane built on top of JavaFX {@link VBox}.
 * It mimics behavior similar to Discord's expandable server/channel sections,
 * featuring a customizable header, arrow icon rotation, and dynamic content visibility.
 *
 * @author MuhammedTJ
 * @version 1.1
 * @since 2025
 *
 * <p>This component supports features like hover color, arrow tint, custom title font,
 * text fill, and optional animation during expand/collapse.</p>
 *
 * <p>Usage example:</p>
 * <pre>
 *     OFxExpandablePane pane = new OFxExpandablePane("My Section", new Label("Child Node"));
 *     pane.setArrowImage(new Image("arrow.png"));
 * </pre>
 *
 *
 *
 */
public class OFxExpandablePane extends VBox {

    /** Pseudo class for controlling underline visibility in CSS. */
    private static final PseudoClass UNDERLINE_VISIBLE = PseudoClass.getPseudoClass("underline-visible");

    /** Controls whether the underline beneath the title is shown. */
    private final BooleanProperty underlineVisible = new SimpleBooleanProperty(this, "underlineVisible", true);

    /** Image property for the arrow icon. */
    private final ObjectProperty<Image> arrowImage = new SimpleObjectProperty<>();

    /** Width property of the arrow icon. */
    private final DoubleProperty arrowIconWidth = new SimpleDoubleProperty(this, "arrowIconWidth", 10);

    /** Height property of the arrow icon. */
    private final DoubleProperty arrowIconHeight = new SimpleDoubleProperty(this, "arrowIconHeight", 10);

    /** Whether the pane is currently expanded (i.e., content is visible). */
    private final BooleanProperty expanded = new SimpleBooleanProperty(false);

    /** Title text of the pane. */
    private final StringProperty text = new SimpleStringProperty(this, "text", "Title");

    /** Font used for the title label. */
    private final ObjectProperty<Font> font = new SimpleObjectProperty<>(Font.getDefault());

    /** Text color of the title. */
    private final ObjectProperty<Paint> textFill = new SimpleObjectProperty<>(Color.WHITE);

    /** Tint color applied to the arrow icon. */
    private final ObjectProperty<Paint> arrowTint = new SimpleObjectProperty<>(Color.WHITE);

    /** Hover color used for both the title and arrow. */
    private final ObjectProperty<Paint> hoverColor = new SimpleObjectProperty<>(Color.LIGHTGRAY);

    /** Whether the arrow icon rotation is animated. */
    private final BooleanProperty animated = new SimpleBooleanProperty(this, "animated", true);

    /** Label for the title. */
    private final Label titleLabel = new Label();

    /** Arrow icon shown on the right of the header. */
    private final ImageView arrowIcon = new ImageView();

    /** Container for child content (shown when expanded). */
    private final VBox contentBox = new VBox();

    /** Header container holding title and arrow. */
    private final HBox headerBox = new HBox();

    /** Underline rectangle shown below the header. */
    private final Rectangle underline = new Rectangle();

    /** Default constructor using default title and an empty VBox as content. */
    public OFxExpandablePane() {
        this("Title", new VBox());
    }

    /**
     * Constructs an expandable pane with specified title and content.
     * @param title the header title.
     * @param content the content node to show on expand.
     */
    public OFxExpandablePane(String title, Node content) {
        setText(title);
        contentBox.getChildren().add(content);
        initialize();
    }

    /** Initializes all bindings, layout configurations, and event handlers. */
    private void initialize() {
        getStyleClass().add("expandable-list-section");

        // Bind visual properties
        titleLabel.textProperty().bind(text);
        titleLabel.fontProperty().bind(font);
        titleLabel.getStyleClass().add("title-label");
        titleLabel.setMaxWidth(Double.MAX_VALUE);

        // Arrow icon settings
        arrowIcon.fitWidthProperty().bind(arrowIconWidth);
        arrowIcon.fitHeightProperty().bind(arrowIconHeight);
        arrowIcon.setPreserveRatio(true);
        arrowIcon.setSmooth(true);
        arrowIcon.imageProperty().bind(arrowImage);
        arrowIcon.setOpacity(0.8);

        // Bind hover color logic for text and underline
        titleLabel.textFillProperty().bind(Bindings.createObjectBinding(() ->
                        headerBox.isHover() ? hoverColor.get() : textFill.get(),
                headerBox.hoverProperty(), hoverColor, textFill));

        underline.fillProperty().bind(Bindings.createObjectBinding(() ->
                        headerBox.isHover() ? hoverColor.get() : textFill.get(),
                headerBox.hoverProperty(), hoverColor, textFill));

        // Arrow tint effect
        arrowIcon.effectProperty().bind(Bindings.createObjectBinding(() ->
                        new Blend(
                                BlendMode.SRC_ATOP,
                                null,
                                new ColorInput(0, 0, arrowIcon.getFitWidth(), arrowIcon.getFitHeight(),
                                        (Color) (headerBox.isHover() ? hoverColor.get() : arrowTint.get()))
                        ),
                headerBox.hoverProperty(), hoverColor, arrowTint,
                arrowIcon.fitWidthProperty(), arrowIcon.fitHeightProperty()
        ));

        // Spacer between title and icon
        Region spacer = new Region();
        HBox.setHgrow(spacer, Priority.ALWAYS);

        headerBox.getChildren().addAll(titleLabel, spacer, arrowIcon);
        headerBox.setAlignment(Pos.CENTER_LEFT);
        headerBox.setPadding(new Insets(6, 8, 6, 8));

        // Click event toggles expand state and rotates arrow
        headerBox.setOnMouseClicked(e -> {
            boolean willExpand = !isExpanded();
            setExpanded(willExpand);
            if (isAnimated()) animateArrow(willExpand);
            else arrowIcon.setRotate(willExpand ? 90 : 0);
        });

        // Underline layout config
        underline.setHeight(1);
        underline.visibleProperty().bind(underlineVisible);
        underline.managedProperty().bind(underlineVisible);
        underline.widthProperty().bind(headerBox.widthProperty());

        VBox underlineBox = new VBox(headerBox, underline);

        // Bind content visibility to expanded state
        contentBox.managedProperty().bind(expanded);
        contentBox.visibleProperty().bind(expanded);
        contentBox.setOpacity(1);

        super.getChildren().addAll(underlineBox);

        // Sync underline pseudo-class
        underlineVisible.addListener((obs, oldVal, newVal) -> pseudoClassStateChanged(UNDERLINE_VISIBLE, newVal));
        pseudoClassStateChanged(UNDERLINE_VISIBLE, underlineVisible.get());
    }

    @Override
    public ObservableList<Node> getChildren() {
        return contentBox.getChildren();
    }

    /** Animates the arrow icon on expand/collapse. */
    private void animateArrow(boolean expand) {
        RotateTransition rotate = new RotateTransition(Duration.millis(100), arrowIcon);
        rotate.setFromAngle(expand ? 0 : 90);
        rotate.setToAngle(expand ? 90 : 0);
        rotate.play();
    }

    // ----- Public API Getters/Setters and Properties (omitted comments for brevity) -----

    public void expand() { setExpanded(true); }
    public void collapse() { setExpanded(false); }
    public boolean isExpanded() { return expanded.get(); }
    public void setExpanded(boolean value) { expanded.set(value); }
    public BooleanProperty expandedProperty() { return expanded; }
    public boolean isUnderlineVisible() { return underlineVisible.get(); }
    public void setUnderlineVisible(boolean value) { underlineVisible.set(value); }
    public BooleanProperty underlineVisibleProperty() { return underlineVisible; }
    public Image getArrowImage() { return arrowImage.get(); }
    public void setArrowImage(Image image) { this.arrowImage.set(image); }
    public ObjectProperty<Image> arrowImageProperty() { return arrowImage; }
    public double getArrowIconWidth() { return arrowIconWidth.get(); }
    public void setArrowIconWidth(double width) { this.arrowIconWidth.set(width); }
    public DoubleProperty arrowIconWidthProperty() { return arrowIconWidth; }
    public double getArrowIconHeight() { return arrowIconHeight.get(); }
    public void setArrowIconHeight(double height) { this.arrowIconHeight.set(height); }
    public DoubleProperty arrowIconHeightProperty() { return arrowIconHeight; }
    public String getText() { return text.get(); }
    public void setText(String value) { text.set(value); }
    public StringProperty textProperty() { return text; }
    public Font getFont() { return font.get(); }
    public void setFont(Font value) { font.set(value); }
    public ObjectProperty<Font> fontProperty() { return font; }
    public Paint getTextFill() { return textFill.get(); }
    public void setTextFill(Paint value) { textFill.set(value); }
    public ObjectProperty<Paint> textFillProperty() { return textFill; }
    public Paint getArrowTint() { return arrowTint.get(); }
    public void setArrowTint(Paint value) { arrowTint.set(value); }
    public ObjectProperty<Paint> arrowTintProperty() { return arrowTint; }
    public Paint getHoverColor() { return hoverColor.get(); }
    public void setHoverColor(Paint value) { hoverColor.set(value); }
    public ObjectProperty<Paint> hoverColorProperty() { return hoverColor; }
    public boolean isAnimated() { return animated.get(); }
    public void setAnimated(boolean value) { animated.set(value); }
    public BooleanProperty animatedProperty() { return animated; }
}

