package io.github.coalangsoft.intern.suitefx.state;

import javafx.beans.property.BooleanProperty;
import javafx.beans.property.SimpleBooleanProperty;

public class SuiteSetup {

	private SimpleBooleanProperty darkMode = new SimpleBooleanProperty(false);
	
	public void setDarkMode(boolean dm){
		darkMode.set(dm);
	}
	public boolean isDarkMode(){
		return darkMode.get();
	}
	public BooleanProperty darkModeProperty(){
		return darkMode;
	}
	
}
