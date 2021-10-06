package com.vimukti.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.vimukti.accounter.web.client.core.ClientBank;

public class BankDropDownTable extends AbstractDropDownTable<ClientBank> {

	List<ClientBank> banks;

	public BankDropDownTable(List<ClientBank> banks) {
		super(banks, true);
		this.banks = banks;
	}

	@Override
	public void initColumns() {

		TextColumn<ClientBank> nameColumn = new TextColumn<ClientBank>() {

			@Override
			public String getValue(ClientBank object) {
				return object.getName();
			}
		};
		this.addColumn(nameColumn);

	}

	@Override
	protected String getDisplayValue(ClientBank value) {
		return value.getDisplayName();
	}

	@Override
	public void addNewItem() {
		addNewItem("");
	}

	@Override
	public List<ClientBank> getTotalRowsData() {
		return banks;
	}

	@Override
	protected Class<?> getType() {
		return ClientBank.class;
	}

	@Override
	protected ClientBank getAddNewRow() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean filter(ClientBank t, String string) {
		if (t != null && t.getName() != null
				&& t.getName().equalsIgnoreCase(string)) {
			return true;
		}
		return false;
	}

	@Override
	protected void addNewItem(String text) {
		// TODO Auto-generated method stub

	}

}
