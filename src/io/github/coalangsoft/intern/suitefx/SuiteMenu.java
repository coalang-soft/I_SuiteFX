package io.github.coalangsoft.intern.suitefx;

import java.util.ArrayList;
import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

public class SuiteMenu extends Menu {

	private SuiteView view;

	public SuiteMenu(final SuiteView view, final List<Stage> windows) {
		super(view.name);
		this.view = view;
		System.out.println(windows);
		getItems().add(createPerspectiveMenu());
		
		MenuItem newWindow = new MenuItem("New window");
		newWindow.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent e) {
				final Stage s = new Stage();
				SuiteView clone = (SuiteView) view.clone();
				s.setScene(new Scene(clone));
				s.show();
				s.setOnCloseRequest(new EventHandler<WindowEvent>() {
					public void handle(WindowEvent arg0) {
						windows.remove(s);
					}
				});
			}
		});
		getItems().add(newWindow);
		
		MenuItem allToFront = new MenuItem("Show all windows");
		allToFront.setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent arg0) {
				for(int i = 0; i < windows.size(); i++){
					Stage s = windows.get(i);
					s.setIconified(false);
					s.toFront();
				}
			}
		});
		getItems().add(allToFront);
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
