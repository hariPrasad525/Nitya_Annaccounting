package com.vimukti.accounter.web.client.ui.settings;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientUser;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.WarehouseActions;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class WarehouseListGrid extends BaseListGrid<ClientWarehouse> {

	public WarehouseListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("WarehouseListGrid");
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 1:
			return ListGrid.COLUMN_TYPE_LINK;
		case 2:
			return ListGrid.COLUMN_TYPE_TEXT;
		case 3:
			return ListGrid.COLUMN_TYPE_IMAGE;
		case 4:
			return ListGrid.COLUMN_TYPE_IMAGE;
		default:
			return 0;
		}
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 0:
			return 250;
		case 1:
		case 2:
			return 275;
		case 3:
		case 4:
			return 50;
		default:
			break;
		}
		return 0;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.warehouseCode(),
				messages.warehouseName(), messages.ddiNumber(),
				messages.items(), messages.delete() };
	}

	@Override
	protected void executeDelete(ClientWarehouse object) {

		AccounterAsyncCallback<ClientWarehouse> callback = new AccounterAsyncCallback<ClientWarehouse>() {

			public void onException(AccounterException caught) {
			}

			public void onResultSuccess(ClientWarehouse result) {
				if (result != null) {
					deleteObject(result);
				}
			}
		};
		Accounter.createGETService().getObjectById(AccounterCoreType.WAREHOUSE,
				object.getID(), callback);

	}

	@Override
	protected Object getColumnValue(ClientWarehouse obj, int index) {
		switch (index) {
		case 0:
			return obj.getWarehouseCode();
		case 1:
			return obj.getName();
		case 2:
			return obj.getDDINumber();
		case 3:
			return Accounter.getFinanceMenuImages().accounterRegisterIcon();
		case 4:
			return Accounter.getFinanceMenuImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	protected void onClick(ClientWarehouse obj, int row, int col) {
		if (!isCanOpenTransactionView(0, IAccounterCore.WAREHOUSE)) {
			return;
		}
		switch (col) {
		case 3:
			WarehouseActions.warehouseItemsList(obj.getID()).run(null, false);
			break;
		case 4:
			showWarnDialog(obj);
			break;

		default:
			break;
		}
	}

	@Override
	public void onDoubleClick(ClientWarehouse obj) {
		if (!isCanOpenTransactionView(0, IAccounterCore.WAREHOUSE)) {
			return;
		}
		WarehouseActions.newWarehouse().run(obj, false);
	}

	private boolean isUserHavePermissions(ClientWarehouse obj) {
		ClientUser user = Accounter.getUser();
		if (user.canDoInvoiceTransactions()) {
			return true;
		}

		if (user.getPermissions().getTypeOfInventoryWarehouse() == RolePermissions.TYPE_YES) {
			return true;
		}
		return false;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "warehousecode", "warehousename", "ddinumber",
				"items", "delete" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "warehousecode-value", "warehousename-value",
				"ddinumber-value", "items-value", "delete-value" };
	}
}
