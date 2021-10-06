package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.reports.TransactionHistory;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class CustomerTransactionHistoryServerReport extends
		AbstractFinaneReport<TransactionHistory> {

	public CustomerTransactionHistoryServerReport(

	IFinanceReport<TransactionHistory> reportView) {

		this.reportView = reportView;
	}

	public CustomerTransactionHistoryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	private String sectionName = "";

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {

		return new String[] { Global.get().customer(), getMessages().date(),
				getMessages().type(), getMessages().noDot(),
				// ".invoicedAmount(),
				// ".paidAmount(),
				// ".paymentTerms(),
				// ".dueDate(),
				// ".debit(),
				// ".credit(),
				// ".reference(),
				getMessages().Account(), getMessages().amount() };

	}

	@Override
	public String getTitle() {
		return messages.payeeTransactionHistory(Global.get().Customer());
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 1:
			return 130;
		case 2:
			return 200;
		case 3:
			return 80;
		case 4:
			return 150;
		case 5:
			return 135;

		default:
			return -1;
		}
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// initValues();
		// try {
		// if (navigateObjectName == null) {
		//
		// onSuccess(this.financeTool.getCustomerTransactionHistory(start,
		// end));
		// }
		// } catch (DAOException e) {
		// //
		// e.printStackTrace();
		// }
		// }
		//
		// private void initValues() {

	}

	@Override
	public Object getColumnData(TransactionHistory record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 2:
			return Utility.getTransactionName(record.getType());
		case 1:
			return getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
			// case 3:
			// // return record.getInvoicedAmount();
			// return record.getReference();
		case 4:
			// return record.getPaidAmount();
			return record.getAccount();
		case 5:
			// return record.getPaymentTerm();
			return DecimalUtil.isEquals(record.getInvoicedAmount(), 0.0) ? record
					.getPaidAmount() : Math.abs(record.getInvoicedAmount());
			// return record.getInvoicedAmount() - record.getPaidAmount();
			// case 6:
			// return record.getPaymentTerm();
			// case 6:
			// return record.getDueDate();
			// case 7:
			// return record.getDebitanotherString();
			// case 8:
			// return record.getCredit();
		}
		return null;
	}

	@Override
	public void processRecord(TransactionHistory record) {
		// if (sectionDepth == 0) {
		// if (constants != null) {
		// addSection(new String[] { "", "" }, new String[] { "", "", "",
		// "", messages.total() }, new int[] { 5 });
		// } else {
		// addSection(new String[] { "", "" }, new String[] { "", "", "",
		// "", messages.total() }, new int[] { 5 });
		// }
		//
		// } else
		if (sectionDepth == 0) {
			// First time
			this.sectionName = record.getName();
			addSection(new String[] { sectionName }, new String[] {},
					new int[] {});

		}
		// else if (sectionDepth == 2) {
		// // Inside fist section
		// addSection("
		// .beginingBalance(), "
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

	public void print() {

	}

	@Override
	public ClientFinanceDate getEndDate(TransactionHistory obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(TransactionHistory obj) {
		return obj.getStartDate();
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
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());
		case 3:
			return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
					Integer.parseInt(obj2.getNumber()));
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

		return new String[] { Global.get().customer(), getMessages().date(),
				getMessages().type(), getMessages().noDot(),
				getMessages().Account(), getMessages().amount() };

	}

	// private void printDataForIEBrowser() {
	// String gridhtml = grid.toString();
	// String headerhtml = grid.getHeader();
	// String footerhtml = grid.getFooter();
	//
	// gridhtml = gridhtml.replaceAll("\r\n", "");
	// headerhtml = headerhtml.replaceAll("\r\n", "");
	// footerhtml = footerhtml.replaceAll("\r\n", "");
	//
	// gridhtml = gridhtml.replaceAll(headerhtml, "");
	// gridhtml = gridhtml.replaceAll(footerhtml, "");
	// headerhtml = headerhtml.replaceAll("TD", "TH");
	// headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
	// headerhtml.indexOf("</TBODY>"));
	// footerhtml = footerhtml.substring(footerhtml.indexOf("<TR>"),
	// footerhtml.indexOf("</TBODY"));
	// footerhtml = footerhtml.replaceAll("<TR>",
	// "<TR class=listgridfooter>");
	//
	// String firsRow = "<TR class=ReportGridRow>"
	// + "" + "</TR>";
	// // String lastRow = "<TR class=ReportGridRow>"
	// // + grid.rowFormatter.getElement(grid.getRowCount() - 1)
	// // .getInnerHTML() + "</TR>";
	//
	// firsRow = firsRow.replaceAll("\r\n", "");
	// lastRow = lastRow.replaceAll("\r\n", "");
	//
	// headerhtml = headerhtml + firsRow;
	// footerhtml = lastRow + footerhtml;
	//
	// gridhtml = gridhtml.replace(firsRow, headerhtml);
	// gridhtml = gridhtml.replace(lastRow, footerhtml);
	// gridhtml = gridhtml.replaceAll("<TBODY>", "");
	// gridhtml = gridhtml.replaceAll("</TBODY>", "");
	//
	// String dateRangeHtml =
	// "<div style=\"font-family:sans-serif;\"><strong>"
	// + this.getStartDate()
	// + " - "
	// + this.getEndDate() + "</strong></div>";
	//
	// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	// }
	//
	// private void printDataForOtherBrowser() {
	// // String gridhtml = grid.toString();
	// String headerhtml = grid.getHeader();
	// String footerhtml = grid.getFooter();
	//
	// gridhtml = gridhtml.replaceAll(headerhtml, "");
	// gridhtml = gridhtml.replaceAll(footerhtml, "");
	// headerhtml = headerhtml.replaceAll("td", "th");
	// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
	// headerhtml.indexOf("</tbody>"));
	// footerhtml = footerhtml.substring(footerhtml.indexOf("<tr>"),
	// footerhtml.indexOf("</tbody"));
	// footerhtml = footerhtml.replaceAll("<tr>",
	// "<tr class=\"listgridfooter\">");
	//
	// String firsRow = "<tr class=\"ReportGridRow\">" + "" + "</tr>";
	// String lastRow = "<tr class=\"ReportGridRow\">"
	// + grid.rowFormatter.getElement(grid.getRowCount() - 1)
	// .getInnerHTML() + "</tr>";
	//
	// headerhtml = headerhtml + firsRow;
	// footerhtml = lastRow + footerhtml;
	//
	// gridhtml = gridhtml.replace(firsRow, headerhtml);
	// gridhtml = gridhtml.replace(lastRow, footerhtml);
	// gridhtml = gridhtml.replaceAll("<tbody>", "");
	// gridhtml = gridhtml.replaceAll("</tbody>", "");
	//
	// String dateRangeHtml =
	// "<div style=\"font-family:sans-serif;\"><strong>"
	// + this.getStartDate()
	// + " - "
	// + this.getEndDate()
	// + "</strong></div>";
	//
	// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	// }
	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}
}
