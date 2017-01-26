package com.jneko.jnekouilib.fragment;

import com.jneko.jnekouilib.editor.Editor;
import com.jneko.jnekouilib.editor.EditorFormValidator;
import com.jneko.jnekouilib.panel.PanelButton;
import com.jneko.jnekouilib.utils.MessageBus;
import com.jneko.jnekouilib.utils.MessageBusActions;
import com.jneko.jnekouilib.utils.UIUtils;
import java.util.ArrayList;
import java.util.Collection;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
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
    
    private T
            selectedObject = null;
    
    private FragmentListItem
            selectedItem = null;
    
    private final Editor
            editor = new Editor();
    
    private EditorFormValidator
            formValForEdit = null,
            formValForAdd = null;
    
    private FragmentListObjectRequester<T>
            objectRequesterForNew = null;
    
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
        
        MessageBus.registerMessageReceiver( 
                MessageBusActions.EditorFragmentListRefresh, 
                (b, objects) -> {
            final String msg = UIUtils.getStringFromObject(0, objects);
            if (msg == null) return;
            if (msg.equalsIgnoreCase(collectionRefName)) create();
        });
        
        super.getPanel().clear();
        super.getPanel().addSeparator();
        super.getPanel().addNodes(
                new PanelButton("iconAddForList", "Add item to list...", e -> {
                    addItemForm();
                }),
                new PanelButton("iconDeleteForList", "Delete item from list", e -> {
                    deleteSelectedItem();
                }),
                new PanelButton("iconEditForList", "Edit selected item...", e -> {
                    editSelectedItem();
                })
        );
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
    
    private void goBack() {
        super.getHost().back();
        create();
    }
    
    public void addItemForm() {
        if (objectRequesterForNew == null) return;
        
        final T newObject = objectRequesterForNew.requestObject(null);
        
        editor.readObject(newObject); 
        
        System.out.println(newObject.getClass().getName());
        
        editor.getPanel().clear();
        editor.getPanel().addSeparator();
        editor.getPanel().addNodes(
                new PanelButton("iconBackForList", "Exit without save", ex -> {
                        goBack();
                }),
                new PanelButton("iconSaveForList", "Save and exit", ex -> {
                    if (formValForAdd != null) 
                        editor.validateForm(formValForAdd);
                    editor.saveObject();
                    items.add(newObject);
                    MessageBus.sendMessage(MessageBusActions.HibernateAddNew, newObject);
                    goBack();
                })
        );
        
        super.getHost().showFragment(editor, false);
    }

    
    public void deleteSelectedItem() {
        if (selectedObject == null) return;
        
        items.remove(selectedObject);
        MessageBus.sendMessage(MessageBusActions.HibernateDelete, selectedObject);
        
        selectedObject = null;
        selectedItem = null;
        
        create();
    }

    
    public void addCollectionHelper(String name, Collection items) {
        editor.addCollectionHelper(name, items);
    }
    
    public void editSelectedItem() {
        if (selectedObject == null) return;
        if (objectRequesterForNew == null) return;
        
        editor.readObject(selectedObject);
        
        editor.getPanel().clear();
        editor.getPanel().addSeparator();
        editor.getPanel().addNodes(
                new PanelButton("iconBackForList", "Exit without save", e -> {
                    goBack();
                }),
                new PanelButton("iconSaveForList", "Save and exit", e -> {
                    if (formValForEdit != null) 
                            editor.validateForm(formValForEdit);
                    editor.saveObject();
                    MessageBus.sendMessage(MessageBusActions.HibernateEdit, selectedObject);
                    goBack();
                })
        );

        super.getHost().showFragment(editor, false);
    }
    
    @Override
    public void OnItemClick(T object, FragmentListItem fli, MouseEvent me) {
        uiItems.forEach(item -> {
            item.setSelected(false);
        });
        fli.setSelected(true);
        
        selectedObject = object;
        selectedItem = fli;
    }
    
    public T getSelectedObject() {
        return selectedObject;
    }
    
    public T getItem(int index) {
        return uiItems.get(index).getMyObject();
    }
    
    public FragmentListItem<T> getUIItem(int index) {
        return uiItems.get(index);
    }

    public EditorFormValidator getFormValForEdit() {
        return formValForEdit;
    }

    public void setFormValForEdit(EditorFormValidator formValForEdit) {
        this.formValForEdit = formValForEdit;
    }

    public EditorFormValidator getFormValForAdd() {
        return formValForAdd;
    }

    public void setFormValForAdd(EditorFormValidator formValForAdd) {
        this.formValForAdd = formValForAdd;
    }

    public FragmentListObjectRequester<T> getObjectRequesterForNew() {
        return objectRequesterForNew;
    }

    public void setObjectRequesterForNew(FragmentListObjectRequester<T> objectRequesterForNew) {
        this.objectRequesterForNew = objectRequesterForNew;
    }
}
