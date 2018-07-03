package com.brita.idl;

import java.io.Reader;
import java.util.ArrayList;
import java.util.List;

import com.brita.idl.Lexer.LexParserExeption;
import com.brita.idl.Lexer.LexerOutput;
import com.brita.idl.define.Define;
import com.brita.idl.node.*;
import com.brita.idl.token.*;

public class Parser {
		
	private RootNode rootNode = new RootNode();
	
	private Token word;
	
	public RootNode parse(Reader reader) throws Exception{

		LexerOutput output = new LexerOutput();

		new Lexer().parse(reader, output);


		while (true) {
			readNode(output);
			if (word == null) {
				break;
			}
			if (word instanceof KeyWord) {
				if (word.getText().equals(Define.PACKAGE)) {
					rootNode.packageNode = parsePackageNode(output);
				}else if (word.getText().equals(Define.SERVICE)){
					rootNode.addNode(parseServiceNode(output));
				}else if (word.getText().equals(Define.MODEL)){
					rootNode.addNode(parseModelNode(output));
				}else if (word.getText().equals(Define.IMPORT)){
					rootNode.importNodes.add(parseImportNode(output));
				}else if (word.getText().equals(Define.ENUM)){
					rootNode.addNode(parseEnumNode(output));
				}else if (word.getText().equals(Define.MESSAGE)){
					rootNode.addNode(parseMessageNode(output, null));
				}
			}
		}

		for (Node node : rootNode.getNodes()) {
			if (node instanceof MessageNode) {
				MessageNode messageNode = (MessageNode) node;
				if (messageNode.packagePaths==null) {
					messageNode.packagePaths = rootNode.packageNode.getPaths();
				}
			}
		}
		
		return rootNode;
	}

	private ImportNode parseImportNode(LexerOutput output) throws ParserExeption{

		ImportNode node = new ImportNode();
		node.setName(Define.IMPORT);

		readNode(output);
		if (!(word instanceof StringConstaint)) {
			throw new ParserExeption(word, word.getText()+" is not a valid StringConstaint");
		}

		node.setName(word.getText());

		return node;
	}
	
	private PackageNode parsePackageNode(LexerOutput output) throws ParserExeption{
		
		PackageNode packageNode = new PackageNode();
		packageNode.setName(Define.PACKAGE);

		List<String> paths = new ArrayList<>();

		while(true) {
			readIdentifier(output);
			paths.add(word.toString());
			readNode(output);
			if (word instanceof Punctuation && word.getText().equals(".")) {

			}else{
				break;
			}
		}

		packageNode.setPaths(paths);

		return packageNode;
	}
	
	private ServiceNode parseServiceNode(LexerOutput output) throws ParserExeption{
		
		ServiceNode node = new ServiceNode();
		node.setName(readIdentifier(output));
		
		readPunctuation(output, '{');
		
		while(true) {
			readNode(output);
			
			if (word instanceof KeyWord) {
				if (word.getText().equals(Define.RPC)) {
					node.addFunctions(parseFunctionNode(output));
				}else {
					throw new ParserExeption(word, "invalid function declaration");
				}
			}else {
				break;
			}
			
		}
		
		readSymbolNode(output, '}', false);
		
		return node;
	}
	
	private FunctionNode parseFunctionNode(LexerOutput output) throws ParserExeption{
		
		
		FunctionNode node = new FunctionNode();
		
		node.setName(readIdentifier(output));
		
		readPunctuation(output, '(');
		
		node.setRequestModel(getMessageNode(output));

		readSymbolNode(output, ')', false);

		readKeyWord(output, Define.RETURNS);
		
		readPunctuation(output, '(');
		
		node.setReturnModel(getMessageNode(output));

		readSymbolNode(output, ')', false);
		
		readPunctuation(output, ';');
		
		return node;
	}
	
	private MessageNode parseModelNode(LexerOutput output) throws ParserExeption{
		
		String name = readIdentifier(output);
		
		MessageNode node = getMessageNode(null, name);
		
		readPunctuation(output, '{');

		while(true) {
			readNode(output);

			if (word instanceof Identifier) {
				node.addMember(parseMemberNode(output));
			}else {
				break;
			}
			
		}
		
		readSymbolNode(output, '}', false);
		
		return node;
	}

	private MessageNode parseMessageNode(LexerOutput output, MessageNode parentNode) throws ParserExeption{

		MessageNode node = new MessageNode(parentNode);
		node.setName(readIdentifier(output));

		readPunctuation(output, '{');

		while(true) {
			readNode(output);

			if (word instanceof KeyWord) {
				if (word.getText().equals(Define.MESSAGE)) {
					node.addMessageNode(parseMessageNode(output, node));
				}else if (word.getText().equals(Define.ENUM)) {
					node.addEnumNode(parseEnumNode(output));
				}else {
					node.addMember(parseMemberNode(output));
				}
			}else {
				break;
			}

		}

		readSymbolNode(output, '}', false);

		return node;
	}

	private EnumNode parseEnumNode(LexerOutput output) throws ParserExeption{

		String name = readIdentifier(output);

		EnumNode node = new EnumNode();
		node.setName(name);

		readPunctuation(output, '{');

		while(true) {
			readNode(output);

			if (word instanceof Identifier) {
				node.addItem(parseEnumItemNode(output));
			}else {
				break;
			}

		}

		readSymbolNode(output, '}', false);

		return node;
	}
	
