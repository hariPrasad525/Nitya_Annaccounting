package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

/**
 * 
 * @author Raj Vimal
 */

public class RecordExpensesAction extends Action {

	public RecordExpensesAction() {
		super();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	@Override
	public void run() {

		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				SelectExpenseType dialog = new SelectExpenseType();
				ViewManager.getInstance().showDialog(dialog);
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
		//
		// }
		//
		// });
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().recordExpenses();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/record_expenses.png";
	// }

	@Override
	public String getHistoryToken() {

		return "recordExpenses";
	}

	@Override
	public String getHelpToken() {
		return "add-expense";
	}

	@Override
	public String getText() {
		return messages.recordExpenses();
	}

}
