package com.brita.idl;

import java.io.IOException;
import java.io.Reader;
import java.util.concurrent.LinkedBlockingQueue;

import com.brita.idl.define.Define;
import com.brita.idl.token.*;

public class Lexer {
	private int line = 1;
	private int start = 0;
	
	int c = -1;
	
	public void parse(Reader reader, LexerOutput out) throws LexParserExeption{

		readChar(reader);

		if (c == '\uFEFF') {
			//带BOM的UTF-8
			readChar(reader);
		}

		while( c!=-1 ) {
			
			if (c == '\n') {
				line++;
				start = 0;
				readChar(reader);
			}else if (c == '\r') {
				start = 0;
				readChar(reader);
			}else if(c == ' ' || c == '\t') {
				readChar(reader);
			}else if (c == '/') {
				out.write(parseAnnotation(reader));
				readChar(reader);
			}else if (c == ';' || c == '{' || c == '}' || c == '(' || c == ')' || c == '.' || c == '=' || c == '[' || c == ']') {
				out.write(parsePunctuation(reader));
				readChar(reader);
			}else if (c == '=') {
				out.write(parseOperator(reader));
				readChar(reader);
			}else if (c == '"') {
				out.write(parseStringConstaint(reader));
				readChar(reader);
			}else if (isDigtal(c) || c == '-') {
				out.write(parseExpression(reader));
			}else {
				out.write(parseToken(reader));
			}
		}
		
		out.close();
	}
	
	private void readChar(Reader reader) throws LexParserExeption{

		try {
			c = reader.read();
			start++;
		} catch (IOException e) {
			throw new LexParserExeption(line, start, e.getMessage());
		}
	}
	
	private Token parseToken(Reader reader) throws LexParserExeption{
		
		StringBuilder wordBuilder = new StringBuilder();
		
		if (isDigtal(c)) {
			throw new LexParserExeption(line, start, "Identifier name can not start with digital ");
		}
		
		while(isDigtal(c) || isLetter(c) || c == '_' || c == '$') {
			wordBuilder.append((char)c);
			readChar(reader);
		}
		
		String word = wordBuilder.toString();
		if (word == null || word.equals("")) {
			throw new LexParserExeption(line, start, "Invalid word : '"+(char)c+"'");
		}
		
		if (isKeyWord(word)) {
			return new KeyWord(line, start, word);
		}
		
		return new Identifier(line, start, word);
	}
	
	private Punctuation parsePunctuation(Reader reader) throws LexParserExeption{
		
		return new Punctuation(line, start, String.valueOf((char)c));
	}
	
	private Operator parseOperator(Reader reader) throws LexParserExeption{
		
		return new Operator(line, start, String.valueOf((char)c));
	}
	
	private Annotation parseAnnotation(Reader reader) throws LexParserExeption{
		
		StringBuilder text = new StringBuilder();
		
		readChar(reader);
		if (c == '/') {
			
			readChar(reader);
			while (c!='\n') {
				text.append((char)c);
				readChar(reader);
			}
			line++;
			start = 0;
			return new Annotation(line, start, text.toString());
			
		}else if (c == '*') {
			
			readChar(reader);
			int lastChar = c;
			
			while (true) {
				
				readChar(reader);
				
				if (c == -1) {
					throw new LexParserExeption(line, start, "Annotation has no endding");
				}
				
				if (lastChar == '*' && c == '/')  {
					
					return new Annotation(line, start, text.toString());
				}else {
					text.append((char)lastChar);
				}
				
				lastChar = c;
			}
			
			
		}else {
			throw new LexParserExeption(line, start, "The annotation format is not correct");
		}
	}
	
	private StringConstaint parseStringConstaint(Reader reader) throws LexParserExeption{
		
		StringBuilder text = new StringBuilder();
		
		while (true) {
			
			readChar(reader);
			
			if (c == -1) {
				throw new LexParserExeption(line, start, "Constaint has no endding");
			}
			
			if (c == '\\')  {
				//转义
				readChar(reader);
				if (c == 'b' || c == 't' || c == 'n' || c == 'f' || c == 'r' || c == '"' || c == '\\')  {
					
					if (c == 'n') {
						line++;
						start = 0;
					}
					
					if (c == 'r') {
						start = 0;
					}
					
					text.append("\\"+(char)c);
				}else {
					throw new LexParserExeption(line, start, "valid ones are  \b  \t  \n  \f  \r  \"  \'  \\ ");
				}
			}if (c == '"')  {
				return new StringConstaint(line, start, text.toString());
			}else {
				text.append((char)c);
			}
		}
	}

	private Expression parseExpression(Reader reader) throws LexParserExeption{

		StringBuilder wordBuilder = new StringBuilder();

		if (c == '-') {
			wordBuilder.append((char)c);
			readChar(reader);
		}

		if (c == '0') {
			wordBuilder.append((char)c);
			readChar(reader);
			if (c == 'x') {
				wordBuilder.append((char)c);
				readChar(reader);
			}
		}

		while(isDigtal(c) || (c >= 'a' && c <= 'f') || (c >= 'A' && c <= 'F')) {
			wordBuilder.append((char)c);
			readChar(reader);
		}

		String word = wordBuilder.toString();
		if (word == null || word.equals("")) {
			throw new LexParserExeption(line, start, "Invalid Expression : '"+(char)c+"'");
		}

		return new Expression(line, start, word);
	}
	
	private boolean isDigtal(int c) {
		return c >= '0' && c <= '9';
	}
	
	private boolean isLetter(int c) {
		return (c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z');
	}
	
	private boolean isKeyWord(String word) {
		for (String key : Define.KEY_WORS) {
			if (word.equals(key)) {
				return true;
			}
		}
		return false;
	}
	
	public static class LexParserExeption extends Exception{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public LexParserExeption(int line, int start, String msg) {
			super("["+line+":"+start+"]"+msg);
		}
		
	}
	
	
	public static class LexerOutput {
		
		private LinkedBlockingQueue<Token> buffer = new LinkedBlockingQueue<>();
		
		private Token END = new Token(0, 0, null);
		
		public void close() {
			write(END);
		}
		
		public void write(Token word) {
			try {
				buffer.put(word);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		public Token read() {
			try {
				Token word =  buffer.take();
				if (word == END) {
					return null;
				}
				return word;
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			return null;
		}
	}
	
}
