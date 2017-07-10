package io.github.coalangsoft.intern.suitefx;

import javafx.collections.ObservableList;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.TreeItem;

public class UndockedMenuTree extends TreeItem<MenuData> {

	public UndockedMenuTree(MenuItem base){
		super(new MenuData(base));
		if(base instanceof Menu){
			ObservableList<MenuItem> items = ((Menu) base).getItems();
			for(int i = 0; i < items.size(); i++){
				MenuItem item = items.get(i);
				
				if(!(item instanceof SeparatorMenuItem || item instanceof UndockMenu)){
					getChildren().add(new UndockedMenuTree(item));
				}
			}
		}
	}
	
}
