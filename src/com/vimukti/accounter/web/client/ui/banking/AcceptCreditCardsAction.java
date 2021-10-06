package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ui.core.Action;

public class AcceptCreditCardsAction extends Action {

	public AcceptCreditCardsAction() {
		super();
		this.catagory = messages.banking();
	}

	// @Override
	// public void run() {
	//
	// }

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
		// TODO Auto-generated method stub
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
		return "acceptCreditCards";
	}

	@Override
	public String getHelpToken() {
		return "acceptcreditcards";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

}
