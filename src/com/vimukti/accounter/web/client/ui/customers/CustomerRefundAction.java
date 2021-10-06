package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomerRefund;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class CustomerRefundAction extends Action {


	public CustomerRefundAction() {
		super();
	}

	public CustomerRefundAction(ClientCustomerRefund customerRefund,
			AccounterAsyncCallback<Object> callBack) {
		super();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isEditable) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				CustomerRefundView view = GWT.create(CustomerRefundView.class);
				MainFinanceWindow.getViewManager().showView(view, data,
						isEditable, CustomerRefundAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
//		AccounterAsync.createAsync(new CreateViewAsyncCallback() {
//
//			@Override
//			public void onCreated() {
//				
//
//			}
//
//		});
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newCustomerRefund();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/customer_refunds.png";
	// }

	@Override
	public String getHistoryToken() {
		return "customerRefund";
	}

	@Override
	public String getHelpToken() {
		return "customer-refund";
	}

	@Override
	public String getText() {
		return messages.customerRefund(Global.get().Customer());
	}
}
