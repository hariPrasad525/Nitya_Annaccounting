package com.vimukti.accounter.web.client.ui.customers;

import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientTAXItem;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class ItemsGrid extends BaseListGrid<ClientTransactionItem> {

	public static final int TYPE_ITEM = 1;
	public static final int TYPE_COMMENT = 2;
	public static final int TYPE_SALESTAX = 3;
	public static final int TYPE_ACCOUNT = 4;
	public static final int TYPE_VATITEM = 5;
	public static final int TYPE_NONE = 0;
	public static final int TYPE_SERVICE = 6;

	public ItemsGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("ItemsGrid");
	}

	@Override
	protected void executeDelete(ClientTransactionItem object) {
		// NOTHING TO DO.
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected Object getColumnValue(ClientTransactionItem obj, int index) {
		switch (index) {
		case 0:
			return getNameValue(obj);
		case 1:
			return obj.getQuantity();
		case 2:
			return DataUtils.amountAsStringWithCurrency(obj.getUnitPrice(),
					getCompany().getPrimaryCurrency());
		case 3:
			return DataUtils.amountAsStringWithCurrency(obj.getLineTotal(),
					getCompany().getPrimaryCurrency());
		default:
			return null;
		}
	}

	protected String getNameValue(ClientTransactionItem item) {
		switch (item.getType()) {

		case TYPE_ITEM:
			ClientItem itm = getCompany().getItem(item.getItem());
			return itm != null ? itm.getName() : "";
		case TYPE_ACCOUNT:
			ClientAccount account = getCompany().getAccount(item.getAccount());
			return account != null ? account.getDisplayName() : "";
		case TYPE_SALESTAX:
			ClientTAXItem vatItem = getCompany().getTaxItem(item.getVatItem());
			return vatItem != null ? vatItem.getDisplayName() : "";
		case TYPE_COMMENT:
			return item.getDescription() != null ? item.getDescription() : "";
		case TYPE_SERVICE:
			ClientItem serviceItm = getCompany().getItem(item.getItem());
			return serviceItm != null ? serviceItm.getName() : "";
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientTransactionItem obj) {
		// NOTHING TO DO.
	}

	// @Override
	// public boolean validateGrid() throws InvalidTransactionEntryException {
	// // NOTHING TO DO.
	// return false;
	// }

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name(), messages2.hours(),
				messages2.payTypeHourly()+messages.rate(), messages.total() };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		// case 0:
		// return 70;
		case 1:
			return 55;
		case 2:
			return 65;
		case 3:
			return 70;
		default:
			return 0;
		}
	}

	public AccounterCoreType getType() {
		return AccounterCoreType.ITEM;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "name", "hours", "hourly rate", "total" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "name-value", "quantity-value",
				"unitprice-value", "total-value" };
	}

}
