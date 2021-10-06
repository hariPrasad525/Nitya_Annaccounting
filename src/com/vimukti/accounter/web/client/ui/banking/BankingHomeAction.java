package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class BankingHomeAction extends Action {

	private BankingSectionHomeView view;

	public BankingHomeAction() {
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
	// return this.view;
	// }

	@Override
	public void run() {
		view = new BankingSectionHomeView();

		MainFinanceWindow.getViewManager().showView(view, null, false, this);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().bankingHome();
	}

	@Override
	public String getHistoryToken() {
		return "bankingHome";
	}

	@Override
	public String getHelpToken() {
		return "banking";
	}

	@Override
	public String getText() {
		return messages.bankingHome();
	}
}
