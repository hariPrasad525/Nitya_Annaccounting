package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;

public class RevenueAccountCombo extends AccountCombo {

	private List<ClientAccount> revenueAccounts;

	public RevenueAccountCombo(String title) {
		super(title);
	}

	public RevenueAccountCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired);
	}

	@Override
	protected List<ClientAccount> getAccounts() {
		revenueAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts())
			if (account.getType() == ClientAccount.TYPE_INCOME) {
				revenueAccounts.add(account);
			}
		return revenueAccounts;
	}

}
