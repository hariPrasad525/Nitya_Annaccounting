package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.TransactionDetailByCatgoryServerReport;

public class TransactionDetailByAccountAndLocationRG extends
		AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_LOCATION;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		TransactionDetailByCatgoryServerReport byCatgoryServerReport = new TransactionDetailByCatgoryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(byCatgoryServerReport, financeTool);
		byCatgoryServerReport.resetVariables();

		long categoryType = getInputAsLong(0);
		long accountId = getInputAsLong(1);
		byCatgoryServerReport.onResultSuccess(financeTool.getReportManager()
				.getTransactionDetailByAccountAndCategory(2, categoryType,
						accountId, startDate.toClientFinanceDate(),
						endDate.toClientFinanceDate(), company.getId()));

		return byCatgoryServerReport.getGridTemplate();
	}

}
