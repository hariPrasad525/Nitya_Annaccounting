package com.nitya.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

public class UsersAction extends Action {

	public UsersAction() {
		super();
		this.catagory = messages.settings();
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
	// return null;
	// }

	@Override
	public void run() {
		try {
			UsersView view = new UsersView();
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, UsersAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	@Override
	public String getHistoryToken() {

		return "users";
	}

	@Override
	public String getHelpToken() {
		return "user";
	}

	@Override
	public String getText() {
		return messages.users();
	}

}
