package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.core.FinanceDate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.EstimatesByJobServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class EstimatesByJobRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_ESTIMATE_BY_JOB;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		EstimatesByJobServerReport estimatesByJobServerReport = new EstimatesByJobServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(estimatesByJobServerReport);
		try {
			estimatesByJobServerReport
					.onResultSuccess(financeTool.getReportManager()
							.getEstimatesByJob(
									new FinanceDate(startDate.getDate()),
									new FinanceDate(endDate.getDate()),
									company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return estimatesByJobServerReport.getGridTemplate();
	}

}
