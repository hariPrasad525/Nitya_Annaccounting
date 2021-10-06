package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class PurchaseAccountsCombo extends GridAccountsCombo {

	public PurchaseAccountsCombo(String title) {
		super(title);
		initCombo(getAccounts());
	}

	public PurchaseAccountsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired);
		initCombo(getAccounts());
	}

	public List<ClientAccount> getAccounts() {
		ArrayList<ClientAccount> gridAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getType() != ClientAccount.TYPE_CASH
					&& account.getType() != ClientAccount.TYPE_BANK
					&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
					&& account.getType() != ClientAccount.TYPE_INCOME
					&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
					&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
					&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
					&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
					&& account.getType() != ClientAccount.TYPE_EQUITY
					&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY)
				gridAccounts.add(account);
		}
		return gridAccounts;
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
		// action.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.GRID_ACCOUNTS_COMBO));
		action.setAccountTypes(Arrays.asList(
				ClientAccount.TYPE_COST_OF_GOODS_SOLD,
				ClientAccount.TYPE_OTHER_EXPENSE,
				ClientAccount.TYPE_FIXED_ASSET, ClientAccount.TYPE_EXPENSE));

		action.run(null, true);

	}

}
