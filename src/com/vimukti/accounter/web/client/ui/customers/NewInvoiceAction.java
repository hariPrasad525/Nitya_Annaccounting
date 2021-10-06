package com.vimukti.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientInvoice;
import com.vimukti.accounter.web.client.core.ConsultantsDetailsList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class NewInvoiceAction extends Action<ClientInvoice> {

	public NewInvoiceAction() {
		super();
	}

	public NewInvoiceAction(ClientInvoice invoice,
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
				InvoiceView view = GWT.create(InvoiceView.class);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewInvoiceAction.this);
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
		// });
	}
	public void runNewInvoiceView(ConsultantsDetailsList selectedCustomer)
	{
		runNewAsync(data, isDependent, selectedCustomer);
	}
	
	
	private void runNewAsync(final Object data, boolean isDependent, ConsultantsDetailsList selectedCustomer) {
		// TODO Auto-generated method stub
		GWT.runAsync(new RunAsyncCallback() {
			
			@Override
			public void onSuccess() {
				// TODO Auto-generated method stub
				InvoiceView view = GWT.create(InvoiceView.class);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewInvoiceAction.this);
				view.selectedConsultantValue(selectedCustomer);
				
			}
			
			@Override
			public void onFailure(Throwable reason) {
				// TODO Auto-generated method stub
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
	}

	
	
	// @Override
	// public ParentCanvas<?> getView() {
	// // NOTHING TO DO.
	// return null;
	// }
	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().newInvoice();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/new_invoice.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newInvoice";
	}

	@Override
	public String getHelpToken() {
		return "customer-invoice";
	}

	@Override
	public String getText() {
		return messages.newInvoice();
	}
}
