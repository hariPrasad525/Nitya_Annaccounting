package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class UserDetailsAction extends Action {

	public UserDetailsAction() {
		super();
	}

	// its not using any where
	@Override
	public ImageResource getBigImage() {
		return null;
	}

	// its not using any where
	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	// its not using any where
	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		UserDetailsView view = new UserDetailsView();
		try {
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, this);
		} catch (Exception e) {
		}
	}

	@Override
	public String getHistoryToken() {
		return "userDetails";
	}

	@Override
	public String getHelpToken() {
		return "user-details";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return messages.userDetails();
	}

}
