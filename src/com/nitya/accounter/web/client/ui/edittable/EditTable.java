package com.nitya.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Element;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;

public abstract class EditTable<R> extends FlowPanel {
	protected AccounterMessages messages = Global.get().messages();
	EditTableImpl<R> impl;

	public EditTable() {
		this(1);
	}

	public EditTable(int numOfRowsPerObject) {
		impl = GWT.create(EditTableImpl.class);
		impl.init(this, numOfRowsPerObject);
		impl.addEmptyMessage(messages.noRecordsToShow());
	}

	public void addColumn(EditColumn<R> column) {
		impl.addColumn(column);
	}

	public void addColumn(EditColumn<R> column, int rowIndex) {
		impl.addColumn(column, rowIndex);
	}

	public void setEnabled(boolean isEnabled) {
		impl.setEnabled(isEnabled);
	}

	/**
	 * Update a given row
	 * 
	 * @param row
	 */
	public void update(R row) {
		impl.update(row);
	}

	public void updateFromGUI(R row) {
		impl.updateFromGUI(row);
	}

	/**
	 * Add a new row
	 * 
	 * @param row
	 */
	public void add(R row) {
		impl.add(row);
	}

	/**
	 * Delete given row
	 * 
	 * @param row
	 */
	public void delete(R row) {
		impl.delete(row);
	}
	
	/**
	 * Expand Parent row
	 * 
	 * @param row
	 * @param isExpand 
	 */
	public void onExpand(R row, boolean isExpand) {
	}

	/**
	 * Return copy list of all rows
	 * 
	 * @return
	 */
	public List<R> getAllRows() {
		return impl.getAllRows();
	}

	/**
	 * Remove all rows from table
	 */
	public void clear() {
		impl.clear();

	}

	public void setAllRows(List<R> rows) {
		impl.setAllRows(rows);

	}

	public void addRows(List<R> rows) {
		impl.addRows(rows);
	}
	
	public void updateRows(List<R> rows) {
		impl.updateRows(rows);
	}

	public List<R> getSelectedRecords(int colInd) {
		return impl.getSelectedRecords(colInd);
	}

	public void checkColumn(int row, int colInd, boolean isChecked) {
		impl.checkColumn(row, colInd, isChecked);
	}

	public boolean isChecked(R row, int colInd) {
		return impl.isChecked(row, colInd);
	}

	public boolean isDisabled() {
		return impl.isDisabled();
	}

	protected void onDelete(R obj) {

	}

	protected abstract void initColumns();

	public List<EditColumn<R>> getColumns() {
		return impl.getColumns();
	}
	
	public EditColumn<R> getConcretColumn(EditColumn<R> column, R row, RenderContext<R> context) {
		return column;
	}

	public List<EditColumn<R>> getColumns(int row) {
		return impl.getColumns(row);
	}

	public void reDraw() {
		impl.reDraw();
	}

	public void updateColumnHeaders() {
		impl.updateColumnHeaders();
	}

	public void addEmptyMessage(String emptyMessage) {
		impl.addEmptyMessage(emptyMessage);
	}

	protected abstract boolean isInViewMode();

	public Widget getWidget(R row, EditColumn<R> column) {
		return impl.getWidget(row, column);
	}

	@Override
	protected void onAttach() {
		createColumns();
		if (impl.getRows() == null || impl.getRows().isEmpty()) {
			addEmptyMessage(messages.noRecordsToShow());
		}
		super.onAttach();
	}

	public void createColumns() {
		if (!impl.iscolumnsCreated()) {
			initColumns();
		}
		impl.setcolumnsCreated(true);
	}

	public IsWidget getWidget(int rowIndex, int colIndex) {
		return impl.getWidget(rowIndex, colIndex);
	}

	protected int getDefaultEmptyRowsSize() {
		return Accounter.isWin8App() ? 1 : 1;
	}

	protected R getEmptyRow() {
		return null;
	}

	protected void addEmptyRecords() {
		impl.addEmptyRecords();
	}

	public void addEmptyRowAtLast() {
		R row = getEmptyRow();
		if (row != null) {
			impl.add(row);
		}
	}
	
	public Element getRowElement(int row) {
		return this.impl.getRowElement(row);
	}
	
	public void consultantFocus()  {
		impl.focusConsultantTable();
	}
}
