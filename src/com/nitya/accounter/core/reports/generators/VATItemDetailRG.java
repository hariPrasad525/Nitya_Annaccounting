package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.VATItemDetailServerReport;

public class VATItemDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_VATITEMDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		VATItemDetailServerReport vatItemDetailServerReport = new VATItemDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(vatItemDetailServerReport, financeTool);
		vatItemDetailServerReport.resetVariables();
		try {
			vatItemDetailServerReport.onResultSuccess(financeTool
					.getReportManager().getVATItemDetailReport(
							getInputAsString(0), startDate, endDate,
							getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatItemDetailServerReport.getGridTemplate();
	}

}
