package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.vat.ChalanDetailsListView;

public class ChalanListViewAction extends Action {

	public ChalanListViewAction() {
		super();
		this.catagory = messages.taxList();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ChalanDetailsListView view = new ChalanDetailsListView(false);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ChalanListViewAction.this);

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

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return HistoryTokens.CHALANDETAILSLIST;
	}

	@Override
	public String getHelpToken() {
		return "chalanDetailsList";
	}

	@Override
	public String getText() {
		return messages.challanListView();
	}
}
