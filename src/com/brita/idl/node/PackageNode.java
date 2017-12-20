package com.brita.idl.node;

import java.util.List;

public class PackageNode extends Node{


	private List<String> paths;

	public List<String> getPaths() {
		return paths;
	}

	public void setPaths(List<String> paths) {
		this.paths = paths;
	}


	@Override
	public String toString() {
		

		StringBuilder stringBuilder = new StringBuilder();
		for (String path: paths) {
			stringBuilder.append(path);
			stringBuilder.append(" ");
		}

		return "package : "+stringBuilder.toString();
	}
	
}
