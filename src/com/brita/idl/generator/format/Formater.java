package com.brita.idl.generator.format;

import java.util.HashMap;
import java.util.Map;

public class Formater {
	
	private String text;
	
	private Map<String, String> formats = new HashMap<>();
	
	public Formater(String text) {
		this.text = text;
	}
	
	public void addProperty(String property, String value) {
		formats.put(property, value);
	}
	
	public String format() {
		if (text == null) {
			return null;
		}
		String copy_text = text;
		
		for (String property : formats.keySet()) {
			
			String value = formats.get(property);
			
			if (value == null) {
				value = "";
			}
			
			copy_text = copy_text.replace(property, value);
		}
		
		
		return copy_text;
	}
	
	@Override
	public String toString() {
		return format();
	}

}
