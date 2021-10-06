package com.nitya.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class SalesReportsAction extends Action {

	public static final int TYPE_CUSTOMER_SUMMARY = 1;
	public static final int TYPE_CUSTOMER_DETAIL = 2;
	public static final int TYPE_ITEM_SUMMARY = 3;
	public static final int TYPE_ITEM_DETAIL = 4;
	public static final int TYPE_SO_REPORT = 5;

	private int type;

	public SalesReportsAction(int type) {
		this.type = type;
	}

	@Override
	public void run() {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractReportView report = null;
				switch (type) {
				case TYPE_CUSTOMER_SUMMARY:
					report = new SalesByCustomerSummaryReport();
					break;
				case TYPE_CUSTOMER_DETAIL:
					report = new SalesByCustomerDetailReport();
					break;
				case TYPE_ITEM_SUMMARY:
					report = new SalesByItemSummaryReport();
					break;
				case TYPE_ITEM_DETAIL:
					report = new SalesByItemDetailReport();
					break;
				case TYPE_SO_REPORT:
					report = new SalesOrderReport();
					break;
				}
				if (report != null) {
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, SalesReportsAction.this);
				}

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		switch (type) {
		case TYPE_CUSTOMER_SUMMARY:
			return "salesByCustomerSummary";
		case TYPE_CUSTOMER_DETAIL:
			return "salesByCustomerDetail";
		case TYPE_ITEM_SUMMARY:
			return "salesByItemSummary";
		case TYPE_ITEM_DETAIL:
			return "salesByItemDetail";
		case TYPE_SO_REPORT:
			return "salesOrderReport";
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_CUSTOMER_SUMMARY:
			return "sales-by-customer";
		case TYPE_CUSTOMER_DETAIL:
			return "sales-by-customer-details";
		case TYPE_ITEM_SUMMARY:
			return "sales-by-item-summary";
		case TYPE_ITEM_DETAIL:
			return "sales-by-item-detail";
		case TYPE_SO_REPORT:
			return "sales-open-order";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_CUSTOMER_SUMMARY:
			return messages.salesByCustomerSummary(Global.get().customer());
		case TYPE_CUSTOMER_DETAIL:
			return messages.salesByCustomerDetail(Global.get().Customer());
		case TYPE_ITEM_SUMMARY:
			return messages.salesByItemSummary();
		case TYPE_ITEM_DETAIL:
			return messages.salesByItemDetail();
		case TYPE_SO_REPORT:
			return messages.salesOrderReport();
		}
		return null;
	}

	public static SalesReportsAction customerSummary() {
		return new SalesReportsAction(TYPE_CUSTOMER_SUMMARY);
	}

	public static SalesReportsAction customerDetail() {
		return new SalesReportsAction(TYPE_CUSTOMER_DETAIL);
	}

	public static SalesReportsAction itemSummary() {
		return new SalesReportsAction(TYPE_ITEM_SUMMARY);
	}

	public static SalesReportsAction itemDetail() {
		return new SalesReportsAction(TYPE_ITEM_DETAIL);
	}

	public static SalesReportsAction salesOrder() {
		return new SalesReportsAction(TYPE_SO_REPORT);
	}

}
