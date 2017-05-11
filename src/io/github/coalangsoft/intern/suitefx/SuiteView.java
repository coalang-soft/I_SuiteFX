package io.github.coalangsoft.intern.suitefx;

import io.github.coalangsoft.dragdropfx.DragDropFX;
import io.github.coalangsoft.intern.suitefx.state.PartState;
import io.github.coalangsoft.intern.suitefx.state.PartStateImpl;
import io.github.coalangsoft.jsearch.JSearchEngine;
import io.github.coalangsoft.jsearchfx.JSearchFX;
import io.github.coalangsoft.jsearchfx.NodeSearch;
import io.github.coalangsoft.jsearchfx.ui.AppSearchField;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.scene.Node;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.ToolBar;
import javafx.scene.effect.Lighting;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.VBox;

public class SuiteView extends BorderPane {
	
	{
		ChangeListener<Object> l = new ChangeListener<Object>() {
			
			public void changed(ObservableValue<? extends Object> arg0, Object arg1,
					Object arg2) {
				init(0);
				sceneProperty().removeListener(this);
				parentProperty().removeListener(this);
			}
		};
		
		parentProperty().addListener(l);
		sceneProperty().addListener(l);
	}
	
	String name;
	private int lastIndex = -1;
	private Node lastView;
	
	private MenuBar menuBar;
	private ToolBar toolBar;

	List<SuitePart<?>> parts;
	
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
		JSearchEngine<NodeSearch> se = initToolBar(view, p);
		
		List<Menu> menus = p.createMenus(se);
		for(int i = 0; i < menus.size(); i++){
			menuBar.getMenus().add(menus.get(i));
		}
	}
	
	private void storeState() throws IOException {
		PartStateImpl newState = new PartStateImpl();
		parts.get(lastIndex).storeState(lastView, newState);
		states.put(parts.get(lastIndex), newState);
		newState.apply(lastIndex + "");
	}

	public SuiteView(String name){
		this.name = name;
		
		this.menuBar = new MenuBar();
		this.toolBar = new ToolBar();
		this.parts = new ArrayList<SuitePart<?>>();
		
		VBox top = new VBox();
		top.getChildren().add(menuBar);
		top.getChildren().add(toolBar);
		
		setTop(top);
	}

	@SuppressWarnings("unchecked")
	<T extends Node> JSearchEngine<NodeSearch> initToolBar(Node view, SuitePart<T> p) {
		toolBar.getItems().clear();
		JSearchEngine<NodeSearch> e = new JSearchFX().createSearchEngine(getCenter());
		
		AppSearchField searchField;
		toolBar.getItems().add(searchField = new AppSearchField(e,new Lighting(),true));
		
		toolBar.getItems().addAll(p.createTools(e));
		p.updateView((T) view, searchField);
		
		new DragDropFX().handle(this);
		return e;
	}

	void initMenuBar() {
		menuBar.getMenus().clear();
		menuBar.getMenus().add(new SuiteMenu(this));
	}
	
	public void exit(){
		if(lastIndex > 0){
			
		}
	}
	
}
