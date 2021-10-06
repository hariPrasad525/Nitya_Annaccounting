package com.vimukti.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class CopyThemeAction extends Action {

	public CopyThemeAction() {
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

	// @SuppressWarnings("rawtypes")
	// @Override
	// public ParentCanvas getView() {
	// return null;
	// }

	@Override
	public void run() {
		try {
			CopyThemeDialog copyThemeDialog = new CopyThemeDialog(
					messages.copyTheme(), "", (ClientBrandingTheme) data);
			ViewManager.getInstance().showDialog(copyThemeDialog);
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
		return "copy-theme";
	}

	@Override
	public String getText() {
		// TODO Auto-generated method stub
		return messages.copyTheme();
	}

}
