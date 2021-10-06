package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.reports.InventoryDetails;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.InventoryDetailsServerReport;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class InventoryDetailsReport extends
		AbstractReportView<InventoryDetails> {
	public InventoryDetailsReport() {
		this.serverReport = new InventoryDetailsServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getInventoryDetails(start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(InventoryDetails record) {
		ClientItem item = Accounter.getCompany().getItem(record.getId());
		if (item.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
			ReportsRPC.openTransactionView(IAccounterCore.ASSEMBLY,
					record.getId());
		} else {
			ReportsRPC.openTransactionView(IAccounterCore.ITEM, record.getId());
		}
	}

	@Override
	public boolean canPrint() {
		return true;
	}

	@Override
	public boolean canExportToCsv() {
		return true;
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 202);
	}
}
