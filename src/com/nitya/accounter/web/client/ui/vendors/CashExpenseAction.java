package com.nitya.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class CashExpenseAction extends Action {

	public CashExpenseAction() {
		super();
	}

	public CashExpenseAction(ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		super();
	}

	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				CashExpenseView view = GWT.create(CashExpenseView.class);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, CashExpenseAction.this);
				// UIUtils.setCanvas(view, getViewConfiguration());

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
		// AccounterAsync.createAsync(new CreateViewAsyncCallback() {
		//
		// public void onCreated() {
		//
		//
		// }
		//
		// });

	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCashPurchage();
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return this.view;
	// }

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_cash_purchase.png";
	// }

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	@Override
	public String getHistoryToken() {
		return "cashExpense";
	}

	@Override
	public String getHelpToken() {
		return "cash-expense";
	}

	@Override
	public String getText() {
		return messages.cashExpense();
	}
}
