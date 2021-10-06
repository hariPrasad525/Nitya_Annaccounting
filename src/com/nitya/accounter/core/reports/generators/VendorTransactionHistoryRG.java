package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.VendorTransactionHistoryServerReport;

public class VendorTransactionHistoryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_VENDORTRANSACTIONHISTORY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		VendorTransactionHistoryServerReport vendorTransactionHistoryServerReport = new VendorTransactionHistoryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(vendorTransactionHistoryServerReport, financeTool);
		vendorTransactionHistoryServerReport.resetVariables();
		try {
			vendorTransactionHistoryServerReport.onResultSuccess(reportsSerivce
					.getVendorTransactionHistory(
							startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vendorTransactionHistoryServerReport.getGridTemplate();
	}

}
