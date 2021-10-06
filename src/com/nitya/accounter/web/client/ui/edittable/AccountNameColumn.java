package com.nitya.accounter.web.client.ui.edittable;

import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ListFilter;

public abstract class AccountNameColumn<T> extends
		ComboColumn<T, ClientAccount> {

	AccountDropDownTable accountsList = new AccountDropDownTable(
			getAccountsFilter(), getCanAddedAccountTypes());

	public abstract ListFilter<ClientAccount> getAccountsFilter();

	public abstract List<Integer> getCanAddedAccountTypes();

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractDropDownTable getDisplayTable(T row) {
		return accountsList;
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected String getColumnName() {
		return messages.name();
	}

	@Override
	public boolean isPrimaryColumn() {
		return true;
	}
}
