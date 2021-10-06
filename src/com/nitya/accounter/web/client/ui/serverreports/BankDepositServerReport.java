package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.BankDepositDetail;
import com.nitya.accounter.web.client.ui.core.ReportUtility;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class BankDepositServerReport extends
		AbstractFinaneReport<BankDepositDetail> {
	private long transactionId;

	public BankDepositServerReport(IFinanceReport<BankDepositDetail> reportView) {
		this.reportView = reportView;
	}

	public BankDepositServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.type(), messages.number(),
				messages.date(), messages.name(), messages.Account(),
				messages.debit(), messages.credit() };
	}

	@Override
	public String getTitle() {
		return messages.depositDetail();
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public void processRecord(BankDepositDetail record) {
		if (sectionDepth == 0) {
			this.transactionId = record.getTransactionId();
			addSection(new String[] { "" }, new String[] { "", "", "", "",
					getMessages().total() }, new int[] { 5, 6 });
			return;
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (transactionId != record.getTransactionId()) {
				endSection();
				processRecord(record);
			} else {
				record.setTransactionType(0);
				record.setNumber("0");
				return;
			}
		}
		// Go on recursive calling if we reached this place
		// processRecord(record);
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.type(), messages.number(),
				messages.date(), messages.name(), messages.Account(),
				messages.debit(), messages.credit() };
	}

	@Override
	public Object getColumnData(BankDepositDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getTransactionType() == 0 ? "" : ReportUtility
					.getTransactionName(record.getTransactionType());
		case 1:
			return record.getNumber() == null ? "" : record.getNumber();
		case 2:
			return record.getTransactionDate() == null ? "" : record
					.getTransactionDate();
		case 3:
			return record.getPayeeName();
		case 4:
			return record.getAccountName();
		case 5:
			return record.getAmount() > 0 ? record.getAmount() : 0;
		case 6:
			return record.getAmount() < 0 ? -record.getAmount() : 0;

		}
		return null;
	}

	@Override
	public ClientFinanceDate getStartDate(BankDepositDetail obj) {
		return obj.getStartDate();
	}

	@Override
	public ClientFinanceDate getEndDate(BankDepositDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

	@Override
	public int getColumnWidth(int index) {

		switch (index) {
		case 3:
			return 120;
		}
		return -1;
	}
}
