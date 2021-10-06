package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.CustomerTransactionHistoryServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class CustomerTransactionHistoryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_CUSTOMERTRANSACTIONHISTORY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		CustomerTransactionHistoryServerReport customerTransactionHistoryServerReport = new CustomerTransactionHistoryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(customerTransactionHistoryServerReport, financeTool);
		customerTransactionHistoryServerReport.resetVariables();
		try {
			customerTransactionHistoryServerReport.onResultSuccess(financeTool
					.getCustomerManager().getCustomerTransactionHistory(
							startDate, endDate, company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return customerTransactionHistoryServerReport.getGridTemplate();
	}

}
