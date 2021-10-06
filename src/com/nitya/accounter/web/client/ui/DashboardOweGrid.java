package com.nitya.accounter.web.client.ui;

import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.ui.company.NewTAXAgencyAction;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.PayablesAndReceivablesReportsAction;

public class DashboardOweGrid extends ListGrid<ClientPayee> {

	public DashboardOweGrid() {
		super(false);
		this.getElement().addClassName("dashboard_grid_header");
	}

	@Override
	public void init() {
		super.init();
	}

	@Override
	protected int getColumnType(int index) {
		switch (index) {
		case 0:
			return ListGrid.COLUMN_TYPE_LABEL;
		case 1:
			return ListGrid.COLUMN_TYPE_DECIMAL_TEXT;
		default:
			break;
		}
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
		return new String[] { messages.name(), messages.balance() };
	}

	@Override
	protected Object getColumnValue(ClientPayee obj, int index) {
		switch (index) {
		case 0:
			return obj.getName();
		case 1:
			return DataUtils.amountAsStringWithCurrency(obj.getBalance(),
					obj.getCurrency());
		default:
			break;
		}
		return null;
	}

	@Override
	protected String[] getSelectValues(ClientPayee obj, int index) {
		return null;
	}

	@Override
	protected void onValueChange(ClientPayee obj, int index, Object value) {

	}

	@Override
	protected boolean isEditable(ClientPayee obj, int row, int index) {
		return false;
	}

	@Override
	protected void onClick(ClientPayee obj, int row, int index) {
		onClickOnObj(obj);
	}

	@Override
	public void onDoubleClick(ClientPayee obj) {
		onClickOnObj(obj);
	}

	private void onClickOnObj(ClientPayee obj) {
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			if (obj instanceof ClientTAXAgency) {
				new NewTAXAgencyAction().run((ClientTAXAgency) obj, false);
				return;
			}
			PayablesAndReceivablesReportsAction.statementReport(obj.getID(),
					obj.isVendor()).run();
		}
	}

	@Override
	protected int sort(ClientPayee obj1, ClientPayee obj2, int index) {
		return 0;
	}

	@Override
	protected String getHeaderStyle(int index) {
		switch (index) {
		case 0:
			return "name";
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
			return "name-val";
		case 1:
			return "balance-val";
		default:
			return "";
		}
	}

}
