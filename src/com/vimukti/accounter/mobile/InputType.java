package com.vimukti.accounter.mobile;

public class InputType {
	int type;
	private int inputType;
	private String name = "";
	private String value = "";

	public InputType(int inputType) {
		this(inputType, "", "");
	}

	public InputType(int inputType, String name) {
		this(inputType, name, "");
	}

	public InputType(int inputType, String name, String value) {
		this.setInputType(inputType);
		this.setName(name);
		this.setValue(value);
		this.type = 3;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getInputType() {
		return inputType;
	}

	public void setInputType(int inputType) {
		this.inputType = inputType;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
}
