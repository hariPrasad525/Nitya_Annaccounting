/**
 * 
 */
package com.nitya.accounter.web.client.ui.vat;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * @author Murali.A
 * 
 */
public class ManageTAXCodesListAction extends Action {

	/**
	 * @param text
	 */
	public ManageTAXCodesListAction() {
		super();
		this.catagory = messages.tax();
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.core.Action#getBigImage()
	 */
	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.core.Action#getSmallImage()
	 */
	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().manageSalesTaxGroup();
	}

	/*
	 * @see com.nitya.accounter.web.client.ui.core.Action#getView()
	 */

	// @Override
	// public ParentCanvas getView() {
	// return this.view;
	// }

	/*
	 * @see
	 * com.nitya.accounter.web.client.ui.core.Action#run(java.lang.Object,
	 * java.lang.Boolean)
	 */
	@Override
	public void run() {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				ManageTAXCodesListView view = new ManageTAXCodesListView();
				MainFinanceWindow.getViewManager().showView(view, data,
						isDependent, ManageTAXCodesListAction.this);
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
	// return "/images/Manage_Sales_Tax_Group.png";
	// }

	@Override
	public String getHistoryToken() {
		return HistoryTokens.VATCODES;
	}

	@Override
	public String getHelpToken() {
		return "manage-tax-code";
	}

	@Override
	public String getText() {
		return messages.taxCodesList();
	}
}
