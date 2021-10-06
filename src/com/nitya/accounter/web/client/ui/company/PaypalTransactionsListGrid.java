package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.user.client.rpc.AsyncCallback;
import com.nitya.accounter.web.client.core.ClientPaypalTransation;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;

public class PaypalTransactionsListGrid extends
		BaseListGrid<ClientPaypalTransation> {

	public PaypalTransactionsListGrid(boolean isMultiSelectionEnable) {
		super(false);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT, };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "timeStampHeader", "timezoneHeader",
				"typeHeader", "emailHeader", "nameHeader", "stateHeader",
				"grossAmountHeader", "paypalChargeHeader", "netAmountHeader" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "timeStampData", "timezoneData", "typeData",
				"emailData", "nameData", "stateData", "grossAmountData",
				"paypalChargeData", "netAmountData" };
	}

	@Override
	protected void executeDelete(ClientPaypalTransation object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientPaypalTransation obj, int index) {
		switch (index) {
		case 0:
			return obj.getDate();
		case 1:
			return obj.getTimeZone();
		case 2:
			return obj.getType();
		case 3:
			return obj.getEmail();
		case 4:
			return obj.getBuyerName();
		case 5:
			return obj.getTransactionStatus();
		case 6:
			return obj.getGrossAmount();
		case 7:
			return obj.getPaypalFees();
		case 8:
			return obj.getNetAmount();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientPaypalTransation obj) {

		String transactionID = obj.getTransactionID();
		long accountID = obj.getAccountID();

		Accounter.createHomeService().getPaypalTransactionDetailsForId(
				transactionID, accountID, new AsyncCallback<String>() {

					@Override
					public void onFailure(Throwable caught) {
						// TODO Auto-generated method stub

					}

					@Override
					public void onSuccess(String result) {
						// TODO Auto-generated method stub

					}
				});

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.date(), messages.timezone(),
				messages.type(), messages.email(), messages.name(),
				messages.state(), messages.grossAmount(), "paypalCharge",
				messages.netAmount() };
	}

}
