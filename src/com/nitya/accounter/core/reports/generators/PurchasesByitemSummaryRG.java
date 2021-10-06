package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.PurchaseByItemSummaryServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PurchasesByitemSummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PURCHASEBYITEMSUMMARY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PurchaseByItemSummaryServerReport purchaseByItemSummaryServerReport = new PurchaseByItemSummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(purchaseByItemSummaryServerReport, financeTool);
		purchaseByItemSummaryServerReport.resetVariables();
		try {

			purchaseByItemSummaryServerReport.onResultSuccess(financeTool
					.getPurchageManager().getPurchasesByItemSummary(startDate,
							endDate, getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseByItemSummaryServerReport.getGridTemplate();
	}

}
