package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.PurchaseByVendorSummaryServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PurchasesByVendorSummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PURCHASEBYVENDORSUMMARY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PurchaseByVendorSummaryServerReport purchaseByVendorSummaryServerReport = new PurchaseByVendorSummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(purchaseByVendorSummaryServerReport, financeTool);
		purchaseByVendorSummaryServerReport.resetVariables();
		try {
			purchaseByVendorSummaryServerReport.onResultSuccess(financeTool
					.getVendorManager().getPurchasesByVendorSummary(startDate,
							endDate, getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseByVendorSummaryServerReport.getGridTemplate();
	}

}
