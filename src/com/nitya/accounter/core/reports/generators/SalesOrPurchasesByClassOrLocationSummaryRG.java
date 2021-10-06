package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.core.Utility_R;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.nitya.accounter.web.client.ui.serverreports.SalesByLocationsummaryServerReport;

public class SalesOrPurchasesByClassOrLocationSummaryRG extends
		AbstractReportGenerator {

	@Override
	public int getReportType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		SalesByLocationsummaryServerReport salesByLocationsummaryServerReport = new SalesByLocationsummaryServerReport(
				startDate.getDate(), endDate.getDate(), generationType) {

			@Override
			public ClientFinanceDate getCurrentFiscalYearEndDate() {
				return Utility_R.getCurrentFiscalYearEndDate(company);
			}

			@Override
			public ClientFinanceDate getCurrentFiscalYearStartDate() {
				return Utility_R.getCurrentFiscalYearStartDate(company);
			}

			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(salesByLocationsummaryServerReport, financeTool);
		salesByLocationsummaryServerReport.resetVariables();
		try {
			salesByLocationsummaryServerReport.onResultSuccess(reportsSerivce
					.getSalesByLocationSummaryReport(isLocation(),
							isCustomer(), startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByLocationsummaryServerReport.getGridTemplate();
	}

	private boolean isLocation() {
		return reportType == REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION
				|| reportType == REPORT_TYPE_PURCHASEBYLOCATIONDETAILFORLOCATION;
	}

	private boolean isCustomer() {
		return reportType == REPORT_TYPE_SALESBYCLASSDETAILFORCLASS
				|| reportType == REPORT_TYPE_SALESBYLOCATIONDETAILFORLOCATION;
	}

}
