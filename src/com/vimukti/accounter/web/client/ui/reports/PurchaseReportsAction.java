package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class PurchaseReportsAction extends Action {

	public static final int TYPE_VENDOR_SUMMARY = 1;
	public static final int TYPE_VENDOR_DETAIL = 2;
	public static final int TYPE_ITEM_SUMMARY = 3;
	public static final int TYPE_ITEM_DETAIL = 4;
	public static final int TYPE_PO_REPORT = 5;

	private int type;

	public PurchaseReportsAction(int type) {
		super();
		this.type = type;
		this.catagory = Global.get().Vendor();
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractReportView report = null;
				switch (type) {
				case TYPE_VENDOR_SUMMARY:
					report = new PurchaseByVendorSummaryReport();
					break;
				case TYPE_VENDOR_DETAIL:
					report = new PurchaseByVendorDetailReport();
					break;
				case TYPE_ITEM_SUMMARY:
					report = new PurchaseByItemSummaryReport();
					break;
				case TYPE_ITEM_DETAIL:
					report = new PurchaseByItemDetailReport();
					break;
				case TYPE_PO_REPORT:
					report = new PurchaseOrderReport();
					break;
				}
				if (report != null) {
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, PurchaseReportsAction.this);
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
		case TYPE_VENDOR_SUMMARY:
			return "purchaseByVendorSummary";
		case TYPE_VENDOR_DETAIL:
			return "purchaseByVendorDetail";
		case TYPE_ITEM_SUMMARY:
			return "purchaseByItemSummary";
		case TYPE_ITEM_DETAIL:
			return "purchaseByItemDetail";
		case TYPE_PO_REPORT:
			return "purchaseOrderReport";
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_VENDOR_SUMMARY:
			return "purchase-vendor-summary";
		case TYPE_VENDOR_DETAIL:
			return "purchase-vendor-detail";
		case TYPE_ITEM_SUMMARY:
			return "purchased-item";
		case TYPE_ITEM_DETAIL:
			return "purchased-item";
		case TYPE_PO_REPORT:
			return "purchase-open-order";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_VENDOR_SUMMARY:
			return Global.get().messages()
					.purchaseByVendorSummary(Global.get().Vendor());
		case TYPE_VENDOR_DETAIL:
			return Global.get().messages()
					.purchaseByVendorDetail(Global.get().Vendor());
		case TYPE_ITEM_SUMMARY:
			return messages.purchaseByItemSummary();
		case TYPE_ITEM_DETAIL:
			return messages.purchaseByItemDetail();
		case TYPE_PO_REPORT:
			return messages.purchaseOrderReport();
		}
		return null;
	}

	public static PurchaseReportsAction vendorSummary() {
		return new PurchaseReportsAction(TYPE_VENDOR_SUMMARY);
	}

	public static PurchaseReportsAction vendorDetail() {
		return new PurchaseReportsAction(TYPE_VENDOR_DETAIL);
	}

	public static PurchaseReportsAction itemSummary() {
		return new PurchaseReportsAction(TYPE_ITEM_SUMMARY);
	}

	public static PurchaseReportsAction itemDetail() {
		return new PurchaseReportsAction(TYPE_ITEM_DETAIL);
	}

	public static PurchaseReportsAction purchaseOrder() {
		return new PurchaseReportsAction(TYPE_PO_REPORT);
	}

}
