package com.jneko.jnekouilib.editor;

import com.jneko.jnekouilib.anno.UIStringField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;

public class ElementTextField extends VBox implements EditorTypeText, EditorTypeLabeled, EditorTypeValidable, EditorFillable {
    private final TextField
            field = new TextField();
    
    private final Label
            title = new Label();
    
    public ElementTextField() {
        super.getStyleClass().addAll("eStringFieldElementRoot", Editor.maxWidthStyle);
        title.getStyleClass().addAll("eStringFieldElementLabel", Editor.maxWidthStyle);
        field.getStyleClass().addAll("eStringFieldElementText", Editor.maxWidthStyle);
        
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
        field.getStyleClass().removeAll("eStringFieldElementText", "eStringFieldElementText_RO");
        field.getStyleClass().addAll((!ro) ? "eStringFieldElementText_RO" : "eStringFieldElementText");
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

    @Override
    public void fillFromObject(Object o, Method m) {
        final String annoLabel = m.getAnnotation(UIStringField.class).labelText();
        if (annoLabel != null) 
            setXLabelText(annoLabel);

        final String annoHelp = m.getAnnotation(UIStringField.class).helpText();
        if (annoHelp != null) 
            setXTextHelp(annoHelp); 

        final int annoMaxChars = m.getAnnotation(UIStringField.class).maxChars();
        setXTextMaxChars(annoMaxChars); 

        final int annoRO = m.getAnnotation(UIStringField.class).readOnly();
        setXTextReadOnly(annoRO == 0);

        String annoRetVal;
        try {
            annoRetVal = (String) m.invoke(o);
            if (annoRetVal != null) setXText(annoRetVal);

        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}