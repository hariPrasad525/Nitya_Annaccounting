package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.TransactionDetailByCatgoryServerReport;

public class TransactionDetailByAccountAndClassRG extends
		AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_TRANSACTION_DETAILS_BY_ACCOUNT_AND_CLASS;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		TransactionDetailByCatgoryServerReport byCatgoryServerReportbyClass = new TransactionDetailByCatgoryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(byCatgoryServerReportbyClass, financeTool);
		byCatgoryServerReportbyClass.resetVariables();

		long classtype = getInputAsLong(0);
		long id = getInputAsLong(1);
		byCatgoryServerReportbyClass.onResultSuccess(financeTool
				.getReportManager().getTransactionDetailByAccountAndCategory(1,
						classtype, id, startDate.toClientFinanceDate(),
						endDate.toClientFinanceDate(), company.getId()));

		return byCatgoryServerReportbyClass.getGridTemplate();
	}

}
