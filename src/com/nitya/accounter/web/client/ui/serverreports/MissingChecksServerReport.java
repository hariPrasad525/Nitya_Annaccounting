package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.NumberUtils;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class MissingChecksServerReport extends
		AbstractFinaneReport<TransactionDetailByAccount> {

	private String previousNumber = null;

	public MissingChecksServerReport(
			IFinanceReport<TransactionDetailByAccount> reportView) {
		this.reportView = reportView;
	}

	public MissingChecksServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().number(), getMessages().name(),
				getMessages().memo(), getMessages().accountName(),
				getMessages().totalAmount() };
	}

	@Override
	public String getTitle() {
		return "Missing Checks";
	}

	@Override
	public String[] getColunms() {
		return new String[] { getMessages().type(), getMessages().date(),
				getMessages().number(), getMessages().name(),
				getMessages().memo(), getMessages().accountName(),
				getMessages().totalAmount() };
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 200;
		case 1:
			return 100;
		case 2:
			return 70;
		case 3:
			return 100;
		case 4:
			return 100;
		case 5:
			return 150;
		case 6:
			return 100;
		}
		return -1;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(TransactionDetailByAccount record) {

		if (previousNumber != null) {
			String nextNumber = NumberUtils
					.getStringwithIncreamentedDigit(previousNumber);
			if (!record.getTransactionNumber().equals(nextNumber)) {
				addSection("", "** missing numbers here **", new int[] { 0 });
				endSection();
			}
		}
		previousNumber = record.getTransactionNumber();
	}

	@Override
	public Object getColumnData(TransactionDetailByAccount record,
			int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getTransactionType());
		case 1:
			return getDateByCompanyType(record.getTransactionDate());
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.getName();
		case 4:
			return record.getMemo();
		case 5:
			return record.getAccountName();
		case 6:
			return record.getTotal();
		default:
			return null;
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

	}
	@Override
	public void initGrid() {
		this.previousNumber= null;
		super.initGrid();
	}

}
