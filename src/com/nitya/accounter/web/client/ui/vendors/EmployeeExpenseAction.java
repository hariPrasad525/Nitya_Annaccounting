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

public class EmployeeExpenseAction extends Action {


	public EmployeeExpenseAction() {
		super();
	}

	public EmployeeExpenseAction(ClientVendor vendor,
			AccounterAsyncCallback<Object> callback) {
		super();
	}

	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				EmployeeExpenseView view = new EmployeeExpenseView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, EmployeeExpenseAction.this);
				// UIUtils.setCanvas(view, getViewConfiguration());
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
//
//			}
//
//			public void onCreateFailed(Throwable t) {
//				// //UIUtils.logError("Failed to Load Customer View..", t);
//			}
//		});

	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCashPurchage();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_cash_purchase.png";
	// }

	@Override
	public String getHistoryToken() {
		return "employeeExpense";
	}

	@Override
	public String getHelpToken() {
		return "employee-expense";
	}

	@Override
	public String getText() {
		return messages.employeeExpense();
	}

}
