package com.nitya.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class InventoryReportsAction extends Action {

	public static final int TYPE_VALUATION_SUMMARY = 1;
	public static final int TYPE_VALUATION_DETAILS = 2;
	public static final int TYPE_STOCK_STATUS_BY_ITEM = 3;
	public static final int TYPE_STOCK_STATUS_BY_VENDOR = 4;
	public static final int TYPE_ITEM_REPORT = 5;

	private int type;

	private long id;

	public InventoryReportsAction(int type) {
		super();
		this.type = type;
		this.catagory = messages.report();
	}

	public InventoryReportsAction(int type, Long id) {
		this(type);
		this.id = id;
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractReportView report = null;
				switch (type) {
				case TYPE_VALUATION_SUMMARY:
					report = new InventoryValutionSummaryReport();
					break;
				case TYPE_VALUATION_DETAILS:
					report = new InventoryValuationDetailsReport(id);
					break;
				case TYPE_STOCK_STATUS_BY_ITEM:
					report = new InventoryStockStatusByItemReport();
					break;
				case TYPE_STOCK_STATUS_BY_VENDOR:
					report = new InventoryStockStatusByVendorReport();
					break;
				case TYPE_ITEM_REPORT:
					report = new InventoryItemReport();
					break;
				default:
					break;
				}

				if (report != null) {
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, InventoryReportsAction.this);
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
		case TYPE_VALUATION_SUMMARY:
			return "InventoryValutionSummaryReport";
		case TYPE_VALUATION_DETAILS:
			return "InventoryValuationDetailReport";
		case TYPE_STOCK_STATUS_BY_ITEM:
			return "InventoryStockStatusByItemReport";
		case TYPE_STOCK_STATUS_BY_VENDOR:
			return "InventoryStockStatusByVendorReport";
		case TYPE_ITEM_REPORT:
			return "InventoryItemReport";
		default:
			break;
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_VALUATION_SUMMARY:
			return "inventory-summary";
		case TYPE_VALUATION_DETAILS:
			return "inventory-valuation-report";
		case TYPE_STOCK_STATUS_BY_ITEM:
			return "inventory-stock-status-by-item-report";
		case TYPE_STOCK_STATUS_BY_VENDOR:
			return "inventory-stock-status-by-vendor-report";
		case TYPE_ITEM_REPORT:
			return "inventory-item";
		default:
			break;
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_VALUATION_SUMMARY:
			return messages.inventoryValutionSummary();
		case TYPE_VALUATION_DETAILS:
			return messages.inventoryValuationDetails();
		case TYPE_STOCK_STATUS_BY_ITEM:
			return messages.inventoryStockStatusByItem();
		case TYPE_STOCK_STATUS_BY_VENDOR:
			return messages.inventoryStockStatusByVendor();
		case TYPE_ITEM_REPORT:
			return messages.inventoryItem();
		default:
			break;
		}
		return null;
	}

	public static InventoryReportsAction valuationSummary() {
		return new InventoryReportsAction(TYPE_VALUATION_SUMMARY);
	}

	public static InventoryReportsAction valuationDetails() {
		return valuationDetails(0l);
	}

	public static InventoryReportsAction valuationDetails(long itemId) {
		return new InventoryReportsAction(TYPE_VALUATION_DETAILS, itemId);
	}

	public static InventoryReportsAction stockStatusByItem() {
		return new InventoryReportsAction(TYPE_STOCK_STATUS_BY_ITEM);
	}

	public static InventoryReportsAction stockStatusByVendor() {
		return new InventoryReportsAction(TYPE_STOCK_STATUS_BY_VENDOR);
	}

	public static InventoryReportsAction itemReport() {
		return new InventoryReportsAction(TYPE_ITEM_REPORT);
	}

}
