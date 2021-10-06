package com.nitya.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.customers.NewCustomerAction;

public class CustomerDropDownTable extends
		AbstractDropDownTable<ClientCustomer> {

	private ListFilter<ClientCustomer> filter;

	public CustomerDropDownTable(ListFilter<ClientCustomer> filter) {
		super(getCustomers(filter), true);
		this.filter = filter;
	}

	private static List<ClientCustomer> getCustomers(
			ListFilter<ClientCustomer> filter) {
		ArrayList<ClientCustomer> activeCustomers = Accounter.getCompany()
				.getActiveCustomers();
		return Utility.filteredList(filter, activeCustomers);
	}

	@Override
	public List<ClientCustomer> getTotalRowsData() {
		return getCustomers(filter);
	}

	@Override
	protected ClientCustomer getAddNewRow() {
		ClientCustomer clientCustomer = new ClientCustomer();
		clientCustomer.setName(messages.comboDefaultAddNew(Global.get()
				.Customer()));
		return clientCustomer;
	}

	@Override
	public void initColumns() {
		this.addColumn(new TextColumn<ClientCustomer>() {

			@Override
			public String getValue(ClientCustomer object) {
				return object.getDisplayName();
			}
		});
	}

	@Override
	protected String getDisplayValue(ClientCustomer value) {
		return value.getName();
	}

	@Override
	protected void addNewItem() {
		addNewItem("");
	}

	@Override
	protected boolean filter(ClientCustomer t, String string) {
		return t.getDisplayName().toLowerCase().startsWith(string);
	}

	@Override
	protected Class<?> getType() {
		return ClientCustomer.class;
	}

	@Override
	protected void addNewItem(String text) {
		NewCustomerAction action = new NewCustomerAction();
		action.setCallback(new ActionCallback<ClientCustomer>() {

			@Override
			public void actionResult(ClientCustomer result) {
				if (result.getDisplayName() != null) {
					selectRow(result);
				}
			}
		});
		action.setCustomerName(text);
		action.run(null, true);
	}
}
