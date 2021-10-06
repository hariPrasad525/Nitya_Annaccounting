package com.nitya.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.ui.core.Action;

public class EnterPaymentsAction extends Action {

	public EnterPaymentsAction() {
		super();
		this.catagory = messages.banking();
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */
	public void run(AccounterAsyncCallback<Object> AccounterAsyncCallback) {
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO
	// return null;
	// }

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "enterPayments";
	}

	@Override
	public String getHelpToken() {
		return "enterpayment";
	}

	@Override
	public String getText() {
		return messages.enterPayments();
	}

}
