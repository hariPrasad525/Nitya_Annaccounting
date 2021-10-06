package com.nitya.accounter.mobile.xtream;

import com.nitya.accounter.mobile.Cell;

public class JCell {
	String title;
	String value;

	public void set(Cell cell) {
		title = cell.getTitle();
		value = cell.toString();
	}

}
