package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class AutomaticTransactionsServerReport extends
		AbstractFinaneReport<TransactionDetailByAccount> {

	private String sectionName = "";

	public AutomaticTransactionsServerReport(
			IFinanceReport<TransactionDetailByAccount> reportView) {
		this.reportView = reportView;
	}

	public AutomaticTransactionsServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", messages.name(), messages.date(),
				messages.number(), messages.amount() };
	}

	@Override
	public String getTitle() {
		return messages.automaticTransactions();
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", messages.name(), messages.date(),
				messages.number(), messages.amount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_DATE, COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(TransactionDetailByAccount record) {
		if (sectionDepth == 0) {
			this.sectionName = Utility.getTransactionName(record
					.getTransactionType());
			addSection(new String[] { sectionName }, new String[] { "" },
					new int[] { 4 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(Utility.getTransactionName(record
					.getTransactionType()))) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public Object getColumnData(TransactionDetailByAccount record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return record.getName();
		case 2:
			return getDateByCompanyType(record.getTransactionDate());
		case 3:
			return record.getTransactionNumber();
		case 4:
			return record.getTotal();
		}
		return null;
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 3:
			return 60;
		case 4:
			return 100;
		default:
			return 130;
		}
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionDetailByAccount obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(TransactionDetailByAccount obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getPreviousReportDateRange(Object object) {
		return ((BaseReport) object).getDateRange();
	}

	@Override
	protected ClientFinanceDate getPreviousReportStartDate(Object object) {
		return ((BaseReport) object).getStartDate();
	}

	@Override
	protected ClientFinanceDate getPreviousReportEndDate(Object object) {
		return ((BaseReport) object).getEndDate();
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
	}
}
