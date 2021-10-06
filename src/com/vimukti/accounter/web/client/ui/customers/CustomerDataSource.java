package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.ui.BaseDataSource;

public class CustomerDataSource extends BaseDataSource {

	AccounterAsyncCallback<ArrayList<ClientCustomer>> callback;

	public CustomerDataSource(
			AccounterAsyncCallback<ArrayList<ClientCustomer>> callback) {
		this.callback = callback;
		// FinanceApplication.createGETService().getCustomers( callback);
		// FinanceApplication.getCompany().getCustomers();

	}
}
