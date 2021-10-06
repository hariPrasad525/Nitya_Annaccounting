package com.nitya.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class TaxHistoryAction extends Action {

	public TaxHistoryAction() {
		super();
		catagory = messages.tax();
	}

	@Override
	public void run() {

		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				TaxHistoryView view = new TaxHistoryView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, TaxHistoryAction.this);
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
		//
		// }
		//
		// public void onCreateFailed(Throwable t) {
		// // //UIUtils.logError("Failed to Load Vendor list", t);
		// }
		// });
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "taxHistory";
	}

	@Override
	public String getHelpToken() {
		return "tax-History";
	}

	@Override
	public String getText() {
		return messages.taxHistory();
	}

}
