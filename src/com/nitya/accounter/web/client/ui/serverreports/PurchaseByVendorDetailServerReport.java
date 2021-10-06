package com.nitya.accounter.web.client.ui.serverreports;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.reports.BaseReport;
import com.nitya.accounter.web.client.core.reports.SalesByCustomerDetail;
import com.nitya.accounter.web.client.ui.reports.IFinanceReport;

public class PurchaseByVendorDetailServerReport extends
		AbstractFinaneReport<SalesByCustomerDetail> {

	private String sectionName = "";

	public PurchaseByVendorDetailServerReport(
			IFinanceReport<SalesByCustomerDetail> reportView) {
		this.reportView = reportView;
	}

	public PurchaseByVendorDetailServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	@Override
	public Object getColumnData(SalesByCustomerDetail record, int columnIndex) {
		switch (columnIndex) {
		case 2:
			return Utility.getTransactionName(record.getType());
		case 1:
			return getDateByCompanyType(record.getDate());
		case 3:
			return record.getNumber();
		case 0:
			return "";
			// case 4:
			// return record.getDueDate();
		case 4:
			return record.getAmount();
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_DATE,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_NUMBER, COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {

		return new String[] { messages.payeeName(Global.get().vendor()),
				getMessages().date(), getMessages().type(),
				getMessages().noDot(), getMessages().amount() };

	}

	@Override
	public String getTitle() {
		return messages.purchaseByVendorDetail(Global.get().vendor());
	}

	@Override
	public String getDefaultDateRange() {
		return messages.thisMonth();
	}

	@Override
	public int getColumnWidth(int index) {
		// if (index == 0)
		// return 180;
		if (index == 1)
			return 100;
		else if (index == 4)
			return 160;
		else if (index == 0)
			return 200;
		else if (index == 3)
			return 100;
		else
			return -1;
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// if (navigateObjectName == null) {
		//
		// onSuccess(this.financeTool.getPurchasesByVendorDetail(start,
		// end));
		// } else {
		// onSuccess(this.financeTool.getPurchasesByVendorDetail(
		// navigateObjectName, start, end));
		// }
		// } catch (DAOException e) {
		// }
	}

	@Override
	public void processRecord(SalesByCustomerDetail record) {
		// if (sectionDepth == 0) {
		// addSection(new String[] { "", "" }, new String[] { "", "", "",
		// getMessages().total() }, new int[] { 4 });
		// } else
		if (sectionDepth == 0) {
			this.sectionName = record.getName();
			addSection(new String[] { sectionName }, new String[] { "", "", "",
					getMessages().total() }, new int[] { 4 });
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

	public void print() {
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
		// + this.getEndDate()
		// + "</strong></div>";
		//
		// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
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
	public void resetVariables() {
		this.sectionDepth = 0;
		this.sectionName = "";
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { messages.payeeName(Global.get().vendor()),
				getMessages().date(), getMessages().type(),
				getMessages().noDot(), getMessages().amount() };
	}

}
