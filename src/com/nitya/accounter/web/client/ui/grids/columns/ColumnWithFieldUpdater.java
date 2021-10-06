package com.nitya.accounter.web.client.ui.grids.columns;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

public abstract class ColumnWithFieldUpdater<T, C> extends Column<T,C> implements FieldUpdater<T, C> {

	public ColumnWithFieldUpdater(Cell<C> cell) {
		super(cell);
		setSortable(true);
		setFieldUpdater(this);
	}

}
