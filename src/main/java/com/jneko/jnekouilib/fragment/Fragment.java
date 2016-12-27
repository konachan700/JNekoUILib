package com.jneko.jnekouilib.fragment;

import javafx.scene.layout.VBox;

public abstract class Fragment extends VBox {
    private FragmentHost host = null;
    
    public Fragment() {
        super();
        super.getStylesheets().add("/styles/window.css");
        super.getStyleClass().addAll("maxHeight", "maxWidth");
    }
    
    public FragmentHost getHost() {
        return host;
    }

    public void setHost(FragmentHost host) {
        this.host = host;
    }

    public void back() {
        if (host == null) return;
        host.back();
    }
}
