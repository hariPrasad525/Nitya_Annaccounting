package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class DepositInAccountCombo extends AccountCombo {

	public DepositInAccountCombo(String title) {
		super(title);
	}

	public List<ClientAccount> getAccounts() {
		ArrayList<ClientAccount> deposiInAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (filter(account)) {
				deposiInAccounts.add(account);
			}
		}

		return deposiInAccounts;
	}

	private boolean filter(ClientAccount acc) {
		return Arrays.asList(ClientAccount.TYPE_BANK, ClientAccount.TYPE_CASH,
				ClientAccount.TYPE_PAYPAL, ClientAccount.TYPE_CREDIT_CARD,
				ClientAccount.TYPE_OTHER_CURRENT_ASSET,
				ClientAccount.TYPE_INVENTORY_ASSET,
				ClientAccount.TYPE_FIXED_ASSET).contains(acc.getType())
				&& acc.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE;

	}

	public void setAccounts() {
		ArrayList<ClientAccount> deposiInAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (filter(account)) {
				deposiInAccounts.add(account);
			}
		}

		this.initCombo(deposiInAccounts);
	}

	private void setDefaultDepositInAccount() {
		/* Default deposit in account is set to Bank Current Account */
		// TODO SET DEFAULT ACCOUNT
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
				.getOptionsByType(AccountCombo.DEPOSIT_IN_ACCOUNT));

		action.run(null, true);

	}

	@Override
	public void init() {
		super.init();
		setDefaultDepositInAccount();
	}
}
