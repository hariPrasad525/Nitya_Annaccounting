package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.NumberReportInput;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.core.reports.TrialBalance;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.TransactionDetailByCatgoryServerReport;

public class TransactionDetailByCatgoryReport extends
		AbstractReportView<TransactionDetailByAccount> {
	private static final int CLASS = 1;
	private static final int LOCATION = 2;
	private static final int JOB = 3;

	public TransactionDetailByCatgoryReport() {
		this.serverReport = new TransactionDetailByCatgoryServerReport(this);
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		TrialBalance accountdetails = (TrialBalance) data;
		Accounter.createReportService()
				.getTransactionDetailByAccountAndCategory(
						accountdetails.getCategoryType(),
						accountdetails.getCategoryId(),
						accountdetails.getAccountId(), start, end, this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void OnRecordClick(TransactionDetailByAccount record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(getType(record),
				record.getTransactionId());
	}

	int getType(TransactionDetailByAccount record) {
		if (record.getTransactionType() == 11) {
			return (record.getMemo() != null && record.getMemo()
					.equalsIgnoreCase("supplier prepayment")) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getTransactionType();
	}

	@Override
	public void export(int generationType) {
		TrialBalance trialBalance = (TrialBalance) data;
		long accountId = data != null ? ((TrialBalance) data).getAccountId()
				: 0;
		int reportType = 0;
		if (trialBalance.getCategoryType() == CLASS) {
			reportType = 194;
		} else if (trialBalance.getCategoryType() == JOB) {
			reportType = 193;
		} else if (trialBalance.getCategoryType() == LOCATION) {
			reportType = 195;
		}
		UIUtils.generateReport(generationType, startDate.getDate(), endDate
				.getDate(), reportType,
				new NumberReportInput(trialBalance.getCategoryId()),
				new NumberReportInput(accountId));
	}

}
