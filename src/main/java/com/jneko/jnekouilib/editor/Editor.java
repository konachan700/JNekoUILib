package com.jneko.jnekouilib.editor;

import com.jneko.jnekouilib.anno.UIBooleanField;
import com.jneko.jnekouilib.anno.UIFieldType;
import com.jneko.jnekouilib.anno.UILibDataSource;
import com.jneko.jnekouilib.anno.UILongField;
import com.jneko.jnekouilib.anno.UISortIndex;
import com.jneko.jnekouilib.anno.UIStringField;
import com.jneko.jnekouilib.anno.UITextArea;
import com.jneko.jnekouilib.fragment.Fragment;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.scene.Parent;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.VBox;

public class Editor extends Fragment {
    protected static final String 
            okStyle         = "controlsGoodInput", 
            errorStyle      = "controlsBadInput",
            maxWidthStyle   = "maxWidth"
            ;
    
    private enum FieldType {
        BOOLEAN_CHECK, STRING, LONG, STRING_MULTILINE
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
    
    
    private void mmCreate(String mName, Object o, Method m, FieldType ft) {
        if (!methodsMap.containsKey(mName)) {
            final EditorMethods mm = new EditorMethods();
            mm.fieldType = ft;
            mm.ref = o;
            methodsMap.put(mName, mm);
        }
    }

    private void readObjectTextArea(Object o, Method m) {
        final String mName = m.getAnnotation(UITextArea.class).name();
        if (mName == null) return;
        mmCreate(mName, o, m, FieldType.STRING_MULTILINE);

        if (m.getAnnotation(UITextArea.class).type() == UIFieldType.SETTER) {
            methodsMap.get(mName).setter = m;
        } else if (m.getAnnotation(UITextArea.class).type() == UIFieldType.GETTER) {
            if (m.getReturnType().equals(String.class)) {
                methodsMap.get(mName).getter = m;

                final ElementTextArea elString = new ElementTextArea();
                elString.fillFromObject(o, m); 

                vContainer.getChildren().add(elString);
                methodsMap.get(mName).uiElementRef = elString;
            }
        }
    } 
    
    private void readObjectTextField(Object o, Method m) {
        final String mName = m.getAnnotation(UIStringField.class).name();
        if (mName == null) return;
        mmCreate(mName, o, m, FieldType.STRING);

        if (m.getAnnotation(UIStringField.class).type() == UIFieldType.SETTER) {
            methodsMap.get(mName).setter = m;
        } else if (m.getAnnotation(UIStringField.class).type() == UIFieldType.GETTER) {
            if (m.getReturnType().equals(String.class)) {
                methodsMap.get(mName).getter = m;

                final ElementTextField elString = new ElementTextField();
                elString.fillFromObject(o, m);

                vContainer.getChildren().add(elString);
                methodsMap.get(mName).uiElementRef = elString;
            }
        }
    }
    
    private void readObjectSimpleNumberField(Object o, Method m) {
        final String mName = m.getAnnotation(UILongField.class).name();
        if (mName == null) return;
        mmCreate(mName, o, m, FieldType.LONG);

        if (m.getAnnotation(UILongField.class).type() == UIFieldType.SETTER) {
            methodsMap.get(mName).setter = m;
        } else if (m.getAnnotation(UILongField.class).type() == UIFieldType.GETTER) {
            if (m.getReturnType().equals(long.class)) {
                methodsMap.get(mName).getter = m;

                final ElementSimpleNumberField elString = new ElementSimpleNumberField();
                elString.fillFromObject(o, m);

                vContainer.getChildren().add(elString);
                methodsMap.get(mName).uiElementRef = elString;
            }
        }
    }
    
