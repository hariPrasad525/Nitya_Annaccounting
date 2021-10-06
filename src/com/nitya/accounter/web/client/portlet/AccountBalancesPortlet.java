package com.nitya.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientPortletConfiguration;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.Portlet;
import com.nitya.accounter.web.client.ui.StyledPanel;

public class AccountBalancesPortlet extends Portlet {
	private AccountBalancesPortletToolBar toolBar;
	private StyledPanel gridPanel;

	public AccountBalancesPortlet(ClientPortletConfiguration configuration) {
		super(configuration, messages.accountBalances(), "", "100%");
		this.getElement().setId("AccountBalancesPortlet");
		this.setConfiguration(configuration);
		this.getElement().setId("AccountBalancesPortlet");
	}

	@Override
	public void createBody() {
		gridPanel = new StyledPanel("gridPanel");
		createToolbar();
		this.body.add(gridPanel);
	}

	private void createToolbar() {
		toolBar = new AccountBalancesPortletToolBar() {

			@Override
			protected void initOrSetConfigDataToPortletConfig() {
				if (getConfiguration().getPortletMap().get(ACCOUNT_TYPE) != null) {
					portletConfigData.put(ACCOUNT_TYPE, getConfiguration()
							.getPortletMap().get(ACCOUNT_TYPE));
				} else {
					portletConfigData.put(ACCOUNT_TYPE, messages.income());
				}
			}

			@Override
			protected void refreshPortletData() {
				AccountBalancesPortlet.this.clearGrid();
				AccountBalancesPortlet.this.getConfiguration().setPortletMap(
						portletConfigData);
				AccountBalancesPortlet.this.updateData(Utility
						.getAccountType(portletConfigData.get(ACCOUNT_TYPE)));
				updateConfiguration();
			}

			@Override
			public void setDefaultDateRange(String defaultDateRange) {
				updateData(Utility.getAccountType(defaultDateRange));
			}

		};
		this.body.add(toolBar);
	}

	protected void updateData(int accountType) {
		List<ClientAccount> accounts = Accounter.getCompany().getAccounts(
				accountType);
		AccountBalancesGrid grid = new AccountBalancesGrid();
		grid.init();
		if (accounts != null && (!accounts.isEmpty())) {
			List<ClientAccount> filteredAccounts = new ArrayList<ClientAccount>();
			for (ClientAccount account : accounts) {
				if (account.getTotalBalanceInAccountCurrency() != 0.0) {
					filteredAccounts.add(account);
				}
			}
			if (filteredAccounts != null && (!filteredAccounts.isEmpty())) {
				grid.setRecords(filteredAccounts);
			} else {
				grid.addEmptyMessage(messages.noRecordsToShow());
			}
		} else {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
		gridPanel.add(grid);
		completeInitialization();
	}

	public void clearGrid() {
		gridPanel.clear();
	}

	@Override
	public void refreshWidget() {
		super.refreshWidget();
		this.body.clear();
		createBody();
	}
}
