package io.github.coalangsoft.intern.suitefx;

import javafx.scene.control.MenuItem;

public class MenuData {

	private MenuItem menu;
	
	public MenuData(MenuItem base) {
		this.menu = base;
	}

	public MenuItem get(){
		return menu;
	}
	
	public String toString(){
		return menu.getText();
	}
	
}
