package com.vimukti.accounter.mobile.xtream;

import com.vimukti.accounter.mobile.Cell;

public class JCell {
	String title;
	String value;

	public void set(Cell cell) {
		title = cell.getTitle();
		value = cell.toString();
	}

}
