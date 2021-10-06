package com.vimukti.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author kumar
 * 
 */

public class ReceiveVATAction extends Action {

	public ReceiveVATAction() {
		super();
		this.catagory = messages.tax();
	}

	@Override
	public ImageResource getBigImage() {
		// its not using any where return null;
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// its not using any where return null;
		return null;
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // its not using any where return null;
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);

	}

	private void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ReceiveVATView view = GWT.create(ReceiveVATView.class);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ReceiveVATAction.this);
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
		//
		//
		// }
		//
		// });
	}

	// @Override
	// public String getImageUrl() {
	// // its not using any where return null;
	// return "";
	// }

	@Override
	public String getHistoryToken() {
		return "taxRefund";
	}

	@Override
	public String getHelpToken() {
		return "tax-refund";
	}

	@Override
	public String getText() {
		return messages.tAXRefund();
	}

}