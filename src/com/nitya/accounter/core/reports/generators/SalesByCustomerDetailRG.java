package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.SalesByCustomerDetailServerReport;

public class SalesByCustomerDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_SALESBYCUSTOMERDETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		SalesByCustomerDetailServerReport salesByCustomerDetailServerReport = new SalesByCustomerDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(salesByCustomerDetailServerReport, financeTool);
		salesByCustomerDetailServerReport.resetVariables();
		try {
			if (getInputAsString(0) == null) {
				salesByCustomerDetailServerReport
						.onResultSuccess(reportsSerivce
								.getSalesByCustomerDetailReport(
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										company.getID()));
			} else {
				salesByCustomerDetailServerReport
						.onResultSuccess(reportsSerivce
								.getSalesByCustomerDetailReport(
										getInputAsString(0),
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										company.getID()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByCustomerDetailServerReport.getGridTemplate();
	}

}