    public void readObjectCheckBox(Object o, Method m) {
        final String mName = m.getAnnotation(UIBooleanField.class).name();
        if (mName == null) return;
        mmCreate(mName, o, m, FieldType.BOOLEAN_CHECK);

        if (m.getAnnotation(UIBooleanField.class).type() == UIFieldType.SETTER) {
            methodsMap.get(mName).setter = m;
        } else if (m.getAnnotation(UIBooleanField.class).type() == UIFieldType.GETTER) {
            if (m.getReturnType().equals(boolean.class)) {
                methodsMap.get(mName).getter = m;

                final ElementCheckBox elString = new ElementCheckBox();
                elString.fillFromObject(o, m);

                vContainer.getChildren().add(elString);
                methodsMap.get(mName).uiElementRef = elString;
            }
        }
    }
    
    private void createSeparator() {
        final VBox separator = new VBox();
        separator.setMaxSize(9999, 8);
        separator.setPrefSize(9999, 8);
        separator.setMinSize(8, 8);
        vContainer.getChildren().add(separator);
    }
    
    private void createSeparatorHeader(String name) {
        final Label sh = new Label(name);
        sh.getStyleClass().addAll("StringFieldElementLabel", "maxWidth");
        vContainer.getChildren().add(sh);
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
        
        final List<Method> mSorted = Arrays.asList(methods);
        mSorted.sort((a, b) -> {
               final Long 
                       valA = (a.isAnnotationPresent(UISortIndex.class)) ? a.getAnnotation(UISortIndex.class).index() : -9999,
                       valB = (b.isAnnotationPresent(UISortIndex.class)) ? b.getAnnotation(UISortIndex.class).index() : -9999;
               return valA.compareTo(valB);
        });
        
        for (int i=0; i<mSorted.size(); i++) {
            final Method m = mSorted.get(i);
            if (m.isAnnotationPresent(UIStringField.class))     readObjectTextField(obj, m);
            if (m.isAnnotationPresent(UITextArea.class))        readObjectTextArea(obj, m);
            if (m.isAnnotationPresent(UILongField.class))       readObjectSimpleNumberField(obj, m);
            if (m.isAnnotationPresent(UIBooleanField.class))    readObjectCheckBox(obj, m);
            
            if (m.isAnnotationPresent(UISortIndex.class)) {
                if (m.getAnnotation(UISortIndex.class).separatorPresent() == 1) {
                    createSeparator();
                    if (m.getAnnotation(UISortIndex.class).separatorName().length() > 0) 
                        createSeparatorHeader(m.getAnnotation(UISortIndex.class).separatorName());
                }
            }
        }
    }
    
    private void saveStringField(EditorMethods em) {    
        if (! (em.uiElementRef instanceof ElementTextField)) return;
        
        final ElementTextField elString = (ElementTextField) em.uiElementRef;
        try {
            em.setter.invoke(em.ref, elString.getXText());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void saveTextArea(EditorMethods em) {
        if (! (em.uiElementRef instanceof ElementTextArea)) return;
        
        final ElementTextArea elString = (ElementTextArea) em.uiElementRef;
        try {
            em.setter.invoke(em.ref, elString.getXText());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void saveSimpleNumberField(EditorMethods em) {
        if (! (em.uiElementRef instanceof ElementSimpleNumberField)) return;
        
        final ElementSimpleNumberField elString = (ElementSimpleNumberField) em.uiElementRef;
        try {
            em.setter.invoke(em.ref, elString.getXNumber());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private void saveCheckBox(EditorMethods em) {
        if (! (em.uiElementRef instanceof ElementCheckBox)) return;
        
        final ElementCheckBox elString = (ElementCheckBox) em.uiElementRef;
        try {
            em.setter.invoke(em.ref, elString.getValue());
        } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
            Logger.getLogger(Editor.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    public void saveObject() {
        final Set<String> ml = methodsMap.keySet();
        ml.forEach(element -> {
            EditorMethods em = methodsMap.get(element);
            if ((em.getter == null) || (em.setter == null)) return;
            
            switch (em.fieldType) {
                case STRING:
                    saveStringField(em);
                    break;
                case STRING_MULTILINE:
                    saveTextArea(em);
                    break;
                case LONG:
                    saveSimpleNumberField(em);
                    break;
                case BOOLEAN_CHECK:
                    saveCheckBox(em);
                    break;   
            }
        });
    }
}
