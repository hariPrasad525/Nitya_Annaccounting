package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientAccount;

public class AccountDataSource extends BaseDataSource {

	public AccountDataSource(
			AccounterAsyncCallback<ArrayList<ClientAccount>> callback) {
		Accounter.getCompany().getAccounts();
	}
}
