package com.nitya.accounter.web.client.portlet;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.Portlet;
import com.nitya.accounter.web.client.ui.combo.AccountCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.reports.PortletToolBar;

public abstract class YearOverYearToolBar extends PortletToolBar {
	protected SelectCombo dateRangeItemCombo;
	protected List<String> dateRangesList;
	protected AccountCombo accountCombo;
	private int chartType;
	protected long accountId;

	public YearOverYearToolBar(int chartType) {
		this.chartType = chartType;
		init();
	}

	protected void init() {
		createControls();
	}

	private void createControls() {
		initOrSetConfigDataToPortletConfig();
		dateRangesList = new ArrayList<String>();
		for (int i = 0; i < monthDateRangeArray.length; i++) {
			dateRangesList.add(monthDateRangeArray[i]);
		}
		dateRangeItemCombo = new SelectCombo(messages.dateRange());
		dateRangeItemCombo.addStyleName("date_range_combo");
		dateRangeItemCombo.initCombo(dateRangesList);
		dateRangeItemCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						portletConfigData.put(Portlet.DATE_RANGE, selectItem);
						refreshPortletData();
					}
				});

		accountCombo = new AccountCombo(messages.Account()) {

			@Override
			protected List<ClientAccount> getAccounts() {
				if (chartType == YearOverYearPortlet.YEAR_OVER_YEAR_EXPENSE) {
					return Accounter.getCompany().getAccounts(
							ClientAccount.TYPE_EXPENSE);
				} else if (chartType == YearOverYearPortlet.YEAR_OVER_YEAR_INCOME) {
					return Accounter.getCompany().getAccounts(
							ClientAccount.TYPE_INCOME);
				}
				return null;
			}
		};
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						portletConfigData.put(Portlet.ACCOUNT_ID,
								String.valueOf(selectItem.getID()));
						refreshPortletData();
					}
				});
		this.addItems(dateRangeItemCombo, accountCombo);
		if (portletConfigData.size() > 1
				&& Long.parseLong(portletConfigData.get(Portlet.ACCOUNT_ID)) != 0) {
			accountCombo.setComboItem(Accounter.getCompany().getAccount(
					Long.parseLong(portletConfigData.get(Portlet.ACCOUNT_ID))));
		}
		setDefaultDateRange(portletConfigData.get(Portlet.DATE_RANGE));
	}
}
