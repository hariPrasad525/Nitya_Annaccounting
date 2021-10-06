package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.Action;

public class ViewSalesTaxLiabilityAction extends Action {

	public ViewSalesTaxLiabilityAction() {
		super();

	}

	@Override
	public void run() {

	}

	public ImageResource getBigImage() {
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {

		return null;
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}
}
