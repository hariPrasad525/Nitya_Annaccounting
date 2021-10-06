package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class CashBackAccountsCombo extends AccountCombo {

	private List<ClientAccount> cashBackAccounts;

	public CashBackAccountsCombo(String title) {
		super(title);
	}

	public List<ClientAccount> getAccounts() {
		AccounterMessages messages = Global.get().messages();
		cashBackAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : Accounter.getCompany().getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
					&& !account.getName().equals(messages.unDepositedFunds())) {
				cashBackAccounts.add(account);
			}
		}
		return cashBackAccounts;
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
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.CASH_BACK_ACCOUNTS_COMBO));

		action.run(null, true);

	}

}
