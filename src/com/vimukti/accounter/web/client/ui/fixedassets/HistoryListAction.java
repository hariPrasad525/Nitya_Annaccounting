package com.vimukti.accounter.web.client.ui.fixedassets;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFixedAsset;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class HistoryListAction extends Action {

	public HistoryListAction() {
		super();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				HistoryListView view = new HistoryListView((ClientFixedAsset) data);
				MainFinanceWindow.getViewManager().showView(view, null,
						isDependent, HistoryListAction.this);
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
//				/*
//				 * From "ClientFixedAsset", in listview, we'll get the list of
//				 * "FixedAssetHistory" objects
//				 */
//				
//
//			}
//
//		});
	}

	@Override
	public String getHistoryToken() {
		return "historyList";
	}

	@Override
	public String getHelpToken() {
		return "history-list";
	}

	@Override
	public String getText() {
		return messages.history();
	}

}
