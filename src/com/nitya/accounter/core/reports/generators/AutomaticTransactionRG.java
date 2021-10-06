package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.core.Utility_R;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.AutomaticTransactionsServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class AutomaticTransactionRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_AUTOMATIC_TRANSACTIONS;
	}

	@Override
	protected ReportGridTemplate<?> generate() {

		AutomaticTransactionsServerReport automaticTransactions = new AutomaticTransactionsServerReport(
				startDate.getDate(), endDate.getDate(), generationType) {

			@Override
			public ClientFinanceDate getCurrentFiscalYearEndDate() {
				return Utility_R.getCurrentFiscalYearEndDate(company);
			}

			@Override
			public ClientFinanceDate getCurrentFiscalYearStartDate() {
				return Utility_R.getCurrentFiscalYearStartDate(company);
			}

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(automaticTransactions);
		automaticTransactions.resetVariables();
		try {
			automaticTransactions.onResultSuccess(financeTool
					.getReportManager().getAutomaticTransactions(
							this.startDate, this.endDate, company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return automaticTransactions.getGridTemplate();
	}

}
