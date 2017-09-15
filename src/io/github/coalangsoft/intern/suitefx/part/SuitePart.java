package io.github.coalangsoft.intern.suitefx.part;

import io.github.coalangsoft.intern.suitefx.state.PartState;
import io.github.coalangsoft.jsearch.JSearchEngine;
import io.github.coalangsoft.jsearchfx.ui.SearchField;

import java.util.List;

import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.control.Menu;

public interface SuitePart {
	
	Node createView();
	void updateView(Node view, SearchField<?> search);
	void storeState(Node view, PartState s);
	void restoreState(Node view, PartState s);
	List<Menu> createMenus(JSearchEngine<?> se);
	ObservableList<Node> createTools(JSearchEngine<?> se);
	String getName();
	
}
