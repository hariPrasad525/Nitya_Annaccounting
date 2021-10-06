package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.BalanceSheetServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class BalanceSheetRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_BALANCESHEET;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		BalanceSheetServerReport balanceSheetServerReport = new BalanceSheetServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(balanceSheetServerReport, financeTool);
		balanceSheetServerReport.resetVariables();
		try {
			balanceSheetServerReport.onResultSuccess(reportsSerivce
					.getBalanceSheetReport(startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return balanceSheetServerReport.getGridTemplate();
	}

}
