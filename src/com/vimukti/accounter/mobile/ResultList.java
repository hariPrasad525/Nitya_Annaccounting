package com.vimukti.accounter.mobile;

import java.util.ArrayList;

public class ResultList extends ArrayList<Record> {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String name;
	private String title;

	private boolean isMultiSelection;

	/**
	 * Creates new Instance
	 */
	public ResultList(String name) {
		this(name, false);
	}

	public ResultList(String name, boolean isMultiSelection) {
		this.name = name;
		this.isMultiSelection = isMultiSelection;
	}

	public boolean isMultiSelection() {
		return isMultiSelection;
	}

	public void setMultiSelection(boolean isMultiSelection) {
		this.isMultiSelection = isMultiSelection;
	}

	public String getName() {
		return name;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
