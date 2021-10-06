/**
 * 
 */
package com.nitya.accounter.web.client.ui.grids.columns;

import java.util.ArrayList;

import com.google.gwt.user.cellview.client.CellTable;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.Header;

/**
 * @author Prasanna Kumar G
 * 
 */
public class CustomCellTable<T> extends CellTable<T> {

	/**
	 * The provider that holds the list of data.
	 */
	protected VListDataProvider<T> dataProvider = new VListDataProvider<T>();

	/** Attach a column sort handler to the ListDataProvider to sort the list. */
	protected ListHandler<T> sortHandler = new ListHandler<T>(dataProvider
			.getList());

	public void init() {
		this.addColumnSortHandler(sortHandler);
	}

	public void setData(ArrayList<T> list) {
		dataProvider.setList(list);
		if (dataProvider.getDataDisplays().contains(this)) {
			dataProvider.removeDataDisplay(this);
		}
		dataProvider.addDataDisplay(this);
	}

	/**
	 * @return the dataProvider
	 */
	public VListDataProvider<T> getDataProvider() {
		return dataProvider;
	}

	@Override
	public void insertColumn(int beforeIndex, Column<T, ?> col,
			Header<?> header, Header<?> footer) {
		super.insertColumn(beforeIndex, col, header, footer);
		if (col instanceof CustomColumn) {
			CustomColumn<T, ?> column = (CustomColumn<T, ?>) col;
			if (column.enableSorting()) {
				sortHandler.setComparator(column, column);
			}
			Width width = column.getWidth();
			if (width != null) {
				setColumnWidth(column, width.width, width.unit);
			}
		}
	}

}
