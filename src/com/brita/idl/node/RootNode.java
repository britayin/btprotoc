package com.brita.idl.node;

import com.brita.idl.token.Annotation;

import java.util.HashSet;
import java.util.Set;

public class RootNode extends Node{

	private Set<Node> nodes;

	public PackageNode packageNode;

	public Set<Annotation> annotations = new HashSet<>();

	public Set<ImportNode> importNodes = new HashSet<>();

	public Set<Node> getNodes() {
		return nodes;
	}

	public void addNode(Node node) {
		if (nodes == null) {
			nodes = new HashSet<Node>();
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
