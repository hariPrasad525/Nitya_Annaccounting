package com.nitya.accounter.web.client.ui.customers;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class NewTimeEntryAction extends Action {

	public NewTimeEntryAction() {
		super();
	}

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
		return "newTimeEntry";
	}

	@Override
	public String getCatagory() {
		return Global.get().Customer();
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
