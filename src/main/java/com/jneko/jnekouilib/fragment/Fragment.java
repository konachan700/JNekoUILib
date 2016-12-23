package com.jneko.jnekouilib.fragment;

import java.util.Stack;
import javafx.scene.layout.VBox;

public abstract class Fragment extends VBox {
    private Fragment host = null;
    private final Stack<Fragment> fragments = new Stack<>();
    
    public Fragment() {
        super();
        super.getStylesheets().add("/styles/window.css");
        super.getStyleClass().addAll("maxHeight", "maxWidth");
    }
    
    public Fragment getHost() {
        return host;
    }

    public void setHost(Fragment host) {
        this.host = host;
    }

    public void addSubfragment(Fragment f) {
        f.setHost(this);
        super.getChildren().clear();
        super.getChildren().add(f);
        fragments.push(this);
    }
    
    public void addFragment(Fragment f) {
        if (host == null) return;
        
        f.setHost(host);
        host.getChildren().clear();
        host.getChildren().add(f);
        host.fragments.push(this);
    }
    
    public void back() {
        if (host == null) return;
        if (!fragments.empty()) {
            Fragment f = fragments.pop();
            host.getChildren().clear();
            host.getChildren().add(f);
        } else {
            host.back();
        }        
    }
}
