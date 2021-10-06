package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReconcilationsServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ReconciliationAccountsListRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_RECONCILATION_ACCOUNTSLIST;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ReconcilationsServerReport reconcilationsReport = new ReconcilationsServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(reconcilationsReport, financeTool);
		// reconcilationsReport.resetVariables();
		try {
			reconcilationsReport.onResultSuccess(reportsSerivce
					.getAllReconciliations(startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reconcilationsReport.getGridTemplate();
	}

}
