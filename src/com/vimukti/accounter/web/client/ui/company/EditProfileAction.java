package com.vimukti.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class EditProfileAction extends Action {

	public EditProfileAction() {
		super();
	}

	@Override
	public void run() {
		runAsync(data);

	}

	private void runAsync(Object data) {
		EditProfileDialog view = new EditProfileDialog(messages.editProfile(),
				null);
		ViewManager.getInstance().showDialog(view);
	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		return "editProfile";
	}

	@Override
	public String getHelpToken() {
		return "edit-profile";
	}

	@Override
	public String getText() {
		return messages.editProfile();
	}

}
