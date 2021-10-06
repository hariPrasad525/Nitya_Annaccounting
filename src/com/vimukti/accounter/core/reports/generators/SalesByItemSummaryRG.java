package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByItemSummaryServerReport;

public class SalesByItemSummaryRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_SALESBYITEMSUMMARY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		SalesByItemSummaryServerReport salesByItemSummaryServerReport = new SalesByItemSummaryServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(salesByItemSummaryServerReport, financeTool);
		salesByItemSummaryServerReport.resetVariables();
		try {
			salesByItemSummaryServerReport.onResultSuccess(reportsSerivce
					.getSalesByItemSummary(startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByItemSummaryServerReport.getGridTemplate();
	}

}
