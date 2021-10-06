package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.core.Utility_R;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.serverreports.ProfitAndLossByLocationServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ProfitAndLossByClassOrLocationorJobRG extends
		AbstractReportGenerator {

	@Override
	public int getReportType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ClientCompany clientCompany;
		try {
			clientCompany = financeTool.getManager()
					.getObjectById(AccounterCoreType.COMPANY, company.getID(),
							company.getID());
			ProfitAndLossByLocationServerReport profitAndLossBylocationServerReport = new ProfitAndLossByLocationServerReport(
					clientCompany, startDate.getDate(), getType(),
					endDate.getDate(), generationType) {

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
					return getDateByCompanyType(date);
				}
			};
			updateReport(profitAndLossBylocationServerReport);
			profitAndLossBylocationServerReport.resetVariables();
			try {
				profitAndLossBylocationServerReport.onResultSuccess(financeTool
						.getReportManager().getProfitAndLossByLocation(
								getType(), startDate, endDate,
								getCompany().getId()));
			} catch (Exception e) {
				e.printStackTrace();
			}
			return profitAndLossBylocationServerReport.getGridTemplate();
		} catch (AccounterException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		return null;
	}

	private int getType() {
		switch (reportType) {
		case REPORT_TYPE_PROFITANDLOSSBYCLASS:
			return 1;
		case REPORT_TYPE_PROFITANDLOSSBYLOCATION:
			return 2;
		case REPORT_TYPE_PROFITANDLOSSBYJOB:
			return 3;
		default:
			break;
		}
		return 0;
	}

}
