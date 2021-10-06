package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class PurchaseItemCombo extends AccountCombo {
	private ArrayList<ClientAccount> filtrdAccounts;

	public PurchaseItemCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		return getCompany().getActiveAccounts();
	}

	public List<ClientAccount> getFilterdAccounts() {
		filtrdAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getType() == ClientAccount.TYPE_COST_OF_GOODS_SOLD
					|| account.getType() == ClientAccount.TYPE_EXPENSE
					|| account.getType() == ClientAccount.TYPE_OTHER_EXPENSE) {
				filtrdAccounts.add(account);
			}
		}

		return filtrdAccounts;
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
		action.setAccountTypes(Arrays.asList(
				ClientAccount.TYPE_COST_OF_GOODS_SOLD,
				ClientAccount.TYPE_OTHER_EXPENSE, ClientAccount.TYPE_EXPENSE));

		action.run(null, true);

	}

}
