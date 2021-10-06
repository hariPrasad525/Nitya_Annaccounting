package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCashSales;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author kumar kasimala
 */

public class NewCashSaleAction extends Action {

	public NewCashSaleAction() {
		super();
	}

	public NewCashSaleAction(ClientCashSales cashSales,
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
				CashSalesView view = GWT.create(CashSalesView.class);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewCashSaleAction.this);

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
		// }
		//
		// });
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCashSale();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_cash_sale.png";
	// }
	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	@Override
	public String getHistoryToken() {
		return "newCashSale";
	}

	@Override
	public String getHelpToken() {
		return "new_cash-sale";
	}

	@Override
	public String getText() {
		return messages.newCashSale();
	}
}
