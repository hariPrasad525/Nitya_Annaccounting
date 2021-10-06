package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCustomerCreditMemo;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author kumar kasimala
 */

public class NewCreditsAndRefundsAction extends Action {

	public NewCreditsAndRefundsAction() {
		super();
	}

	public NewCreditsAndRefundsAction(ClientCustomerCreditMemo creditMemo,
			AccounterAsyncCallback<Object> callback) {
		super();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				CustomerCreditMemoView view = GWT
						.create(CustomerCreditMemoView.class);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewCreditsAndRefundsAction.this);
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

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCreditsAndRefunds();
	}

	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_credit_and_refunds.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newCreditNote";
	}

	@Override
	public String getHelpToken() {
		return "new-credit_refund";
	}

	@Override
	public String getText() {
		return messages.newCreditNotes();
	}
}
