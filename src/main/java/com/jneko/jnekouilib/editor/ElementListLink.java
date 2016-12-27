package com.jneko.jnekouilib.editor;

import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import jiconfont.javafx.IconNode;

public class ElementListLink extends HBox implements EditorTypeLabeled {
    private final Label
            textLabel = new Label();
    
    private final IconNode 
            icon = new IconNode();
    
    public ElementListLink() {
        super.getStyleClass().addAll("eStringFieldElementRoot", Editor.maxWidthStyle);
        textLabel.getStyleClass().addAll("labelListOnEditor", Editor.maxWidthStyle);
        icon.getStyleClass().addAll("iconListOnEditor");
        super.getChildren().addAll(icon, textLabel);
    }

    @Override
    public void setXLabelText(String text) {
        textLabel.setText(text);
    }
}
