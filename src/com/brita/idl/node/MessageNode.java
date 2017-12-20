package com.brita.idl.node;

import java.util.ArrayList;
import java.util.List;

public class MessageNode extends Node{

	public List<String> packagePaths;

	public MessageNode parentNode;

	public List<EnumNode> enums;

	public List<MessageNode> models;

	public List<MemberNode> members;

	public List<MemberNode> getMembers() {
		return members;
	}

	public MessageNode(MessageNode messageNode) {
		super();
		parentNode = messageNode;
	}

	public void addMember(MemberNode member) {
		if (members == null) {
			members = new ArrayList<>();
		}
		members.add(member);
	}

	public void addEnumNode(EnumNode enumNode) {
		if (enums == null) {
			enums = new ArrayList<>();
		}
		enums.add(enumNode);
	}

	public void addMessageNode(MessageNode messageNode) {
		if (models == null) {
			models = new ArrayList<>();
		}
		models.add(messageNode);
	}
	
	@Override
	public String toString() {
		
		StringBuilder stringBuilder = new StringBuilder();
		stringBuilder.append("Message : "+getName()+"\r\n");
		
		if (members!=null) {
			for (MemberNode node : members) {
				stringBuilder.append(node+"\r\n");
			}
		}
		
		return stringBuilder.toString();
	}

	public boolean isSame(List<String> packagePaths, String name){
		if (!this.getName().equals(name)) {
			return false;
		}

		if (packagePaths == null) {
			if (this.packagePaths == null) {
				return true;
			}else {
				return false;
			}
		}else{
			for (int i = 0; i < packagePaths.size(); i++) {
				if (i < this.packagePaths.size()) {
					String path = packagePaths.get(i);
					String path1 = this.packagePaths.get(i);
					if (!path.equals(path1)) {
						return false;
					}
				}else{
					return false;
				}
			}
		}

		return false;
	}
	
}
