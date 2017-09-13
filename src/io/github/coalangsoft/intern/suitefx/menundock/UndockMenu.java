package io.github.coalangsoft.intern.suitefx.menundock;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.util.Callback;

public class UndockMenu extends MenuItem{

	private MenuItem toUndock;

	public UndockMenu(final Callback<Void, Stage> stage, final List<String> stylesheets, MenuItem toUndock){
		super("Undock");
		this.toUndock = toUndock;
		
		setOnAction(new EventHandler<ActionEvent>() {
			public void handle(ActionEvent event) {
				Stage popup = undock();
				Stage s = stage.call(null);
				if(s != null){popup.initOwner(s);};
				popup.getScene().getStylesheets().addAll(stylesheets);
				popup.show();
			}
		});
	}

	public Stage undock() {
		Stage s = new Stage(StageStyle.UTILITY);
		s.setScene(new Scene(new UndockedMenuView(toUndock)));
		return s;
	}
	
}
