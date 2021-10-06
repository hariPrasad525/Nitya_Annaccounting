package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.TAXItemDetailServerReportView;

public class TAXItemDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_TAX_ITEM_DETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		TAXItemDetailServerReportView taxItemDetailServerReportView = new TAXItemDetailServerReportView(
				startDate.getDate(), endDate.getDate(), generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(taxItemDetailServerReportView);
		taxItemDetailServerReportView.resetVariables();

		long taxReturnId = getInputAsLong(0);
		if (taxReturnId != 0) {
			taxItemDetailServerReportView.onResultSuccess(financeTool
					.getReportManager().getTaxItemDetailByTaxReturnId(
							taxReturnId, getCompany().getId()));
		} else {
			taxItemDetailServerReportView.onResultSuccess(financeTool
					.getReportManager().getTAXItemDetailReport(
							getCompany().getId(), getInputAsLong(1),
							startDate.getDate(), endDate.getDate()));
		}
		return taxItemDetailServerReportView.getGridTemplate();
	}

}
