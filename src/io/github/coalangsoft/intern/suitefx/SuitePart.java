package io.github.coalangsoft.intern.suitefx;

import io.github.coalangsoft.intern.suitefx.state.PartState;
import io.github.coalangsoft.jsearch.JSearchEngine;
import io.github.coalangsoft.jsearchfx.NodeSearch;
import io.github.coalangsoft.jsearchfx.ui.AppSearchField;

import java.util.List;

import javafx.scene.Node;
import javafx.scene.control.Menu;

public interface SuitePart<T extends Node> {
	
	T createView();
	void updateView(T view, AppSearchField search);
	void storeState(Node view, PartState s);
	void restoreState(T view, PartState s);
	List<Menu> createMenus(JSearchEngine<NodeSearch> se);
	List<Node> createTools(JSearchEngine<NodeSearch> e);
	String getName();
	
}
