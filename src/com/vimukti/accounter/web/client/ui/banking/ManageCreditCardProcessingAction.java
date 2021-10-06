package com.vimukti.accounter.web.client.ui.banking;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;

public class ManageCreditCardProcessingAction extends Action {

	public ManageCreditCardProcessingAction() {
		super();
		this.catagory = messages.banking();
	}

	/**
	 * Runs this action with call back.The default implementation of this method
	 * in <code>Action</code> does nothing.
	 */

	// @Override
	// public ParentCanvas getView() {
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
		return "manageCreditCardProcess";
	}

	@Override
	public String getHelpToken() {
		return "managecreditcards";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}

}
