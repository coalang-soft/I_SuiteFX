package io.github.coalangsoft.intern.suitefx;

import javafx.scene.Scene;

public class Styles {
	
	static String using;
	
	public static void dark(Scene s){
		reset(s);
		s.getStylesheets().add(using = Styles.class.getResource("res/style/dark.css").toExternalForm());
	}
	
	public static void light(Scene s){
		reset(s);
		s.getStylesheets().add(using = Styles.class.getResource("res/style/light.css").toExternalForm());
	}
	
	private static void reset(Scene s){
		if(using != null){
			s.getStylesheets().remove(using);
			using = null;
		}
	}
	
}
