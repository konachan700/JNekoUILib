package com.jneko.jnekouilib.elements;

import javafx.beans.value.ObservableValue;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class EditorNumberSimpleElement extends VBox implements EditorTypeText, EditorTypeLabeled, EditorTypeNumber, EditorTypeValidable {
    private boolean valid = false;
    private volatile long 
            value = 0,
            max = Long.MAX_VALUE,
            min = Long.MIN_VALUE;
    
    private final TextField
            field = new TextField();
    
    private final Label
            title = new Label();
    
    public EditorNumberSimpleElement() {
        field.textProperty().addListener((final ObservableValue<? extends String> observable, final String oldValue, final String newValue) -> {
            field.getStyleClass().remove(Editor.okStyle);
            field.getStyleClass().remove(Editor.errorStyle);
            try {
                value = Long.parseLong(newValue.trim(), 10);
                if ((value >= min) && (value <= max)) {
                    field.getStyleClass().add(Editor.okStyle);
                    valid = true;
                } else {
                    field.getStyleClass().add(Editor.errorStyle);
                    value = 0;
                    valid = false;
                }
            } catch (NumberFormatException e) { 
                valid = false;
                field.getStyleClass().add(Editor.errorStyle);
            }
        });
        
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
    public void setXNumberBorderValues(long min, long max) {
        this.max = max;
        this.min = min;
    }

    @Override
    public void setXNumber(long val) {
        field.setText(Long.toString(val));
        value = val;
    }

    @Override
    public long getXNumber() {
        return value;
    }

    @Override
    public boolean isXNumberValid() {
        return valid;
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
