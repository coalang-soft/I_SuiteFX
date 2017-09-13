package io.github.coalangsoft.intern.suitefx.menundock;

import javafx.event.EventHandler;
import javafx.scene.control.MenuItem;
import javafx.scene.control.TreeItem;
import javafx.scene.control.TreeView;
import javafx.scene.input.MouseEvent;
import javafx.scene.input.TouchEvent;

public class UndockedMenuView extends TreeView<MenuData> {
	
	public UndockedMenuView(MenuItem base){
		setRoot(new UndockedMenuTree(base));
		setShowRoot(false);
		setOnMouseClicked(new EventHandler<MouseEvent>() {
			public void handle(MouseEvent arg0) {
				TreeItem<MenuData> dat = getSelectionModel().getSelectedItem();
				if(dat != null){
					dat.getValue().get().fire();
				}
			}
		});
	}
	
}
