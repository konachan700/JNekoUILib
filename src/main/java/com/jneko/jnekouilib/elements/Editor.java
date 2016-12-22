package com.jneko.jnekouilib.elements;

import com.jneko.jnekouilib.anno.UIFieldType;
import com.jneko.jnekouilib.anno.UILibDataSource;
import com.jneko.jnekouilib.anno.UIStringField;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class Editor extends VBox {
    protected static final String 
            okStyle = "controlsGoodInput", 
            errorStyle = "controlsBadInput";
    
    private enum FieldType {
        BOOLEAN, STRING, LONG
    }
    
    private class EditorMethods {
        public Method getter;
        public Method setter;
        public FieldType fieldType;
        public Parent uiElementRef;
        public Object ref;
    }
    
    private final Map<String, EditorMethods>
            methodsMap = new HashMap<>();
    
    private final Label
            title = new Label();
    
    private final ScrollPane
            elementsSP= new ScrollPane();
    
    private final VBox
            vContainer = new VBox();
    
    private void genUI() {
        super.getStylesheets().add("/styles/window.css");
        
        this.getStyleClass().addAll("maxHeight", "maxWidth");
        title.getStyleClass().addAll("StringFieldElementLabel", "maxWidth"); 

        elementsSP.getStyleClass().addAll("maxWidth", "maxHeight", "ScrollPane");
        elementsSP.setContent(vContainer);
        elementsSP.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        elementsSP.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
        elementsSP.setFitToWidth(true);
        elementsSP.setFitToHeight(false);
    }
    
    public Editor() {
        super();
        genUI();
        
        super.getChildren().addAll(elementsSP);
    }
    
    public Editor(String eTitle) {
        super();
        genUI();
        
        if (eTitle != null) title.setText(eTitle);
        super.getChildren().addAll(title, elementsSP);
    }
    
    public Editor(Parent customHeader, Parent customFooter) {
        super();
        genUI();
        
        if (customHeader != null) super.getChildren().add(customHeader);
        super.getChildren().add(elementsSP);
        if (customFooter != null) super.getChildren().add(customFooter);
    }
    
    public void validateForm(EditorFormValidator efv) {
        final Set<String> ml = methodsMap.keySet();
        ml.forEach(element -> {
            final EditorMethods em = methodsMap.get(element);
            if (em.uiElementRef instanceof EditorTypeValidable)
                ((EditorTypeValidable) em.uiElementRef).setValid(efv.validateForm(em.ref, em.uiElementRef));
        });
    }
    
    
    
    
    
    private void processStrAnno(Object o, Method m) {
        final String mName = m.getAnnotation(UIStringField.class).name();
        if (mName == null)
            return;

        if (!methodsMap.containsKey(mName)) {
            final EditorMethods mm = new EditorMethods();
            mm.fieldType = FieldType.STRING;
            mm.ref = o;
            methodsMap.put(mName, mm);
        }

        if (m.getAnnotation(UIStringField.class).type() == UIFieldType.SETTER) {
            methodsMap.get(mName).setter = m;
            System.err.println("ms = "+m.getName());
        } else if (m.getAnnotation(UIStringField.class).type() == UIFieldType.GETTER) {
            if (m.getReturnType().equals(String.class)) {
                methodsMap.get(mName).getter = m;

                final EditorStringLineElement elString = new EditorStringLineElement();

                final String annoLabel = m.getAnnotation(UIStringField.class).labelText();
                if (annoLabel != null) 
                    elString.setXLabelText(annoLabel);

                final String annoHelp = m.getAnnotation(UIStringField.class).helpText();
                if (annoHelp != null) 
                    elString.setXTextHelp(annoHelp); 

                final int annoMaxChars = m.getAnnotation(UIStringField.class).maxChars();
                elString.setXTextMaxChars(annoMaxChars); 

                final int annoRO = m.getAnnotation(UIStringField.class).readOnly();
                elString.setXTextReadOnly(annoRO == 0);

                String annoRetVal;
                try {
                    annoRetVal = (String) m.invoke(o);
                    if (annoRetVal != null) elString.setXText(annoRetVal);

                } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
                }

                vContainer.getChildren().add(elString);
                methodsMap.get(mName).uiElementRef = elString;
            }
        }
    }
    
    public void parseObject(Object obj) {
        vContainer.getChildren().clear();
        
        if (obj == null) return;
        
        final Class<?> cl = obj.getClass();
        if (cl == null) return;
        if (!cl.isAnnotationPresent(UILibDataSource.class)) return;
        
        final Method[] methods = cl.getMethods();
        if (methods == null) return;
        if (methods.length == 0) return;
        
        for (Method m : methods) {
            if (m.isAnnotationPresent(UIStringField.class)) processStrAnno(obj, m);
        }
    }
    
    private void saveString(EditorMethods em) {
        if ((em.getter == null) || (em.setter == null)) return;
        if (! (em.uiElementRef instanceof EditorStringLineElement)) return;
        
        final EditorStringLineElement elString = (EditorStringLineElement) em.uiElementRef;
        try {
            em.setter.invoke(em.ref, elString.getXText());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveObject() {
        final Set<String> ml = methodsMap.keySet();
        ml.forEach(element -> {
            final EditorMethods em = methodsMap.get(element);
            switch (em.fieldType) {
                case STRING:
                    saveString(em);
                    break;
                case LONG:
                    
                    break;
                case BOOLEAN:
                    
                    break;   
            }
        });
    }
}
