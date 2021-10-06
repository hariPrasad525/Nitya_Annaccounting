package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.company.options.PreferenceSettingsView;
import com.nitya.accounter.web.client.ui.core.Action;

public class PreferencesAction extends Action {

	public int CATEGORY;
	public static final int COMPANY = 1;
	public static final int SETTINGS = 2;

	public PreferencesAction(int catagory) {
		super();
		this.CATEGORY = catagory;
		if (catagory == COMPANY)
			this.catagory = messages.company();
		else
			this.catagory = messages.settings();
	}

	@Override
	public void run() {

		try {
			PreferenceSettingsView page = new PreferenceSettingsView();
			MainFinanceWindow.getViewManager().showView(page, null, false,
					PreferencesAction.this);
		} catch (Exception e) {
			e.printStackTrace();
			Accounter.showError(messages.failedToLoadCompanyPreferences());
		}

	}

	public ImageResource getBigImage() {
		// NOTHING TO DO.
		return null;
	}

	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().preferences();
	}

	@Override
	public String getHistoryToken() {
		if (CATEGORY == COMPANY) {
			return "companyPreferences";
		} else {
			return "companyPreferencesFromSettings";
		}
	}

	@Override
	public String getHelpToken() {
		return "company-preferences";
	}

	@Override
	public String getText() {
		return messages.companyPreferences();
	}

}
