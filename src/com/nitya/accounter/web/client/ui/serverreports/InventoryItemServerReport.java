package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class InventoryItemServerReport extends AbstractFinaneReport<ClientItem> {

	public InventoryItemServerReport(IFinanceReport<ClientItem> reportView) {
	}

	@Override
	public String[] getColunms() {

		return new String[] { "", getMessages().itemDescription(),
				getMessages().quantity(), getMessages().avgCost(),
				getMessages().assetValue(), getMessages().percentOfToTAsset(),
				getMessages().salesPrice(), getMessages().retailValue(),
				getMessages().perOfTotRetail() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_PERCENTAGE, COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_PERCENTAGE };

	}

	@Override
	public String[] getDynamicHeaders() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getTitle() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void processRecord(ClientItem record) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(ClientItem record, int columnIndex) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(ClientItem obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ClientFinanceDate getEndDate(ClientItem obj) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}
}
