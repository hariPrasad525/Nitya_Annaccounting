package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.core.ActionCallback;
import com.nitya.accounter.web.client.ui.core.QuickAddDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.vendors.NewVendorAction;

public class VendorCombo extends CustomCombo<ClientVendor> {
	private boolean isAddNew;

	public VendorCombo(String title) {
		super(title, "VendorCombo");
		initCombo(getCompany().getActiveVendors());
	}

	public VendorCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "VendorCombo");
		initCombo(getCompany().getActiveVendors());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return Global.get().Vendor();

	}

	@Override
	protected String getDisplayName(ClientVendor object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		isAddNew = true;
		NewVendorAction action = new NewVendorAction();
		action.setCallback(new ActionCallback<ClientVendor>() {

			@Override
			public void actionResult(ClientVendor result) {
				if (result.getName() != null && result.isActive())
					isAddNew = false;
				addItemThenfireEvent(result);

			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientVendor object, int col) {
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
		final QuickAddDialog dialog = new QuickAddDialog(
				messages.newPayee(Global.get().vendor())) {

		};
		dialog.setDefaultText(textBox.getText());
		dialog.setListener(new QuickAddListenerImpl(this) {
			@Override
			public IAccounterCore getData(String text) {
				if (isVenodrExists(text)) {
					return null;
				}
				return super.getData(text);
			}
		});
		ViewManager.getInstance().showDialog(dialog);
	}

	protected boolean isVenodrExists(String text) {
		for (ClientPayee customer : getCompany().getPayees()) {
			if (customer.getName().equals(text)) {
				if (customer instanceof ClientVendor) {
					this.setComboItem(null);
					this.setComboItem((ClientVendor) customer);
				}
				return true;
			}
		}
		return false;
	}

	@Override
	protected void onAddAllInfo(String text) {
		NewVendorAction action = new NewVendorAction(text);
		action.setCallback(new ActionCallback<ClientVendor>() {

			@Override
			public void actionResult(ClientVendor result) {
				if (result.getDisplayName() != null)
					addItemThenfireEvent(result);
			}
		});
		action.run(null, true);
	}

	@Override
	protected ClientVendor getQuickAddData(String text) {
		ClientVendor clientVendor = new ClientVendor();
		clientVendor.setName(text);
		clientVendor.setCurrency(getCompany().getPrimaryCurrency().getID());
		clientVendor.setBalanceAsOf(new ClientFinanceDate().getDate());
		return clientVendor;
	}
}
