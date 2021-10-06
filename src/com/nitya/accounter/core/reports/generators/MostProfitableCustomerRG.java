package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.MostProfitableCustomerServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class MostProfitableCustomerRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_MOSTPROFITABLECUSTOMER;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		MostProfitableCustomerServerReport mostProfitableCustomerServerReport = new MostProfitableCustomerServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(mostProfitableCustomerServerReport, financeTool);
		mostProfitableCustomerServerReport.resetVariables();
		try {
			mostProfitableCustomerServerReport.onResultSuccess(financeTool
					.getCustomerManager().getMostProfitableCustomers(startDate,
							endDate, getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return mostProfitableCustomerServerReport.getGridTemplate();
	}

}