	private MemberNode parseMemberNode(LexerOutput output) throws ParserExeption{

		MemberNode memberNode = new MemberNode();
		memberNode.line = word.getLine();

		String modifier = word.getText();
		memberNode.setModifier(modifier);

//		List<String> messageName = readMessageName(output);
//		if (messageName.size() == 1) {
//			memberNode.setModelType(getMessageNode(null, messageName.get(0)));
//		}else if (messageName.size() > 1){
//			memberNode.setModelType(getMessageNode(messageName.subList(0, messageName.size()-2), messageName.get(messageName.size()-1)));
//		}

		memberNode.setModelType(getMessageNode(output));

		if (!(word instanceof Identifier)) {
			//关键字作为标识符也放行吧
			//throw new ParserExeption(word, word.getText()+" is not a valid Identifier name");
		}

		String name = word.getText();
		
		memberNode.setName(name);

		readPunctuation(output, '=');

		String expression = readExpression(output);
		memberNode.index = expression;

		readNode(output);

		if (word.getText().equals(";")) {

		}else if (word.getText().equals("[")){
			readKeyWord(output, "default");
			readPunctuation(output, '=');
			readNode(output);
			readPunctuation(output, ']');
			readPunctuation(output, ';');
		}else{
			throw new ParserExeption(word, word.getText()+" is not a valid; should be ';' or '[default = ]'");
		}

		//readPunctuation(output, ';');

		return memberNode;
	}

	private EnumItemNode parseEnumItemNode(LexerOutput output) throws ParserExeption {

		EnumItemNode memberNode = new EnumItemNode();

		memberNode.line = word.getLine();

		String name = word.getText();

		memberNode.setName(name);

		readPunctuation(output, '=');

		String expression = readExpression(output);

		memberNode.value = expression;

		readPunctuation(output, ';');

		return memberNode;
	}
	
	private void readKeyWord(LexerOutput output, String keyWord) throws ParserExeption{
		readNode(output);
		
		if (word instanceof KeyWord) {
			if (!word.getText().equals(keyWord)) {
				throw new ParserExeption(word, "must use keyword "+keyWord);
			}
		}
	}
	
	private String readIdentifier(LexerOutput output) throws ParserExeption{
		readNode(output);
		if (!(word instanceof Identifier)) {
			throw new ParserExeption(word, word.getText()+" is not a valid Identifier name");
		}
		
		return word.getText();
	}

	private String readExpression(LexerOutput output) throws ParserExeption{
		readNode(output);
		if (!(word instanceof Expression)) {
			throw new ParserExeption(word, word.getText()+" is not a valid Expression");
		}

		return word.getText();
	}

	private List<String> readMessageName(LexerOutput output) throws ParserExeption{

		List<String> paths = new ArrayList<>();

		while(true) {
			readIdentifier(output);
			paths.add(word.toString());
			readNode(output);
			if (word instanceof Punctuation && word.getText().equals(".")) {

			}else{
				break;
			}
		}

		if (paths.size() == 0) {
			throw new ParserExeption(word, word.getText()+" is not a valid MessageName");
		}

		return paths;
	}
	
	private void readPunctuation(LexerOutput output, char symbol) throws ParserExeption{
		readSymbolNode(output, symbol, true);
	}
	
	private void readSymbolNode(LexerOutput output, char symbol, boolean step) throws ParserExeption{
		if (step) readNode(output);
		if (!(word instanceof Punctuation)) {
			throw new ParserExeption(word, word.getText()+" is not a valid Symbol");
		}
		
		String symbolStr = String.valueOf(symbol);
		if (!word.getText().equals(symbolStr)) {
			throw new ParserExeption(word, "must has "+symbolStr+"");
		}
	}
	
	private void readNode(LexerOutput output) {
		word = output.read();
		while (word instanceof Annotation) {
			rootNode.annotations.add((Annotation)word);
			word = output.read();
		}
	}

	private  MessageNode getMessageNode(LexerOutput output) throws ParserExeption{
		List<String> messageName = readMessageName(output);
		if (messageName.size() == 1) {
			return getMessageNode(null, messageName.get(0));
		}else if (messageName.size() > 1){
			return getMessageNode(messageName.subList(0, messageName.size()-2), messageName.get(messageName.size()-1));
		}
		return null;
	}
	
	private MessageNode getMessageNode(List<String> packagePaths, String name) {

		if (rootNode.getNodes()!=null) {
			for (Node node : rootNode.getNodes()) {
				if (node instanceof MessageNode && ((MessageNode) node).isSame(packagePaths, name)) {
					return (MessageNode) node;
				}
			}
		}
		MessageNode messageNode = new MessageNode(null);
		messageNode.setName(name);
		messageNode.packagePaths = packagePaths;
		
		rootNode.addNode(messageNode);
		return messageNode;
		
		
	}
	
	public static class ParserExeption extends Exception{
		
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;

		public ParserExeption(Token word, String msg) {
			super("["+word.getLine()+":"+word.getStart()+"]"+msg);
		}
		
	}
	
}
