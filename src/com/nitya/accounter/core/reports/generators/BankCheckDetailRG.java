package com.nitya.accounter.core.reports.generators;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.serverreports.BankCheckDetailServerReport;
import com.nitya.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class BankCheckDetailRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_BANK_CHECK_DETAIL_REPORT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		BankCheckDetailServerReport bankCheckDetailReport = new BankCheckDetailServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(bankCheckDetailReport, financeTool);

		bankCheckDetailReport.onResultSuccess(financeTool.getReportManager()
				.getBankCheckDetails(company.getID(),
						new ClientFinanceDate(startDate.getDate()),
						new ClientFinanceDate(endDate.getDate())));

		return bankCheckDetailReport.getGridTemplate();
	}

}
