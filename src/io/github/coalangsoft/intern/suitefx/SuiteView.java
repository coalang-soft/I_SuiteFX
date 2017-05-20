package io.github.coalangsoft.intern.suitefx;

import io.github.coalangsoft.lib.data.Func;
import io.github.coalangsoft.dragdropfx.DragDropFX;
import io.github.coalangsoft.intern.suitefx.state.PartState;
import io.github.coalangsoft.intern.suitefx.state.PartStateImpl;
import io.github.coalangsoft.jsearch.JSearchEngine;
import io.github.coalangsoft.jsearchfx.JSearchFX;
import io.github.coalangsoft.jsearchfx.NodeSearch;
import io.github.coalangsoft.jsearchfx.ui.AppSearchField;
import io.github.coalangsoft.jsearchfx.ui.AutoComplete;
import io.github.coalangsoft.jsearchfx.ui.SearchField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import javafx.stage.Window;

public class SuiteView extends BorderPane {
	
	{
		ChangeListener<Scene> l = new ChangeListener<Scene>() {
			
			public void changed(ObservableValue<? extends Scene> arg0, Scene arg1,
					final Scene o) {
				init(0);
				sceneProperty().removeListener(this);
				o.windowProperty().addListener(new ChangeListener<Window>() {
					public void changed(
							ObservableValue<? extends Window> arg0,
							Window arg1, Window n) {
						if(n != null){
							if(n instanceof Stage){
								windows.add((Stage) n);
							}
							o.windowProperty().removeListener(this);
						}
					}
				});
			}
		};
		
		sceneProperty().addListener(l);
	}
	
	String name;
	private int lastIndex = -1;
	private Node lastView;
	
	private MenuBar menuBar;
	private ToolBar toolBar;

	List<SuitePart<?>> parts;
	private List<Stage> windows;
	
	private Map<SuitePart<?>, PartState> states = new HashMap<SuitePart<?>, PartState>();
	
	public <T extends Node> void add(SuitePart<T> p){
		parts.add(p);
	}

	public <T extends Node> void init(int index){
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
		
		@SuppressWarnings("unchecked")
		SuitePart<T> p = (SuitePart<T>) parts.get(index);
		
		T view;
		setCenter(lastView = view = p.createView());
		
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
		
		initMenuBar();
		SearchField<NodeSearch> sf = initToolBar(view, p);
		
		List<Menu> menus = p.createMenus(sf.getEngine());
		for(int i = 0; i < menus.size(); i++){
			menuBar.getMenus().add(menus.get(i));
		}
		
		final JSearchEngine<String> autoComplete = new JSearchEngine<String>();
		sf.getEngine().forAllKeys(new Func<String,Object>(){
			public Object call(String p) {
				autoComplete.add(p, p);
				System.out.println(p);
				return null;
			}
		});
		new AutoComplete().attach(sf, autoComplete);
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
	
	public SuiteView(List<Stage> windows, String name){
		this.name = name;
		
		this.menuBar = new MenuBar();
		this.toolBar = new ToolBar();
		this.parts = new ArrayList<SuitePart<?>>();
		this.windows = windows;
		
		VBox top = new VBox();
		top.getChildren().add(menuBar);
		top.getChildren().add(toolBar);
		
		setTop(top);
	}

	@SuppressWarnings("unchecked")
	<T extends Node> SearchField<NodeSearch> initToolBar(Node view, SuitePart<T> p) {
		toolBar.getItems().clear();
		JSearchEngine<NodeSearch> e = new JSearchFX().createSearchEngine(getCenter());
		
		AppSearchField searchField;
		toolBar.getItems().add(searchField = new AppSearchField(e,new Lighting(),true));
		
		toolBar.getItems().addAll(p.createTools(e));
		p.updateView((T) view, searchField);
		
		new DragDropFX().handle(this);
		return searchField;
	}

	void initMenuBar() {
		menuBar.getMenus().clear();
		menuBar.getMenus().add(new SuiteMenu(this, windows));
	}
	
	public void exit(){
		if(lastIndex > 0){
			
		}
	}
	
	public SuiteView clone(){
		SuiteView s = new SuiteView(windows, name);
		for(int i = 0; i < parts.size(); i++){
			s.add(parts.get(i));
		}
		return s;
	}
	
}
