package com.vimukti.accounter.web.client.ui.edittable;

import com.vimukti.accounter.web.client.core.ClientAccounterClass;

public abstract class TransactionClassColumn<T> extends
		ComboColumn<T, ClientAccounterClass> {
	private final AbstractDropDownTable<ClientAccounterClass> classTable = new ClassTable();

	@Override
	public AbstractDropDownTable<ClientAccounterClass> getDisplayTable(T row) {
		return classTable;
	}

	@Override
	public int getWidth() {
		return 100;
	}

	@Override
	protected String getColumnName() {
		return "Class";
	}
}
