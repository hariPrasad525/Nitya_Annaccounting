package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;

public class IntegrateWithBusinessContactManagerAction extends Action {

	public IntegrateWithBusinessContactManagerAction() {
		super();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "integrateWithBusinessContactManager";
	}

	@Override
	public String getHelpToken() {
		return "integrate-business-contact-manager";
	}

	@Override
	public String getText() {
		return messages.integrateWithBusinessContactManager();
	}

}
