package com.nitya.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.TrialBalance;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.serverreports.ProfitAndLossServerReport;

public class ProfitAndLossReport extends AbstractReportView<TrialBalance> {
	// private ClientFinanceDate start, end;

	public ProfitAndLossReport() {
		// createToolBar();
		this.serverReport = new ProfitAndLossServerReport(this);
		// startDate.getTime(), endDate.getTime(), this);
	}

	@Override
	public void OnRecordClick(TrialBalance record) {
		record.setStartDate(toolbar.getStartDate());
		record.setEndDate(toolbar.getEndDate());
		record.setDateRange(toolbar.getSelectedDateRange());
		UIUtils.runAction(record,
				CompanyAndFinancialReportsAction.transactionDetailByAccount());
	}

	@Override
	public int getToolbarType() {
		return TOOLBAR_TYPE_DATE_RANGE;
	}

	@Override
	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
		TrialBalance record = (TrialBalance) data;
		if (record == null) {
			Accounter.createReportService().getProfitAndLossReport(start, end,
					this);
		} else {
			Accounter.createReportService().getProfitAndLossReport(
					record.getStartDate(), record.getEndDate(), this);
		}
		// this.start = start;
		// this.end = end;
		// this.serverReport = new ProfitAndLossServerReport(
		/*
		 * this.toolbar .getStartDate ().getTime(), this.toolbar .getEndDate
		 * ().getTime(),
		 */// this.start.getTime(), this.end.getTime(), this);
	}

	@Override
	public void onEdit() {

	}

	@Override
	public void export(int generationType) {
		UIUtils.generateReport(generationType, startDate.getDate(),
				endDate.getDate(), 111);
	}

	@Override
	public void printPreview() {

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

	//
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
	// footerhtml = footerhtml.replaceAll("<TR>", "<TR class=listgridfooter>");
	//
	// String firsRow = "<TR class=ReportGridRow>"
	// + grid.rowFormatter.getElement(0).getInnerHTML() + "</TR>";
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
	// String dateRangeHtml = null;
	//
	// UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	// }
	//
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
	// + grid.rowFormatter.getElement(0).getInnerHTML() + "</tr>";
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
	// String dateRangeHtml = null;
	//
	// UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	// }

}
