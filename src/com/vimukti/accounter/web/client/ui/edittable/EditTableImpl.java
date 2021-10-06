package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class EditTableImpl<R> {

	protected AccounterMessages messages = Global.get().messages();
	private ArrayList<ArrayList<EditColumn<R>>> columns = new ArrayList<ArrayList<EditColumn<R>>>();

	private List<R> rows = new ArrayList<R>();
	private boolean isEnabled = true;
	private boolean columnsCreated;
	private int numOfRowsPerObject;
	FlexTable table;
	private CellFormatter cellFormatter;
	private RowFormatter rowFormatter;
	private FlexCellFormatter flexCellFormatter;
	protected EditTable<R> wrapper;

	public EditTableImpl() {
	}

	public void init(EditTable<R> wrapper, int numOfRowsPerObject) {
		this.wrapper = wrapper;
		this.numOfRowsPerObject = numOfRowsPerObject;
		this.wrapper.addStyleName("editTable");

		table = new FlexTable();
		cellFormatter = table.getCellFormatter();
		rowFormatter = table.getRowFormatter();
		flexCellFormatter = table.getFlexCellFormatter();
		for (int x = 0; x < numOfRowsPerObject; x++) {
			rowFormatter.addStyleName(x, "editheader");
		}

		this.wrapper.add(table);

		for (int x = 0; x < numOfRowsPerObject; x++) {
			this.columns.add(new ArrayList<EditColumn<R>>());
		}
	}

	public void addColumn(EditColumn<R> column) {

		addColumn(column, 0);
	}

	public void addColumn(EditColumn<R> column, int rowIndex) {
		columns.get(rowIndex).add(column);
		int index = columns.get(rowIndex).size() - 1;
		column.setTable(this.wrapper);
		table.setWidget(rowIndex, index, column.getHeader());
		flexCellFormatter.setColSpan(rowIndex, index, column.getColumnSpan());
		// Set width
		int width = column.getWidth();
		if (width != -1) {
			cellFormatter.setWidth(rowIndex, index, width + "px");
		}
	}

	public void setEnabled(boolean isEnabled) {
		if (this.isEnabled != isEnabled) {
			this.isEnabled = isEnabled;
			updateHeaderState(isEnabled);
			for (R r : rows) {
				update(r);
			}
		}
	}

	/**
	 * Update a given row
	 * 
	 * @param row
	 */
	public void update(R row) {
		int index = rows.indexOf(row) * numOfRowsPerObject;
		index += numOfRowsPerObject;// for header
		RenderContext<R> context = new RenderContext<R>(row);
		context.setDesable(!isEnabled);
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				EditColumn<R> column = list.get(y);
				column = this.wrapper.getConcretColumn(column, row, context);
				IsWidget widget = table.getWidget(index, y);
				column.render(widget, context);
			}
			index++;
		}
	}

	public void updateFromGUI(R row) {
		int index = rows.indexOf(row) * numOfRowsPerObject;
		index += numOfRowsPerObject;// for header
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				EditColumn<R> column = list.get(y);
				column = this.wrapper.getConcretColumn(column, row, null);
				IsWidget widget = table.getWidget(index, y);
				column.updateFromGUI(widget, row);
			}
			index++;
		}
	}

	/**
	 * Add a new row
	 * 
	 * @param row
	 */
	public void add(R row) {
		this.wrapper.createColumns();
		clearEmptyMessageIfPresent();
		rows.add(row);
		int index = rows.size() * numOfRowsPerObject;
		RenderContext<R> context = new RenderContext<R>(row);
		context.setDesable(!isEnabled);
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				EditColumn<R> column = list.get(y);
				column = this.wrapper.getConcretColumn(column, row, context);
				IsWidget widget = column.getWidget(context);
				table.setWidget(index, y, widget);
				flexCellFormatter.setColSpan(index, y, column.getColumnSpan());
				column.render(widget, context);
			}
			index++;
		}
	}

	protected void clearEmptyMessageIfPresent() {
		if (rows.size() == 0) {
			if (table != null && table.getRowFormatter() != null) {
				this.table.getRowFormatter().removeStyleName(
						numOfRowsPerObject, "norecord-empty-message");
				this.table.removeStyleName("no_records");
			}
		}
	}

	/**
	 * Delete given row
	 * 
	 * @param row
	 */
	public void delete(R row) {
		int index = rows.indexOf(row);
		rows.remove(row);
		if (index != -1) {
			index += 1;// For header
			index *= numOfRowsPerObject;
			for (int x = 0; x < numOfRowsPerObject; x++) {
				table.removeRow(index);
			}
		}
		addEmptyMessage(messages.noRecordsToShow());
	}

	/**
	 * Return copy list of all rows
	 * 
	 * @return
	 */
	public List<R> getAllRows() {
		return new ArrayList<R>(rows);
	}

	/**
	 * Remove all rows from table
	 */
	public void clear() {
		for (int x = 1; x <= rows.size(); x++) {
			for (int y = 1; y <= numOfRowsPerObject; y++) {
				table.removeRow(y);
			}
		}
		rows.clear();
	}

	protected void addEmptyRecords() {
		clearEmptyMessageIfPresent();
		int size = wrapper.getDefaultEmptyRowsSize();
		for (int i = 0; i < size; i++) {
			R r = wrapper.getEmptyRow();
			if (r == null) {
				break;
			}
			add(r);
		}
	}

	public void setAllRows(List<R> rows) {
		clear();
		if (rows == null) {
			return;
		}
		for (R row : rows) {
			add(row);
		}

	}

	public void addRows(List<R> rows) {
		for (R row : rows) {
			add(row);
		}
	}
	
	public void updateRows(List<R> rows) {
		for (R row : rows) {
			update(row);
		}
	}

	public List<R> getSelectedRecords(int colInd) {
		List<R> selected = new ArrayList<R>();
		for (int x = 0; x < rows.size(); x++) {
			for (int y = 0; y < numOfRowsPerObject; y++) {
				int index = (x + 1) * numOfRowsPerObject;
				IsWidget widget = table.getWidget(index + y, colInd);
				if (widget instanceof CheckBox) {
					CheckBox checkedWidget = (CheckBox) widget;
					if (checkedWidget.getValue()) {
						R r = rows.get(x);
						if (!selected.contains(r)) {
							selected.add(r);
						}
					}
				}
			}
		}
		return selected;
	}

	public void checkColumn(int row, int colInd, boolean isChecked) {
		int index = (row + 1) * numOfRowsPerObject;
		IsWidget widget = table.getWidget(index, colInd);
		if (widget instanceof CheckBox) {
			CheckBox checkedWidget = (CheckBox) widget;
			checkedWidget.setValue(isChecked);
		}
	}

	public boolean isChecked(R row, int colInd) {
		int x = getAllRows().indexOf(row);
		int index = (x + 1) * numOfRowsPerObject;
		IsWidget widget = table.getWidget(index, colInd);
		if (widget instanceof CheckBox) {
			CheckBox checkedWidget = (CheckBox) widget;
			return checkedWidget.getValue();
		}
		return false;
	}

	public boolean isDisabled() {
		return !isEnabled;
	}

	protected void onDelete(R obj) {

	}

	private void updateHeaderState(boolean isDisable) {
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				Widget widget = table.getWidget(x, y);
				if (widget instanceof CheckBox) {
					((CheckBox) widget).setEnabled(isDisable);
				}
			}
		}
	}

	public List<EditColumn<R>> getColumns() {
		return getColumns(0);
	}

	public List<EditColumn<R>> getColumns(int row) {
		return columns.get(row);
	}

	public void reDraw() {
		clear();
		columnsCreated = false;
		for (ArrayList<EditColumn<R>> list : columns) {
			list.clear();
		}
		table.removeAllRows();

		this.wrapper.createColumns();
	}

	public void updateColumnHeaders() {
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (EditColumn<R> column : list) {
				column.updateHeader();
			}
		}
	}

	public void addEmptyMessage(String emptyMessage) {
		if (rows.size() != 0) {
			return;
		}
		clearEmptyMessageIfPresent();
		this.table.setWidget(numOfRowsPerObject, 0, new Label(emptyMessage));
		this.table.addStyleName("no_records");
		flexCellFormatter.setColSpan(numOfRowsPerObject, 0,
				columns.size() <= 0 ? 0 : columns.get(0).size());
	}

	public Widget getWidget(R row, EditColumn<R> column) {
		int rowIndex = rows.indexOf(row);
		rowIndex++;// for header
		rowIndex *= numOfRowsPerObject;

		int columnIndex = -1;
		for (ArrayList<EditColumn<R>> list : columns) {
			columnIndex = list.indexOf(column);
			if (columnIndex != -1) {
				break;
			}
		}
		if (columnIndex == -1) {
			return null;
		}
		return table.getWidget(rowIndex, columnIndex);
	}

	public void addEmptyRowAtLast() {
	}
	
	protected Element getRowElement(int row) {
		return this.rowFormatter.getElement(row);
	}

	public List<R> getRows() {
		return rows;
	}

	public boolean iscolumnsCreated() {
		return columnsCreated;
	}

	public void setcolumnsCreated(boolean b) {
		columnsCreated = b;
	}

	// @Override
	// protected void onAttach() {
	// createColumns();
	// if (getRows() == null || getRows().isEmpty()) {
	// addEmptyMessage(messages.noRecordsToShow());
	// }
	// super.onAttach();
	// }
	//
	// protected void createColumns() {
	// if (!iscolumnsCreated()) {
	// initColumns();
	// }
	// setcolumnsCreated(true);
	// }

	protected void initColumns() {
	}

	protected boolean isInViewMode() {
		return false;
	}

	public Widget getWidget(int rowIndex, int colIndex) {
		return table.getWidget(rowIndex, colIndex);
	}
}
