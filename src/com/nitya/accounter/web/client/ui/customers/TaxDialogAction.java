package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.ui.core.Action;

public class TaxDialogAction extends Action<ClientTAXCode> {
	public TaxDialogAction() {
		super();
	}

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// return "/images/record_expenses.png";
	// }

	@Override
	public String getHistoryToken() {
		return "TaxDialog";
	}

	@Override
	public String getHelpToken() {
		return "tax-dialog";
	}

	@Override
	public String getText() {
		return messages.tax();
	}
}
