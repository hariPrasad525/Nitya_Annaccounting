package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByCustomerSummaryServerReport;

public class SalesByCustomerSummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_SALESBYCUSTOMERSUMMARY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		SalesByCustomerSummaryServerReport salesByCustomerSummaryServerReport = new SalesByCustomerSummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(salesByCustomerSummaryServerReport, financeTool);
		salesByCustomerSummaryServerReport.resetVariables();
		try {
			salesByCustomerSummaryServerReport.onResultSuccess(reportsSerivce
					.getSalesByCustomerSummary(startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByCustomerSummaryServerReport.getGridTemplate();
	}

}
