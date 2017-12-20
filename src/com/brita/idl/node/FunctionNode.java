package com.brita.idl.node;

public class FunctionNode extends Node{
	
	private MessageNode requestModel;
	private MessageNode returnModel;
	
	
	public MessageNode getRequestModel() {
		return requestModel;
	}
	
	public void setRequestModel(MessageNode requestModel) {
		this.requestModel = requestModel;
	}
	
	public MessageNode getReturnModel() {
		return returnModel;
	}
	
	public void setReturnModel(MessageNode returnModel) {
		this.returnModel = returnModel;
	}
	
	@Override
	public String toString() {
		
		return "\t"+"function "+getName()+"("+requestModel.getName()+") returns ("+returnModel.getName()+")";
	}
	
}
