package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.VATItemSummaryServerReport;

public class VATItemSummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_VATITEMSUMMARY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		VATItemSummaryServerReport vatItemSummaryServerReport = new VATItemSummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(vatItemSummaryServerReport, financeTool);
		vatItemSummaryServerReport.resetVariables();
		try {
			vatItemSummaryServerReport.onResultSuccess(financeTool
					.getReportManager().getVATItemSummaryReport(startDate,
							endDate, getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatItemSummaryServerReport.getGridTemplate();
	}

}
