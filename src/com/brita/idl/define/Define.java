package com.brita.idl.define;

public class Define {

	public static final String PACKAGE = "package";
	public static final String IMPORT = "import";
	public static final String ENUM = "enum";
	public static final String MESSAGE = "message";

	public static final String SERVICE = "service";
	public static final String MODEL = "model";
	public static final String RPC = "rpc";
	public static final String RETURNS = "returns";
	public static final String REQUIRED = "required";
	public static final String REPEATED = "repeated";
	public static final String OPTIONAL = "optional";
	public static final String DEFAULT = "default";
	
	public static final String[] KEY_WORS
			= {PACKAGE, SERVICE, MODEL, RPC, RETURNS, IMPORT, ENUM, MESSAGE, REQUIRED, REPEATED, OPTIONAL, DEFAULT};
	
	
	public static final String INT = "int";
	public static final String LONG = "long";
	public static final String STRING = "string";
	public static final String FLOAT = "float";
	public static final String DOUBLE = "double";
	public static final String BOOL = "bool";
	
	public static final String[] BASE_TYPE = {INT, LONG, STRING, FLOAT, DOUBLE, BOOL};

}
