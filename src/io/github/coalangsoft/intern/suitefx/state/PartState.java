package io.github.coalangsoft.intern.suitefx.state;

import java.io.IOException;

public interface PartState {

	void putString(String key, String val);
	String getString(String key);
	void apply(String name) throws IOException;
	void load(String name) throws IOException;
	
}
