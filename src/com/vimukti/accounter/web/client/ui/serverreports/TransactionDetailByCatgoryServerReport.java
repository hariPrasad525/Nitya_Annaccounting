package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.TransactionDetailByAccount;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class TransactionDetailByCatgoryServerReport extends
		AbstractFinaneReport<TransactionDetailByAccount> {
	private String sectionName = "";

	private double accountBalance = 0.0D;

	private String currentsectionName = "";

	public TransactionDetailByCatgoryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	public TransactionDetailByCatgoryServerReport(
			IFinanceReport<TransactionDetailByAccount> reportView) {
		this.reportView = reportView;
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
			return ReportUtility.getTransactionName(getType(record));
		case 4:
			return record.getTransactionNumber();
		case 5:
			return record.getTotal();
		case 6:
			if (!currentsectionName.equals(record.getAccountName())) {
				currentsectionName = record.getAccountName();
				accountBalance = 0.0D;
			}
			return accountBalance += record.getTotal();
		}
		return null;
	}

	int getType(TransactionDetailByAccount record) {
		if (record.getTransactionType() == 11) {
			return (record.getMemo() != null && record.getMemo().equals(
					getMessages().payeePrePayment(Global.get().Vendor()))) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getTransactionType();
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_DATE, COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { "", messages.name(), messages.date(), " ",
				messages.number(), messages.amount(), messages.balance() };
	}

	@Override
	public String getTitle() {
		return messages.transactionDetailsByAccount();
	}

	@Override
	public void makeReportRequest(long start, long end) {
	}

	@Override
	public void processRecord(TransactionDetailByAccount record) {
		// if (sectionDepth == 0) {
		// addSection(new String[] { "", "" }, new String[] { "", "", "", "",
		// messages.total() }, new int[] { 5 });
		// } else
		if (sectionDepth == 0) {
			this.sectionName = record.getAccountName();
			addSection(new String[] { sectionName }, new String[] { "" },
					new int[] { 5 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getAccountName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	public void print() {

	}

	@Override
	public ClientFinanceDate getEndDate(TransactionDetailByAccount obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionDetailByAccount obj) {
		return obj.getStartDate();

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
	public int getColumnWidth(int index) {
		switch (index) {
		case 1:
			return 130;
		case 2:
			return 75;
		case 3:
			return 130;
		case 4:
			return 60;
		case 5:
			return 110;
		case 6:
			return 110;
		default:
			return -1;
		}
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
		this.currentsectionName = "";
	}

	public TransactionDetailByAccount getObject(
			TransactionDetailByAccount parent, TransactionDetailByAccount child) {
		if (parent == null) {

		}
		return null;
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { "", messages.name(), messages.date(), " ",
				messages.number(), messages.amount(), messages.balance() };
	}

}
