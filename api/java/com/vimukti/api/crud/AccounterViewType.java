package com.vimukti.api.crud;

public enum AccounterViewType {
	OPEN("open"), ALL("all");

	private String value;

	AccounterViewType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
