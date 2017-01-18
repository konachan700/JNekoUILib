package com.jneko.jnekouilib.windows;

import com.jneko.jnekouilib.appmenu.AppMenu;
import com.jneko.jnekouilib.appmenu.AppMenuGroup;
import com.jneko.jnekouilib.fragment.Fragment;
import com.jneko.jnekouilib.fragment.FragmentHost;
import com.jneko.jnekouilib.panel.Panel;
import com.jneko.jnekouilib.panel.PanelButton;
import com.jneko.jnekouilib.panel.PanelButtonActionListener;
import java.util.Arrays;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import jiconfont.icons.GoogleMaterialDesignIcons;
import jiconfont.javafx.IconFontFX;

public class UIDialog extends Stage {
    private final FragmentHost
            rootFragment = new FragmentHost();
    
    private final Panel
            panel = new Panel();
    
    private final AppMenu
            appMenu = new AppMenu();
    
    private final Scene 
            scene;
    
    private final VBox
            rootPane = new VBox();
    
    private final HBox
            headerBox = new HBox(),
            rootBox = new HBox();
    
    public UIDialog(int width, int height, boolean isHeaderPresent, boolean isMenuPresent, String title) {
        super();
        IconFontFX.register(GoogleMaterialDesignIcons.getIconFont());
        
        scene = new Scene(rootPane, width, height);
        scene.getStylesheets().add("/styles/window.css");
        
        if (isHeaderPresent)  {
            rootPane.getChildren().addAll(headerBox, rootBox);
            headerBox.getChildren().addAll(panel);
        } else 
            rootPane.getChildren().addAll(rootBox);
        
        rootPane.getStyleClass().addAll("maxHeight", "maxWidth", "dialog_root");
        headerBox.getStyleClass().addAll("dialog_headerBox", "maxWidth");
        rootBox.getStyleClass().addAll("maxHeight", "maxWidth");
        appMenu.getStyleClass().addAll("windowMenu");
        
        if (isMenuPresent)
            rootBox.getChildren().addAll(appMenu, rootFragment);
        else 
            rootBox.getChildren().addAll(rootFragment);
        
        super.setMinWidth(width);
        super.setMinHeight(height);
        super.setTitle(title);
        super.setScene(scene);
    }
    
    public UIDialog(int width, int height, String title) {
        this(width, height, true, true, title);
    }
    
    public void addPanelButton(String iconStyle, String tooltip, PanelButtonActionListener a) {
        final PanelButton pb = new PanelButton(iconStyle, tooltip, a);
        panel.addNode(pb); 
    }
    
    public void addPanelElements(boolean isClear, Parent ... p) {
        if (isClear)
            panel.getChildren().clear();
        panel.getChildren().addAll(Arrays.asList(p));
    }
    
    public void addPanelSeparator(boolean isFixed) {
        if (isFixed)
            panel.addFixedSeparator();
        else
            panel.addSeparator();
    }
    
    public void clearPanel() {
        panel.getChildren().clear();
    }
    
    public void addMenu(AppMenuGroup ... mg) {
        getAppMenu().addMenuGroups(mg); 
    }
    
    public void back() {
        rootFragment.back();
    }
    
    public void showFragment(Fragment f, boolean isClear) {
        rootFragment.showFragment(f, isClear); 
    }

    public FragmentHost getRootFragment() {
        return rootFragment;
    }

    public Panel getPanel() {
        return panel;
    }

    public AppMenu getAppMenu() {
        return appMenu;
    }
    
    public void addCustomStyle(String style) {
        scene.getStylesheets().add(style);
    }
    
    public void removeCustomStyle(String style) {
        scene.getStylesheets().remove(style);
    }
}
