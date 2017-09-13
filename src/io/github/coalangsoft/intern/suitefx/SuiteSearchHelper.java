package io.github.coalangsoft.intern.suitefx;

import io.github.coalangsoft.jsearch.JSearchEngine;
import io.github.coalangsoft.jsearchfx.ui.AppSearchField;
import io.github.coalangsoft.jsearchfx.ui.AutoComplete;
import io.github.coalangsoft.jsearchfx.ui.CollectionSearchField;
import io.github.coalangsoft.jsearchfx.ui.SearchField;
import io.github.coalangsoft.lib.data.Func;
import javafx.scene.Node;
import javafx.scene.control.ListView;
import javafx.scene.control.TableView;
import javafx.scene.effect.Lighting;

public class SuiteSearchHelper {

	public static SearchField<?> createSearchField(Node view) {
		if(view instanceof TableView){
			return new CollectionSearchField(((TableView) view).itemsProperty(), new Func<Object, String>() {

				public String call(Object p) {
					// TODO Auto-generated method stub
					return p.toString();
				}
				
			}, true);
		}else if(view instanceof ListView){
			return new CollectionSearchField(((ListView) view).itemsProperty(), new Func<Object, String>() {

				public String call(Object p) {
					// TODO Auto-generated method stub
					return p.toString();
				}
				
			}, true);
		}else{
			SearchField<?> sf = AppSearchField.make(view, new Lighting(), true);
			return sf;
		}
	}

}
