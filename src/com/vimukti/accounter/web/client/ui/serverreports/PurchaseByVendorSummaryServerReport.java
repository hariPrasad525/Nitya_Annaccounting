package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class PurchaseByVendorSummaryServerReport extends
		AbstractFinaneReport<SalesByCustomerDetail> {
	public PurchaseByVendorSummaryServerReport(
			IFinanceReport<SalesByCustomerDetail> reportView) {
		this.reportView = reportView;
	}

	public PurchaseByVendorSummaryServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(SalesByCustomerDetail record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			return record.getName();
			// case 1:
			// return record.getGroupName();
		case 1:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		return new String[] { messages.payeeName(Global.get().Vendor()),

		// getVendorString("Supplier Group", "Vendor Group"),
				messages.amount() };
	}

	@Override
	public String getTitle() {
		return messages.purchaseByVendorSummary(Global.get().Vendor());

	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// onSuccess(this.financeTool.getPurchasesByVendorSummary(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void processRecord(SalesByCustomerDetail record) {
		if (sectionDepth == 0) {
			addSection("", messages.total(), new int[] { 1 });
		} else if (sectionDepth == 1) {
			return;
		}
		// Go on recursive calling if we reached this place
		processRecord(record);
	}

	public void print() {
		// String gridhtml = grid.toString();
		// String headerhtml = grid.getHeader();
		//
		// gridhtml = gridhtml.replaceAll(headerhtml, "");
		// gridhtml = gridhtml.replaceAll(grid.getFooter(), "");
		//
		// headerhtml = headerhtml.replaceAll("td", "th");
		// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
		// headerhtml.indexOf("</tbody>"));
		//
		// String firsRow = "<tr class=\"ReportGridRow depth\">" + "" + "</tr>";
		// headerhtml = headerhtml + firsRow;
		// gridhtml = gridhtml.replace(firsRow, headerhtml);
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
		// generateReportPDF(this.getTitle(), gridhtml,
		// dateRangeHtml);
	}

	@Override
	public ClientFinanceDate getEndDate(SalesByCustomerDetail obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(SalesByCustomerDetail obj) {
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
		if (index == 1)
			return 300;
		else
			return -1;
	}

	public int sort(SalesByCustomerDetail obj1, SalesByCustomerDetail obj2,
			int col) {
		switch (col) {
		case 0:
			return obj1.getName().toLowerCase().compareTo(
					obj2.getName().toLowerCase());
			// case 1:
			// return obj1.getGroupName().toLowerCase().compareTo(
			// obj2.getGroupName().toLowerCase());
		case 1:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());

		}
		return 0;
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.payeeName(Global.get().vendor()),
		// getVendorString("Supplier Group", "Vendor Group"),
				messages.amount() };
	}

}
