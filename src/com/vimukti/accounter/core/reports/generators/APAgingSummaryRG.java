package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.APAgingSummaryServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class APAgingSummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_AP_AGEINGSUMMARY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		APAgingSummaryServerReport apAgingSummaryServerReport = new APAgingSummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(apAgingSummaryServerReport, financeTool);
		apAgingSummaryServerReport.resetVariables();
		try {
			apAgingSummaryServerReport.onResultSuccess(reportsSerivce
					.getCreditors(startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apAgingSummaryServerReport.getGridTemplate();
	}

}
