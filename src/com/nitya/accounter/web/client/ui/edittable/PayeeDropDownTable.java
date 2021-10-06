package com.nitya.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.banking.SelectPayeeDialog;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.ViewManager;

public class PayeeDropDownTable extends AbstractDropDownTable<ClientPayee> {

	private ListFilter<ClientPayee> filter;
	List<Integer> canAddAccountTypes;

	public PayeeDropDownTable(ListFilter<ClientPayee> filter) {
		super(getPayees(filter), true);
		this.filter = filter;
	}

	private static List<ClientPayee> getPayees(ListFilter<ClientPayee> filter) {
		ArrayList<ClientPayee> filteredList = Utility.filteredList(filter,
				Accounter.getCompany().getActivePayees());
		return filteredList;
	}

	@Override
	public List<ClientPayee> getTotalRowsData() {
		return getPayees(filter);
	}

	@Override
	protected ClientPayee getAddNewRow() {
		ClientPayee payee = new ClientPayee() {

			@Override
			public String getDisplayName() {
				return getName();
			}

			@Override
			public AccounterCoreType getObjectType() {
				return null;
			}

		};
		payee.setName(messages.comboDefaultAddNew(messages.payee()));
		return payee;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientPayee> nameColumn = new TextColumn<ClientPayee>() {

			@Override
			public String getValue(ClientPayee object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(nameColumn);

	}

	@Override
	protected boolean filter(ClientPayee t, String string) {
		if ((t instanceof ClientCustomer || t instanceof ClientVendor)
				&& t.getName().toLowerCase().startsWith(string)) {
			return true;
		}
		return false;
	}

	@Override
	protected String getDisplayValue(ClientPayee value) {
		return value.getDisplayName();
	}

	@Override
	protected void addNewItem(String text) {
		SelectPayeeDialog dialog = new SelectPayeeDialog();
		dialog.setCallback(new ActionCallback<ClientPayee>() {

			@Override
			public void actionResult(ClientPayee result) {
				selectRow(result);
			}
		});
		ViewManager.getInstance().showDialog(dialog);
	}

	@Override
	protected void addNewItem() {

		SelectPayeeDialog dialog = new SelectPayeeDialog();
		dialog.setCallback(new ActionCallback<ClientPayee>() {

			@Override
			public void actionResult(ClientPayee result) {
				if (result.isActive()) {
					selectRow(result);
				}

			}
		});
		ViewManager.getInstance().showDialog(dialog);
	}

}
