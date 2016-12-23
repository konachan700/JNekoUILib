package com.jneko.jnekouilib.fragment;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import javafx.scene.layout.VBox;

public class FragmentHost extends VBox {
    private final Map<String, Fragment> 
            fragmentTabs = new HashMap<>();
        
    public FragmentHost() {
        super();
        super.getStylesheets().add("/styles/window.css");
        super.getStyleClass().addAll("maxHeight", "maxWidth");
    }
    
    public Fragment addFragment(String name, Fragment f) {
        fragmentTabs.put(name, f);
        return f;
    }
    
    public Fragment removeFragment(String name) {
        return fragmentTabs.remove(name);
    }
    
    public Set<String> getFragmentsNames() {
        return fragmentTabs.keySet();
    }
    
    public Fragment getFragment(String name) {
        return fragmentTabs.get(name);
    }
    
    public void showFragmentTab(String name) {
        if (!fragmentTabs.containsKey(name)) return;
        super.getChildren().clear();
        super.getChildren().add(fragmentTabs.get(name));
    }
}
