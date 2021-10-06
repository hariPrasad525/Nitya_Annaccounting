package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.InventoryValutionSummaryServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class InventoryValutionSummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_INVENTORY_VALUTION_SUMMARY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		InventoryValutionSummaryServerReport summaryReport = new InventoryValutionSummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(summaryReport);
		try {
			summaryReport.onResultSuccess(financeTool.getInventoryManager()
					.getInventoryValutionSummary(getInputAsLong(0),
							company.getID(),
							new ClientFinanceDate(startDate.getDate()),
							new ClientFinanceDate(endDate.getDate())));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return summaryReport.getGridTemplate();
	}

}
