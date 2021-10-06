package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.NumberReportInput;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.serverreports.ExpenseServerReport;

public class ExpenseReport extends AbstractReportView<ExpenseList> {

	private String currentsectionName = "";

	private static int type;

	public ExpenseReport() {
		this.serverReport = new ExpenseServerReport(this);
		this.getElement().setId("ExpenseReport");
	}

	@Override
	public void OnRecordClick(ExpenseList record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getTransactionType(),
					record.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_EXPENSE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

	}

	@Override
	public void init() {
		super.init();
		((ExpenseReportToolbar) toolbar).setSelectedType(type);
	}

	@Override
	public void makeReportRequest(int status, ClientFinanceDate startDate,
			ClientFinanceDate endDate) {
		Accounter.createReportService().getExpenseReportByType(status,
				startDate, endDate, this);

		ExpenseReport.type = status;
	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 116, new NumberReportInput(type));

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO
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
	public void restoreView(HashMap<String, Object> map) {
		if (map == null || map.isEmpty()) {
			isDatesArranged = false;
			return;
		}
		ClientFinanceDate startDate = (ClientFinanceDate) map.get("startDate");
		ClientFinanceDate endDate = (ClientFinanceDate) map.get("endDate");
		this.serverReport.setStartAndEndDates(startDate, endDate);
		toolbar.setEndDate(endDate);
		toolbar.setStartDate(startDate);
		toolbar.setDefaultDateRange((String) map.get("selectedDateRange"));
		int status1 = (Integer) map.get("Expense related to");
		isDatesArranged = true;
		ExpenseReport.type = status1;
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		int expenserelatedto = ExpenseReport.type;
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("Expense related to", expenserelatedto);
		map.put("selectedDateRange", selectedDateRange);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
	}

	public void setType(Integer type) {
		this.type = type == null ? 0 : type;
	}

	/*
	 * private void printDataForIEBrowser() { String gridhtml = grid.toString();
	 * String headerhtml = grid.getHeader(); String footerhtml =
	 * grid.getFooter();
	 * 
	 * gridhtml = gridhtml.replaceAll("\r\n", ""); headerhtml =
	 * headerhtml.replaceAll("\r\n", ""); footerhtml =
	 * footerhtml.replaceAll("\r\n", "");
	 * 
	 * gridhtml = gridhtml.replaceAll(headerhtml, ""); gridhtml =
	 * gridhtml.replaceAll(footerhtml, ""); headerhtml =
	 * headerhtml.replaceAll("TD", "TH"); headerhtml =
	 * headerhtml.substring(headerhtml.indexOf("<TR "),
	 * headerhtml.indexOf("</TBODY>")); footerhtml =
	 * footerhtml.substring(footerhtml.indexOf("<TR>"),
	 * footerhtml.indexOf("</TBODY")); footerhtml =
	 * footerhtml.replaceAll("<TR>", "<TR class=listgridfooter>");
	 * 
	 * String firsRow = "<TR class=ReportGridRow>" +
	 * grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>"; String lastRow
	 * = "<TR class=ReportGridRow>" +
	 * grid.rowFormatter.getElement(grid.getRowCount() - 1) .getInnerHTML() +
	 * "</TR>";
	 * 
	 * firsRow = firsRow.replaceAll("\r\n", ""); lastRow =
	 * lastRow.replaceAll("\r\n", "");
	 * 
	 * headerhtml = headerhtml + firsRow; footerhtml = lastRow + footerhtml;
	 * 
	 * gridhtml = gridhtml.replace(firsRow, headerhtml); gridhtml =
	 * gridhtml.replace(lastRow, footerhtml); gridhtml =
	 * gridhtml.replaceAll("<TBODY>", ""); gridhtml =
	 * gridhtml.replaceAll("</TBODY>", "");
	 * 
	 * String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
	 * + this.toolbar.getStartDate() + " - " + this.toolbar.getEndDate() +
	 * "</strong></div>";
	 * 
	 * UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml); }
	 * 
	 * private void printDataForOtherBrowser() { String gridhtml =
	 * grid.toString(); String headerhtml = grid.getHeader(); String footerhtml
	 * = grid.getFooter();
	 * 
	 * gridhtml = gridhtml.replaceAll(headerhtml, ""); gridhtml =
	 * gridhtml.replaceAll(footerhtml, ""); headerhtml =
	 * headerhtml.replaceAll("td", "th"); headerhtml =
	 * headerhtml.substring(headerhtml.indexOf("<tr "),
	 * headerhtml.indexOf("</tbody>")); footerhtml =
	 * footerhtml.substring(footerhtml.indexOf("<tr>"),
	 * footerhtml.indexOf("</tbody")); footerhtml =
	 * footerhtml.replaceAll("<tr>", "<tr class=\"listgridfooter\">");
	 * 
	 * String firsRow = "<tr class=\"ReportGridRow\">" +
	 * grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>"; String lastRow
	 * = "<tr class=\"ReportGridRow\">" +
	 * grid.rowFormatter.getElement(grid.getRowCount() - 1) .getInnerHTML() +
	 * "</tr>";
	 * 
	 * headerhtml = headerhtml + firsRow; footerhtml = lastRow + footerhtml;
	 * 
	 * gridhtml = gridhtml.replace(firsRow, headerhtml); gridhtml =
	 * gridhtml.replace(lastRow, footerhtml); gridhtml =
	 * gridhtml.replaceAll("<tbody>", ""); gridhtml =
	 * gridhtml.replaceAll("</tbody>", "");
	 * 
	 * String dateRangeHtml = "<div style=\"font-family:sans-serif;\"><strong>"
	 * + this.toolbar.getStartDate() + " - " + this.toolbar.getEndDate() +
	 * "</strong></div>";
	 * 
	 * UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml); }
	 */
}
