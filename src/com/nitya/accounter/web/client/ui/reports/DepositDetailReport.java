package com.nitya.accounter.web.client.ui.reports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.DepositDetail;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;

public class DepositDetailReport extends AbstractReportView<DepositDetail> {
	private String sectionName = "";

	private String sectionID = "";

	private double accountBalance = 0.0D;

	private String currentsectionName = "";

	@Override
	public void OnRecordClick(DepositDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		ReportsRPC.openTransactionView(getType(record),
				record.getTransactionId());
	}

	@Override
	public void export(int generationType) {
		// TODO Auto-generated method stub

	}

	@Override
	public Object getColumnData(DepositDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return "";
		case 1:
			return record.getTransactionNumber();
		case 2:
			return UIUtils.getDateByCompanyType(record.getTransactionDate());
		case 3:
			return record.getName();
		case 4:
			return record.getAccountName();
		case 5:
			return record.getTotal();
		}
		return null;
	}

	int getType(DepositDetail record) {
		if (record.getTransactionType() == 11) {
			return (record.getMemo() != null && record.getMemo()
					.equalsIgnoreCase("supplier prepayment")) ? ClientTransaction.TYPE_VENDOR_PAYMENT
					: ClientTransaction.TYPE_PAY_BILL;
		}

		return record.getTransactionType();
	}

	@Override
	public String getDefaultDateRange() {
		return messages.financialYearToDate();
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
				messages.name(), messages.accountName(), messages.amount() };
	}

	@Override
	public String getTitle() {
		return messages.depositDetail();
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getDepositDetail(start, end, this);
	}

	@Override
	public void processRecord(DepositDetail record) {
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
		// // if (!sectionName
		// // .equals(Utility.getTransactionName(getType(record)))) {
		// if (!sectionID.equals(record.getTransactionId())) {
		// endSection();
		// } else {
		// return;
		// }
		// }
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	public void print() {

		if (UIUtils.isMSIEBrowser()) {
			printDataForIEBrowser();
		} else
			printDataForOtherBrowser();

	}

	@Override
	public void printPreview() {

	}

	@Override
	public ClientFinanceDate getEndDate(DepositDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(DepositDetail obj) {
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
	public int sort(DepositDetail obj1, DepositDetail obj2, int col) {
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
			int num1 = UIUtils.isInteger(obj1.getTransactionNumber()) ? Integer
					.parseInt(obj1.getTransactionNumber()) : 0;
			int num2 = UIUtils.isInteger(obj2.getTransactionNumber()) ? Integer
					.parseInt(obj2.getTransactionNumber()) : 0;
			if (num1 != 0 && num2 != 0)
				return UIUtils.compareInt(num1, num2);
			else
				return obj1.getTransactionNumber().compareTo(
						obj2.getTransactionNumber());
		case 2:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());
		case 3:
			String name1 = obj1.getName();
			String name2 = obj2.getName();
			return name1.toLowerCase().compareTo(name2.toLowerCase());
		case 4:

		case 5:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		// this.sectionDepth = 0;
		this.sectionName = "";
		this.currentsectionName = "";
	}

	@Override
	public DepositDetail getObject(DepositDetail parent, DepositDetail child) {
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
}
