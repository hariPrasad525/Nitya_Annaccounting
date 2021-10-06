package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.ItemListView;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class ItemsAction extends Action {

	// public ItemsAction() {
	// super();
	// this.catagory = Global.get().customer();
	// }
	private String cat;

	public ItemsAction(String catageory) {
		super();
		this.cat = catageory;
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ItemListView view = new ItemListView();
				view.setCatageoryType(getCatagory());
				// if (getCatagory().equals(Global.get().vendor())) {
				// ItemListView.isPurchaseType = true;
				// ItemListView.isSalesType = false;
				// } else if (getCatagory().equals(Global.get().Customer())) {
				// ItemListView.isPurchaseType = false;
				// ItemListView.isSalesType = true;
				// } else if (getCatagory()
				// .equals(
				// messages.bothCustomerAndVendor(
				// Global.get().Customer(),
				// Global.get().Vendor()))) {
				// ItemListView.isPurchaseType = true;
				// ItemListView.isSalesType = true;
				// }
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ItemsAction.this);

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// @Override
		// public void onCreated() {
		//
		// }
		//
		// });
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().items();
	}

	@Override
	public String getHistoryToken() {
		// view.setCatageoryType(getCatagory());
		if (cat.equalsIgnoreCase(Global.get().vendor()))
			return "vendorItems";
		if (cat.equalsIgnoreCase(Global.get().customer()))
			return "customerItems";
		if (cat.equalsIgnoreCase(messages.bothCustomerAndVendor(Global.get()
				.Customer(), Global.get().Vendor())))
			return "allItems";
		else
			return "customerItems";

	}

	@Override
	public String getHelpToken() {
		if (cat.equalsIgnoreCase(Global.get().vendor()))
			return "vendorItems";
		if (cat.equalsIgnoreCase(Global.get().customer()))
			return "customerItems";
		if (cat.equalsIgnoreCase(messages.bothCustomerAndVendor(Global.get()
				.Customer(), Global.get().Vendor())))
			return "allItems";
		else
			return "customerItems";

	}

	@Override
	public String getCatagory() {
		return cat;
	}

	@Override
	public String getText() {
		return messages.items();
	}

}
