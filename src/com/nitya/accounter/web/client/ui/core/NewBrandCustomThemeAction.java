package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientBrandingTheme;
import com.nitya.accounter.web.client.ui.NewCustomThemeDialog;

public class NewBrandCustomThemeAction extends Action {

	private NewCustomThemeDialog customThemeDialog;
	private boolean isUsersActivityList;

	public NewBrandCustomThemeAction() {
		super();

	}

	@Override
	public void run() {
		if (isDependent) {
			customThemeDialog = new NewCustomThemeDialog(
					messages.editBrandingThemeLabel(),
					(ClientBrandingTheme) data, isDependent);
		} else {
			customThemeDialog = new NewCustomThemeDialog(
					messages.newBrandTheme(), (ClientBrandingTheme) data,
					isDependent);
		}
		customThemeDialog.setUsersActivityList(isUsersActivityList());
		if (getCallback() != null) {
			customThemeDialog.setCallback(getCallback());
		}
		ViewManager.getInstance().showDialog(customThemeDialog);

	}

	private boolean isUsersActivityList() {
		return isUsersActivityList;
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
		return "Custom theme";
	}

	@Override
	public String getHelpToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getText() {
		return messages.customTheme();
	}

	public void setUsersActivityList(boolean isUsersActivityList) {
		this.isUsersActivityList = isUsersActivityList;
	}

}
