package com.vimukti.accounter.web.client.ui.grids;

import com.google.gwt.dom.client.Node;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Event;
import com.google.gwt.user.client.ui.FlexTable;
import com.google.gwt.user.client.ui.FlowPanel;
import com.google.gwt.user.client.ui.HTMLTable.Cell;
import com.google.gwt.user.client.ui.HTMLTable.CellFormatter;
import com.google.gwt.user.client.ui.HTMLTable.RowFormatter;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.ui.StyledPanel;

public class StyledFlexTable extends GwtFlexTable {
	private StyledPanel flexTable;

	public StyledFlexTable() {
		flexTable = new StyledPanel("flexTable");
	}

	public void onClick(Event event) {

	}

	public RowFormatter getRowFormatter() {
		return null;
	}

	public void setStyleName(String string) {
		flexTable.setStyleName(string);
	}

	public CellFormatter getCellFormatter() {
		return null;
	}

	// public void addClickHandler(ClickHandler clickHandler) {
	//
	// }

	public Cell getCellForEvent(ClickEvent event) {
		return null;
	}

	public Node getElement() {
		return null;
	}

	public void setText(final int row, final int column, String text) {
		Label label = new Label(text);
		label.getElement().setClassName("gridElementLabel");
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getCustomTable().cellClicked(row, column);
			}
		});
		setWidget(row, column, label);
	}

	public int getRowCount() {
		return flexTable.getWidgetCount();
	}

	public void removeRow(int row) {
		flexTable.remove(row);
	}

	public void removeCell(int row, int col) {
		FlowPanel rowWiget = (FlowPanel) flexTable.getWidget(row);
		rowWiget.remove(col * 2);
		rowWiget.remove((col * 2) + 1);
	}

	public void removeAllRows() {
		flexTable.clear();
	}

	public void setWidget(int row, int column, Widget widget) {
		if (flexTable.getWidgetCount() > row) {
			FlowPanel rowWiget = (FlowPanel) flexTable.getWidget(row);
			String string = getCustomTable().getColumns()[getCustomTable().isMultiSelectionEnable ? column - 1
					: column];
			Label label = new Label(string);
			label.addStyleName("header-label");
			rowWiget.add(label);
			rowWiget.add(widget);
		} else {
			FlowPanel rowWiget = new FlowPanel();
			rowWiget.addStyleName("rowWidget");
			String string = getCustomTable().getColumns()[getCustomTable().isMultiSelectionEnable ? column - 1
					: column];
			Label label = new Label(string);
			label.addStyleName("header-label");
			rowWiget.add(label);
			flexTable.add(rowWiget);
			rowWiget.add(widget);
		}
	}

	public Widget getWidget(int row, int col) {
		FlowPanel flowPanel = (FlowPanel) flexTable.getWidget(row);
		return flowPanel.getWidget((col * 2) + 1);
	}

	public void setHTML(final int row, final int column, String html) {
		Label label = new Label(html);
		label.getElement().setClassName("gridElementLabel");
		label.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				getCustomTable().cellClicked(row, column);
			}
		});
		setWidget(row, column, label);
	}

	public int getCellCount(int i) {
		int cellCount = 0;
		for (int rowIndex = 0; rowIndex < flexTable.getWidgetCount(); rowIndex++) {
			FlowPanel flowPanel = (FlowPanel) flexTable.getWidget(rowIndex);
			cellCount += flowPanel.getWidgetCount();
		}
		return cellCount / 2;
	}

	public int getOffsetWidth() {
		return flexTable.getOffsetWidth();
	}

	public void setWidth(String string) {
		flexTable.setWidth(string);
	}

	public Widget getParent() {
		return flexTable.getParent();
	}

	public boolean remove(Widget widget) {
		return flexTable.remove(widget);
	}

	public Widget getPanel() {
		return flexTable;

	}

	public void removeStyleName(String string) {
		flexTable.removeStyleName(string);
	}

	public FlexTable getFlexTable() {
		return null;
	}

	public void addStyleName(String string) {
		flexTable.addStyleName(string);
	}

	@Override
	public void insertRow(int beforeRow) {

	}

	public void addEmptyMessage(String msg) {
		flexTable.add(new Label(msg));
		this.addStyleName("no_records");
	}
}
