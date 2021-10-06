package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.InventoryDetailsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class InventoryDetailsRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_INVENTORY_DETAILS;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		InventoryDetailsServerReport inventoryDetailsServerReport = new InventoryDetailsServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateByCompanyType(date);
			}

		};
		updateReport(inventoryDetailsServerReport, financeTool);
		inventoryDetailsServerReport.onResultSuccess(financeTool
				.getReportManager().getInventoryDetails(
						new FinanceDate(startDate.getDate()),
						new FinanceDate(endDate.getDate()), company.getID()));

		return inventoryDetailsServerReport.getGridTemplate();
	}

}
