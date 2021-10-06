package com.nitya.accounter.web.client.ui.company;

import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.grids.BaseListGrid;
import com.nitya.accounter.web.client.ui.grids.ListGrid;
import com.nitya.accounter.web.client.ui.reports.ReportsRPC;

public class FileTaxListGrid extends BaseListGrid<ClientTAXReturn> {
	private ClientCurrency currency = getCompany().getPrimaryCurrency();

	public FileTaxListGrid() {
		super(false);
	}

	public FileTaxListGrid(FileTAXListView fileTAXListView) {
		super(false);
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DATE,
				ListGrid.COLUMN_TYPE_DATE, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "taxAgency", "periodStartDate", "periodEndDate",
				"taxFiledDate", "taxAmount", "totalPaymentMade", "delete" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "taxAgency-value", "periodStartDate-value",
				"periodEndDate-value", "taxFiledDate-value", "taxAmount-value",
				"totalPaymentMade-value", "delete-value" };
	}

	@Override
	protected void executeDelete(final ClientTAXReturn object) {
		Accounter.deleteObject(this, object);
	}

	@Override
	protected void onClick(ClientTAXReturn obj, int row, int col) {
		if (col == 6) {
			showWarnDialog(obj);
		}
	}

	@Override
	protected Object getColumnValue(ClientTAXReturn obj, int index) {
		switch (index) {

		case 0:
			return getCompany().getTaxAgency(obj.getTaxAgency()).getName();
		case 1:
			return new ClientFinanceDate(obj.getPeriodStartDate()).toString();
		case 2:
			return new ClientFinanceDate(obj.getPeriodEndDate());
		case 3:
			return new ClientFinanceDate(obj.getTransactionDate());
		case 4:
			return DataUtils.amountAsStringWithCurrency(
					obj.getTotalTAXAmount(), currency);
		case 5:
			return DataUtils.amountAsStringWithCurrency(
					(obj.getTotalTAXAmount() - obj.getBalance()), currency);
		case 6:
			return Accounter.getFinanceImages().delete();
		default:
			break;
		}
		return null;
	}

	@Override
	public void onDoubleClick(ClientTAXReturn obj) {
		ReportsRPC.openTransactionView(ClientTransaction.TYPE_TAX_RETURN,
				obj.getID());
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.taxAgency(), messages.periodStartDate(),
				messages.periodEndDate(), messages.taxFiledDate(),
				messages.taxAmount(), messages.totalPaymentMade(),
				messages.delete() };
	}

	@Override
	protected int getCellWidth(int index) {
		switch (index) {
		case 1:
		case 2:
		case 3:
		case 5:
			return 100;
		case 4:
			return 80;
		case 6:
			return 40;
		}
		return -1;
	}
}
