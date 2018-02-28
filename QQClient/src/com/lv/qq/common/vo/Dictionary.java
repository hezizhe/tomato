package com.lv.qq.common.vo;

import java.io.Serializable;

public class Dictionary implements Serializable {
	
	private static final long serialVersionUID = 1L;

	private String name;
	private int value;
	private String type;
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public int getValue() {
		return value;
	}
	public void setValue(int value) {
		this.value = value;
	}
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	@Override
	public String toString() {
		return "Dictionary [name=" + name + ", value=" + value + ", type=" + type + "]";
	}
	
}
