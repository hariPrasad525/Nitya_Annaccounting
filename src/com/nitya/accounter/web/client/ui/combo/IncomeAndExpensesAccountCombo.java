package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class IncomeAndExpensesAccountCombo extends AccountCombo {

	private List<ClientAccount> incomeAndExpenseAccounts;

	public IncomeAndExpensesAccountCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		incomeAndExpenseAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts())
			if (account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
				incomeAndExpenseAccounts.add(account);
			}
		return incomeAndExpenseAccounts;
	}

	@Override
	public String getDisplayName(ClientAccount object) {
		if (object != null)
			return object.getDisplayName() != null ? object.getDisplayName()
					: object.getName();
		else
			return "";
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
		action.setAccountTypes(getAccountTypes());

		action.run(null, true);

	}

}
