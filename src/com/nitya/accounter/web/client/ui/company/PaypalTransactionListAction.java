package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.AccounterAsync;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.CreateViewAsyncCallback;

/**
 * 
 * @author Amrit Mishra
 */

public class PaypalTransactionListAction extends Action {

	protected PaypalTransactionListView view;

	public PaypalTransactionListAction() {
		super();
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			public void onCreated() {

				view = PaypalTransactionListView.getInstance();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, PaypalTransactionListAction.this);
			}
		});
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
		return "paypaltransactions";
	}

	@Override
	public String getHelpToken() {
		return HistoryTokens.PAYPALTRANSACTIONS;
	}

	@Override
	public String getText() {
		return "Paypal Transactions";
	}
}
