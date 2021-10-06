package com.nitya.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientBrandingTheme;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class DeleteThemeAction extends Action<ClientBrandingTheme> {

	public DeleteThemeAction() {
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

	@Override
	public void run() {
		try {
			DeleteThemeDialog deleteThemeDialog = new DeleteThemeDialog(
					messages.deleteThemeLabel(), "", (ClientBrandingTheme) data);
			ViewManager.getInstance().showDialog(deleteThemeDialog);
		} catch (Exception e) {
			System.out.println(e.toString());
		}
	}

	@Override
	public String getHistoryToken() {
		return null;
	}

	@Override
	public String getHelpToken() {
		return "delete-theme";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return messages.users();
	}
}
