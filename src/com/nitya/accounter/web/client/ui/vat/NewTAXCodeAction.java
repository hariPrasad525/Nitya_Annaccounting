package com.nitya.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * @author Murali.A
 * 
 */
public class NewTAXCodeAction extends Action<ClientTAXCode> {

	private String taxCodeName;

	public NewTAXCodeAction() {
		super();
		this.catagory = messages.tax();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().salesTaxCode();
	}

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				NewTAXCodeView view = new NewTAXCodeView();
				view.setTaxCodeName(taxCodeName);
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, NewTAXCodeAction.this);
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
//			}
//		});
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/Sales_Tax.png";
	// }

	@Override
	public String getHistoryToken() {
		return "newVatCode";
	}

	@Override
	public String getHelpToken() {
		return "new-tax-code";
	}

	public void setTaxCodeName(String text) {
		this.taxCodeName = text;
	}

	@Override
	public String getText() {
		return messages.taxCode();
	}

}
