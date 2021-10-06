package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.BudgetOverviewServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class BudgetReportGenerator extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_BUDGET;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		BudgetOverviewServerReport budgetServerReport = new BudgetOverviewServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}

		};

		updateReport(budgetServerReport);
		budgetServerReport.resetVariables();
		try {
			budgetServerReport
					.onResultSuccess(reportsSerivce.getBudgetItemsList(
							getInputAsLong(0), getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return budgetServerReport.getGridTemplate();
	}

}
