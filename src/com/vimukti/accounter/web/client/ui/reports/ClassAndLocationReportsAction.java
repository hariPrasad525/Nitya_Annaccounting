package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ClassAndLocationReportsAction extends Action {

	public static final int TYPE_SALES_LOCATION_SUMMARY = 1;
	public static final int TYPE_SALES_LOCATION_DETAIL = 2;
	public static final int TYPE_SALES_CLASS_SUMMARY = 3;
	public static final int TYPE_SALES_CLASS_DETAIL = 4;
	public static final int TYPE_PURCHASE_LOCATION_SUMMARY = 5;
	public static final int TYPE_PURCHASE_LOCATION_DETAIL = 6;
	public static final int TYPE_PURCHASE_CLASS_SUMMARY = 7;
	public static final int TYPE_PURCHASE_CLASS_DETAIL = 8;
	public static final int TYPE_PROFIT_AND_LOSS_LOCATION = 9;
	public static final int TYPE_PROFIT_AND_LOSS_CLASS = 10;

	private int type;

	public ClassAndLocationReportsAction(int type) {
		super();
		this.type = type;
		switch (type) {
		case TYPE_SALES_LOCATION_SUMMARY:
		case TYPE_SALES_LOCATION_DETAIL:
		case TYPE_SALES_CLASS_SUMMARY:
		case TYPE_SALES_CLASS_DETAIL:
			catagory = Global.get().Customer();
		case TYPE_PURCHASE_LOCATION_SUMMARY:
		case TYPE_PURCHASE_LOCATION_DETAIL:
		case TYPE_PURCHASE_CLASS_SUMMARY:
		case TYPE_PURCHASE_CLASS_DETAIL:
			catagory = Global.get().Vendor();
		case TYPE_PROFIT_AND_LOSS_LOCATION:
		case TYPE_PROFIT_AND_LOSS_CLASS:
			catagory = messages.report();
		}
	}

	@Override
	public void run() {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractReportView report = null;
				switch (type) {
				case TYPE_SALES_LOCATION_SUMMARY:
					report = new SalesByLocationsummaryReport(true, true);
					break;
				case TYPE_SALES_LOCATION_DETAIL:
					report = new SalesByLocationDetailsReport(true, true);
					break;
				case TYPE_SALES_CLASS_SUMMARY:
					report = new SalesByLocationsummaryReport(false, true);
					break;
				case TYPE_SALES_CLASS_DETAIL:
					report = new SalesByLocationDetailsReport(false, true);
					break;
				case TYPE_PURCHASE_LOCATION_SUMMARY:
					report = new SalesByLocationsummaryReport(true, false);
					break;
				case TYPE_PURCHASE_LOCATION_DETAIL:
					report = new SalesByLocationDetailsReport(true, false);
					break;
				case TYPE_PURCHASE_CLASS_SUMMARY:
					report = new SalesByLocationsummaryReport(false, false);
					break;
				case TYPE_PURCHASE_CLASS_DETAIL:
					report = new SalesByLocationDetailsReport(false, false);
					break;
				case TYPE_PROFIT_AND_LOSS_LOCATION:
					report = new ProfitAndLossByLocationReport(2);
					break;
				case TYPE_PROFIT_AND_LOSS_CLASS:
					report = new ProfitAndLossByLocationReport(1);
					break;
				}

				if (report != null) {
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, ClassAndLocationReportsAction.this);
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
		case TYPE_SALES_LOCATION_SUMMARY:
			return "salesByLocationSummary";
		case TYPE_SALES_LOCATION_DETAIL:
			return "salesByLocationDetails";
		case TYPE_SALES_CLASS_SUMMARY:
			return "salesByClassSummary";
		case TYPE_SALES_CLASS_DETAIL:
			return "salesByClassDetails";
		case TYPE_PURCHASE_LOCATION_SUMMARY:
			return "PurchasesByLocationSummary";
		case TYPE_PURCHASE_LOCATION_DETAIL:
			return "PurchasesByLocationDetails";
		case TYPE_PURCHASE_CLASS_SUMMARY:
			return "PurchasesbyClassSummary";
		case TYPE_PURCHASE_CLASS_DETAIL:
			return "PurchasesbyClassDetail";
		case TYPE_PROFIT_AND_LOSS_LOCATION:
			return "profitAndLossByLocation";
		case TYPE_PROFIT_AND_LOSS_CLASS:
			return "profitAndLossByClass";
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_SALES_LOCATION_SUMMARY:
			return "sales-by-location-summary";
		case TYPE_SALES_LOCATION_DETAIL:
			return "sales-by-location";
		case TYPE_SALES_CLASS_SUMMARY:
			return "sales-by-class-summary";
		case TYPE_SALES_CLASS_DETAIL:
			return "sales-by-class";
		case TYPE_PURCHASE_LOCATION_SUMMARY:
			return "Purchases-by-location-summary";
		case TYPE_PURCHASE_LOCATION_DETAIL:
			return "Purchases-by-location";
		case TYPE_PURCHASE_CLASS_SUMMARY:
			return "Purchases-by-class-summary";
		case TYPE_PURCHASE_CLASS_DETAIL:
			return "Purchases-by-class";
		case TYPE_PROFIT_AND_LOSS_LOCATION:
			return "profit-loss-by-location";
		case TYPE_PROFIT_AND_LOSS_CLASS:
			return "profit-loss-by-class";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_SALES_LOCATION_SUMMARY:
			return messages.salesByLocationSummary(Global.get().Location());
		case TYPE_SALES_LOCATION_DETAIL:
			return messages.getSalesByLocationDetails(Global.get().Location());
		case TYPE_SALES_CLASS_SUMMARY:
			return messages.salesByClassSummary();
		case TYPE_SALES_CLASS_DETAIL:
			return messages.salesByClassDetails();
		case TYPE_PURCHASE_LOCATION_SUMMARY:
			return messages.purchasesbyLocationSummary(Global.get().Location());
		case TYPE_PURCHASE_LOCATION_DETAIL:
			return messages.purchasesbyLocationDetail(Global.get().Location());
		case TYPE_PURCHASE_CLASS_SUMMARY:
			return messages.purchasesbyClassSummary();
		case TYPE_PURCHASE_CLASS_DETAIL:
			return messages.purchasesbyClassDetail();
		case TYPE_PROFIT_AND_LOSS_LOCATION:
			return messages.profitAndLossByLocation(Global.get().Location());
		case TYPE_PROFIT_AND_LOSS_CLASS:
			return messages.profitAndLossbyClass();
		}
		return null;
	}

	public static ClassAndLocationReportsAction salesLocationSummary() {
		return new ClassAndLocationReportsAction(TYPE_SALES_LOCATION_SUMMARY);
	}

	public static ClassAndLocationReportsAction salesLocationDetail() {
		return new ClassAndLocationReportsAction(TYPE_SALES_LOCATION_DETAIL);
	}

	public static ClassAndLocationReportsAction salesClassSummary() {
		return new ClassAndLocationReportsAction(TYPE_SALES_CLASS_SUMMARY);
	}

	public static ClassAndLocationReportsAction salesClassDetail() {
		return new ClassAndLocationReportsAction(TYPE_SALES_CLASS_DETAIL);
	}

	public static ClassAndLocationReportsAction purchaseLocationSummary() {
		return new ClassAndLocationReportsAction(TYPE_PURCHASE_LOCATION_SUMMARY);
	}

	public static ClassAndLocationReportsAction purchaseLocationDetail() {
		return new ClassAndLocationReportsAction(TYPE_PURCHASE_LOCATION_DETAIL);
	}

	public static ClassAndLocationReportsAction purchaseClassSummary() {
		return new ClassAndLocationReportsAction(TYPE_PURCHASE_CLASS_SUMMARY);
	}

	public static ClassAndLocationReportsAction purchaseClassDetail() {
		return new ClassAndLocationReportsAction(TYPE_PURCHASE_CLASS_DETAIL);
	}

	public static ClassAndLocationReportsAction profitAndLossLocation() {
		return new ClassAndLocationReportsAction(TYPE_PROFIT_AND_LOSS_LOCATION);
	}

	public static ClassAndLocationReportsAction profitAndLossClass() {
		return new ClassAndLocationReportsAction(TYPE_PROFIT_AND_LOSS_CLASS);
	}

	public static ClassAndLocationReportsAction getReport(boolean location,
			boolean sales) {
		if (sales) {
			if (location) {
				return salesLocationDetail();
			} else {
				return salesClassDetail();
			}
		} else {
			if (location) {
				return purchaseLocationDetail();
			} else {
				return purchaseClassDetail();
			}
		}
	}
}
