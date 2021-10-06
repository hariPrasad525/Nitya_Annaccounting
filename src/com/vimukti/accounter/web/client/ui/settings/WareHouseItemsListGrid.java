package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientItemStatus;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class WareHouseItemsListGrid extends BaseListGrid<ClientItemStatus> {

	public WareHouseItemsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("WareHouseItemsListGrid");
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected int getCellWidth(int index) {
		return 455;
	}

	@Override
	protected void executeDelete(ClientItemStatus object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientItemStatus obj, int index) {
		switch (index) {
		case 0:
			ClientItem item = getCompany().getItem(obj.getItem());
			return item.getName();
		case 1:
			StringBuffer result = new StringBuffer();
			ClientUnit unit = getCompany().getUnitById(
					obj.getQuantity().getUnit());
			result.append(obj.getQuantity().getValue());
			result.append(" ");
			result.append(unit.getName());
			return result.toString();
		default:
			break;
		}
		return "";
	}

	@Override
	public void onDoubleClick(ClientItemStatus obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.itemName(),
				messages.availableQty() };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "itemname", "availableqty" };
		}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "itemname-value", "availableqty-value" };
		}

}
