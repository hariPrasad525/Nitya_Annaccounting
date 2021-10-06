package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.ReconciliationDiscrepancy;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class ReconciliationDiscrepancyServerReport extends
		AbstractFinaneReport<ReconciliationDiscrepancy> {

	public ReconciliationDiscrepancyServerReport(
			IFinanceReport<ReconciliationDiscrepancy> reportView) {
		this.reportView = reportView;
	}

	public ReconciliationDiscrepancyServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().number(), getMessages().name(),
				messages.reconciledAmount(), messages.effectofChange() };
	}

	@Override
	public String getTitle() {
		return messages.reconcilationDiscrepany();
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().number(), getMessages().name(),
				messages.reconciledAmount(), messages.effectofChange() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(ReconciliationDiscrepancy record) {

	}

	@Override
	public Object getColumnData(ReconciliationDiscrepancy record, int index) {
		switch (index) {
		case 0:
			return Utility.getTransactionName(record.getTransactionType());
		case 1:
			return getDateByCompanyType(record.getTransactionDate());
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.getName();
		case 4:
			return record.getReconciliedAmount();
		case 5:
			double reconciliedAmount = record.getReconciliedAmount();
			double transactionAmount = record.getTransactionAmount();
			return reconciliedAmount - transactionAmount;
		default:
			return null;
		}
	}

	@Override
	public ClientFinanceDate getStartDate(ReconciliationDiscrepancy obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(ReconciliationDiscrepancy obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}
}
