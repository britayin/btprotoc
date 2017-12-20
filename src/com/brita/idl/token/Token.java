package com.brita.idl.token;

public class Token {

	private int line = 0;
	private int start = 0;
	
	private String text;
	
	public Token(int line, int start, String text) {
		this.line = line;
		this.start = start;
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public int getLine() {
		return line;
	}

	public void setLine(int line) {
		this.line = line;
	}

	public int getStart() {
		return start;
	}

	public void setStart(int start) {
		this.start = start;
	}
	
	@Override
	public String toString() {
		return text;
	}
	
}
