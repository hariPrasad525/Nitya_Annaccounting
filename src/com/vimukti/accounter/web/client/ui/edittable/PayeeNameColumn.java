package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ListFilter;

public abstract class PayeeNameColumn<T> extends ComboColumn<T, ClientPayee> {

	PayeeDropDownTable payees = new PayeeDropDownTable(getPayeesFilter());

	private ListFilter<ClientPayee> getPayeesFilter() {
		return new ListFilter<ClientPayee>() {

			@Override
			public boolean filter(ClientPayee e) {
				return ((e instanceof ClientCustomer || e instanceof ClientVendor) && e
						.isActive());
			}
		};
	}

	@Override
	public AbstractDropDownTable<ClientPayee> getDisplayTable(T row) {
		return payees;
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected String getColumnName() {
		return messages.payee();
	}
}
