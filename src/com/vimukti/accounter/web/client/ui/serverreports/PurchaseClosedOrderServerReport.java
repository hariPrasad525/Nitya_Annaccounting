//package com.vimukti.accounter.web.client.ui.serverreports;
//
//import com.vimukti.accounter.web.client.core.ClientFinanceDate;
//import com.vimukti.accounter.web.client.core.Lists.OpenAndClosedOrders;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.reports.IFinanceReport;
//
//public class PurchaseClosedOrderServerReport extends
//		AbstractFinaneReport<OpenAndClosedOrders> {
//
//	private String sectionName;
//
//	public PurchaseClosedOrderServerReport(
//			IFinanceReport<OpenAndClosedOrders> reportView) {
//		this.reportView = reportView;
//	}
//
//	public PurchaseClosedOrderServerReport(long startDate, long endDate,
//			int generationType) {
//		super(startDate, endDate, generationType);
//	}
//
//	@Override
//	public Object getColumnData(OpenAndClosedOrders record, int columnIndex) {
//		switch (columnIndex) {
//		case 0:
//			if (record.getTransactionDate() != null)
//				return UIUtils
//						.getDateByCompanyType(record.getTransactionDate());
//			else
//				break;
//		case 1:
//			return record.getVendorOrCustomerName();
//		case 2:
//			return record.getDescription();
//		case 3:
//			return ((Double) record.getQuantity()).toString();
//		case 4:
//			return record.getAmount();
//
//		default:
//			break;
//		}
//		return null;
//	}
//
//	@Override
//	public int[] getColumnTypes() {
//		return new int[] { COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT,
//				COLUMN_TYPE_TEXT, COLUMN_TYPE_TEXT, COLUMN_TYPE_AMOUNT };
//	}
//
//	@Override
//	public String[] getColunms() {
//		return new String[] { getMessages().orderDate(),
//				getMessages().Vendor(),
//				getMessages().description(),
//				getMessages().quantity(), getMessages().value() };
//
//	}
//
//	@Override
//	public String getTitle() {
//
//		return getMessages().purchaseClosedOrderReport();
//	}
//
//	@Override
//	public void makeReportRequest(long start, long end) {
//		// if (this.financeTool == null)
//		// return;
//		// resetVariables();
//		// try {
//		// onSuccess(this.financeTool.getClosedPurchaseOrders(start, end));
//		// } catch (DAOException e) {
//		// e.printStackTrace();
//		// }
//	}
//
//	@Override
//	public void processRecord(OpenAndClosedOrders record) {
//		if (sectionDepth == 0) {
//			addSection("", getMessages().totalOf(), new int[] { 4 });
//		} else if (sectionDepth == 1) {
//			// First time
//			this.sectionName = record.getVendorOrCustomerName();
//			addSection(sectionName, getMessages().totalOf()
//					+ sectionName, new int[] { 4 });
//		} else if (sectionDepth == 2) {
//			// No need to do anything, just allow adding this record
//			if (!sectionName.equals(record.getVendorOrCustomerName())) {
//				endSection();
//			} else {
//				return;
//			}
//
//		}
//		processRecord(record);
//
//	}
//
//	public void print() {
//		// String gridhtml = grid.toString();
//		// String headerhtml = grid.getHeader();
//		// String footerhtml = grid.getFooter();
//		//
//		// gridhtml = gridhtml.replaceAll(headerhtml, "");
//		// gridhtml = gridhtml.replaceAll(footerhtml, "");
//		// headerhtml = headerhtml.replaceAll("td", "th");
//		// headerhtml = headerhtml.substring(headerhtml.indexOf("<tr "),
//		// headerhtml.indexOf("</tbody>"));
//		// footerhtml = footerhtml.substring(footerhtml.indexOf("<tr>"),
//		// footerhtml.indexOf("</tbody"));
//		// footerhtml = footerhtml.replaceAll("<tr>",
//		// "<tr class=\"listgridfooter\">");
//		//
//		// String firsRow = "<tr class=\"ReportGridRow\">"
//		// + "" + "</tr>";
//		// String lastRow = "<tr class=\"ReportGridRow\">"
//		// + grid.rowFormatter.getElement(grid.getRowCount() - 1)
//		// .getInnerHTML() + "</tr>";
//		//
//		// headerhtml = headerhtml + firsRow;
//		// footerhtml = lastRow + footerhtml;
//		//
//		// gridhtml = gridhtml.replace(firsRow, headerhtml);
//		// gridhtml = gridhtml.replace(lastRow, footerhtml);
//		// gridhtml = gridhtml.replaceAll("<tbody>", "");
//		// gridhtml = gridhtml.replaceAll("</tbody>", "");
//		//
//		// String dateRangeHtml =
//		// "<div style=\"font-family:sans-serif;\"><strong>"
//		// + this.getStartDate()
//		// + " - "
//		// + this.getEndDate() + "</strong></div>";
//		//
//		// UIUtils.generateReportPDF(this.getTitle(), gridhtml, dateRangeHtml);
//	}
//
//	@Override
//	public ClientFinanceDate getEndDate(OpenAndClosedOrders obj) {
//		return obj.getEndDate();
//	}
//
//	@Override
//	public ClientFinanceDate getStartDate(OpenAndClosedOrders obj) {
//		return obj.getStartDate();
//	}
//
//	@Override
//	public String[] getDynamicHeaders() {
//		return new String[] { getMessages().orderDate(),
//				getMessages().Vendor(),
//				getMessages().description(),
//				getMessages().quantity(), getMessages().value() };
//
//	}
//	
//
// }
