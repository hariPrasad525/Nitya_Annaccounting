package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.core.Utility_R;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.IncomeByCustomerDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class IncomeByCustomerDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_INCOMEBY_CUSTOMERDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		IncomeByCustomerDetailServerReport report = new IncomeByCustomerDetailServerReport(
				startDate.getDate(), endDate.getDate(), generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateByCompanyType(date);
			}

			@Override
			public ClientFinanceDate getCurrentFiscalYearEndDate() {
				return Utility_R.getCurrentFiscalYearEndDate(company);
			}

			@Override
			public ClientFinanceDate getCurrentFiscalYearStartDate() {
				return Utility_R.getCurrentFiscalYearStartDate(company);
			}
		};
		updateReport(report, financeTool);
		report.resetVariables();

		try {
			report.onResultSuccess(financeTool.getReportManager()
					.getIncomeByCustomerDetails(
							startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return report.getGridTemplate();
	}
}
