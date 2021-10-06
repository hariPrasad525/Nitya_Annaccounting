package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.AccounterAsync;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class CustomerTransactionHistoryListAction  extends Action{

	private CustomerTransactionHistoryListView view;
	private ClientCustomer selected;

	public  CustomerTransactionHistoryListAction(ClientCustomer selectedCustomer) {
		super();
		selected = selectedCustomer;
	}


	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		AccounterAsync.createAsync(new CreateViewAsyncCallback() {

			@Override
			public void onCreated() {
				view = new CustomerTransactionHistoryListView();
				view.setSelectedCustomer(selected);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CustomerTransactionHistoryListAction.this);

			}

		});
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
		return "customerTransactionHistoryListAction";
	}

	@Override
	public String getHelpToken() {
		return null;
	}

	@Override
	public String getText() {
		return null;
	}



}
