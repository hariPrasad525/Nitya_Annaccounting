package com.vimukti.accounter.web.client.uibinder.setup;

import com.google.gwt.user.client.ui.Composite;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;

public abstract class AbstractSetupPage extends Composite {

	protected static ClientCompanyPreferences preferences;
	protected AccounterMessages messages = Global.get().messages();
	protected AccounterMessages2 messages2 = Global.get().messages2();
	
	private static String country;
	protected static boolean isSkip;
	protected static boolean isPayrollSetup;

	public AbstractSetupPage(ClientCompanyPreferences preferences) {
		AbstractSetupPage.preferences = preferences;
	}

	public static void setPreferences(ClientCompanyPreferences preferences) {
		AbstractSetupPage.preferences = preferences;
	}

	public AbstractSetupPage() {
		if (preferences == null)
			preferences = new ClientCompanyPreferences();
	}

	protected abstract void createControls();

	protected abstract void onSave();

	protected abstract void onLoad();

	protected abstract boolean validate();

	public boolean canShow() {
		return (!isSkip && !isPayrollSetup);
	}

	protected void setCountry(String country) {
		AbstractSetupPage.country = country;
	}

	public static String getCountry() {
		return country;
	}

	public abstract String getViewName();

	public boolean isShowProgressPanel() {
		return true;
	}
}
