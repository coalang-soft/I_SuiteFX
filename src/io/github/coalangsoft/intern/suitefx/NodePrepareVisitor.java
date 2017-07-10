package io.github.coalangsoft.intern.suitefx;

import java.util.ArrayList;
import java.util.List;

import io.github.coalangsoft.lib.data.Func;
import io.github.coalangsoft.visit.Visitor;
import io.github.coalangsoft.visitfx.ParentChildrenVisitor;
import io.github.coalangsoft.visitfx.TabPaneContentVisitor;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.TabPane;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Pane;

public class NodePrepareVisitor extends Visitor{

	private List<Menu> menus;
	private List<Node> tools;
	
	{
		tools = new ArrayList<Node>();
		menus = new ArrayList<Menu>();
		
		addFunction(Parent.class, new ParentChildrenVisitor(this));
        addFunction(TabPane.class, new TabPaneContentVisitor(this));
        addFunction(MenuBar.class, new Func<Object, Void>() {
            public Void call(Object o) {
                MenuBar mb = (MenuBar) o;
                Parent p = mb.getParent();
                if(p instanceof Pane){
                    ((Pane) p).getChildren().remove(mb);
                }
                menus.addAll(mb.getMenus());
                return null;
            }
        });
        addFunction(ToolBar.class, new Func<Object, Void>() {
            public Void call(Object o) {
                ToolBar tb = (ToolBar) o;
                Parent p = tb.getParent();
                if(p instanceof Pane){
                    ((Pane) p).getChildren().remove(tb);
                }
                tools.addAll(tb.getItems());
                return null;
            }
        });
        addFunction(Parent.class, new Func<Object, Void>() {
            public Void call(Object o) {
                Parent p = (Parent) o;
                for(int i = 0; i < p.getChildrenUnmodifiable().size(); i++){
                    handle(p.getChildrenUnmodifiable().get(i));
                }
                return null;
            }
        });
	}

	public List<Menu> getMenus() {
		return menus;
	}

	public List<Node> getTools() {
		return tools;
	}
	
}
