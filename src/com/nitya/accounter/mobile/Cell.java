package com.nitya.accounter.mobile;

public class Cell {
	Object value;

	String title;

	Cell(String title, Object value) {
		this.title = title;
		this.value = value;
	}

	public Object getValue() {
		return value;
	}

	public void setValue(Object value) {
		this.value = value;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public String toString() {
		if (value == null) {
			return "";
		}
		return value.toString();
	}
}
