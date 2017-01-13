package com.jneko.jnekouilib.panel;

import javafx.geometry.Pos;
import javafx.scene.control.Button;
import javafx.scene.control.Tooltip;
import jiconfont.javafx.IconNode;

public class PanelButton extends Button {
    private final PanelButtonActionListener al;
    private final IconNode iconNode = new IconNode();
    
    public PanelButton(String iconStyle, String tooltip, PanelButtonActionListener a) {
        al = a;
        this.getStyleClass().addAll("topPanelButton");
        this.setAlignment(Pos.CENTER);
        iconNode.getStyleClass().addAll(iconStyle, "topPanelButtonIcon");
        this.setGraphic(iconNode); 
        this.setOnMouseClicked(c -> {
            if (al != null) al.OnClick(c);
        });
        this.setTooltip(new Tooltip(tooltip));
    } 
}
