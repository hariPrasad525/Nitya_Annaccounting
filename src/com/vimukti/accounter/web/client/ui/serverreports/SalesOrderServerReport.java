package com.vimukti.accounter.web.client.ui.serverreports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;

public class SalesOrderServerReport extends
		AbstractFinaneReport<OpenAndClosedOrders> {

	private String sectionName;

	public SalesOrderServerReport(long startDate, long endDate,
			int generationType) {
		super(startDate, endDate, generationType);
	}

	public SalesOrderServerReport(IFinanceReport<OpenAndClosedOrders> reportView) {
		this.reportView = reportView;
	}

	@Override
	public Object getColumnData(OpenAndClosedOrders record, int columnIndex) {
		switch (columnIndex) {
		case 0:
			if (record.getTransactionDate() != null)
				return getDateByCompanyType(record.getTransactionDate());
			else
				break;
		case 1:
			return record.getNumber();
			// case 2:
			// // if (isSales)
			// return record.getDescription();
			// else
			// return record.getAmount();
			// case 2:
			// return ((Double) record.getQuantity()).toString();
		case 2:
			return record.getVendorOrCustomerName();
		case 3:
			return record.getAmount();
		default:
			break;
		}
		return null;
	}

	@Override
	public int[] getColumnTypes() {
		// if (isSales)
		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
				COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
		// else
		// return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
		// COLUMN_TYPE_AMOUNT };
	}

	@Override
	public String[] getColunms() {
		// if (isSales)
		return new String[] { getMessages().orderDate(),
				getMessages().number(), getMessages().customer(),
				// FinanceApplication.constants().description(),
				// FinanceApplication.constants().quantity(),
				getMessages().amount() };

		// else
		// return new String[] {
		// FinanceApplication.constants().orderDate(),
		// FinanceApplication.constants().customer(),
		// FinanceApplication.constants().value() };
	}

	@Override
	public String getTitle() {
		return getMessages().salesOrderReport();
	}

	@Override
	public void makeReportRequest(long start, long end) {
		// if (this.financeTool == null)
		// return;
		// resetVariables();
		// try {
		// onSuccess(this.financeTool.getOpenSalesOrders(start, end));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }
	}

	@Override
	public void makeReportRequest(int status, ClientFinanceDate start,
			ClientFinanceDate end) {
		// if (status == 1)
		// FinanceApplication.createReportService().getSalesOpenOrderReport(
		// start.getTime(), end.getTime(), this);
		// else if (status == 2)
		// FinanceApplication.createReportService()
		// .getSalesCompletedOrderReport(start.getTime(),
		// end.getTime(), this);
		// else if (status == 3)
		// FinanceApplication.createReportService()
		// .getSalesCancelledOrderReport(start.getTime(),
		// end.getTime(), this);
		// else
		// FinanceApplication.createReportService()
		// .getSalesOrderReport(start.getTime(),
		// end.getTime(), this);
		//
		// if (this.financeTool == null)
		// return;
		// try {
		// onSuccess(this.financeTool.getOpenSalesOrders(start.getTime(), end
		// .getTime()));
		// } catch (DAOException e) {
		// e.printStackTrace();
		// }

	}

	@Override
	public void processRecord(OpenAndClosedOrders record) {
		int col;
		// if (isSales)
		col = 3;
		// else
		// col = 2;
		// if (sectionDepth == 0) {
		// addSection("", getMessages().total(), new int[] { col });
		// } else
		if (sectionDepth == 0) {
			// First time
			this.sectionName = record.getVendorOrCustomerName();
			addSection(sectionName, getMessages().total(), new int[] { col });
		} else if (sectionDepth == 1) {
			// No need to do anything, just allow adding this record
			if (sectionName != null
					&& !sectionName.equals(record.getVendorOrCustomerName())) {
				endSection();
			} else {
				return;
			}

		}
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
		// + this.getEndDate() + "</strong></div>";
		//
		// generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
	}

	@Override
	public ClientFinanceDate getEndDate(OpenAndClosedOrders obj) {
		return obj.getEndDate();
	}

	@Override
	public ClientFinanceDate getStartDate(OpenAndClosedOrders obj) {
		return obj.getStartDate();

	}

	@Override
	public int getColumnWidth(int index) {
		if (index == 0)
			return 250;
		else if (index == 2)
			return 250;

		else
			return -1;
	}

	public int sort(OpenAndClosedOrders obj1, OpenAndClosedOrders obj2, int col) {

		int ret = obj1.getVendorOrCustomerName().toLowerCase()
				.compareTo(obj2.getVendorOrCustomerName().toLowerCase());
		if (ret != 0) {
			return ret;
		}
		switch (col) {

		case 0:
			return obj1.getTransactionDate().compareTo(
					obj2.getTransactionDate());

		case 1:
			return UIUtils.compareToInt(obj1.getNumber(), obj2.getNumber()
					.toLowerCase());
		case 2:
			return obj1.getVendorOrCustomerName().toLowerCase()
					.compareTo(obj2.getVendorOrCustomerName().toLowerCase());
			// case 2:
			// // if (isSales)
			// return obj1.getDescription().toLowerCase().compareTo(
			// obj2.getDescription().toLowerCase());
			// else
			// return Utility_R
			// .compareDouble(obj1.getAmount(), obj2.getAmount());
			// case 2:
			// return Utility_R
			// .compareDouble(obj1.getQuantity(), obj2.getQuantity());

		case 3:
			return UIUtils.compareDouble(obj1.getAmount(), obj2.getAmount());

		}
		return 0;
	}

	public void resetVariables() {
		sectionDepth = 0;
		sectionName = "";
		super.resetVariables();
	}

	@Override
	public String[] getDynamicHeaders() {
		return new String[] { getMessages().orderDate(),
				getMessages().number(), getMessages().customer(),
				// FinanceApplication.constants().description(),
				// FinanceApplication.constants().quantity(),
				getMessages().amount() };
	}

}
