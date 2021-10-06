package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;

public class BankAccountCombo extends AccountCombo {

	private ArrayList<ClientAccount> bankAccounts;

	public BankAccountCombo(String title) {
		super(title);
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.bankAccount();
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
				addItemThenfireEvent(result);
			}
		});
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.BANK_ACCOUNTS_COMBO));

		action.run(null, true);

	}

	@Override
	public List<ClientAccount> getAccounts() {
		bankAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : Accounter.getCompany().getActiveAccounts()) {
			if (account.getType() == ClientAccount.TYPE_BANK)
				bankAccounts.add(account);
		}
		return bankAccounts;
	}

}
