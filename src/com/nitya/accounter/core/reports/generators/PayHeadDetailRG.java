package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.PayHeadDetailServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PayHeadDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PAY_HEAD_DETAIL_REPORT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PayHeadDetailServerReport summaryReport = new PayHeadDetailServerReport(
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
					.getPayHeadDetailReportList(getInputAsLong(0),
							getInputAsLong(1), startDate, endDate,
							company.getId()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return summaryReport.getGridTemplate();
	}
}
