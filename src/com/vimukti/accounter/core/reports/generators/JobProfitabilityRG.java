package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.JobProfitabilityDetailServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class JobProfitabilityRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_JOB_PROFITABILITY_BY_JOBID;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		JobProfitabilityDetailServerReport jobProfitabilityDetailServerReport = new JobProfitabilityDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(jobProfitabilityDetailServerReport);
		try {

			long customerId = getInputAsLong(0);
			long jobId = getInputAsLong(1);
			jobProfitabilityDetailServerReport.onResultSuccess(financeTool
					.getReportManager().getJobProfitabilityDetailByJobReport(
							customerId, jobId, company.getId(),
							new ClientFinanceDate(startDate.getDate()),
							new ClientFinanceDate(endDate.getDate())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jobProfitabilityDetailServerReport.getGridTemplate();
	}

}
