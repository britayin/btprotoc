package com.brita.idl.token;

public class Annotation extends Token{

	public Annotation(int line, int start, String text) {
		super(line, start, text);
	}
	
	@Override
	public String toString() {
		return "//"+getText();
	}
	
}
