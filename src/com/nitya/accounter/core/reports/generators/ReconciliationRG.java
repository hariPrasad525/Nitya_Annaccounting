package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReconcilationDetailsByAccountServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ReconciliationRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_RECONCILATIONS;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ReconcilationDetailsByAccountServerReport reconcilationDetailsByAccountServerReport = new ReconcilationDetailsByAccountServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(reconcilationDetailsByAccountServerReport, financeTool);
		reconcilationDetailsByAccountServerReport.resetVariables();
		try {
			reconcilationDetailsByAccountServerReport
					.onResultSuccess(reportsSerivce
							.getReconciliationItemByBankAccountID(
									startDate.toClientFinanceDate(),
									endDate.toClientFinanceDate(),
									getInputAsLong(0), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reconcilationDetailsByAccountServerReport.getGridTemplate();
	}

}
