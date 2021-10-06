package com.nitya.accounter.web.client.ui.vendors;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class ServicesOverviewAction extends Action {

	public ServicesOverviewAction() {
		super();
	}

	// its not using any where

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	// @Override
	// public String getImageUrl() {
	// // NOTHING TO DO.
	// return "";
	// }

	@Override
	public String getHistoryToken() {
		return "ServicesIverview";
	}

	@Override
	public String getHelpToken() {
		return "service-overview";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return null;
	}
}
