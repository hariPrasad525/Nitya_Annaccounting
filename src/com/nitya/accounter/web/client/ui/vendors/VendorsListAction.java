package com.nitya.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class VendorsListAction extends Action {

	public VendorsListAction() {
		super();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				MainFinanceWindow.getViewManager().showView(
						new VendorListView(), null, false,
						VendorsListAction.this);

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
		// // UIUtils.setCanvas(new VendorListView(),
		// // getViewConfiguration());
		//
		// }
		//
		// public void onCreateFailed(Throwable t) {
		// // //UIUtils.logError("Failed to Load Vendor list", t);
		// }
		// });

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vendors();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/vendors.png";
	// }

	@Override
	public String getHistoryToken() {

		return HistoryTokens.VENDORLIST;
	}

	@Override
	public String getHelpToken() {
		return "vendor-list";
	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	@Override
	public String getText() {
		return Global.get().messages().payees(Global.get().Vendors());
	}

}
