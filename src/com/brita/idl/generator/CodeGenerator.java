package com.brita.idl.generator;

import java.io.File;
import java.util.Set;

import com.brita.idl.define.Define;
import com.brita.idl.node.MessageNode;
import com.brita.idl.node.Node;
import com.brita.idl.node.RootNode;

public abstract class CodeGenerator {

	public static final String PATH_TEMPLATE = "template";

	public RootNode rootNode;
	public File inputFile;
	
	public CodeGenerator(File inputFile, RootNode rootNode) {
		this.rootNode = rootNode;
		this.inputFile = inputFile;
	}
	
	public boolean generateCode(File outputDir) {
		String  ds = outputDir.getAbsolutePath();
		outputDir = new File(outputDir.getAbsolutePath());
		return generate(outputDir);
	}
	
	protected abstract boolean generate(File outputDir);
	

	protected String getConfig(String name) {
		/*
		Set<Node> nodes = rootNode.getNodes();
		if (nodes == null) {
			return null;
		}
		
		for (Node node : nodes) {
			if (node instanceof PackageNode) {
				if(((PackageNode)node).getName().equals(name)) {
					return ((PackageNode)node).getValue();
				}
			}
		}
		*/
		return null;
	}
	
	
	protected Set<Node> getNodes() {
		return rootNode.getNodes();
	}
	
	protected boolean isBasicType(MessageNode model) {
		for (String basicType : Define.BASE_TYPE) {
			if (model.getName().equals(basicType)) {
				return true;
			}
		}
		return false;
			
	}
	
}
