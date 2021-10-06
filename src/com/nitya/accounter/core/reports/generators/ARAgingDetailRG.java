package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ARAgingDetailServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ARAgingDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_AR_AGEINGDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ARAgingDetailServerReport arAgingDetailServerReport = new ARAgingDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(arAgingDetailServerReport, financeTool);
		arAgingDetailServerReport.resetVariables();
		try {
			if (getInputAsString(0) == null) {
				arAgingDetailServerReport
						.onResultSuccess(reportsSerivce.getAgedDebtors(
								startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), company.getID()));
			} else {
				arAgingDetailServerReport
						.onResultSuccess(reportsSerivce.getAgedDebtors(
								getInputAsString(0),
								startDate.toClientFinanceDate(),
								endDate.toClientFinanceDate(), company.getID()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return arAgingDetailServerReport.getGridTemplate();
	}

}
