package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class ChangePasswordAction extends Action {
	private ChangePasswordDialog view;

	public ChangePasswordAction() {
		super();
	}

	@Override
	public ImageResource getBigImage() {
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return null;
	}

	//
	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		runAsync(data, isDependent);
	}

	private void runAsync(Object data, Boolean isDependent) {
		view = new ChangePasswordDialog(messages.changePassword(), null);
		ViewManager.getInstance().showDialog(view);
	}

	@Override
	public String getHistoryToken() {
		return "changePassword";
	}

	@Override
	public String getHelpToken() {
		return "change-password";
	}

	@Override
	public String getText() {
		return messages.changePassword();
	}

}
