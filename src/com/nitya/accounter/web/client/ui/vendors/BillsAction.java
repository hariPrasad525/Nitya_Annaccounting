package com.nitya.accounter.web.client.ui.vendors;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Kumar kasimala
 * @modified by Ravi Kiran.G
 */

public class BillsAction extends Action {

	public String viewType;

	public BillsAction() {
		super();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				BillListView view;
				if (viewType == null)
					view = BillListView.getInstance();
				else
					view = new BillListView(viewType);

				// UIUtils.setCanvas(view, getViewConfiguration());
				MainFinanceWindow.getViewManager().showView(view, null, false,
						BillsAction.this);

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
//			}
//
//		});
	}

	public void run(Object data, Boolean isDependent, String viewType) {
		this.viewType = viewType;
		run(data, isDependent);
	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().bills();
	}

	// @Override
	// public String getImageUrl() {
	// return "images/bills.png";
	// }

	@Override
	public String getHistoryToken() {
		return "billsAndExpenses";
	}

	@Override
	public String getHelpToken() {
		return "bill-expense";
	}

	@Override
	public String getText() {
		return messages.billsAndExpenses();
	}

	@Override
	public String getCatagory() {
		return Global.get().Vendor();
	}

}
