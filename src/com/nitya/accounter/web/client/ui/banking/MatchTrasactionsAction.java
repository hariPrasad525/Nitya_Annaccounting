package com.nitya.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.ui.core.Action;

public class MatchTrasactionsAction extends Action {

	public MatchTrasactionsAction() {
		super();
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AccounterAsyncCallback<Object> AccounterAsyncCallback) {
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "matchTransactions";
	}

	@Override
	public String getHelpToken() {
		return "match-transactions";
	}

	@Override
	public String getText() {
		return messages.matchTrasactions();
	}

}
