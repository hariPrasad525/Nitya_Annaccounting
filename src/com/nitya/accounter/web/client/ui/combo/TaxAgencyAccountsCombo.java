package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;

public class TaxAgencyAccountsCombo extends AccountCombo {

	private List<ClientAccount> taxAgencyAccounts;

	public TaxAgencyAccountsCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		taxAgencyAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts())
			if (account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
					&& account.getType() != ClientAccount.TYPE_INCOME
					&& account.getType() != ClientAccount.TYPE_EXPENSE
					&& account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD)
				taxAgencyAccounts.add(account);
		return taxAgencyAccounts;
	}

}
