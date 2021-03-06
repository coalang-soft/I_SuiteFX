package io.github.coalangsoft.intern.suitefx;

import io.github.coalangsoft.dragdropfx.DnDPrepare;
import io.github.coalangsoft.dragdropfx.DragDropFX;
import io.github.coalangsoft.intern.fxparts.input.KeyCombinationHandler;
import io.github.coalangsoft.intern.suitefx.menundock.UndockMenu;
import io.github.coalangsoft.intern.suitefx.part.FinalSuitePart;
import io.github.coalangsoft.intern.suitefx.part.SuitePart;
import io.github.coalangsoft.intern.suitefx.state.PartState;
import io.github.coalangsoft.intern.suitefx.state.PartStateImpl;
import io.github.coalangsoft.intern.suitefx.state.SuiteSetup;
import io.github.coalangsoft.jsearchfx.ui.SearchField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.control.SeparatorMenuItem;
import javafx.scene.control.ToolBar;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyCodeCombination;
import javafx.scene.input.KeyCombination;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;
import javafx.util.Callback;

public class SuiteView extends BorderPane {
	
	{
		ChangeListener<Scene> l = new ChangeListener<Scene>() {
			
			public void changed(ObservableValue<? extends Scene> arg0, Scene arg1,
					final Scene o) {
				sceneProperty().removeListener(this);
				o.getStylesheets().addAll(stylesheets);
				stylesheets = o.getStylesheets();
				
				o.windowProperty().addListener(new ChangeListener<Window>() {
					public void changed(
							ObservableValue<? extends Window> arg0,
							Window arg1, Window n) {
						if(n != null){
							if(n instanceof Stage){
								stage = (Stage) n;
								stage.setOnCloseRequest((e) -> {
									try {
										storeState();
									} catch (IOException e1) {
										// TODO Auto-generated catch block
										e1.printStackTrace();
									}
								});
								windows.add(stage);
								stage.setTitle(name);
							}
							o.windowProperty().removeListener(this);
						}
					}
				});
				init(0);
			}
		};
		
		sceneProperty().addListener(l);
	}
	
	private Stage stage;
	private List<String> stylesheets = new ArrayList<String>();
	SuiteSetup setup;
	
	String name;
	private int lastIndex = -1;
	private Node lastView;
	
	private MenuBar menuBar;
	private ToolBar toolBar;

	List<SuitePart> parts;
	private List<Stage> windows;
	
	private Map<SuitePart, PartState> states = new HashMap<SuitePart, PartState>();
	private boolean usesMenu = true;
	
	public <T extends Node> void add(SuitePart p){
		parts.add(p);
	}
	
	public void init(int index){
		if(lastIndex == -1){
			lastIndex = index;
		}
		
		if(lastIndex != index){
			try {
				storeState();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			lastIndex = index;
		}
		
		SuitePart p = (SuitePart) parts.get(index);
		p = new FinalSuitePart(p);
		
		Node view;
		setCenter(lastView = view = p.createView());
		
		if(usesMenu){
			initMenuBar();
		}
		final SearchField<?> sf = initToolBar(view, p);
		
		//control-f
		setOnKeyPressed(KeyCombinationHandler.focus(sf, new KeyCodeCombination(KeyCode.F, KeyCombination.CONTROL_DOWN)));
		
		List<Menu> menus = p.createMenus(sf.getEngine());
		for(int i = 0; i < menus.size(); i++){
			menuBar.getMenus().add(setupMenu(menus.get(i)));
		}
		
		if(stage != null){
			stage.setTitle(name + " - " + p.getName());
		}
		
		PartState s = states.get(p);
		if(s != null){
			p.restoreState(view, s);
		}else{
			s = new PartStateImpl();
			try {
				s.load(index + "");
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			p.restoreState(view, s);
		}
	}
	
	private Menu setupMenu(final Menu menu) {
		MenuItem undockMen = new UndockMenu(new Callback<Void, Stage>(){

			public Stage call(Void arg0) {
				// TODO Auto-generated method stub
				return stage;
			}
			
		}, stylesheets, menu);
		menu.getItems().add(new SeparatorMenuItem());
		menu.getItems().add(undockMen);
		
		for(int i = 0; i < menu.getItems().size(); i++){
			MenuItem mi = menu.getItems().get(i);
			if(mi instanceof Menu){
				setupMenu((Menu) mi);
			}
		}
		return menu;
	}

	private void storeState() throws IOException {
		PartStateImpl newState = new PartStateImpl();
		parts.get(lastIndex).storeState(lastView, newState);
		states.put(parts.get(lastIndex), newState);
		newState.apply(lastIndex + "");
	}

	public SuiteView(String name){
		this(new ArrayList<Stage>(), name);
	}
	
	public SuiteView(String name, boolean usesMenu){
		this(name);
		this.usesMenu = usesMenu;
	}
	
	private SuiteView(List<Stage> windows, String name){
		this.name = name;
		this.setup = new SuiteSetup();
		
		this.menuBar = new MenuBar();
		this.toolBar = new ToolBar();
		DnDPrepare.toolBar(this.toolBar);
		this.parts = new ArrayList<SuitePart>();
		this.windows = windows;
		
		VBox top = new VBox();
		if(usesMenu){
			top.getChildren().add(menuBar);
		}
		top.getChildren().add(toolBar);
		
		setTop(top);
	}

	@SuppressWarnings("unchecked")
	<T extends Node> SearchField<?> initToolBar(Node view, SuitePart p) {
		toolBar.getItems().clear();
		
		SearchField<?> searchField = SuiteSearchHelper.createSearchField(view);
		toolBar.getItems().add(searchField);
		
		toolBar.getItems().addAll(p.createTools(searchField.getEngine()));
		p.updateView(view, searchField);
		
		new DragDropFX().handle(this);
		return searchField;
	}

	void initMenuBar() {
		menuBar.getMenus().clear();
		menuBar.getMenus().add(new SuiteMenu(this, windows));
	}
	
	public SuiteView clone(){
		SuiteView s = new SuiteView(windows, name);
		for(int i = 0; i < parts.size(); i++){
			s.add(parts.get(i));
		}
		s.stylesheets = stylesheets;
		s.setup = setup;
		return s;
	}
	
}
