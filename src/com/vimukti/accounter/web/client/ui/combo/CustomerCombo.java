package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.QuickAddDialog;
import com.vimukti.accounter.web.client.ui.core.ViewManager;
import com.vimukti.accounter.web.client.ui.customers.NewCustomerAction;

public class CustomerCombo extends CustomCombo<ClientCustomer> {

	private boolean isAddNew;

	public CustomerCombo(String title) {
		super(title, "customerCombo");
		super.setToolTip(messages.selectWhichWeHaveInOurCompanyOrAddNew(Global
				.get().Customer()));
		initCombo(getCompany().getCustomers());
	}

	public CustomerCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1, "customerCombo");
		if (isAddNewRequire)
			super.setToolTip(messages
					.selectWhichWeHaveInOurCompanyOrAddNew(Global.get()
							.Customer()));
		initCombo(getCompany().getCustomers());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return Global.get().Customer();
	}

	@Override
	protected String getDisplayName(ClientCustomer object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {

		isAddNew = true;
		NewCustomerAction action = new NewCustomerAction();
		action.setCallback(new ActionCallback<ClientCustomer>() {

			@Override
			public void actionResult(ClientCustomer result) {
				if (result.getDisplayName() != null && result.isActive())
					isAddNew = false;
				addItemThenfireEvent(result);
			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientCustomer object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

	@Override
	protected void selectionFaildOnClose() {
		if (isAddNew) {
			return;
		}
		final QuickAddDialog dialog = new QuickAddDialog(messages.NewCustomer());
		dialog.setDefaultText(textBox.getText());
		dialog.setListener(new QuickAddListenerImpl(this) {
			@Override
			public IAccounterCore getData(String text) {
				if (!isCustomerExists(text)) {
					return super.getData(text);
				}
				return null;
			}
		});
		ViewManager.getInstance().showDialog(dialog);

	}

	protected boolean isCustomerExists(String text) {
		for (ClientPayee customer : getCompany().getPayees()) {
			if (customer.getName().equals(text)) {
				if (customer instanceof ClientCustomer) {
					this.setComboItem(null);
					this.setComboItem((ClientCustomer) customer);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onAddAllInfo(String text) {
		NewCustomerAction action = new NewCustomerAction(text);
		action.setCallback(new ActionCallback<ClientCustomer>() {

			@Override
			public void actionResult(ClientCustomer result) {
				if (result.getDisplayName() != null)
					addItemThenfireEvent(result);
			}
		});
		action.run(null, true);
	}

	@Override
	protected ClientCustomer getQuickAddData(String text) {
		ClientCustomer clientCustomer = new ClientCustomer();
		clientCustomer.setName(text);
		clientCustomer.setCurrency(getCompany().getPrimaryCurrency().getID());
		clientCustomer.setBalanceAsOf(new ClientFinanceDate().getDate());
		return clientCustomer;
	}
}
