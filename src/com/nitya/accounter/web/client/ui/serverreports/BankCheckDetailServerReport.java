package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.BankCheckDetail;
import com.nitya.accounter.web.client.ui.core.ReportUtility;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class BankCheckDetailServerReport extends
		AbstractFinaneReport<BankCheckDetail> {

	public BankCheckDetailServerReport(
			IFinanceReport<BankCheckDetail> reportview) {
		this.reportView = reportview;
	}

	public BankCheckDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.type(), messages.date(),
				messages.number(), messages.name(), messages.checkNumber(),
				messages.checkAmount() };
	}

	@Override
	public String getTitle() {
		return messages.checkDetail();
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.type(), messages.date(),
				messages.number(), messages.name(), messages.checkNumber(),
				messages.checkAmount() };
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(BankCheckDetail record) {

	}

	@Override
	public Object getColumnData(BankCheckDetail record, int col) {
		switch (col) {
		case 0:
			return ReportUtility
					.getTransactionName(record.getTransactionType());
		case 1:
			return record.getTransactionDate();
		case 2:
			return record.getTransactionNumber();
		case 3:
			return record.getPayeeName();
		case 4:
			return record.getCheckNumber();
		case 5:
			return record.getCheckAmount();

		}
		return "";
	}

	@Override
	public ClientFinanceDate getStartDate(BankCheckDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(BankCheckDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {
	}

	@Override
	public int getColumnWidth(int col) {
		switch (col) {
		case 0:
			return 100;
		case 1:
			return 100;
		case 2:
			return 70;
		case 3:
			return 100;
		case 4:
			return 85;
		case 5:
			return 100;

		}
		return -1;
	}
}
