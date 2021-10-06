package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class InvoiceBrandingAction extends Action {

	
	public InvoiceBrandingAction() {
		super();
		this.catagory = messages.settings();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		try {
			InvoiceBrandingView view = new InvoiceBrandingView();
			MainFinanceWindow.getViewManager().showView(view, data, false,
					InvoiceBrandingAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getHistoryToken() {
		return "invoiceBranding";
	}

	@Override
	public String getHelpToken() {
		return "new-branding-theme";
	}

	@Override
	public String getText() {
		return messages.invoiceBranding();
	}

}
