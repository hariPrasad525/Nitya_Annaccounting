package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.MissingChecksServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class MissingChecksRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_MISSION_CHECKS;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		MissingChecksServerReport checksServerReport = new MissingChecksServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(checksServerReport);
		try {
			checksServerReport.onResultSuccess(financeTool.getReportManager()
					.getMissionChecksByAccount(getInputAsLong(0), startDate,
							endDate, company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return checksServerReport.getGridTemplate();
	}

}
