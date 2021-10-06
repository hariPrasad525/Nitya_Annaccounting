package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.core.Utility_R;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.SalesByLocationDetailsServerReport;

public class SalesOrPurchasesByClassOrLocationDetailRG extends
		AbstractReportGenerator {

	@Override
	public int getReportType() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		SalesByLocationDetailsServerReport salesByLocationDetailsServerReport = new SalesByLocationDetailsServerReport(
				startDate.getDate(), endDate.getDate(), generationType,
				isLocation(), isCustomer()) {

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
		updateReport(salesByLocationDetailsServerReport, financeTool);
		salesByLocationDetailsServerReport.resetVariables();
		try {
			if (getInputAsString(0) == null) {
				salesByLocationDetailsServerReport
						.onResultSuccess(reportsSerivce
								.getSalesByLocationDetailsReport(isLocation(),
										isCustomer(),
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										company.getID()));
			} else {
				salesByLocationDetailsServerReport
						.onResultSuccess(reportsSerivce
								.getSalesByLocationDetailsForLocation(
										isLocation(), isCustomer(),
										getInputAsString(0),
										startDate.toClientFinanceDate(),
										endDate.toClientFinanceDate(),
										company.getID()));
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesByLocationDetailsServerReport.getGridTemplate();
	}

	private boolean isLocation() {
		return reportType == REPORT_TYPE_SALESBYLOCATIONDETAIL
				|| reportType == REPORT_TYPE_PURCHASEBYLOCATIONDETAIL;
	}

	private boolean isCustomer() {
		return reportType == REPORT_TYPE_SALESBYCLASSDETAIL
				|| reportType == REPORT_TYPE_SALESBYLOCATIONDETAIL;
	}

}
