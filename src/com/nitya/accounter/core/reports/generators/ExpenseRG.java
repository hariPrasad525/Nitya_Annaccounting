package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ExpenseServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ExpenseRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_EXPENSE;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ExpenseServerReport expenseServerReport = new ExpenseServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(expenseServerReport, financeTool);
		expenseServerReport.resetVariables();
		try {
			expenseServerReport.onResultSuccess(reportsSerivce
					.getExpenseReportByType(getInputAsInteger(0),
							startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return expenseServerReport.getGridTemplate();
	}

}
