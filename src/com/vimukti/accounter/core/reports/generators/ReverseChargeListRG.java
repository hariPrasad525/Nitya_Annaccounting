package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.ReverseChargeListServerReport;

public class ReverseChargeListRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_REVERSECHARGELIST;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ReverseChargeListServerReport reverseChargeListServerReport = new ReverseChargeListServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(reverseChargeListServerReport, financeTool);
		reverseChargeListServerReport.resetVariables();
		try {
			reverseChargeListServerReport.onResultSuccess(financeTool
					.getReportManager().getReverseChargeListReport(startDate,
							endDate, getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return reverseChargeListServerReport.getGridTemplate();
	}

}
