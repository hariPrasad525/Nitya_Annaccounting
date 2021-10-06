package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.ReconciliationDiscrepancyServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class ReconciliationDescipancyRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_RECONCILIATION_DISCREPANCY;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		ReconciliationDiscrepancyServerReport discrepancyServerReport = new ReconciliationDiscrepancyServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			@Override
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(discrepancyServerReport);
		try {
			discrepancyServerReport.onResultSuccess(financeTool
					.getReportManager().getReconciliationDiscrepancyByAccount(
							getInputAsLong(0), startDate.toClientFinanceDate(),
							endDate.toClientFinanceDate(), company.getID()));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return discrepancyServerReport.getGridTemplate();
	}

}
