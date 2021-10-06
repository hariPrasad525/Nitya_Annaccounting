/**
 * 
 */
package com.nitya.accounter.web.client.ui.combo;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

/**
 * @author Raj Vimal
 * 
 */
public class FixedAssetAccountCombo extends AccountCombo {

	private ArrayList<ClientAccount> assetAccounts;

	/**
	 * @param title
	 */
	public FixedAssetAccountCombo(String title) {
		super(title);
	}

	@Override
	public List<ClientAccount> getAccounts() {
		assetAccounts = new ArrayList<ClientAccount>();

		for (ClientAccount account : getCompany().getActiveAccounts()) {
			if (account.getType() == ClientAccount.TYPE_FIXED_ASSET)
				assetAccounts.add(account);
		}
		return assetAccounts;
	}

	@Override
	public void onAddNew() {
		NewAccountAction action = new NewAccountAction();
		action.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.FIXEDASSET_COMBO));
		action.setCallback(new ActionCallback<ClientAccount>() {

			@Override
			public void actionResult(ClientAccount result) {
				addItemThenfireEvent(result);
			}
		});

		action.run(null, true);

	}

}
