package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;

import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.ui.BaseDataSource;

public class CustomerDataSource extends BaseDataSource {

	AccounterAsyncCallback<ArrayList<ClientCustomer>> callback;

	public CustomerDataSource(
			AccounterAsyncCallback<ArrayList<ClientCustomer>> callback) {
		this.callback = callback;
		// FinanceApplication.createGETService().getCustomers( callback);
		// FinanceApplication.getCompany().getCustomers();

	}
}
