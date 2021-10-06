package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.CashFlowStatementServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class CashFlowStatementRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_CASHFLOWSTATEMENT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		CashFlowStatementServerReport cashFlowStatementServerReport = new CashFlowStatementServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(cashFlowStatementServerReport, financeTool);
		cashFlowStatementServerReport.resetVariables();
		try {
			cashFlowStatementServerReport
					.onResultSuccess(reportsSerivce.getCashFlowReport(
							startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return cashFlowStatementServerReport.getGridTemplate();
	}

}
