package com.jneko.jnekouilib.elements;

import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class EditorStringLineElement extends VBox implements EditorTypeText, EditorTypeLabeled, EditorTypeValidable {
    private final TextField
            field = new TextField();
    
    private final Label
            title = new Label();
    
    public EditorStringLineElement() {
        super.getStyleClass().addAll("StringFieldElementRoot", "maxWidth");
        title.getStyleClass().addAll("StringFieldElementLabel", "maxWidth");
        field.getStyleClass().addAll("StringFieldElementText", "maxWidth");
        
        super.getChildren().addAll(title, field);
    }

    @Override
    public void setXText(String text) {
        field.setText(text);
    }

    @Override
    public String getXText() {
        return field.getText();
    }

    @Override
    public boolean isXTextEmpty() {
        return (field.getText().trim().length() <= 0);
    }

    @Override
    public void setXLabelText(String text) {
        title.setText(text);
    }

    @Override
    public void setXTextHelp(String text) {
        field.setPromptText(text);
    }

    @Override
    public void setXTextReadOnly(boolean ro) {
        field.setEditable(ro);
    }

    @Override
    public void setXTextMaxChars(int max) {
    }

    @Override
    public void setValid(boolean v) {
        field.getStyleClass().remove(Editor.okStyle);
        field.getStyleClass().remove(Editor.errorStyle);
        field.getStyleClass().add((v) ? Editor.okStyle : Editor.errorStyle);
    }
}
