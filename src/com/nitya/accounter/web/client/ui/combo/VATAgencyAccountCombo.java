package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class VATAgencyAccountCombo extends AccountCombo {

	private ArrayList<ClientAccount> accounts;

	public VATAgencyAccountCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.Account();
	}

	@Override
	protected String getDisplayName(ClientAccount object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewAccountAction action = new NewAccountAction();
		action.setCallback(new ActionCallback<ClientAccount>() {

			@Override
			public void actionResult(ClientAccount result) {
				if ((result.getDisplayName() != null || result.getName() != null)
						&& canAdd(result)) {
					addItemThenfireEvent(result);
				}

			}
		});

		action.run(null, true);

	}

	@Override
	protected List<ClientAccount> getAccounts() {
		accounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (canAdd(account)) {
				accounts.add(account);
			}
		}
		return accounts;
	}

	private boolean canAdd(ClientAccount account) {
		return Arrays.asList(ClientAccount.TYPE_INCOME,
				ClientAccount.TYPE_EXPENSE,
				ClientAccount.TYPE_OTHER_CURRENT_LIABILITY,
				ClientAccount.TYPE_OTHER_CURRENT_ASSET,
				ClientAccount.TYPE_FIXED_ASSET).contains(account.getType());
	}

}
