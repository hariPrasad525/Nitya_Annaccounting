/**
 * 
 */
package com.vimukti.accounter.web.client.ui.grids.columns;

import java.util.Comparator;

import com.google.gwt.cell.client.Cell;
import com.google.gwt.cell.client.FieldUpdater;
import com.google.gwt.user.cellview.client.Column;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class CustomColumn<R, C> extends Column<R, C> implements
		Comparator<R>, FieldUpdater<R, C> {

	/**
	 * Creates new Instance
	 */
	public CustomColumn(Cell<C> cell) {
		super(cell);
	}

	@Override
	public void update(int index, R object, C value) {
		// To Be Override by SortableColumns
	}

	public int compare(R o1, R o2) {
		// To Be Override by SortableColumns
		return 0;
	}

	@Override
	public boolean isSortable() {
		return enableSorting();
	}

	/**
	 * @return
	 */
	protected abstract boolean enableSorting();

	/**
	 * Column Width in Pixels
	 * 
	 * @return
	 */
	public Width getWidth() {
		return null;
	}

}
