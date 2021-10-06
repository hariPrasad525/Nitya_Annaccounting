package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.company.SalesPersonListView;
import com.nitya.accounter.web.client.ui.core.Action;

public class SalesPersonAction extends Action {


	public SalesPersonAction() {
		super();
		this.catagory = messages.company();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	public void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				SalesPersonListView view = new SalesPersonListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, SalesPersonAction.this);
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
		return Accounter.getFinanceMenuImages().customers();
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/customers.png";
	// }

	@Override
	public String getHistoryToken() {
		return "salesPersons";
	}

	@Override
	public String getHelpToken() {
		return "sales-person-list";
	}

	@Override
	public String getText() {
		return messages.salesPersons();
	}
}
