package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class PayFromAccountsCombo extends AccountCombo {

	private List<ClientAccount> payFromAccounts;

	public PayFromAccountsCombo(String title, boolean b) {
		super(title, b);
		initCombo(getAccounts());
	}

	public PayFromAccountsCombo(String title) {
		super(title);
		initCombo(getAccounts());
	}

	public List<ClientAccount> getAccounts() {
		payFromAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_ASSET
					|| account.getType() == ClientAccount.TYPE_CREDIT_CARD) {
				payFromAccounts.add(account);
			}
		}
		return payFromAccounts;
	}

	public void setAccounts() {
		payFromAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : getCompany().getActiveAccounts()) {

			if (account.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_ASSET
					|| account.getType() == ClientAccount.TYPE_CREDIT_CARD) {
				payFromAccounts.add(account);
			}
		}
		this.initCombo(payFromAccounts);
	}

	public void setDefaultPayFromAccount() {
		/* Default deposit in account is set to Bank Current Account */
		List<ClientAccount> accounts = getCompany().getAccounts();
		for (ClientAccount account : accounts) {
			if (account.getNumber().equals("1100")) {
				this.addItemThenfireEvent(account);
				break;
			}
		}

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
		action.setAccountTypes(Arrays.asList(ClientAccount.TYPE_BANK,
				ClientAccount.TYPE_OTHER_CURRENT_ASSET,
				ClientAccount.TYPE_CASH, ClientAccount.TYPE_INVENTORY_ASSET,
				ClientAccount.TYPE_CREDIT_CARD, ClientAccount.TYPE_PAYPAL));

		action.run(null, true);

	}

	@Override
	public void addItemThenfireEvent(ClientAccount obj) {
		if (obj.getSubBaseType() == ClientAccount.SUBBASETYPE_CURRENT_ASSET
				|| obj.getType() == ClientAccount.TYPE_CREDIT_CARD) {
			super.addItemThenfireEvent(obj);
		}

	}

	@Override
	public void init() {
		super.init();
		setDefaultPayFromAccount();
	}

}
