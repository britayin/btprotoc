package com.brita.idl.node;

public class EnumItemNode extends Node{

	public int line;

	public String value;

	
	@Override
	public String toString() {
		
		return "\t"+getName()+"  "+value;
	}
}
