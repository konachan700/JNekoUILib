package com.jneko.jnekouilib.fragment;

import com.jneko.jnekouilib.utils.FSParser;
import com.jneko.jnekouilib.utils.FSParserActionListener;
import com.jneko.jnekouilib.utils.FSParserActions;
import com.jneko.jnekouilib.utils.MessageBus;
import com.jneko.jnekouilib.utils.MessageBusActions;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArrayList;
import javafx.scene.Node;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;

public class FragmentFileList extends Fragment implements FragmentListItemActionListener<Path>, FSParserActionListener {
    private final ArrayList<FragmentListItem<Path>> 
            uiItems = new ArrayList<>();
    
    private final ScrollPane
            elementsSP= new ScrollPane();
    
    private final VBox
            vContainer = new VBox();
    
    private FragmentListItem
            selectedItem = null;
    
    private final ArrayList<Node> 
            lastPanelButtons = new ArrayList<>();
    
    private final FSParser
            files = new FSParser(this);
    
    
    public FragmentFileList() {
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
                MessageBusActions.FileListReloadRun, 
                (b, obj) -> {
            create();
        });
        
        files.init();
        files.setPath("./");
        files.getFiles();
    }
    
    public final void create() {
        MessageBus.sendMessage(MessageBusActions.FileListReloadStarted);
        uiItems.clear();
        vContainer.getChildren().clear();
        files.getFiles();
    }
    
    
    
    public void dispose() {
        files.dispose();
    }
    
    @Override
    public void OnItemClick(Path object, FragmentListItem fli, MouseEvent me) {
        if (me.getButton() == MouseButton.PRIMARY) {
            if (me.getClickCount() == 1) {
                uiItems.forEach(item -> {
                    item.setSelected(false);
                });
                fli.setSelected(true);
                selectedItem = fli;
            }else if (me.getClickCount() == 2) {
                if (object.toFile().isDirectory()) {
                    files.setPath(object);
                    create();
                } else {

                }
            }
        }
    }

    public void levelUp() {
        files.levelUp();
    }
    
    @Override
    public void rootListGenerated(Set<Path> pList) {
        // TODO: add some code
        MessageBus.sendMessage(MessageBusActions.FileListReloadFinished, pList);
    }

    @Override
    public void fileListRefreshed(Path p, CopyOnWriteArrayList<Path> pList, long execTime) {
        pList.forEach(file -> {
            final FragmentListItem fli = new FragmentListItem(file, this);
            fli.create();
            uiItems.add(fli);
            vContainer.getChildren().add(fli);
        });
        MessageBus.sendMessage(MessageBusActions.FileListReloadFinished, pList);
    }

    @Override
    public void onLevelUp(Path p) {
        create();
    }

    @Override
    public void onError(FSParserActions act, Exception e) {
        System.err.println(e.getMessage()); 
        MessageBus.sendMessage(MessageBusActions.FileListReloadError, e);
    }
}
