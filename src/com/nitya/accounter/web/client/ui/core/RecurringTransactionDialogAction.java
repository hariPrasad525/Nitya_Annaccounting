package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientRecurringTransaction;
import com.nitya.accounter.web.client.ui.customers.RecurringTransactionDialog;

public class RecurringTransactionDialogAction extends
		Action<ClientRecurringTransaction> {

	public RecurringTransactionDialogAction() {
		super();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	public void run() {

		runAsync(data, isDependent);

	}

	private void runAsync(ClientRecurringTransaction data, Boolean isDependent) {

		RecurringTransactionDialog comboDialog = new RecurringTransactionDialog(
				null, data);

		ViewManager.getInstance().showDialog(comboDialog);
	}

	@Override
	public String getHistoryToken() {
		return "recurringTransaction";
	}

	@Override
	public String getHelpToken() {
		return "recurring-Transaction";
	}

	@Override
	public String getText() {
		return messages.recurring();
	}

}
