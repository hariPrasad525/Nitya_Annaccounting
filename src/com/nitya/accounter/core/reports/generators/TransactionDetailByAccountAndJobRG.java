package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.TransactionDetailByCatgoryServerReport;

public class TransactionDetailByAccountAndJobRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_JOB;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		TransactionDetailByCatgoryServerReport byCatgoryServerReportByJOB = new TransactionDetailByCatgoryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(byCatgoryServerReportByJOB, financeTool);
		byCatgoryServerReportByJOB.resetVariables();

		long catType = getInputAsLong(0);
		long account_id = getInputAsLong(1);
		byCatgoryServerReportByJOB.onResultSuccess(financeTool
				.getReportManager().getTransactionDetailByAccountAndCategory(3,
						catType, account_id, startDate.toClientFinanceDate(),
						endDate.toClientFinanceDate(), company.getId()));

		return byCatgoryServerReportByJOB.getGridTemplate();
	}

}
