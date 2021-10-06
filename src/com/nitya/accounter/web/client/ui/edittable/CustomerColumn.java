package com.nitya.accounter.web.client.ui.edittable;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ListFilter;

public abstract class CustomerColumn<T> extends ComboColumn<T, ClientCustomer> {

	CustomerDropDownTable customers = new CustomerDropDownTable(
			getCustomersFilter());

	private ListFilter<ClientCustomer> getCustomersFilter() {
		return new ListFilter<ClientCustomer>() {

			@Override
			public boolean filter(ClientCustomer e) {
				return e.isActive();
			}
		};
	}

	@Override
	public AbstractDropDownTable<ClientCustomer> getDisplayTable(T row) {
		return customers;
	}

	@Override
	public int getWidth() {
		return 110;
	}

	@Override
	protected String getColumnName() {
		return Global.get().Customer();
	}

}
