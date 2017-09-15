package io.github.coalangsoft.intern.suitefx;

import java.io.File;
import java.io.IOException;

import cpa.subos.io.IO;
import javafx.scene.Scene;
import javafx.scene.paint.Color;

public class Styles {
	
	static String using;
	static File styleFile;
	
	public static void dark(Scene s){
		reset(s);
		s.getStylesheets().add(using = Styles.class.getResource("res/style/dark.css").toExternalForm());
	}
	
	public static void light(Scene s){
		reset(s);
		s.getStylesheets().add(using = Styles.class.getResource("res/style/light.css").toExternalForm());
	}
	
	public static void apply(String url, Scene s){
		reset(s);
		s.getStylesheets().add(using = url);
	}
	
	private static void reset(Scene s){
		if(using != null){
			s.getStylesheets().remove(using);
			using = null;
		}
	}
	
	public static void color(Scene scene, Color c) throws IOException{
		String selection = ".list-cell:filled:selected:focused, .list-cell:filled:selected {-fx-background-color:rgba(%d,%d,%d,0.8);}";
		selection = String.format(selection, 
				(int) (c.getRed() * 255),
				(int) (c.getGreen() * 255),
				(int) (c.getBlue() * 255)
			);
		
		if(styleFile != null){
			styleFile.delete();
		}
		
		styleFile = File.createTempFile("jukeboxcolor", ".css");
		Styles.apply(IO.file(styleFile).writeString(String.format(
				selection + ".root{-fx-base:rgb(%d,%d,%d);}",
				(int) (c.getRed() * 255),
				(int) (c.getGreen() * 255),
				(int) (c.getBlue() * 255)
			)).toUrl(), scene
		);
	}
	
}
