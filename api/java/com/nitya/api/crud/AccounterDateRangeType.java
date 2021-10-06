package com.nitya.api.crud;

public enum AccounterDateRangeType {
	THIS_MONTH("thismonth"), ALL("all");

	private String value;

	AccounterDateRangeType(String value) {
		this.value = value;
	}

	public String getValue() {
		return value;
	}
}
