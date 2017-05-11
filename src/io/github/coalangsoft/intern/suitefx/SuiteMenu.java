package io.github.coalangsoft.intern.suitefx;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;

public class SuiteMenu extends Menu {

	private SuiteView view;

	public SuiteMenu(SuiteView view) {
		super(view.name);
		this.view = view;
		getItems().add(createPerspectiveMenu());
	}

	private MenuItem createPerspectiveMenu() {
		Menu m = new Menu("Perspective");
		for(int i = 0; i < view.parts.size(); i++){
			SuitePart<?> p = view.parts.get(i);
			final int k = i;
			MenuItem item = new MenuItem(p.getName());
			item.setOnAction(new EventHandler<ActionEvent>() {
				
				public void handle(ActionEvent e) {
					view.init(k);
				}
			});
			m.getItems().add(item);
		}
		return m;
	}

}
