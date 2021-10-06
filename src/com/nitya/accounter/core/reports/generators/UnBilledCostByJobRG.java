package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.UnBilledCostsByJobServerReport;

public class UnBilledCostByJobRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_UNBILLED_COSTS_BY_JOB;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		UnBilledCostsByJobServerReport unBilledCostsByJobServerReport = new UnBilledCostsByJobServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(unBilledCostsByJobServerReport);
		try {
			unBilledCostsByJobServerReport.onResultSuccess(financeTool
					.getReportManager().getUnBilledCostsByJobReport(
							company.getId(),
							new ClientFinanceDate(startDate.getDate()),
							new ClientFinanceDate(endDate.getDate())));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return unBilledCostsByJobServerReport.getGridTemplate();
	}

}
