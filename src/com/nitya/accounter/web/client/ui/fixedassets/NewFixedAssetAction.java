package com.nitya.accounter.web.client.ui.fixedassets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class NewFixedAssetAction extends Action {


	public NewFixedAssetAction() {
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
		return Accounter.getFinanceMenuImages().newFixedAsset();
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
				NewFixedAssetView view = new NewFixedAssetView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewFixedAssetAction.this);
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
//			public void onCreateFailed(Throwable t) {
//				// //UIUtils.logError("Failed to Load Vendor View..", t);
//			}
//		});

	}

	// @Override
	// public String getImageUrl() {
	// return "/images/New Fixed Asset.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newFixedAsset";
	}

	@Override
	public String getHelpToken() {
		return "new-fixed-assets";
	}

	@Override
	public String getText() {
		return messages.newFixedAsset();
	}

}
