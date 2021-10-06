package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.ui.serverreports.InventoryItemServerReport;

public class InventoryItemReport extends AbstractReportView<ClientItem> {
	public InventoryItemReport() {

		this.serverReport = new InventoryItemServerReport(this);

	}

	@Override
	public void export(int generationType) {
		// TODO Auto-generated method stub

	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		// TODO Auto-generated method stub

	}

	@Override
	public int getToolbarType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void OnRecordClick(ClientItem record) {
		// TODO Auto-generated method stub

	}
}
