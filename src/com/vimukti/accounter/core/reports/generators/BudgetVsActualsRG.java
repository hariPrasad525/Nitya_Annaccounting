package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.reports.BudgetVsActualsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class BudgetVsActualsRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_BUDGET_VS_ACTUALS;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		BudgetVsActualsServerReport budgetVsActualsServerReport = new BudgetVsActualsServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}

		};
		updateReport(budgetVsActualsServerReport, financeTool);
		budgetVsActualsServerReport.resetVariables();
		int type = getInputAsInteger(0);
		try {
			budgetVsActualsServerReport.onResultSuccess(financeTool
					.getReportManager().getBudgetvsAcualReportData(startDate,
							endDate, getCompany().getID(), getInputAsLong(1),
							type));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return budgetVsActualsServerReport.getGridTemplate();
	}

}
