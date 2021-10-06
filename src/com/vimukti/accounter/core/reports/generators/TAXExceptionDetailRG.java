package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;
import com.vimukti.accounter.web.client.ui.serverreports.TAXItemExceptionDetailServerReport;

public class TAXExceptionDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_TAX_EXCEPTION_DETAIL;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		TAXItemExceptionDetailServerReport taxReport = new TAXItemExceptionDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(taxReport);
		taxReport.resetVariables();
		try {
			long taxReturnId = getInputAsLong(0);
			long taxAgencyId = getInputAsLong(1);

			taxReport.onResultSuccess(financeTool.getReportManager()
					.getTAXExceptionsForPrint(company.getId(), taxReturnId,
							taxAgencyId));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return taxReport.getGridTemplate();
	}

}
