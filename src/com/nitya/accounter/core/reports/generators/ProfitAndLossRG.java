package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.core.Utility_R;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ProfitAndLossServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ProfitAndLossRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PROFITANDLOSS;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ProfitAndLossServerReport profitAndLossServerReport = new ProfitAndLossServerReport(
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
		updateReport(profitAndLossServerReport, financeTool);
		profitAndLossServerReport.resetVariables();
		try {
			profitAndLossServerReport.onResultSuccess(reportsSerivce
					.getProfitAndLossReport(startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return profitAndLossServerReport.getGridTemplate();
	}

}
