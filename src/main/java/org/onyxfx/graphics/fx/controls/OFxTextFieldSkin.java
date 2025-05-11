package org.onyxfx.graphics.fx.controls;

import javafx.scene.control.SkinBase;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.paint.Paint;
import javafx.scene.text.Text;

public class OFxTextFieldSkin extends SkinBase<OFxTextField> {

    private StackPane root;
    private Text text;

    public OFxTextFieldSkin(OFxTextField control) {
        super(control);

        root = new StackPane();
        text = new Text("Your Custom TextField");

        // Add your custom visual logic
        root.getChildren().add(text);

        // Apply colors based on the state of the TextField
        getSkinnable().normalColorProperty().addListener((obs, oldColor, newColor) -> {
            if (newColor != null) {
                root.setStyle("-fx-background-color: " + toHex(newColor) + ";");
            }
        });

        // Add more listeners for hover, pressed, focused states...
        
        getChildren().add(root);
    }

    // Convert Paint to hex string
    private String toHex(Paint paint) {
        if (paint instanceof Color) {
            Color color = (Color) paint;
            return String.format("#%02X%02X%02X", (int)(color.getRed() * 255), (int)(color.getGreen() * 255), (int)(color.getBlue() * 255));
        }
        return "#FFFFFF"; // Default fallback
    }
}