package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientRecurringTransaction;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.RecurringTransactionsListView;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * Not completed.
 * 
 * @author vimukti3
 * 
 */
public class RecurringsListAction extends Action<ClientRecurringTransaction> {

	public RecurringsListAction() {
		super();
		this.catagory = messages.company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				RecurringTransactionsListView view = new RecurringTransactionsListView();

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, RecurringsListAction.this);

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
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "recurringTransactions";
	}

	@Override
	public String getHelpToken() {
		return "recurring-list";
	}

	@Override
	public String getText() {
		return "Recurring Transactions List";
	}
}
