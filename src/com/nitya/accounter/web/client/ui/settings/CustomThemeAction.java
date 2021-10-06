package com.nitya.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class CustomThemeAction extends Action {

	public CustomThemeAction() {
		super();
	}

	@Override
	public ImageResource getBigImage() {
		// NOTHING TO DO
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// NOTHING TO DO
		return null;
	}

	// @Override
	// public ParentCanvas getView() {
	// // NOTHING TO DO
	// return null;
	// }

	@Override
	public void run() {
		try {
			CustomThemeDialog customThemeDialog = new CustomThemeDialog(
					messages.newBrandThemeLabel(), "");
			ViewManager.getInstance().showDialog(customThemeDialog);
		} catch (Exception e) {
			System.out.println(e.toString());
		}

	}

	@Override
	public String getHistoryToken() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHelpToken() {
		return "custom-theme";
	}

	@Override
	public String getText() {
		return messages.newBrandThemeLabel();
	}
}
