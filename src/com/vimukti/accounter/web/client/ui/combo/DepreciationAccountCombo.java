/**
 * 
 */
package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.company.NewAccountAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;

/**
 * @author vimukti16
 * 
 */
public class DepreciationAccountCombo extends AccountCombo {

	/**
	 * @param title
	 */
	public DepreciationAccountCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		List<ClientAccount> accounts = getCompany().getActiveAccounts();
		List<ClientAccount> expenseAccounts = new ArrayList<ClientAccount>();
		for (ClientAccount account : accounts) {
			if (account.getType() == ClientAccount.TYPE_EXPENSE)
				expenseAccounts.add(account);
		}
		return expenseAccounts;
	}

	public void onAddNew() {
		NewAccountAction action = new NewAccountAction();
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.DEPRECIATION_COMBO));
		action.setCallback(new ActionCallback<ClientAccount>() {

			@Override
			public void actionResult(ClientAccount result) {
				addItemThenfireEvent(result);
			}
		});

		action.run(null, true);

	}

}
