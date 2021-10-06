package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.PaySlipSummaryServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PaySlipSummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PAYSLIP_SUMMARY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PaySlipSummaryServerReport byCatgoryServerReport = new PaySlipSummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(byCatgoryServerReport, financeTool);
		byCatgoryServerReport.resetVariables();

		byCatgoryServerReport.onResultSuccess(financeTool.getPayrollManager()
				.getPaySlipSummary(startDate, endDate, company.getId()));

		return byCatgoryServerReport.getGridTemplate();
	}

}
