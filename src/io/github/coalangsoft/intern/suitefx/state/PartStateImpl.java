package io.github.coalangsoft.intern.suitefx.state;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class PartStateImpl implements PartState {

	private Properties strings;
	
	{
		strings = new Properties();
	}
	
	public void putString(String key, String val) {
		strings.setProperty(key, val);
	}

	public String getString(String key) {
		return strings.getProperty(key);
	}

	public void apply(String name) throws IOException {
		strings.store(new FileOutputStream(new File("strings_" + name + ".properties")), "");
	}

	public void load(String name) throws IOException {
		File f = new File("strings_" + name + ".properties");
		if(f.exists()){
			strings.load(new FileInputStream(f));
		}
	}

}
