package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class PurchaseOrderAction extends Action {

	public PurchaseOrderAction() {
		super();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				PurchaseOrderView view = GWT.create(PurchaseOrderView.class);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, PurchaseOrderAction.this);
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
		// // / UIUtils.setCanvas(view, getViewConfiguration());
		//
		// }
		//
		// public void onCreateFailed(Throwable t) {
		// // //UIUtils.logError("Failed to Load Customer list...", t);
		// }
		// });

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().purchaseOrder();
	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Purchase-order.png";
	// }

	@Override
	public String getHistoryToken() {

		return "purchaseOrder";
	}

	@Override
	public String getHelpToken() {
		return "purchase-order";
	}

	@Override
	public String getText() {
		return messages.purchaseOrder();
	}

}
