package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class PayTypeListAction extends Action {

	public PayTypeListAction() {
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
		return "payTypeList";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		return messages.payTypeList();
	}

}
