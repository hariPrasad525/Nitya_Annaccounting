package com.nitya.accounter.web.client.ui.combo;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;

public class OtherAccountsCombo extends AccountCombo {

	public OtherAccountsCombo(String title) {
		super(title);
	}

	public OtherAccountsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		return getCompany().getActiveAccounts();
	}
}
