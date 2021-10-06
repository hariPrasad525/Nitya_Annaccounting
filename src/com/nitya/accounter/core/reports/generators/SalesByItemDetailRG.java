package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.SalesByItemDetailServerReport;
import com.nitya.accounter.web.server.managers.SalesManager;

public class SalesByItemDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_SALESBYITEMDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		SalesByItemDetailServerReport salesByItemDetailServerReport = new SalesByItemDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(salesByItemDetailServerReport, financeTool);
		salesByItemDetailServerReport.resetVariables();
		try {
			SalesManager salesManager = financeTool.getSalesManager();
			if (getInputAsString(0) == null) {
				salesByItemDetailServerReport.onResultSuccess(salesManager
						.getSalesByItemDetail(startDate, endDate,
								company.getID()));
			} else {
				salesByItemDetailServerReport.onResultSuccess(salesManager
						.getSalesByItemDetail(getInputAsString(0), startDate,
								endDate, company.getID()));
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByItemDetailServerReport.getGridTemplate();
	}

}
