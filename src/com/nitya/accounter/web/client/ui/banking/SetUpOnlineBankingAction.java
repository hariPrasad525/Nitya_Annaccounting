package com.nitya.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.ui.core.Action;

public class SetUpOnlineBankingAction extends Action {

	public SetUpOnlineBankingAction() {
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
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "setUpOnlineBanking";
	}

	@Override
	public String getHelpToken() {
		return "online-banking";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

}
