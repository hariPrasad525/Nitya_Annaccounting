package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;

public class ReconciliationAccountCombo extends AccountCombo {

	public ReconciliationAccountCombo(String title) {
		super(title, false);
		initCombo(getAccounts());
	}

	@Override
	protected List<ClientAccount> getAccounts() {
		List<ClientAccount> reconciliationAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : getCompany().getActiveAccounts()) {
			int type = account.getType();
			if (type == ClientAccount.TYPE_CREDIT_CARD
					|| type == ClientAccount.TYPE_BANK) {
				reconciliationAccounts.add(account);
			}
		}
		return reconciliationAccounts;
	}

}
