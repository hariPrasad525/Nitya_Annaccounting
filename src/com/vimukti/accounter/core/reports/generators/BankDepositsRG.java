package com.vimukti.accounter.core.reports.generators;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.serverreports.BankDepositServerReport;
import com.vimukti.accounter.web.client.ui.serverreports.ReportGridTemplate;

public class BankDepositsRG extends AbstractReportGenerator {

	@Override
	public int getReportType() {
		return REPORT_TYPE_BANK_DEPOSIT_REPORT;
	}

	@Override
	protected ReportGridTemplate<?> generate() {
		BankDepositServerReport bankDepositReport = new BankDepositServerReport(
				this.startDate.getDate(), this.endDate.getDate(),
				generationType) {
			public String getDateByCompanyType(ClientFinanceDate date) {
				return getDateInDefaultType(date);
			}
		};
		updateReport(bankDepositReport);

		bankDepositReport.onResultSuccess(financeTool.getReportManager()
				.getBankDepositDetails(company.getID(),
						new ClientFinanceDate(startDate.getDate()),
						new ClientFinanceDate(endDate.getDate())));

		return bankDepositReport.getGridTemplate();
	}

}
