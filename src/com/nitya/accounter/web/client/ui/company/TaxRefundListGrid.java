package com.nitya.accounter.web.client.ui.company;

import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientReceiveVAT;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class TaxRefundListGrid extends BaseListGrid<ClientReceiveVAT> {
	private ClientCurrency currency = getCompany().getPrimaryCurrency();

	public TaxRefundListGrid() {
		super(false);
	}

	public TaxRefundListGrid(TaxRefundListView taxRefundListView) {
		super(false);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_LINK,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "depositIn", "periodStartDate", "periodEndDate",
				"taxFiledDate", "taxAmount", "totalPaymentMade" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "depositIn-value", "periodStartDate-value",
				"periodEndDate-value", "taxFiledDate-value", "taxAmount-value",
				"totalPaymentMade-value" };
	}

	@Override
	protected void executeDelete(final ClientReceiveVAT object) {
		deleteTransaction(object);
	}

	private void deleteTransaction(final ClientReceiveVAT obj) {
		Accounter.deleteObject(this, obj);
	}

	@Override
	protected void onClick(ClientReceiveVAT obj, int row, int col) {
		if (col == 5) {
			showWarnDialog(obj);
		}
	}

	@Override
	protected void onDoubleClick(ClientReceiveVAT obj, int row, int index) {
		ReportsRPC.openTransactionView(ClientTransaction.TYPE_RECEIVE_TAX,
				obj.getID());
	}

	@Override
	protected Object getColumnValue(ClientReceiveVAT obj, int index) {
		switch (index) {
		case 0:
			return obj.getDepositIn() == 0 ? "" : getCompany().getAccount(
					obj.getDepositIn()).getName();
		case 1:
			return obj.getPaymentMethod();
		case 2:
			return obj.getCheckNumber();
		case 3:
			return new ClientFinanceDate(obj.getTransactionDate());
		case 4:
			return DataUtils.amountAsStringWithCurrency(obj.getTotal(),
					currency);
		case 5:
			return Accounter.getFinanceImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientReceiveVAT obj) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.depositIn(), messages.paymentMethod(),
				messages.checkNumber(), messages.transactionDate(),
				messages.amountReceived(), messages.delete() };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 1:
		case 2:
		case 3:
			return 150;
		case 4:
			return 80;
		case 5:
			return 40;
		}
		return -1;
	}
}
