package com.nitya.accounter.web.client.ui.grids;

import java.util.List;
import java.util.Map;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.cellview.client.AbstractCellTableBuilder;
import com.google.gwt.user.cellview.client.AbstractHeaderOrFooterBuilder;
import com.google.gwt.user.cellview.client.Column;
import com.google.gwt.user.cellview.client.ColumnSortEvent.ListHandler;
import com.google.gwt.user.cellview.client.DataGrid;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;

public class AttendanceManagementGrid<T> extends DataGrid<T> {
	List<T> data;
	private SimplePager pager;
	private ListHandler<T> sortHandler;

	protected AccounterMessages messages = Global.get().messages();
	private boolean viewMode;
	private boolean isEnabled;

	public AttendanceManagementGrid(int numOfRowsPerObject) {
		initialize();
	}

	/**
	 * Initialize grid.
	 */
	public void initialize() {

		setWidth("100%");
		setAutoHeaderRefreshDisabled(true);

		setEmptyTableWidget(new Label(""));

		// Attach a column sort handler to the ListDataProvider to sort the list.
		sortHandler = new ListHandler<T>(data);
		addColumnSortHandler(sortHandler);

		// Create a Pager to control the table.
		SimplePager.Resources pagerResources = GWT.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0, true);
		pager.setDisplay(this);

		setKeyboardSelectionPolicy(KeyboardSelectionPolicy.ENABLED);

		addEmptyMessage(messages.noRecordsToShow());
		// Specify a custom table.
		setTableBuilder(new CustomTableBuilder(this));
		setHeaderBuilder(new CustomHeaderBuilder(this));
		setFooterBuilder(new CustomFooterBuilder(this));

	}

	public void addEmptyMessage(String noRecordsToShow) {
		setEmptyTableWidget(new Label(noRecordsToShow));
	}

	@SuppressWarnings("unchecked")
	public void setColumns(Map<String, Column<T, String>> cells) {
		for (String name : cells.keySet()) {
			this.addColumn((Column<T, ?>) cells.get(name), name);
		}
	}

	public void setEnabled(boolean isEnabled) {
		for (int i = 0; i < getColumnCount(); i++) {
			if (!isEnabled) {
				this.getColumn(i).setFieldUpdater(null);
			}
		}
	}

	/**
	 * Update a given row
	 * 
	 * @param row
	 */
	public void update(T row) {
		if (this.data.contains(row)) {
			this.data.set(this.data.indexOf(row), row);
		}
	}

	/**
	 * Add a new row
	 * 
	 * @param row
	 */
	public void add(T row) {
		this.data.add(row);
	}

	/**
	 * Delete given row
	 * 
	 * @param row
	 */
	public void delete(T row) {
		this.data.remove(row);
	}

	/**
	 * Return copy list of all rows
	 * 
	 * @return
	 */
	public List<T> getAllRows() {
		return data;
	}

	/**
	 * Remove all rows from table
	 */
	public void clear() {
		this.data.clear();
	}

	public void setAllRows(List<T> rows) {

	}

	public void addRows(List<T> rows) {
	}

	public List<T> getSelectedRecords(int colInd) {
		return null;
	}

	public void checkColumn(int row, int colInd, boolean isChecked) {
	}

	public boolean isDisabled() {
		return !isEnabled;
	}

	protected void onDelete(T obj) {

	}

	public void updateColumnHeaders() {

	}

	public boolean isInViewMode() {
		return viewMode;
	}

	public void createColumns() {
	}

	protected int getDefaultEmptyRowsSize() {
		return Accounter.isWin8App() ? 1 : 4;
	}

	protected T getEmptyRow() {
		return null;
	}

	protected void addEmptyRecords() {

	}

	public void addEmptyRowAtLast() {
		T row = getEmptyRow();
		if (row != null) {

		}
	}

	private class CustomHeaderBuilder extends AbstractHeaderOrFooterBuilder<T> {

		public CustomHeaderBuilder(DataGrid grid) {
			super(grid, false);
			setSortIconStartOfLine(false);
		}

		@Override
		protected boolean buildHeaderOrFooterImpl() {

			return true;
		}
	}

	/**
	 * Renders custom table footers that appear beneath the columns in the table.
	 * This footer consists of a single cell containing the average age of all
	 * contacts on the current page. This is an example of a dynamic footer that
	 * changes with the row data in the table.
	 */
	private class CustomFooterBuilder extends AbstractHeaderOrFooterBuilder<T> {

		public CustomFooterBuilder(DataGrid<T> dataGrid) {
			super(dataGrid, true);
		}

		@Override
		protected boolean buildHeaderOrFooterImpl() {

			return true;
		}
	}

	/**
	 * Renders the data rows that display each contact in the table.
	 */
	private class CustomTableBuilder extends AbstractCellTableBuilder<T> {

		@SuppressWarnings("deprecation")
		public CustomTableBuilder(DataGrid dataGrid) {
			super(dataGrid);

		}

		@SuppressWarnings("deprecation")
		@Override
		public void buildRowImpl(T rowValue, int absRowIndex) {
		}

	}

}
