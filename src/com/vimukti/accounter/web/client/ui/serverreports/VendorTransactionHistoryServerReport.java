package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ReportUtility;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

/**
 * @author kumar kasimala
 * 
 * 
 */
public class VendorTransactionHistoryServerReport extends
		AbstractFinaneReport<TransactionHistory> {

	protected TransactionHistory transactionHistory;
	private String sectionName = "";

	public VendorTransactionHistoryServerReport(
			IFinanceReport<TransactionHistory> reportView) {
		this.reportView = reportView;
	}

	public VendorTransactionHistoryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.vendor1099(Global.get().vendor()),
				messages.date(), messages.type(), messages.no(),
				// FinanceApplication.constants().reference(),
				messages.Account(), messages.amount()
		// FinanceApplication.constants().transactionAmount(),
		// FinanceApplication.constants().paidAmount(),
		// FinanceApplication.constants().discount(),
		// FinanceApplication.constants().balance(),
		// FinanceApplication.constants().paymentSatus(),
		// FinanceApplication.constants().PONum(),
		// FinanceApplication.constants().memo(),
		// FinanceApplication.constants().dueDate(),
		// FinanceApplication.constants().debit(),
		// FinanceApplication.constants().credit()
		};

		// "Aging(days)", "Payment Terms",
		// "Vendor Group"
		// FIXME if required add it, "Void", "Reference"

	}

	@Override
	public String getTitle() {
		return messages.payeeTransactionHistory(Global.get().Vendor());
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 0:
			return 200;
		case 1:
			return 85;
		case 2:
			return 200;
		case 3:
			return 60;
		case 5:
			return 150;
		default:
			return -1;
		}
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// onSuccess(this.financeTool.getVendorTransactionHistory(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public Object getColumnData(TransactionHistory record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 2:
			return ReportUtility.getTransactionName(getType(record));
		case 1:
			return getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 4:
			return record.getAccount();
		case 5:
			return DecimalUtil.isEquals(record.getInvoicedAmount(), 0.0) ? Math
					.abs(record.getPaidAmount()) : Math.abs(record
					.getInvoicedAmount());

		}
		return null;
	}

	@Override
	public void processRecord(TransactionHistory record) {
		// if (sectionDepth == 0) {
		// addSection(new String[] { "", "" }, new String[] { "", "", "", "",
		// messages.total() }, new int[] { 5 });
		// } else
		if (sectionDepth == 0) {
			// First time
			this.sectionName = record.getName();
			addSection(new String[] { sectionName }, new String[] {},
					new int[] {});
		}
		// else if (sectionDepth == 2) {
		// // Inside fist section
		// addSection(FinanceApplication.constants()
		// .beginingBalance(), FinanceApplication.constants()
		// .endingBalance(), new int[] { 5 });
		// }
		else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getName())) {
				endSection();
				// endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public ClientFinanceDate getEndDate(TransactionHistory obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionHistory obj) {
		return obj.getStartDate();
	}

	int getType(TransactionHistory record) {
		if (record.getType() == 11) {
			return (record.getMemo() != null && record.getMemo().equals(
					messages.payeePrePayment(Global.get().Vendor()))) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getType();
	}

	public int sort(TransactionHistory obj1, TransactionHistory obj2, int col) {
		int ret = obj1.getName().toLowerCase()
				.compareTo(obj2.getName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return obj1.getName().compareTo(obj2.getName());
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 3:
			return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
					Integer.parseInt(obj2.getNumber()));
			// case 3:
			// return obj1.getReference().toLowerCase().compareTo(
			// obj2.getReference().toLowerCase());
		case 4:
			return obj1.getAccount().toLowerCase()
					.compareTo(obj2.getAccount().toLowerCase());
		case 5:
			if (DecimalUtil.isEquals(obj1.getInvoicedAmount(), 0.0))
				return UIUtils.compareDouble(obj1.getPaidAmount(),
						obj2.getPaidAmount());
			else
				return UIUtils.compareDouble(obj1.getInvoicedAmount(),
						obj2.getInvoicedAmount());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		sectionDepth = 0;
		sectionName = "";
		super.resetVariables();
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.payeeName(Global.get().vendor()),
				messages.date(), messages.type(), messages.no(),
				messages.Account(), messages.amount() };
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

}
