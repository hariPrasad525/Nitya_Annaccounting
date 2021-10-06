package com.vimukti.accounter.web.client.ui.reports;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Lingarao.R
 * 
 */
public class InventoryDetailsAction extends Action {
	public InventoryDetailsAction() {
		super();
		this.catagory = messages.report();
	}

	@Override
	public String getText() {
		return messages.inventoryDetails();
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(final Object data, final Boolean isDependent) {
		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				InventoryDetailsReport inventoryDetailsReport = new InventoryDetailsReport();
				MainFinanceWindow.getViewManager().showView(
						inventoryDetailsReport, data, isDependent,
						InventoryDetailsAction.this);
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "InventoryDetails";
	}

	@Override
	public String getHelpToken() {
		return "Inventory-details";
	}

}
