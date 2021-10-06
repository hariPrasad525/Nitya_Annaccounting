package com.nitya.accounter.mobile.xtream;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.mobile.Cell;
import com.nitya.accounter.mobile.Record;

public class JRecord {
	String code;
	List<JCell> cells = new ArrayList<JCell>();

	public void addAll(Record record) {
		code = record.getCode();
		for (Cell cell : record) {
			JCell jCell = new JCell();
			jCell.set(cell);
			cells.add(jCell);
		}
	}

}
