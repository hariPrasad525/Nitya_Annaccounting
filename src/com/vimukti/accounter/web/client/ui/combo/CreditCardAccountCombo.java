package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;

public class CreditCardAccountCombo extends AccountCombo {

	public CreditCardAccountCombo(String title, boolean isAddnewRequired) {
		super(title, isAddnewRequired);
		initCombo(getAccounts());
	}

	public CreditCardAccountCombo(String title) {
		super(title);
		initCombo(getAccounts());
	}

	public List<ClientAccount> getAccounts() {
		List<ClientAccount> creditCardAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getType() == ClientAccount.TYPE_CREDIT_CARD) {
				creditCardAccounts.add(account);
			}
		}
		return creditCardAccounts;
	}

	public void setAccounts() {
		List<ClientAccount> creditCardAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : getCompany().getActiveAccounts()) {

			if (account.getType() == ClientAccount.TYPE_CREDIT_CARD) {
				creditCardAccounts.add(account);
			}
		}
		this.initCombo(creditCardAccounts);
	}

	@Override
	public void onAddNew() {
		NewAccountAction action = new NewAccountAction();
		action.setCallback(new ActionCallback<ClientAccount>() {

			@Override
			public void actionResult(ClientAccount result) {
				addItemThenfireEvent(result);
			}
		});
		action.setAccountTypes(Arrays.asList(ClientAccount.TYPE_CREDIT_CARD));

		action.run(null, true);

	}

	@Override
	public void addItemThenfireEvent(ClientAccount obj) {
		if (obj.getType() == ClientAccount.TYPE_CREDIT_CARD) {
			super.addItemThenfireEvent(obj);
		}

	}

}
