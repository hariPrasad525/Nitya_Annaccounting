package com.nitya.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;

public class Win8EditTableImpl<R> extends EditTableImpl<R> {

	protected AccounterMessages messages = Global.get().messages();
	private ArrayList<ArrayList<EditColumn<R>>> columns = new ArrayList<ArrayList<EditColumn<R>>>();

	private List<R> rows = new ArrayList<R>();
	private boolean isEnabled = true;
	private int numOfRowsPerObject;
	FlowPanel table;

	public Win8EditTableImpl() {
	}

	@Override
	public void init(EditTable wrapper, int numOfRowsPerObject) {
		this.wrapper = wrapper;
		this.numOfRowsPerObject = numOfRowsPerObject;
		this.wrapper.addStyleName("win8EditTable");

		table = new FlowPanel();
		table.addStyleName("win8EditTableContainer");
		this.wrapper.add(table);

		for (int x = 0; x < numOfRowsPerObject; x++) {
			this.columns.add(new ArrayList<EditColumn<R>>());
		}

	}

	@Override
	public void setEnabled(boolean isEnabled) {
		if (this.isEnabled != isEnabled) {
			this.isEnabled = isEnabled;
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
		RenderContext<R> context = new RenderContext<R>(row);
		context.setDesable(!isEnabled);
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				EditColumn<R> column = list.get(y);
				IsWidget widget = getWidget(index, y);
				column.render(widget, context);
			}
			index++;
		}
	}

	@Override
	public void updateFromGUI(R row) {
		int index = rows.indexOf(row) * numOfRowsPerObject;
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				EditColumn<R> column = list.get(y);
				IsWidget widget = getWidget(index, y);
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
		int index = rows.size() * numOfRowsPerObject;
		FlowPanel rowWiget = new FlowPanel();
		rowWiget.addStyleName("win8editrowPanel");
		table.insert(rowWiget, index);
		rows.add(row);
		RenderContext<R> context = new RenderContext<R>(row);
		context.setDesable(!isEnabled);
		for (int x = 0; x < columns.size(); x++) {
			ArrayList<EditColumn<R>> list = columns.get(x);
			for (int y = 0; y < list.size(); y++) {
				EditColumn<R> column = list.get(y);
				IsWidget widget = column.getWidget(context);
				IsWidget header = column.getHeader();

				if (!(header instanceof Label)) {
					header = new Label();
				}

				setWidget(index, y * 2, header);
				setWidget(index, (y * 2) + 1, widget);
				column.render(widget, context);
			}
			index++;
		}
	}

	private void clearEmptyMessage() {
		if (rows.size() == 0) {
			if (table.getWidgetCount() > 0) {
				removeStyleName(0, "norecord-empty-message");
				removeRow(0);
			}
			this.table.removeStyleName("no_records");
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
			index *= numOfRowsPerObject;
			for (int x = 0; x < numOfRowsPerObject; x++) {
				removeRow(index);
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
		table.clear();
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
				int index = (x) * numOfRowsPerObject;
				IsWidget widget = getWidget(index + y, colInd);
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
		int index = (row) * numOfRowsPerObject;
		IsWidget widget = getWidget(index, colInd);
		if (widget instanceof CheckBox) {
			CheckBox checkedWidget = (CheckBox) widget;
			checkedWidget.setValue(isChecked);
		}
	}

	@Override
	public boolean isChecked(R row, int colInd) {
		int x = getAllRows().indexOf(row);
		int index = (x) * numOfRowsPerObject;
		IsWidget widget = getWidget(index, colInd);
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
		for (ArrayList<EditColumn<R>> list : columns) {
			list.clear();
		}
		removeAllRows();

		this.wrapper.createColumns();
	}

	private void removeAllRows() {
		table.clear();
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
		clearEmptyMessageIfPresent();
		FlowPanel rowWiget = new FlowPanel();
		rowWiget.addStyleName("win8editrowPanel");
		table.insert(rowWiget, 0);
		setWidget(0, 0, new Label(emptyMessage));
		this.table.addStyleName("no_records");
	}

	@Override
	protected void clearEmptyMessageIfPresent() {
		clearEmptyMessage();
	}

	@Override
	public Widget getWidget(R row, EditColumn<R> column) {
		int rowIndex = rows.indexOf(row);
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
		return getWidget(rowIndex, columnIndex);
	}

	@Override
	public void addColumn(EditColumn<R> column, int rowIndex) {
		columns.get(rowIndex).add(column);
		column.setTable(this.wrapper);
	}

	@Override
	public Widget getWidget(int rowIndex, int colIndex) {
		FlowPanel widget = (FlowPanel) table.getWidget(rowIndex);
		return widget.getWidget((colIndex * 2) + 1);
	}

	private void setWidget(int row, int col, IsWidget widget) {
		FlowPanel rowWiget = (FlowPanel) table.getWidget(row);
		rowWiget.add(widget);
	}

	private void removeStyleName(int rowIndex, String string) {
		table.getWidget(rowIndex).removeStyleName(string);
	}

	private void removeRow(int rowIndex) {
		table.remove(rowIndex);
	}
}
