package com.nitya.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class SalesByLocationSummaryAction extends Action {

	private boolean isLocation;
	private boolean isCustomer;

	public SalesByLocationSummaryAction(boolean isLocation, boolean isCustomer) {
		super();
		this.isLocation = isLocation;
		this.isCustomer = isCustomer;

	}

	@Override
	public void run() {
		final boolean isLocation = this.isLocation;
		final boolean isCustomer = this.isCustomer;
		runAsync(data, isDependent, isLocation, isCustomer);

	}

	private void runAsync(final Object data, final boolean isDependent,
			final boolean isLocation, final boolean isCustomer) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				SalesByLocationsummaryReport report = new SalesByLocationsummaryReport(
						isLocation, isCustomer);
				MainFinanceWindow.getViewManager().showView(report, data,
						isDependent, SalesByLocationSummaryAction.this);

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// public void onCreated() {
		//
		//
		// }
		//
		// public void onCreateFailed(Throwable t) {
		// // UIUtils.logError("Failed to Load Report...", t);
		// }
		// });

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getHistoryToken() {
		if (isCustomer) {
			if (isLocation) {
				return "salesByLocationSummary";
			} else {
				return "salesByClassSummary";
			}
		} else {
			if (isLocation) {
				return "PurchasesByLocationSummary";
			} else {
				return "PurchasesbyClassSummary";
			}
		}
	}

	@Override
	public String getHelpToken() {
		if (isCustomer) {
			if (isLocation) {
				return "sales-by-location-summary";
			} else {
				return "sales-by-class-summary";
			}
		} else {
			if (isLocation) {
				return "Purchases-by-location-summary";
			} else {
				return "Purchases-by-class-summary";
			}
		}
	}

	@Override
	public String getText() {
		String actionsting = null;
		if (isCustomer) {
			if (isLocation) {
				actionsting = messages.salesByLocationSummary(Global.get()
						.Location());
			} else {
				actionsting = messages.salesByClassSummary();
			}
		} else {
			if (isLocation) {
				// actionsting =
				// messages.purchasesbyLocationSummary(Global.get()
				// .Location());
			} else {
				actionsting = messages.purchasesbyClassSummary();
			}
		}
		return actionsting;
	}
}
