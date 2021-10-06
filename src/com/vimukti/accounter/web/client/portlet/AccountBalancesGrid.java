package com.vimukti.accounter.web.client.portlet;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class AccountBalancesGrid extends ListGrid<ClientAccount> {

	public AccountBalancesGrid() {
		super(false);
		this.getElement().addClassName("dashboard_grid_header");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected int getColumnType(int index) {
		if (index == 0) {
			return ListGrid.COLUMN_TYPE_TEXT;
		} else {
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		}
	}

	@Override
	protected Object getColumnValue(ClientAccount obj, int index) {
		if (index == 0) {
			return obj.getName();
		} else {
			return DataUtils.amountAsStringWithCurrency(
					Math.abs(obj.getTotalBalanceInAccountCurrency()),
					getCompany().getCurrency(obj.getCurrency()));
		}
	}

	@Override
	protected String[] getSelectValues(ClientAccount obj, int index) {
		return null;
	}

	@Override
	protected void onValueChange(ClientAccount obj, int index, Object value) {
	}

	@Override
	protected boolean isEditable(ClientAccount obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientAccount obj, int row, int index) {

	}

	@Override
	public void onDoubleClick(ClientAccount obj) {

	}

	@Override
	protected int sort(ClientAccount obj1, ClientAccount obj2, int index) {
		return 0;
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 1) {
			return 200;
		} else {
			return -1;
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.accountName(), messages.balance() };
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "accountName";
		case 1:
			return "balance";
		default:
			return "";
		}
	}

	@Override
	protected String getRowElementsStyle(int index) {
		switch (index) {
		case 0:
			return "accountName-value";
		case 1:
			return "balance-value";
		default:
			return "";
		}
	}

}
