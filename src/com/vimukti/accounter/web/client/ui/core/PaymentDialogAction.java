package com.vimukti.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;

public class PaymentDialogAction extends Action {

	public PaymentDialogAction() {
		super();

	}

	@Override
	public ImageResource getBigImage() {

		return null;
	}

	@Override
	public ImageResource getSmallImage() {

		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	//
	// return null;
	// }

	@Override
	public void run() {
		PaymentDialog diaog = new PaymentDialog();
		ViewManager.getInstance().showDialog(diaog);
	}

	@Override
	public String getHistoryToken() {

		return null;
	}

	@Override
	public String getHelpToken() {
		return "payment-list";
	}

	@Override
	public String getText() {
		return messages.payments();
	}

}
