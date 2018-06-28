package com.brita.idl.node;

import com.brita.idl.token.Annotation;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class RootNode extends Node{

	private List<Node> nodes;

	public PackageNode packageNode;

	public List<Annotation> annotations = new ArrayList<>();

	public List<ImportNode> importNodes = new ArrayList<>();

	public List<Node> getNodes() {
		return nodes;
	}

	public void addNode(Node node) {
		if (nodes == null) {
			nodes = new ArrayList<Node>();
		}
		nodes.add(node);
	}
	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();

		if (importNodes!=null) {
			for (Node node : importNodes) {
				stringBuilder.append(node+"\r\n");
			}
		}

		if (nodes!=null) {
			for (Node node : nodes) {
				stringBuilder.append(node+"\r\n");
			}
		}
		
		return stringBuilder.toString();
	}
	
}
