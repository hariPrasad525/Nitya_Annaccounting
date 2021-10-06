package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class TAXAgencyListAction extends Action {

	public TAXAgencyListAction() {
		super();
		this.catagory = messages.tax();
	}

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				TAXAgencyListView view = new TAXAgencyListView();
				MainFinanceWindow.getViewManager().showView(view, null, false,
						TAXAgencyListAction.this);

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
		// }
		// });

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().vendors();
	}

	@Override
	public String getHistoryToken() {

		return "TAXAgencyList";
	}

	@Override
	public String getHelpToken() {
		return "taxAgency-list";
	}

	@Override
	public String getText() {
		return messages.payeeList(messages.taxAgencies());
	}

}
