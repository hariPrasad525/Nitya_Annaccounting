package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;

public class RevenueAndExpenseAccountCombo extends AccountCombo {

	private List<ClientAccount> revenueandExpenseAccounts;

	public RevenueAndExpenseAccountCombo(String title) {
		super(title);
	}

	public RevenueAndExpenseAccountCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired);
	}

	@Override
	protected List<ClientAccount> getAccounts() {
		revenueandExpenseAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts())
			if (account.getType() == ClientAccount.TYPE_INCOME
					|| account.getType() == ClientAccount.TYPE_EXPENSE) {
				revenueandExpenseAccounts.add(account);
			}
		return revenueandExpenseAccounts;
	}

}
