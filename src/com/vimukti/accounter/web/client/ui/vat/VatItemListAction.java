package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class VatItemListAction extends Action {


	public VatItemListAction() {
		super();
		this.catagory = messages.tax();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newVatItem();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				VatItemsListView view = new VatItemsListView();
				MainFinanceWindow.getViewManager().showView(view, null, false,
						VatItemListAction.this);
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
	//
	// return "/images/Vat_item.png";
	// }

	@Override
	public String getHistoryToken() {

		return "vatItems";
	}

	@Override
	public String getHelpToken() {
		return "vat-item-list";
	}

	@Override
	public String getText() {
		return messages.taxItemsList();
	}

}
