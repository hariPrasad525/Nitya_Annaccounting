package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.NumberReportInput;
import com.vimukti.accounter.web.client.core.reports.ReconcilationItemList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.ReconcilationDetailsByAccountServerReport;

public class RecincilationDetailsByAccountReport extends
		AbstractReportView<ReconcilationItemList> {

	private long bankAccountId;

	public RecincilationDetailsByAccountReport(long accountID) {
		this.serverReport = new ReconcilationDetailsByAccountServerReport(this,
				accountID);
		this.bankAccountId = accountID;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		if (bankAccountId != 0)
			Accounter
					.createReportService()
					.getReconciliationItemByBankAccountID(start, end,
							bankAccountId, Accounter.getCompany().getID(), this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(ReconcilationItemList record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			int type = record.getTransationType();
			ReportsRPC.openTransactionView(type, record.getTransaction());
		}
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 169, new NumberReportInput(bankAccountId));
	}
}
