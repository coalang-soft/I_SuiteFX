package io.github.coalangsoft.intern.suitefx;

import io.github.coalangsoft.intern.suitefx.state.PartState;
import io.github.coalangsoft.jsearch.JSearchEngine;
import io.github.coalangsoft.jsearchfx.NodeSearch;
import io.github.coalangsoft.jsearchfx.ui.AppSearchField;

import java.util.ArrayList;
import java.util.List;

import io.github.coalangsoft.jsearchfx.ui.SearchField;
import javafx.scene.Node;
import javafx.scene.control.Menu;

public abstract class SimpleSuitePart<T extends Node> implements SuitePart<T> {

	public abstract T createView();

	public void updateView(T view, SearchField<?> search) {}

	public void storeState(Node view, PartState s) {}

	public void restoreState(T view, PartState s) {}

	public List<Menu> createMenus(JSearchEngine<?> se) {
		return new ArrayList<Menu>();
	}
	
	public List<Node> createTools(JSearchEngine<?> se) {
		return new ArrayList<Node>();
	}

	public String getName() {
		return getClass().getName();
	}

}
