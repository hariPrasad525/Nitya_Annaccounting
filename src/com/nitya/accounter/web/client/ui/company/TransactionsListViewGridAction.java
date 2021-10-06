package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.AccounterAsync;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.CreateViewAsyncCallback;

public class TransactionsListViewGridAction extends Action {

	private TransactionListGrids view;
	private String type;

	public TransactionsListViewGridAction(String type) {
		this.type = type;
	}

	@Override
	public String getText() {
		return "Transactions List";
	}

	@Override
	public void run() {
		runAsync(isDependent, data);
	}

	private void runAsync(final boolean isDependent, final Object data) {

		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
			@Override
			public void onCreated() {
				view = new TransactionListGrids();
				view.setSelectedView(type);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, TransactionsListViewGridAction.this);
			}
		});

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
		return "import";
	}

	@Override
	public String getHelpToken() {
		return "import";
	}

}
