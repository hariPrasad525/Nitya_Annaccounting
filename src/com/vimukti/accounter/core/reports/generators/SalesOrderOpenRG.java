package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.SalesOrderServerReport;

public class SalesOrderOpenRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_SALESORDER_OPEN;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		SalesOrderServerReport salesOpenOrderServerReport = new SalesOrderServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(salesOpenOrderServerReport, financeTool);
		salesOpenOrderServerReport.resetVariables();
		try {
			salesOpenOrderServerReport.onResultSuccess(reportsSerivce
					.getSalesOrderReport(getInputAsInteger(0),
							startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesOpenOrderServerReport.getGridTemplate();
	}

}
