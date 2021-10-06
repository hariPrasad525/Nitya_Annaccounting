package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.VATUncategorisedAmountsServerReport;

public class UnCategorisedVATAmountRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_UNCATEGORISEDVATAMOUNT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		VATUncategorisedAmountsServerReport vatUncategorisedAmountsServerReport = new VATUncategorisedAmountsServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(vatUncategorisedAmountsServerReport, financeTool);
		vatUncategorisedAmountsServerReport.resetVariables();
		try {
			vatUncategorisedAmountsServerReport.onResultSuccess(financeTool
					.getReportManager().getUncategorisedAmountsReport(
							startDate, endDate, getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return vatUncategorisedAmountsServerReport.getGridTemplate();
	}

}
