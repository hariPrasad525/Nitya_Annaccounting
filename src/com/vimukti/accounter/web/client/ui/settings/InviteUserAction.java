package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;

public class InviteUserAction extends Action {

	

	public InviteUserAction() {
		super();
		this.catagory = messages.settings() + " > "
				+ messages.users();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO.
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO.
	// return null;
	// }

	@Override
	public void run() {
		try {
			InviteUserView view = new InviteUserView();
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, InviteUserAction.this);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getHistoryToken() {
		return "inviteUser";
	}

	@Override
	public String getHelpToken() {
		return "add-user";
	}

	@Override
	public String getText() {
		return messages.inviteUser();
	}

}
