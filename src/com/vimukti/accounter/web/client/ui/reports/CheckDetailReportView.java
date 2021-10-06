package com.vimukti.accounter.web.client.ui.reports;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;

public class CheckDetailReportView extends
		AbstractReportView<CheckDetailReport> {
	String sectionName = "";

	private String sectionID = "";

	@Override
	public void OnRecordClick(CheckDetailReport record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(getType(record),
				record.getTransactionId());

	}

	@Override
	public Object getColumnData(CheckDetailReport record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return record.getNumber();
		case 2:
			return UIUtils.getDateByCompanyType(record.getTransactionDate());
		case 3:
			return record.getPayeeName();
		case 4:
			return record.getAccountName();
		case 5:
			return record.getAmount();

		}
		return null;
	}

	@Override
	public void export(int generationType) {
		// TODO Auto-generated method stub

	}

	// @Override
	// public int[] getColumnTypes() {
	// return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER,
	// COLUMN_TYPE_DATE, COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
	// COLUMN_TYPE_AMOUNT };
	// }

	@Override
	public String[] getColunms() {
		return new String[] { "", messages.number(), messages.date(),
				messages.name(), messages.Account(), messages.amount() };
	}

	@Override
	public ClientFinanceDate getEndDate(CheckDetailReport obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(CheckDetailReport obj) {
		return obj.getStartDate();
	}

	@Override
	public String getTitle() {
		return messages.checkDetail();
	}

	@Override
	public String getDefaultDateRange() {
		return messages.financialYearToDate();
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

	}

	@Override
	public void makeReportRequest(long paymentmethod,
			ClientFinanceDate startDate, ClientFinanceDate endDate) {
		Accounter.createReportService().getCheckDetailReport(paymentmethod,
				startDate, endDate, this);

	}

	int getType(CheckDetailReport record) {
		if (record.getTransactionType() == 11) {
			return (record.getMemo() != null && record.getMemo()
					.equalsIgnoreCase("supplier prepayment")) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getTransactionType();
	}

	@Override
	public int getColumnWidth(int index) {
		switch (index) {
		case 5:
			return 140;
		case 1:
			return 55;
		case 2:
			return 80;
		case 3:
			return 140;
		case 4:
			return 230;

		default:
			return -1;
		}
	}

	@Override
	public void processRecord(CheckDetailReport record) {
		// if (sectionDepth == 0) {
		// addSection(new String[] { "", "" }, new String[] { "", "", "", "",
		// FinanceApplication.constants().total() },
		// new int[] { 5 });
		// } else if (sectionDepth == 1) {
		// this.sectionName = Utility.getTransactionName(getType(record));
		// this.sectionID = record.getTransactionId();
		// addSection(new String[] { sectionName }, new String[] { "" },
		// new int[] { 5 });
		// } else if (sectionDepth == 2) {
		// // No need to do anything, just allow adding this record
		// if (!sectionID.equals(record.getTransactionId())) {
		// endSection();
		// } else {
		// return;
		// }
		// }
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public int sort(CheckDetailReport obj1, CheckDetailReport obj2, int col) {
		int ret = UIUtils.compareInt(obj1.getTransactionType(),
				obj2.getTransactionType());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(),
					obj2.getTransactionType());
		case 1:
			int num1 = UIUtils.isInteger(obj1.getNumber()) ? Integer
					.parseInt(obj1.getNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getNumber()) ? Integer
					.parseInt(obj2.getNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getNumber().compareTo(obj2.getNumber());
		case 2:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());
		case 3:
			String name1 = obj1.getPayeeName();
			String name2 = obj2.getPayeeName();
			return name1.toLowerCase().compareTo(name2.toLowerCase());
		case 4:

		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		// this.sectionDepth = 0;
		this.sectionName = "";
	}

	@Override
	public CheckDetailReport getObject(CheckDetailReport parent,
			CheckDetailReport child) {
		if (parent == null) {

		}
		return null;
	}

	@Override
	public boolean isWiderReport() {
		return true;
	}

	private void printDataForIEBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();
		String footerhtml = grid.getFooter();

		gridhtml = gridhtml.replaceAll("\r\n", "");
		headerhtml = headerhtml.replaceAll("\r\n", "");
		footerhtml = footerhtml.replaceAll("\r\n", "");

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(footerhtml, "");
		headerhtml = headerhtml.replaceAll("TD", "TH");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<TR "),
				headerhtml.indexOf("</TBODY>"));
		footerhtml = footerhtml.substring(footerhtml.indexOf("<TR>"),
				footerhtml.indexOf("</TBODY"));
		footerhtml = footerhtml.replaceAll("<TR>", "<TR class=listgridfooter>");

		String firsRow = "<TR class=ReportGridRow>"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>";
		String lastRow = "<TR class=ReportGridRow>"
				+ grid.rowFormatter.getElement(grid.getTableRowCount() - 1)
						.getInnerHTML() + "</TR>";

		firsRow = firsRow.replaceAll("\r\n", "");
		lastRow = lastRow.replaceAll("\r\n", "");

		headerhtml = headerhtml + firsRow;
		footerhtml = lastRow + footerhtml;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		String gridSub = gridhtml.substring(gridhtml.lastIndexOf(lastRow));
		String gridSub2 = gridSub.replace(lastRow, footerhtml);
		gridhtml = gridhtml.replace(gridSub, gridSub2);
		gridhtml = gridhtml.replaceAll("<TBODY>", "");
		gridhtml = gridhtml.replaceAll("</TBODY>", "");

		String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
				+ this.toolbar.getStartDate()
				+ " - "
				+ this.toolbar.getEndDate() + "</strong></div>";

		UIUtils.generateReportPDF(this.getAction().getText(), gridhtml,
				dateRangeHtml);
	}

	private void printDataForOtherBrowser() {
		String gridhtml = grid.toString();
		String headerhtml = grid.getHeader();
		String footerhtml = grid.getFooter();

		gridhtml = gridhtml.replaceAll(headerhtml, "");
		gridhtml = gridhtml.replaceAll(footerhtml, "");
		headerhtml = headerhtml.replaceAll("td", "th");
		headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
				headerhtml.indexOf("</tbody>"));
		footerhtml = footerhtml.substring(footerhtml.indexOf("<tr>"),
				footerhtml.indexOf("</tbody"));
		footerhtml = footerhtml.replaceAll("<tr>",
				"<tr class=\"listgridfooter\">");

		String firsRow = "<tr class=\"ReportGridRow\">"
				+ grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>";
		String lastRow = "<tr class=\"ReportGridRow\">"
				+ grid.rowFormatter.getElement(grid.getTableRowCount() - 1)
						.getInnerHTML() + "</tr>";

		headerhtml = headerhtml + firsRow;
		footerhtml = lastRow + footerhtml;

		gridhtml = gridhtml.replace(firsRow, headerhtml);
		String gridSub = gridhtml.substring(gridhtml.lastIndexOf(lastRow));
		String gridSub2 = gridSub.replace(lastRow, footerhtml);
		gridhtml = gridhtml.replace(gridSub, gridSub2);
		gridhtml = gridhtml.replaceAll("<tbody>", "");
		gridhtml = gridhtml.replaceAll("</tbody>", "");

		String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
				+ this.toolbar.getStartDate()
				+ " - "
				+ this.toolbar.getEndDate() + "</strong></div>";

		UIUtils.generateReportPDF(this.getAction().getText(), gridhtml,
				dateRangeHtml);
	}

	@Override
	public void print() {

		if (UIUtils.isMSIEBrowser()) {
			printDataForIEBrowser();
		} else
			printDataForOtherBrowser();

	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_CHECKDETAIl;
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
	public void onEdit() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	public void onSuccess(ArrayList<CheckDetailReport> result) {
		// NOTHING TO DO
	}

}
