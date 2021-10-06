package com.nitya.accounter.web.client.ui.core;

import java.util.List;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.Lists.InvoicesList;
import com.nitya.accounter.web.client.ui.customers.BrandingThemeComboDialog;

public class BrandingThemeComboAction extends Action {

	public BrandingThemeComboAction() {
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
		if (listData != null) {
			runAsync(listData);
		} else {
			runAsync(data, isDependent);
		}

	}

	private void runAsync(List<InvoicesList> listData) {

		BrandingThemeComboDialog comboDialog = new BrandingThemeComboDialog(
				messages.selectThemes(), "", listData);
		ViewManager.getInstance().showDialog(comboDialog);

	}

	private void runAsync(Object data, Boolean isDependent) {
		BrandingThemeComboDialog comboDialog = new BrandingThemeComboDialog(
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
