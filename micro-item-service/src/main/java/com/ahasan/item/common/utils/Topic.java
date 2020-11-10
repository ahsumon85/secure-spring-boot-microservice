package com.ahasan.item.common.utils;

public enum Topic {

	PRODUCT("Product", 101),
	Sales("Sales", 102);
	
	private final String name;

	private final int code;

	private Topic(String name, int code) {
		this.name = name;
		this.code = code;
	}

	public String getName() {
		return name;
	}

	public int getCode() {
		return code;
	}


}
