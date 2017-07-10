package io.github.coalangsoft.intern.suitefx;

import java.util.ArrayList;
import java.util.List;

import io.github.coalangsoft.intern.suitefx.state.PartState;
import io.github.coalangsoft.jsearch.JSearchEngine;
import io.github.coalangsoft.jsearchfx.ui.SearchField;
import javafx.scene.Node;
import javafx.scene.control.Menu;

public class FinalSuitePart<T extends Node> implements SuitePart<T> {

	private SuitePart<T> base;
	private NodePrepareVisitor visitor;

	public FinalSuitePart(SuitePart<T> p) {
		this.base = p;
	}

	public T createView() {
		this.visitor = new NodePrepareVisitor();
		T node = base.createView();
		this.visitor.handle(node);
		return node;
	}

	public void updateView(T view, SearchField<?> search) {
		base.updateView(view, search);
	}

	public void storeState(Node view, PartState s) {
		base.storeState(view, s);
	}

	public void restoreState(T view, PartState s) {
		base.restoreState(view, s);
	}

	public List<Menu> createMenus(JSearchEngine<?> se) {
		ArrayList<Menu> res = new ArrayList<Menu>();
		res.addAll(base.createMenus(se));
		res.addAll(visitor.getMenus());
		return res;
	}

	public List<Node> createTools(JSearchEngine<?> se) {
		ArrayList<Node> res = new ArrayList<Node>();
		res.addAll(base.createTools(se));
		res.addAll(visitor.getTools());
		return res;
	}

	public String getName() {
		return base.getName();
	}

}
