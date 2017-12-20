package com.brita.idl.node;

import java.util.ArrayList;
import java.util.List;

public class EnumNode extends Node{

	private List<EnumItemNode> items;

	public List<EnumItemNode> getItems() {
		return items;
	}

	public void addItem(EnumItemNode item) {
		if (items == null) {
			items = new ArrayList<>();
		}
		items.add(item);
	}
	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Enum : "+getName()+"\r\n");

		if (items!=null) {
			for (EnumItemNode node : items) {
				stringBuilder.append(node+"\r\n");
			}
		}
		
		return stringBuilder.toString();
	}
	
}
