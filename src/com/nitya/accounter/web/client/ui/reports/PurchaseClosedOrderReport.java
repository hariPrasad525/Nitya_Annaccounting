package com.nitya.accounter.web.client.ui.reports;
//package com.nitya.accounter.web.client.ui.reports;
//
//import com.nitya.accounter.web.client.core.ClientFinanceDate;
//import com.nitya.accounter.web.client.core.Lists.OpenAndClosedOrders;
//import com.nitya.accounter.web.client.ui.Accounter;
//import com.nitya.accounter.web.client.ui.UIUtils;
//import com.nitya.accounter.web.client.ui.serverreports.PurchaseClosedOrderServerReport;
//
//public class PurchaseClosedOrderReport extends
//		AbstractReportView<OpenAndClosedOrders> {
//
//	public PurchaseClosedOrderReport() {
//		this.serverReport = new PurchaseClosedOrderServerReport(this);
//	}
//
//	@Override
//	public void init() {
//		super.init();
//		toolbar.setDateRanageOptions("", messages.present(),
//				messages.lastMonth(), messages.last3Months(),
//				messages.last6Months(), messages.lastYear(),
//				messages.untilEndOfYear(), messages.custom());
//	}
//
//	@Override
//	public void OnRecordClick(OpenAndClosedOrders record) {
//
//	}
//
//	@Override
//	public int getToolbarType() {
//		return TOOLBAR_TYPE_DATE_RANGE;
//	}
//
//	@Override
//	public void makeReportRequest(ClientFinanceDate start, ClientFinanceDate end) {
//		Accounter.createReportService().getPurchaseOrderReport(4, start, end,
//				this);
//
//	}
//
//	@Override
//	public void onEdit() {
//
//	}
//
//	@Override
//	public void print() {
//
//		UIUtils.generateReportPDF(
//				Integer.parseInt(String.valueOf(startDate.getDate())),
//				Integer.parseInt(String.valueOf(endDate.getDate())), 135, "",
//				"");
//
//	}
//
//	@Override
//	public void printPreview() {
//
//	}
//
//	public void exportToCsv() {
//		UIUtils.exportReport(
//				Integer.parseInt(String.valueOf(startDate.getDate())),
//				Integer.parseInt(String.valueOf(endDate.getDate())), 135, "",
//				"");
//	}
// }
