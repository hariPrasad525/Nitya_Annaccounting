package com.vimukti.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class CreditCardExpenseAction extends Action {


	public CreditCardExpenseAction() {
		super();
	}

	public CreditCardExpenseAction(ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		super();
	}

	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				CreditCardExpenseView view = new CreditCardExpenseView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CreditCardExpenseAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			public void onCreated() {
//
//			
//				// UIUtils.setCanvas(view, getViewConfiguration());
//
//			}
//
//			public void onCreateFailed(Throwable t) {
//				// //UIUtils.logError("Failed to Load Customer View..", t);
//			}
//		});

	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCreditCardCharge();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return this.view;
	// }
	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/credit_card_charge.png";
	// }

	@Override
	public String getHistoryToken() {
		return "creditCardExpense";
	}

	@Override
	public String getHelpToken() {
		return "creditcard-expense";
	}

	@Override
	public String getText() {
		return messages.creditCardCharge();
	}
}
