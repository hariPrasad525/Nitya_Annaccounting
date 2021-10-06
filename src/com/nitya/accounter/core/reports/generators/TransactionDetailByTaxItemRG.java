package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.TransactionDetailByTaxItemServerReport;

public class TransactionDetailByTaxItemRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_TRANSACTIONDETAILBYTAXITEM;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		TransactionDetailByTaxItemServerReport transactionDetailByTaxItemServerReport = new TransactionDetailByTaxItemServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(transactionDetailByTaxItemServerReport, financeTool);
		transactionDetailByTaxItemServerReport.resetVariables();
		try {
			if (getInputAsString(0) == null) {
				transactionDetailByTaxItemServerReport
						.onResultSuccess(reportsSerivce
								.getTransactionDetailByTaxItem(
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										company.getID()));
			} else {
				transactionDetailByTaxItemServerReport
						.onResultSuccess(reportsSerivce
								.getTransactionDetailByTaxItem(
										getInputAsString(0),
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										company.getID()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return transactionDetailByTaxItemServerReport.getGridTemplate();
	}

}
