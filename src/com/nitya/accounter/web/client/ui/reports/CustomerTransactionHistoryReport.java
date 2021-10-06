package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.TransactionHistory;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.serverreports.CustomerTransactionHistoryServerReport;

public final class CustomerTransactionHistoryReport extends
		AbstractReportView<TransactionHistory> {

	public CustomerTransactionHistoryReport() {
		this.serverReport = new CustomerTransactionHistoryServerReport(this);
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		Accounter.createReportService().getCustomerTransactionHistory(start,
				end, this);
	}

	@Override
	public void OnRecordClick(TransactionHistory record) {
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getType(),
					record.getTransactionId());
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 119);
	}

	@Override
	public void printPreview() {

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
	public boolean canPrint() {
		// TODO Auto-generated method stub
		return true;
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
		isDatesArranged = true;
	}

	@Override
	public HashMap<String, Object> saveView() {
		HashMap<String, Object> map = new HashMap<String, Object>();
		String selectedDateRange = toolbar.getSelectedDateRange();
		ClientFinanceDate startDate = toolbar.getStartDate();
		ClientFinanceDate endDate = toolbar.getEndDate();
		map.put("selectedDateRange", selectedDateRange);
		map.put("startDate", startDate);
		map.put("endDate", endDate);
		return map;
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
