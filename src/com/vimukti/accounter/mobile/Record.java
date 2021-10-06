package com.vimukti.accounter.mobile;

import java.util.ArrayList;

public class Record extends ArrayList<Cell> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String code;
	private Object object;

	public Record(Object object) {
		this.object = object;
	}

	public void add(String title, Object value) {
		this.add(new Cell(title, value));
	}

	public String getCode() {
		return this.code;
	}

	public Object getObject() {
		return object;
	}

	public void setObject(Object object) {
		this.object = object;
	}

	/**
	 * @param i
	 */
	public void setCode(int i) {
		this.code = String.valueOf(i);
	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		if (size() == 1) {
			Cell cell = get(0);
			if (cell.title.isEmpty()) {
				builder.append(cell);
			} else {
				builder.append(cell.title).append("\t").append(cell);
			}
		} else {
			for (Cell cell : this) {
				builder.append(cell).append("\t\t");
			}
		}
		return code + ". " + builder.append("\n").toString();
	}

	public void add(String string) {
		add(string, "");
	}
}
