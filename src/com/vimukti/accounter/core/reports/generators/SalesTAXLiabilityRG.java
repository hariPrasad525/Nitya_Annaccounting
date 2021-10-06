package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.SalesTaxLiabilityServerReport;

public class SalesTAXLiabilityRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_SALESTAXLIABILITY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {

		SalesTaxLiabilityServerReport salesTaxLiabilityServerReport = new SalesTaxLiabilityServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(salesTaxLiabilityServerReport, financeTool);
		salesTaxLiabilityServerReport.resetVariables();
		try {
			salesTaxLiabilityServerReport.onResultSuccess(financeTool
					.getReportManager().getSalesTaxLiabilityReport(startDate,
							endDate, getCompany().getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return salesTaxLiabilityServerReport.getGridTemplate();
	}

}
