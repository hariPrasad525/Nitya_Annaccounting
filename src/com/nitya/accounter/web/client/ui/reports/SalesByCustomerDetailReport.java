package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.StringReportInput;
import com.nitya.accounter.web.client.core.reports.MostProfitableCustomers;
import com.nitya.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.SalesByCustomerDetailServerReport;

public class SalesByCustomerDetailReport extends
		AbstractReportView<SalesByCustomerDetail> {

	public SalesByCustomerDetailReport() {
		this.serverReport = new SalesByCustomerDetailServerReport(this);
	}

	@Override
	public void OnRecordClick(SalesByCustomerDetail record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		if (Accounter.getUser().canDoInvoiceTransactions())
			ReportsRPC.openTransactionView(record.getType(),
					record.getTransactionId());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {

		if (this.data == null) {
			Accounter.createReportService().getSalesByCustomerDetailReport(
					start, end, this);
		} else {
			if (this.data instanceof SalesByCustomerDetail) {
				SalesByCustomerDetail byCustomerDetail = (SalesByCustomerDetail) this.data;
				if (byCustomerDetail.getName() != null) {
					Accounter.createReportService()
							.getSalesByCustomerDetailReport(
									byCustomerDetail.getName(),
									byCustomerDetail.getStartDate(),
									byCustomerDetail.getEndDate(), this);
				}
			} else if (this.data instanceof MostProfitableCustomers) {
				MostProfitableCustomers record = (MostProfitableCustomers) this.data;
				if (record.getCustomer() != null) {
					Accounter.createReportService()
							.getSalesByCustomerDetailReport(
									record.getCustomer(),
									record.getStartDate(), record.getEndDate(),
									this);
				}
			}
		}
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

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		String customerName = this.data != null ? ((SalesByCustomerDetail) this.data)
				.getName() : "";
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 122, new StringReportInput(customerName));
	}

	@Override
	public void printPreview() {

	}

	public int sort(SalesByCustomerDetail obj1, SalesByCustomerDetail obj2,
			int col) {

		int ret = obj1.getName().toLowerCase()
				.compareTo(obj2.getName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {
		case 2:
			return UIUtils.compareInt(obj1.getType(), obj2.getType());
		case 1:
			return obj1.getDate().compareTo(obj2.getDate());
		case 3:
			return UIUtils.compareInt(Integer.parseInt(obj1.getNumber()),
					Integer.parseInt(obj2.getNumber()));
		case 0:
			return obj1.getName().toLowerCase()
					.compareTo(obj2.getName().toLowerCase());
		case 4:
			return obj1.getDueDate().compareTo(obj2.getDueDate());
		case 5:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());
		}
		return 0;
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
