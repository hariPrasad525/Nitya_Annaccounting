package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.TrialBalanceServerReport;

public class TrailBalanceRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_TRIALBALANCE;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		TrialBalanceServerReport trialBalanceServerReport = new TrialBalanceServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(trialBalanceServerReport, financeTool);
		trialBalanceServerReport.resetVariables();
		try {
			trialBalanceServerReport.onResultSuccess(reportsSerivce
					.getTrialBalance(startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return trialBalanceServerReport.getGridTemplate();
	}

}
