package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class SalesTaxItemsGrid extends BaseListGrid<ClientTAXItem> {

	public SalesTaxItemsGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("SalesTaxItemsGrid");
	}

	@Override
	protected void executeDelete(ClientTAXItem object) {
		// NOTHING TO DO.
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT };
	}

	@Override
	protected Object getColumnValue(ClientTAXItem obj, int index) {
		switch (index) {
		case 0:
			return obj.getName() != null ? obj.getName() : "";
		case 1:
			return obj.getTaxRate() + "%";
		}
		return null;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 1)
			return 450;
		else if (index ==0) {
			return 465;
		}
		return -1;
	}

	@Override
	public void onDoubleClick(ClientTAXItem obj) {
		// NOTHING TO DO.
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(), messages.currentRate() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "name", "current-rate" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "name-value", "current-rate-value" };
	}

}
