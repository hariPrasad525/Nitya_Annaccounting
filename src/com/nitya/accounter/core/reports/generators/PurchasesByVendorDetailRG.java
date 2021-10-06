package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.PurchaseByVendorDetailServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PurchasesByVendorDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PURCHASEBYVENDORDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PurchaseByVendorDetailServerReport purchaseByVendorDetailServerReport = new PurchaseByVendorDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(purchaseByVendorDetailServerReport, financeTool);
		purchaseByVendorDetailServerReport.resetVariables();
		try {
			if (getInputAsString(0) == null) {

				purchaseByVendorDetailServerReport
						.onResultSuccess(reportsSerivce
								.getPurchasesByVendorDetail(
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										getCompany().getID()));
			} else {
				purchaseByVendorDetailServerReport.onResultSuccess(financeTool
						.getVendorManager().getPurchasesByVendorDetail(
								getInputAsString(0), startDate, endDate,
								getCompany().getID()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return purchaseByVendorDetailServerReport.getGridTemplate();
	}

}
