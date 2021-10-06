package com.nitya.accounter.web.client.ui.settings;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.core.ClientBrandingTheme;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;

/**
 * 
 * @author Uday Kumar
 * 
 */
public class NewBrandThemeAction extends Action {

	private NewBrandingThemeView view;

	public NewBrandThemeAction() {
		super();
		this.catagory = messages.settings();
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
		runAysnc(data, isDependent);
	}

	private void runAysnc(Object data, Boolean isDependent) {
		try {
			if (data == null) {
				view = new NewBrandingThemeView(messages.brandingTheme(), "");
			} else {
				view = new NewBrandingThemeView(messages.editBrandThemeLabel(),
						"", (ClientBrandingTheme) data);

			}
			MainFinanceWindow.getViewManager().showView(view, data,
					isDependent, this);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	public String getHistoryToken() {
		return "newBrandingTheme";
	}

	@Override
	public String getHelpToken() {
		return null;
	}

	@Override
	public String getText() {
		return messages.newBrandTheme();
	}
}
