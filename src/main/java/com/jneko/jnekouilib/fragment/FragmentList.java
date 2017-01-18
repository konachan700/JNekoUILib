package com.jneko.jnekouilib.fragment;

import com.jneko.jnekouilib.editor.Editor;
import com.jneko.jnekouilib.editor.EditorFormValidator;
import com.jneko.jnekouilib.panel.Panel;
import com.jneko.jnekouilib.panel.PanelButton;
import com.jneko.jnekouilib.utils.MessageBus;
import java.util.ArrayList;
import java.util.Collection;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class FragmentList<T> extends Fragment implements FragmentListItemActionListener<T> {
    private Collection<T> 
            items;
    
    private final ArrayList<FragmentListItem<T>>
            uiItems = new ArrayList<>();
    
    private final ScrollPane
            elementsSP= new ScrollPane();
    
    private final VBox
            vContainer = new VBox();
    
    private PanelButton 
            pbAdd = null,
            pbDel = null,
            pbEdit = null;
    
    private T
            selectedObject = null;
    
    private FragmentListItem
            selectedItem = null;
    
    private final Editor
            editor = new Editor();
    
    private final ArrayList<Node> 
            lastPanelButtons = new ArrayList<>();
    
    public void setCollection(Collection<T> c) {
        items = c;
    }
    
    public FragmentList(String collectionRefName) {
        super();

        this.getStyleClass().addAll("maxHeight", "maxWidth");

        elementsSP.getStyleClass().addAll("maxWidth", "maxHeight", "ScrollPane");
        elementsSP.setContent(vContainer);
        elementsSP.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        elementsSP.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        elementsSP.setFitToWidth(true);
        elementsSP.setFitToHeight(false);
        
        super.getChildren().addAll(elementsSP);
        
        MessageBus.registerMessageReceiver(collectionRefName, "refresh", (event, pl) -> {
            create();
        });
    }
    
    public final void create() {
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
    
    private void goBack(Panel p) {
        super.getHost().back();
        p.clear();
        p.setAll(lastPanelButtons);
        create();
    }
    
    public void addItemForm(Panel p, T newObject, EditorFormValidator efv) {
        editor.readObject(newObject); 
        
        final PanelButton pbBack = new PanelButton("iconBackForList", "Exit without save", ex -> {
                goBack(p);
            });
        
        final PanelButton pbSave = new PanelButton("iconSaveForList", "Save and exit", ex -> {
                editor.validateForm(efv);
                editor.saveObject();
                items.add(newObject);
                MessageBus.sendMessage("hibernate", "addnew", newObject);
                goBack(p);
            });
        
        lastPanelButtons.clear();
        lastPanelButtons.addAll(p.getChildren());
        
        p.clear();
        p.addNodes(pbBack, pbSave); 
        
        super.getHost().showFragment(editor, false);
    }
    
    public PanelButton getAddButton(Panel p, FragmentListObjectRequester<T> req, EditorFormValidator efv) {
        if (pbAdd == null) 
            pbAdd = new PanelButton("iconAddForList", "Add item to list...", e -> {
                addItemForm(p, req.requestObject(null), efv);
            });
        return pbAdd;
    }
    
    public void deleteSelectedItem() {
        if (selectedObject == null) return;
        
        items.remove(selectedObject);
        MessageBus.sendMessage("hibernate", "remove", selectedObject);
        
        selectedObject = null;
        selectedItem = null;
        
        create();
    }
    
    public PanelButton getDelButton() {
        if (pbDel == null) 
            pbDel = new PanelButton("iconDeleteForList", "Delete item from list", e -> {
                deleteSelectedItem();
            });
        return pbDel;
    }
    
    public void addCollectionHelper(String name, Collection items) {
        editor.addCollectionHelper(name, items);
    }
    
    public void editSelectedItem(Panel p, EditorFormValidator efv) {
        if (selectedObject == null) return;

        final PanelButton pbBack = new PanelButton("iconBackForList", "Exit without save", e -> {
                goBack(p);
            });
        
        final PanelButton pbSave = new PanelButton("iconSaveForList", "Save and exit", e -> {
                editor.validateForm(efv);
                editor.saveObject();
                MessageBus.sendMessage("hibernate", "edit", selectedObject);
                goBack(p);
            });
        
        lastPanelButtons.clear();
        lastPanelButtons.addAll(p.getChildren());
        
        p.clear();
        p.addNodes(pbBack, pbSave); 

        editor.readObject(selectedObject); 
        super.getHost().showFragment(editor, false);
    }
    
    public PanelButton getEditButton(Panel p, EditorFormValidator efv) {
        if (pbEdit == null) 
            pbEdit = new PanelButton("iconEditForList", "Edit selected item...", e -> {
                editSelectedItem(p, efv);
            });
        return pbEdit;
    }
    
    @Override
    public void OnItemClick(T object, FragmentListItem fli) {
        uiItems.forEach(item -> {
            item.setSelected(false);
        });
        fli.setSelected(true);
        
        selectedObject = object;
        selectedItem = fli;
    }
    
    public T getItem(int index) {
        return uiItems.get(index).getMyObject();
    }
    
    public FragmentListItem<T> getUIItem(int index) {
        return uiItems.get(index);
    }
}
