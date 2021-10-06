package com.nitya.accounter.web.client.ui.fixedassets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class SellingRegisteredItemAction extends Action {

	public SellingRegisteredItemAction() {
		super();
		this.catagory = messages.fixedAssets();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				SellingRegisteredItemView view = new SellingRegisteredItemView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, SellingRegisteredItemAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			public void onCreated() {
//
//				
//
//			}
//
//		});

	}

	@Override
	public String getHistoryToken() {
		return "SellingRequesteredItem ";
	}

	@Override
	public String getHelpToken() {
		return "selling-registered-item";
	}

	@Override
	public String getText() {
		return messages.sellingRegisteredItem();
	}
}
