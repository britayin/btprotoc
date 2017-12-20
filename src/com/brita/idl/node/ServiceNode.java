package com.brita.idl.node;

import java.util.ArrayList;
import java.util.List;

public class ServiceNode extends Node{
	
	private List<FunctionNode> functions;

	public List<FunctionNode> getFunctions() {
		return functions;
	}

	public void addFunctions(FunctionNode function) {
		if (functions == null) {
			functions = new ArrayList<>();
		}
		
		functions.add(function);
	}
	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Service : "+getName()+"\r\n");
		
		if(functions!=null) {
			for (FunctionNode functionNode : functions) {
				stringBuilder.append(functionNode+"\r\n");
			}
		}
		
		return stringBuilder.toString();
	}
	
}
