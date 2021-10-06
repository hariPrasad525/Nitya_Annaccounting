package com.nitya.accounter.web.client.ui.edittable;


public class IpadEditTableView extends EditTableView {
/*
	// FIXME this class does not support more than one row per object

	StyledPanel table;
	private IsWidget[] headers = new IsWidget[0];

	@Override
	public void init(int numOfRowsPerObject) {
		table = new StyledPanel("table");
	}

	@Override
	public Widget getWidget() {
		return table;
	}

	@Override
	public void setWidget(int rowIndex, int index, IsWidget widget) {

		if (rowIndex == 0) {
			headers = resize(index);
			headers[index] = widget;
		} else {
			int rowCount = this.table.getWidgetCount();
			for (int x = rowCount; x <= rowIndex; x++) {
				StyledPanel row = new StyledPanel("row");
				table.add(row);
			}
			StyledPanel rowWidget = (StyledPanel) this.table
					.getWidget(rowIndex);
			int columnCount = rowWidget.getWidgetCount();
			for (int x = columnCount; x <= index; x++) {
				StyledPanel column = new StyledPanel("column");
				rowWidget.add(column);
			}
			StyledPanel column = (StyledPanel) rowWidget.getWidget(rowIndex);
			column.add(headers[index]);
			column.add(widget);
		}
	}

	private IsWidget[] resize(int index) {
		if (headers.length <= index) {
			IsWidget[] temp = new IsWidget[index + 1];
			System.arraycopy(headers, 0, temp, 0, headers.length);
			return temp;
		}
		return headers;
	}

	@Override
	public void setColSpan(int rowIndex, int index, int columnSpan) {
	}

	@Override
	public void setWidth(int rowIndex, int index, String width) {
	}

	@Override
	public RowFormatter getRowFormatter() {
		return null;
	}

	public CellFormatter getCellFormatter() {
		return null;
	}

	public FlexCellFormatter getFlexCellFormatter() {
		return null;
	}
*/

}
