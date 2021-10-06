package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ARAgingSummaryServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ARAgingSummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_AR_AGEINGSUMMARY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ARAgingSummaryServerReport arAgingSummaryServerReport = new ARAgingSummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(arAgingSummaryServerReport, financeTool);
		arAgingSummaryServerReport.resetVariables();
		try {
			arAgingSummaryServerReport.onResultSuccess(reportsSerivce
					.getDebitors(startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arAgingSummaryServerReport.getGridTemplate();
	}

}
