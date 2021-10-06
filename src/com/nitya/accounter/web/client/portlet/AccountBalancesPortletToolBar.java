package com.nitya.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Portlet;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.reports.PortletToolBar;

public abstract class AccountBalancesPortletToolBar extends PortletToolBar {
	private SelectCombo accountTypeCombo;
	private List<String> accountTypesList;

	public AccountBalancesPortletToolBar() {
		createControls();
	}

	private void createControls() {
		initOrSetConfigDataToPortletConfig();
		accountTypeCombo = new SelectCombo(messages.accountType());
		accountTypesList = new ArrayList<String>();
		int[] accountTypes = UIUtils.accountTypes;
		for (int i = 0; i < accountTypes.length; i++) {
			accountTypesList.add(Utility.getAccountTypeString(accountTypes[i]));
		}
		accountTypeCombo.initCombo(accountTypesList);
		this.addItems(accountTypeCombo);
		accountTypeCombo.setSelected(portletConfigData
				.get(Portlet.ACCOUNT_TYPE));
		setDefaultDateRange(portletConfigData.get(Portlet.ACCOUNT_TYPE));
		accountTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						portletConfigData.put(Portlet.ACCOUNT_TYPE, selectItem);
						refreshPortletData();
					}
				});
	}

}
