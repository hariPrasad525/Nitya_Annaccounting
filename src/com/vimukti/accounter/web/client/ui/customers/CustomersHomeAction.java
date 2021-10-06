package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class CustomersHomeAction extends Action {

	

	public CustomersHomeAction() {
		super();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		CustomerSectionHomeView view = new CustomerSectionHomeView();
		try {
			MainFinanceWindow.getViewManager().showView(view, null, false,
					CustomersHomeAction.this);
		} catch (Exception e) {
			Accounter.showError(messages.failedToLoadCustomerHome(Global.get()
					.Customer()));
		}

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().customersHome();
	}

	// @Override
	// public String getImageUrl() {
	//
	// return "/images/customers_home.png";
	// }

	@Override
	public String getHistoryToken() {
		return "customerHome";
	}

	@Override
	public String getHelpToken() {
		return "customers";
	}

	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	@Override
	public String getText() {
		return messages.payeesHome(Global.get().Customers());
	}
}
