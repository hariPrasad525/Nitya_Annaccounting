package com.nitya.accounter.web.client.ui.settings;

import java.util.List;

import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientStockTransfer;
import com.nitya.accounter.web.client.core.ClientStockTransferItem;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.company.WarehouseActions;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;

public class WarehouseTransferListGrid extends
		BaseListGrid<ClientStockTransfer> {

	public WarehouseTransferListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("WarehouseTransferListGrid");
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_LINK;
		case 1:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 3:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return 0;
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.fromWarehouse(), messages.toWarehouse(),
				messages.itemStatus(), "" };
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected void executeDelete(ClientStockTransfer object) {
		deleteObject(object);
	}

	@Override
	protected Object getColumnValue(ClientStockTransfer obj, int index) {
		switch (index) {
		case 0:
			return getCompany().getWarehouse(obj.getFromWarehouse()).getName();
		case 1:
			return getCompany().getWarehouse(obj.getToWarehouse()).getName();
		case 2:
			return getTransferItems(obj.getStockTransferItems());
		case 3:
			return Accounter.getFinanceMenuImages().delete();
		}
		return null;
	}

	private String getTransferItems(List<ClientStockTransferItem> items) {
		StringBuffer result = new StringBuffer();
		for (ClientStockTransferItem item : items) {
			result.append(" ");
			result.append(getCompany().getItem(item.getItem()).getName());
			result.append(" : ");
			result.append(item.getQuantity().getValue());
			result.append(" ");
			result.append(getCompany()
					.getUnitById(item.getQuantity().getUnit()).getName());
			result.append(",");
		}
		return result.toString();
	}

	@Override
	public void onDoubleClick(ClientStockTransfer obj) {
		if (!Utility.isUserHavePermissions(AccounterCoreType.STOCK_TRANSFER)) {
			return;
		}
		WarehouseActions.warehouseTransfer().run(obj, false);
	}

	@Override
	protected void onClick(ClientStockTransfer obj, int row, int col) {
		if (!Utility.isUserHavePermissions(AccounterCoreType.STOCK_TRANSFER)) {
			return;
		}
		if (col == 3) {
			showWarnDialog(obj);
		}
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
		case 1:
			return 200;
		case 2:
			return 470;
		case 3:
			return 40;
		}
		return -1;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "fromwarehouse", "towarehouse", "itemstatus",
				"last-col" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "fromwarehouse-value", "towarehouse-value",
				"itemstatus-value", "last-col-value" };
	}
}
