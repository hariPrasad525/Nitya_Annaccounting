package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.APAgingDetailServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class APAgingDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_AP_AGEINGDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		APAgingDetailServerReport apAgingDetailServerReport = new APAgingDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(apAgingDetailServerReport);
		apAgingDetailServerReport.resetVariables();
		try {
			if (getInputAsString(0) == null) {
				apAgingDetailServerReport
						.onResultSuccess(reportsSerivce.getAgedCreditors(
								startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), company.getID()));
			} else {
				apAgingDetailServerReport
						.onResultSuccess(reportsSerivce.getAgedCreditors(
								getInputAsString(0),
								startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), company.getID()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return apAgingDetailServerReport.getGridTemplate();
	}

}
