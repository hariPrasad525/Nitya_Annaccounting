package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.JobProfitabilitySummaryServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class JobProfitabilitySummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_JOB_PROFITABILTY_SUMMARY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		JobProfitabilitySummaryServerReport jobProfitabilitySummaryServerReport = new JobProfitabilitySummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(jobProfitabilitySummaryServerReport);
		try {
			jobProfitabilitySummaryServerReport.onResultSuccess(financeTool
					.getReportManager().getJobProfitabilitySummaryReport(
							company.getID(),
							new ClientFinanceDate(startDate.getDate()),
							new ClientFinanceDate(endDate.getDate())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobProfitabilitySummaryServerReport.getGridTemplate();
	}

}
