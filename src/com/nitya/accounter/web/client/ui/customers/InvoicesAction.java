package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class InvoicesAction extends Action {

	public String viewType;

	public InvoicesAction() {
		super();
	}

	public InvoicesAction(String viewType) {
		super();
		this.viewType = viewType;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void run(Object data, Boolean isDependent, String viewType) {
		this.viewType = viewType;
		runAsync(data, isDependent);
	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				InvoiceListView view;
				if (viewType == null)
					view = new InvoiceListView();
				else
					view = new InvoiceListView(viewType);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, InvoicesAction.this);
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
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().invoices();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/invoices.png";
	// }
	@Override
	public String getCatagory() {
		return Global.get().Customer();
	}

	@Override
	public String getHistoryToken() {
		if (viewType == null) {
			return HistoryTokens.INVOICES;
		} else if (viewType.equals(messages.overDue())) {
			return messages.overDueInvoices();
		}
		return "";
	}

	@Override
	public String getHelpToken() {
		return "about-invoice";
	}

	@Override
	public String getText() {
		return messages.invoices();
	}
}
