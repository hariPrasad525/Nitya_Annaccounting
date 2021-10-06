package com.nitya.accounter.web.client.ui.edittable;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.company.NewAccountAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class AccountDropDownTable extends AbstractDropDownTable<ClientAccount> {

	private ListFilter<ClientAccount> filter;
	List<Integer> canAddAccountTypes;
	private boolean canUseAccountNumbers;

	public AccountDropDownTable(ListFilter<ClientAccount> filter,
			List<Integer> canAddAccountTypes) {
		super(getAccounts(filter), true);
		this.filter = filter;
		this.canAddAccountTypes = canAddAccountTypes;
		canUseAccountNumbers = Accounter.getCompany().getPreferences()
				.getUseAccountNumbers();
	}

	private static List<ClientAccount> getAccounts(
			ListFilter<ClientAccount> filter) {
		ArrayList<ClientAccount> filteredList = Utility.filteredList(filter,
				Accounter.getCompany().getActiveAccounts());
		return filteredList;
	}

	@Override
	public void initColumns() {
		if (canUseAccountNumbers) {
			TextColumn<ClientAccount> numberColumn = new TextColumn<ClientAccount>() {

				@Override
				public String getValue(ClientAccount object) {
					return object.getNumber();
				}
			};
			this.addColumn(numberColumn);
			// this.setColumnWidth(numberColumn, "40px");
		}

		TextColumn<ClientAccount> nameColumn = new TextColumn<ClientAccount>() {

			@Override
			public String getValue(ClientAccount object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(nameColumn);
		// this.setColumnWidth(nameColumn, "100px");

		TextColumn<ClientAccount> typeColumn = new TextColumn<ClientAccount>() {

			@Override
			public String getValue(ClientAccount object) {
				return Utility.getAccountTypeString(object.getType());
			}
		};
		this.addColumn(typeColumn);
		// this.setColumnWidth(typeColumn, "80px");
	}

	@Override
	protected boolean filter(ClientAccount t, String string) {
		if (t.getName().toLowerCase().startsWith(string)) {
			return true;
		}
		if (canUseAccountNumbers) {
			return t.getNumber() != null ? t.getNumber().startsWith(string)
					: false;
		}
		return false;
	}

	@Override
	protected String getDisplayValue(ClientAccount value) {
		return value.getDisplayName();
	}

	@Override
	protected ClientAccount getAddNewRow() {
		ClientAccount account = new ClientAccount();
		account.setName(messages.comboDefaultAddNew(messages.Account()));
		return account;
	}

	@Override
	public void addNewItem() {
		addNewItem("");
	}

	@Override
	public List<ClientAccount> getTotalRowsData() {
		return getAccounts(filter);
	}

	@Override
	protected Class<?> getType() {
		return ClientAccount.class;
	}

	@Override
	protected void addNewItem(String text) {
		NewAccountAction action = new NewAccountAction();
		action.setCallback(new ActionCallback<ClientAccount>() {

			@Override
			public void actionResult(ClientAccount result) {
				if (result.getIsActive() && filter.filter(result)) {
					selectRow(result);
				}

			}
		});
		action.setAccountTypes(canAddAccountTypes);
		action.setAccountName(text);
		action.run(null, true);
	}
}
