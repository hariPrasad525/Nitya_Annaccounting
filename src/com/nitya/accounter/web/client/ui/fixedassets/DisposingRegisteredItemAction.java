package com.nitya.accounter.web.client.ui.fixedassets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class DisposingRegisteredItemAction extends Action {

	public DisposingRegisteredItemAction() {
		super();
		this.catagory = messages.fixedAssets();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				DisposingRegisteredItemView view = new DisposingRegisteredItemView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, DisposingRegisteredItemAction.this);

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
//			}
//
//		});

	}

	@Override
	public String getHistoryToken() {
		return "disposingRegisteredItem";
	}

	@Override
	public String getHelpToken() {
		return "disposing-register-item";
	}

	@Override
	public String getText() {
		return messages.disposingRegisteredItem();
	}

}
