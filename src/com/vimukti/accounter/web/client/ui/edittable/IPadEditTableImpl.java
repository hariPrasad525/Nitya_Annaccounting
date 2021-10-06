package com.vimukti.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.safehtml.shared.SafeHtmlBuilder;
import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.HTML;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.google.gwt.user.client.ui.FlexTable.FlexCellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;

public class IPadEditTableImpl<R> extends EditTableImpl<R> {

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

	public IPadEditTableImpl() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public void init(EditTable wrapper, int numOfRowsPerObject) {
		this.wrapper = wrapper;
		this.numOfRowsPerObject = numOfRowsPerObject;
		this.wrapper.addStyleName("ipadEditTable");

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

	@Override
	public void addColumn(EditColumn<R> column, int rowIndex) {
		// TODO this methods add headers to edit table.
	}

	@Override
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
	@Override
	public void update(R row) {
		int index = rows.indexOf(row) * numOfRowsPerObject;
		index += numOfRowsPerObject;// for header
		RenderContext<R> context = new RenderContext<R>(row);
		context.setDesable(!isEnabled);
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				EditColumn<R> column = list.get(y);
				IsWidget widget = table.getWidget(index, y);
				column.render(widget, context);
			}
			index++;
		}
	}

	@Override
	public void updateFromGUI(R row) {
		int index = rows.indexOf(row) * numOfRowsPerObject;
		index += numOfRowsPerObject;// for header
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				EditColumn<R> column = list.get(y);
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
	@Override
	public void add(R row) {
		this.wrapper.createColumns();
		clearEmptyMessage();
		rows.add(row);
		int index = rows.size() * numOfRowsPerObject;
		RenderContext<R> context = new RenderContext<R>(row);
		context.setDesable(!isEnabled);
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				EditColumn<R> column = list.get(y);
				IsWidget widget = column.getWidget(context);
				String valueAsString = column
						.getValueAsString(context.getRow());

				SafeHtmlBuilder htmlBuilder = new SafeHtmlBuilder();
				htmlBuilder.appendHtmlConstant(valueAsString);

				for (int r = 0; r < column.insertNewLineNumber(); r++) {
					htmlBuilder.appendHtmlConstant("<br>");
				}

				table.setWidget(index, y, new HTML(htmlBuilder.toSafeHtml()));
				flexCellFormatter.setColSpan(index, y, column.getColumnSpan());
				column.render(widget, context);
			}
			index++;
		}
	}

	private void clearEmptyMessage() {
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
	@Override
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
	@Override
	public List<R> getAllRows() {
		return new ArrayList<R>(rows);
	}

	/**
	 * Remove all rows from table
	 */
	@Override
	public void clear() {
		for (int x = 1; x <= rows.size(); x++) {
			for (int y = 0; y < numOfRowsPerObject; y++) {
				table.removeRow(numOfRowsPerObject);
			}
		}
		rows.clear();
	}

	@Override
	public void setAllRows(List<R> rows) {
		clear();
		if (rows == null) {
			return;
		}
		for (R row : rows) {
			add(row);
		}

	}

	@Override
	public void addRows(List<R> rows) {
		for (R row : rows) {
			add(row);
		}
	}

	@Override
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

	@Override
	public void checkColumn(int row, int colInd, boolean isChecked) {
		int index = (row + 1) * numOfRowsPerObject;
		IsWidget widget = table.getWidget(index, colInd);
		if (widget instanceof CheckBox) {
			CheckBox checkedWidget = (CheckBox) widget;
			checkedWidget.setValue(isChecked);
		}
	}

	@Override
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

	@Override
	public boolean isDisabled() {
		return !isEnabled;
	}

	@Override
	protected void onDelete(R obj) {

	}

	// @Override
	// protected void onAttach() {
	// createColumns();
	// if (rows == null || rows.isEmpty()) {
	// addEmptyMessage(messages.noRecordsToShow());
	// }
	// super.onAttach();
	// }
	//
	// @Override
	// protected void createColumns() {
	// if (!columnsCreated) {
	// initColumns();
	// }
	// columnsCreated = true;
	// }

	private void updateHeaderState(boolean isDisable) {
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				Widget widget = table.getWidget(x, y);
				if (widget instanceof CheckBox) {
					((CheckBox) widget).setEnabled(!isDisable);
				}
			}
		}
	}

	@Override
	public List<EditColumn<R>> getColumns() {
		return getColumns(0);
	}

	@Override
	public List<EditColumn<R>> getColumns(int row) {
		return columns.get(row);
	}

	@Override
	public void reDraw() {
		clear();
		columnsCreated = false;
		for (ArrayList<EditColumn<R>> list : columns) {
			list.clear();
		}
		table.removeAllRows();

		this.wrapper.createColumns();
	}

	@Override
	public void updateColumnHeaders() {
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (EditColumn<R> column : list) {
				column.updateHeader();
			}
		}
	}

	@Override
	public void addEmptyMessage(String emptyMessage) {
		if (rows.size() != 0) {
			return;
		}
		clearEmptyMessage();
		this.table.setWidget(numOfRowsPerObject, 0, new Label(emptyMessage));
		if (table.getRowFormatter() != null)
			this.table.getRowFormatter().setStyleName(numOfRowsPerObject,
					"norecord-empty-message");
		this.table.addStyleName("no_records");
		flexCellFormatter.setColSpan(numOfRowsPerObject, 0,
				columns.size() <= 0 ? 0 : columns.get(0).size());
	}

	@Override
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

	@Override
	public void addEmptyRowAtLast() {
	}

	@Override
	protected void initColumns() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isInViewMode() {
		// TODO Auto-generated method stub
		return false;
	}

}
