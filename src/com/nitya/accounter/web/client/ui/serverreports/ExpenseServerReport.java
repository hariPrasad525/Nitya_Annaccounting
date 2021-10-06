package com.nitya.accounter.web.client.ui.serverreports;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.ExpenseList;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class ExpenseServerReport extends AbstractFinaneReport<ExpenseList> {

	private String sectionName = "";

	private double accountBalance = 0.0D;

	private String currentsectionName = "";

	public ExpenseServerReport(IFinanceReport<ExpenseList> reportView) {
		this.reportView = reportView;
	}

	public ExpenseServerReport(long startDate, long endDate, int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(ExpenseList record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return Utility.getTransactionName(record.getTransactionType());
		case 1:
			return getDateByCompanyType(record.getTransactionDate());
		case 2:
			return record.getTotal();
		case 3:
			if (!currentsectionName.equals(record.getName())) {
				currentsectionName = record.getName();
				accountBalance = 0.0D;
			}
			return accountBalance += record.getTotal();
		}
		return null;
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_AMOUNT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { " ", getMessages().transactionDate(),
				getMessages().amount(), getMessages().balance() };
	}

	@Override
	public String getTitle() {
		return getMessages().expenseReport();
	}

	@Override
	public void processRecord(ExpenseList record) {
		// if (sectionDepth == 0) {
		// addSection("", getMessages().total(), new int[] { 2 });
		// } else
		if (sectionDepth == 0) {
			this.sectionName = record.getName();
			addSection(sectionName, "", new int[] { 2 });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (!sectionName.equals(record.getName())) {
				endSection();
			} else {
				return;
			}
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	@Override
	public ClientFinanceDate getEndDate(ExpenseList obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(ExpenseList obj) {
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
			return 200;
		case 2:
			return 200;
		case 3:
			return 200;
		default:
			return -1;
		}

	}

	public int sort(ExpenseList obj1, ExpenseList obj2, int col) {
		switch (col) {
		case 0:
			return UIUtils.compareInt(obj1.getTransactionType(),
					obj2.getTransactionType());
		case 1:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());
		case 2:
			return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
		case 3:
			if (!currentsectionName.toLowerCase().equals(
					obj1.getName().toLowerCase())) {
				return obj1.getName().toLowerCase()
						.compareTo(obj2.getName().toLowerCase());
			} else {
				return UIUtils.compareDouble(obj1.getTotal(), obj2.getTotal());
			}
		}
		return 0;
	}

	@Override
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
		this.currentsectionName = "";
		this.accountBalance = 0.0D;
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
	// String lastRow = "<TR class=ReportGridRow>"
	// + grid.rowFormatter.getElement(grid.getRowCount() - 1)
	// .getInnerHTML() + "</TR>";
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
	// String gridhtml = grid.toString();
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
	// String firsRow = "<tr class=\"ReportGridRow\">"
	// + "" + "</tr>";
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
	// + this.getEndDate() + "</strong></div>";
	//
	// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	// }

	@Override
	public boolean isWiderReport() {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { " ", getMessages().transactionDate(),
				getMessages().amount(), getMessages().balance() };
	}

	@Override
	public void makeReportRequest(long start, long end) {

	}

	@Override
	public void initRecords(List<ExpenseList> records) {
		resetVariables();
		super.initRecords(records);
	}
}
