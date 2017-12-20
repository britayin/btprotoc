package com.brita.idl.node;

public class MemberNode extends Node{

	public int line;

	private String modifier;

	private MessageNode modelType;

	public String index;

	public String anotation;


	public MessageNode getModelType() {
		return modelType;
	}

	public void setModelType(MessageNode modelType) {
		this.modelType = modelType;
	}


	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	@Override
	public String toString() {
		
		return "\t"+modifier+" "+modelType.getName()+"  "+getName()+" = "+index;
	}
}
