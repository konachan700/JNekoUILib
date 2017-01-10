package com.jneko.jnekouilib.fragment;

import java.util.ArrayList;
import java.util.Collection;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class FragmentList<T> extends Fragment implements FragmentListItemActionListener<T> {
    private final Collection<T> 
            items;
    
    private final ArrayList<FragmentListItem<T>>
            uiItems = new ArrayList<>();
    
    private final ScrollPane
            elementsSP= new ScrollPane();
    
    private final VBox
            vContainer = new VBox();
    
    public FragmentList(Collection<T> c) {
        super();
        items = c;
        
        this.getStyleClass().addAll("maxHeight", "maxWidth");

        elementsSP.getStyleClass().addAll("maxWidth", "maxHeight", "ScrollPane");
        elementsSP.setContent(vContainer);
        elementsSP.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        elementsSP.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        elementsSP.setFitToWidth(true);
        elementsSP.setFitToHeight(false);
        
        super.getChildren().addAll(elementsSP);
    }
    
    public void create() {
        if (items == null) return;
        
        uiItems.clear();
        vContainer.getChildren().clear();
        
        items.forEach(item -> {
            final FragmentListItem fli = new FragmentListItem(item, this);
            fli.create();
            uiItems.add(fli);
            vContainer.getChildren().add(fli);
        });
    }

    @Override
    public void OnItemClick(T object, FragmentListItem fli) {
        uiItems.forEach(item -> {
            item.setSelected(false);
        });
        fli.setSelected(true);
    }
    
    public T getItem(int index) {
        return uiItems.get(index).getMyObject();
    }
    
    public FragmentListItem<T> getUIItem(int index) {
        return uiItems.get(index);
    }
}
