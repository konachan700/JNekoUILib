package com.jneko.jnekouilib.fragment;

import com.jneko.jnekouilib.anno.UIListItem;
import com.jneko.jnekouilib.anno.UIListItemHeader;
import com.jneko.jnekouilib.editor.Editor;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.control.Button;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public class FragmentList extends Fragment {
    private class FragmentListHelper {
        public Method getterForName;
        public Collection allData;
        public Collection selData;
        public Object currentItem;
        public Parent uiElement;
        public String uiName;
    }
    
    private final ArrayList<FragmentListHelper>
            flHelper = new ArrayList<>();
    
    private final ScrollPane
            elementsSP= new ScrollPane();
    
    private final VBox
            vContainer = new VBox();
    
     private final HBox
            vYesNoContainer = new HBox();
    
    private final Button
            bYes = new Button("Save"),
            bNo = new Button("Back");
    
    private boolean
            isYesNoHeaderPresent = true,
            isMultiselect = false;
    
    private FragmentListActionListener
            actionListener = null;
    
    private Collection 
            selectedItems = new HashSet();
    
    private final Label
            lNoItems = new Label("No items in this list");
    
    private void onYesClicked() {
        
    }
    
    public FragmentList(boolean isYesNoHeaderPresent, boolean isMultiselect) {
        this.isYesNoHeaderPresent = (isMultiselect) ? true : isYesNoHeaderPresent;  
        this.isMultiselect = isMultiselect;
        
        this.getStyleClass().addAll("maxHeight", "maxWidth");
        
        lNoItems.getStyleClass().addAll("StringFieldElementLabel", "maxWidth");

        elementsSP.getStyleClass().addAll("maxWidth", "maxHeight", "ScrollPane");
        elementsSP.setContent(vContainer);
        elementsSP.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        elementsSP.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        elementsSP.setFitToWidth(true);
        elementsSP.setFitToHeight(false);
        
        if (isYesNoHeaderPresent) {
            bYes.getStyleClass().addAll("yesno_button");
            bYes.setOnAction(c -> {
                onYesClicked();
                if (actionListener != null) 
                    actionListener.OnListYesClick(selectedItems);
                super.back();
            });
            
            bNo.getStyleClass().addAll("yesno_button");
            bNo.setOnAction(c -> {
                if (actionListener != null) 
                    actionListener.OnListNoClick();
                super.back();
            });
            
            final VBox sep1 = new VBox();
            sep1.getStyleClass().addAll("maxWidth");
            
            vYesNoContainer.getStyleClass().addAll("ynbox1", "maxWidth");
            vYesNoContainer.getChildren().addAll(bNo, sep1, bYes);
            super.getChildren().add(vYesNoContainer);
        }
        
        super.getChildren().add(elementsSP);
    }
    
    private void setNoItems() {
        vContainer.getChildren().addAll(lNoItems);
    }
    
    public void readCollection(Collection selectedList, Collection fullList, FragmentListActionListener fl) {
        actionListener = fl;
        vContainer.getChildren().clear();
        
        if ((fullList == null) || (selectedList == null)) {
            setNoItems();
            return;
        }
        
        if (fullList.isEmpty()) {
            setNoItems();
            return;
        }
                
        fullList.forEach(el -> {
            if (el.getClass().isAnnotationPresent(UIListItem.class)) {
                final Method[] methods = el.getClass().getMethods();
                if (methods == null) return;
                if (methods.length == 0) return;
                
                for (Method m : methods) {
                    if (m.isAnnotationPresent(UIListItemHeader.class)) {
                        if (m.getReturnType().equals(String.class)) {
                            String itemName = null;
                            try {
                                itemName = (String) m.invoke(el);
                            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                                Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                            }

                            final FragmentListHelper f = new FragmentListHelper();
                            f.uiName = itemName;

                            if (isMultiselect) {
                                final CheckBox check = new CheckBox();
                                check.getStyleClass().addAll("checkBoxList", Editor.maxWidthStyle);
                                check.setSelected(selectedList.contains(el)); 
                                if (itemName != null) check.setText(itemName);
                                vContainer.getChildren().add(check);
                                f.uiElement = check;
                            } else {
                                final Label lItem = new Label();
                                lItem.getStyleClass().addAll("labelList", Editor.maxWidthStyle);
                                if (!isYesNoHeaderPresent) lItem.setOnMouseClicked(event -> {
                                    onYesClicked();
                                    if (actionListener != null) {
                                        actionListener.OnListYesClick(selectedItems);
                                    }
                                });
                                if (itemName != null) lItem.setText(itemName);
                                vContainer.getChildren().add(lItem);
                                f.uiElement = lItem;
                            }

                            f.allData = fullList;
                            f.currentItem = el;
                            f.getterForName = m;
                            f.selData = selectedList;
                            flHelper.add(f);
                            
                            break;
                        }
                    }
                }
            }
        });
    }
}
