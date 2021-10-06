package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class NewCustomerAction extends Action<ClientCustomer> {

	private String quickAddText;
	private boolean isEditable;

	public NewCustomerAction() {
		super();
		super.setToolTip(Global.get().Customer());
	}

	public NewCustomerAction(String quickAddText) {
		super();
		super.setToolTip(Global.get().Customer());
		this.quickAddText = quickAddText;

	}

	public NewCustomerAction(ClientCustomer customer,
			AccounterAsyncCallback<Object> callback) {
		super();
		this.catagory = Global.get().customer();
	}

	public void run() {
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				CustomerView view = GWT.create(CustomerView.class);
				if (quickAddText != null) {
					view.prepareForQuickAdd(quickAddText);
				}

				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewCustomerAction.this);
				if (isCustomerViewEditable()) {
					view.onEdit();
				}
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

	public boolean isCustomerViewEditable() {
		return isEditable;
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
		return Accounter.getFinanceMenuImages().newCustomer();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_customer.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newCustomer";
	}

	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	@Override
	public String getHelpToken() {
		return "add-customer";
	}

	public void setCustomerName(String text) {
		this.quickAddText = text;
	}

	public void setisCustomerViewEditable(boolean isEditable) {
		this.isEditable = isEditable;
	}

	@Override
	public String getText() {
		return messages.newPayee(Global.get().Customer());
	}

}
