package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.ReverseChargeListDetailServerReport;

public class ReverseChargeListDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_REVERSECHARGELISTDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ReverseChargeListDetailServerReport reverseChargeListDetailServerReport = new ReverseChargeListDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(reverseChargeListDetailServerReport, financeTool);
		reverseChargeListDetailServerReport.resetVariables();
		try {
			reverseChargeListDetailServerReport.onResultSuccess(financeTool
					.getReportManager().getReverseChargeListDetailReport(
							getInputAsString(0), startDate, endDate,
							company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reverseChargeListDetailServerReport.getGridTemplate();
	}

}
