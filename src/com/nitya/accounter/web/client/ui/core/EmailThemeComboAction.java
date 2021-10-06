package com.nitya.accounter.web.client.ui.core;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.ui.customers.EmailThemeComboDialog;

public class EmailThemeComboAction extends Action {

	public EmailThemeComboAction() {
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

	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	public void run() {

		runAsync(data, isDependent);

	}

	private void runAsync(Object data, Boolean isDependent) {
		EmailThemeComboDialog comboDialog = new EmailThemeComboDialog(
				messages.selectThemes(), "", (ClientTransaction) data);
		ViewManager.getInstance().showDialog(comboDialog);
	}

	@Override
	public String getHistoryToken() {
		return "BrandingThemeCombo";
	}

	@Override
	public String getHelpToken() {
		return "new-branding-theme";
	}

	@Override
	public String getText() {
		return messages.brandingThemeCombo();
	}

}
