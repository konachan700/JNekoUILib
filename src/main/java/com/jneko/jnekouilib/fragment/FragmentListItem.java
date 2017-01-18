package com.jneko.jnekouilib.fragment;

import com.jneko.jnekouilib.anno.UIListItem;
import com.jneko.jnekouilib.anno.UIListItemHeader;
import com.jneko.jnekouilib.anno.UIListItemRightText;
import com.jneko.jnekouilib.anno.UIListItemSubtitle;
import com.jneko.jnekouilib.anno.UIListItemTextLine;
import com.jneko.jnekouilib.editor.Editor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.geometry.Pos;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import jiconfont.javafx.IconNode;

public class FragmentListItem<T> extends HBox {
    private final T
            myObject;
    
    private final Label 
            mainTitle = new Label(),
            subTitle  = new Label(),
            textLine  = new Label(),
            rightText = new Label();
    
    private boolean 
            selected = false;
    
    private final IconNode 
            bigIcon   = new IconNode();
    
    private final FragmentListItemActionListener<T>
            actionListener;
    
    public FragmentListItem(T object, FragmentListItemActionListener<T> al) {
        myObject = object;
        actionListener = al;
        
        this.getStyleClass().addAll(Editor.maxWidthStyle, "FLI_this");
        this.setAlignment(Pos.CENTER);
        this.setOnMouseClicked(value -> {
            actionListener.OnItemClick(myObject, this); 
        });
        
        bigIcon.getStyleClass().addAll("FLI_bigIcon");
        mainTitle.getStyleClass().addAll("FLI_mainTitle");
        subTitle.getStyleClass().addAll("FLI_subTitle");
        textLine.getStyleClass().addAll("FLI_textLine");
        rightText.getStyleClass().addAll("FLI_rightText");
        
        rightText.setAlignment(Pos.CENTER_RIGHT);
    }
    
    private VBox createVBox() {
        final VBox sep = new VBox();
        sep.getStyleClass().addAll(Editor.maxWidthStyle);
        return sep;
    }
    
    private HBox createHBox() {
        final HBox sep = new HBox();
        sep.getStyleClass().addAll(Editor.maxWidthStyle);
        return sep;
    }
    
    public void create() {
        if (getMyObject() == null) return;
        if (getMyObject() instanceof String) {
            final String text = (String) getMyObject();
            mainTitle.setText(text);
            this.getChildren().addAll(bigIcon, mainTitle);
        } else if (getMyObject() instanceof Number) {
            final Number val = (Number) getMyObject();
            mainTitle.setText("#"+val.toString());
            this.getChildren().addAll(bigIcon, mainTitle);
        } else {
            if (! myObject.getClass().isAnnotationPresent(UIListItem.class)) return;
            
            final Method[] methods = getMyObject().getClass().getMethods();
            if (methods == null) return;
            if (methods.length == 0) return;
            
            for (Method method : methods) {
                try {
                    String retVal;
                    if (method.isAnnotationPresent(UIListItemHeader.class)) {
                        retVal = (String) method.invoke(getMyObject());
                        if (retVal != null) mainTitle.setText(retVal); else mainTitle.setText("");
                    }

                    if (method.isAnnotationPresent(UIListItemSubtitle.class)) {
                        retVal = (String) method.invoke(getMyObject());
                        if (retVal != null) subTitle.setText(retVal); else subTitle.setText("");
                    }

                    if (method.isAnnotationPresent(UIListItemTextLine.class)) {
                        retVal = (String) method.invoke(getMyObject());
                        if (retVal != null) textLine.setText(retVal); else textLine.setText("");
                    }

                    if (method.isAnnotationPresent(UIListItemRightText.class)) {
                        retVal = (String) method.invoke(getMyObject());
                        if (retVal != null) rightText.setText(retVal); else rightText.setText("");
                    }
                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                }
            }

            final VBox rootContainer = createVBox();
            rootContainer.setAlignment(Pos.CENTER);
            this.getChildren().addAll(bigIcon, rootContainer);
            
            final HBox headerContainer = createHBox();
            headerContainer.setAlignment(Pos.CENTER);
            headerContainer.getChildren().addAll(mainTitle, createHBox(), rightText);
            
            rootContainer.getChildren().addAll(headerContainer, subTitle, textLine);
        }
    }

    public T getMyObject() {
        return myObject;
    }

    public boolean isSelected() {
        return selected;
    }

    public void setSelected(boolean selected) {
        this.selected = selected;
        
        bigIcon.getStyleClass().clear();
        bigIcon.getStyleClass().addAll((selected) ? "FLI_bigIconSelected" : "FLI_bigIcon");
        
        this.getStyleClass().removeAll("FLI_thisSelected", "FLI_this");
        this.getStyleClass().addAll((selected) ? "FLI_thisSelected" : "FLI_this");
    }
}
