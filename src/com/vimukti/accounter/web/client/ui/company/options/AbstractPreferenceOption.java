/**
 * 
 */
package com.vimukti.accounter.web.client.ui.company.options;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.externalization.AccounterMessages2;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;

/**
 * @author Prasanna Kumar G
 * 
 */
public abstract class AbstractPreferenceOption extends StyledPanel {

	public AbstractPreferenceOption(String styleName) {
		super("companyInfoPanel");
	}

	@Override
	public void onLoad() {
		super.onLoad();

	}

	protected static AccounterMessages messages = Global.get().messages();

	protected static AccounterMessages2 messages2 = Global.get().messages2();

	public abstract String getTitle();

	public abstract void onSave();

	public abstract String getAnchor();

	public abstract void createControls();

	public abstract void initData();

	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	protected ClientCompanyPreferences getCompanyPreferences() {
		return getCompany().getPreferences();
	}

	public boolean isValidate() {
		return true;
	}

}
