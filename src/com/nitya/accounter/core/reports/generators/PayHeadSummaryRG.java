package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.PayHeadSummaryServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PayHeadSummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PAY_HEAD_SUMMARY_REPORT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PayHeadSummaryServerReport summaryReport = new PayHeadSummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(summaryReport, financeTool);
		summaryReport.resetVariables();
		try {
			summaryReport.onResultSuccess(reportsSerivce
					.getPayHeadSummaryReportDetais(getInputAsLong(0),
							startDate, endDate, company.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return summaryReport.getGridTemplate();
	}

}
