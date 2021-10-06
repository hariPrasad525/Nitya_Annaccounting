package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;

public class GridAccountsCombo extends AccountCombo {

	public GridAccountsCombo(String title) {
		this(title, true);
	}

	public GridAccountsCombo(String title, boolean isAddNewRequired) {
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
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE)
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
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.GRID_ACCOUNTS_COMBO));

		action.run(null, true);

	}

}
