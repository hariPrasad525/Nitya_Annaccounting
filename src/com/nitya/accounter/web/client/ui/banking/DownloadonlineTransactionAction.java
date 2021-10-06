package com.nitya.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.ui.core.Action;

public class DownloadonlineTransactionAction extends Action {

	public DownloadonlineTransactionAction() {
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
		return "downloadonlineTransaction";
	}

	@Override
	public String getHelpToken() {
		return "downloadtransactions";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

}
