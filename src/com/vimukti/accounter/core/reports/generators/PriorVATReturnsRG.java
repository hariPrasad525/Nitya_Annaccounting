package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.PriorVATReturnsServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class PriorVATReturnsRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_PRIORVATRETURNS;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		PriorVATReturnsServerReport priorVATReturnsServerReport = new PriorVATReturnsServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {

				return getDateInDefaultType(date);
			}
		};
		updateReport(priorVATReturnsServerReport, financeTool);
		priorVATReturnsServerReport.resetVariables();
		try {
			priorVATReturnsServerReport
					.onResultSuccess(reportsSerivce.getPriorReturnVATSummary(
							getInputAsLong(0), endDate.toClientFinanceDate(),
							getCompany().getID()));

		} catch (Exception e) {
			e.printStackTrace();
		}
		return priorVATReturnsServerReport.getGridTemplate();
	}

}
