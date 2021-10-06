package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Raj Vimal
 */

public class FormLayoutsListAction extends Action {

	public FormLayoutsListAction() {
		super();
	}

	// @Override
	// public ParentCanvas<?> getView() {
	// // NOTHING TO DO
	// return null;
	// }

	@Override
	public void run() {
		// TODO Auto-generated method stub

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	public ImageResource getSmallImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "formLayoutsList";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		// Noting to do
		return null;
	}

	@Override
	public String getText() {
		return messages.formLayoutsList();
	}

}
