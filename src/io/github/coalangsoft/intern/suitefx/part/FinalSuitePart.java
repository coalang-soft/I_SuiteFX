package io.github.coalangsoft.intern.suitefx.part;

import java.util.ArrayList;
import java.util.List;

import io.github.coalangsoft.intern.suitefx.NodePrepareVisitor;
import io.github.coalangsoft.intern.suitefx.state.PartState;
import io.github.coalangsoft.jsearch.JSearchEngine;
import io.github.coalangsoft.jsearchfx.ui.SearchField;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.layout.Pane;

public class FinalSuitePart implements SuitePart {

	private SuitePart base;
	private NodePrepareVisitor visitor;

	public FinalSuitePart(SuitePart p) {
		this.base = p;
	}

	public Node createView() {
		this.visitor = new NodePrepareVisitor();
		Node node = base.createView();
		this.visitor.handle(node);
		while(node instanceof Pane){
			List<Node> children = ((Parent) node).getChildrenUnmodifiable();
			if(children.size() == 1){
				node = children.get(0);
			}else{
				break;
			}
		}
		return node;
	}

	public void updateView(Node view, SearchField<?> search) {
		base.updateView(view, search);
	}

	public void storeState(Node view, PartState s) {
		base.storeState(view, s);
	}

	public void restoreState(Node view, PartState s) {
		base.restoreState(view, s);
	}

	public List<Menu> createMenus(JSearchEngine<?> se) {
		ArrayList<Menu> res = new ArrayList<Menu>();
		res.addAll(base.createMenus(se));
		res.addAll(visitor.getMenus());
		return res;
	}

	public ObservableList<Node> createTools(JSearchEngine<?> se) {
		ObservableList<Node> res = FXCollections.observableArrayList();
		res.addAll(base.createTools(se));
		res.addAll(visitor.getTools());
		
		return res;
	}

	public String getName() {
		return base.getName();
	}

}
