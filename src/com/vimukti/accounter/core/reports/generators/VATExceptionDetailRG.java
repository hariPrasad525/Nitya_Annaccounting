package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.VATExceptionServerReport;

public class VATExceptionDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_VAT_EXCEPTION_DETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		VATExceptionServerReport report = new VATExceptionServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(report);
		report.resetVariables();
		try {

			long taxReturnId = 0, taxAgency = 0;

			taxReturnId = getInputAsLong(0);
			taxAgency = getInputAsLong(1);

			report.onResultSuccess(financeTool.getReportManager()
					.getVATExpectionsForPrint(company.getID(), taxAgency,
							taxReturnId));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return report.getGridTemplate();
	}

}
