package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCustomerGroup;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.vendors.ManageSupportListView;

public class CustomerGroupCombo extends CustomCombo<ClientCustomerGroup> {

	public CustomerGroupCombo(String title) {
		super(title, "customerGroupCombo");
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.payeeGroup(Global.get().Customer());
	}

	@Override
	protected String getDisplayName(ClientCustomerGroup object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		ManageSupportListView priceLevelDialog = new ManageSupportListView(
				IAccounterCore.CUSTOMER_GROUP);
		priceLevelDialog.setVisible(false);
		priceLevelDialog.setCallback(createAddNewCallBack());
		priceLevelDialog.showAddEditGroupDialog(null);
	}

	@Override
	protected String getColumnData(ClientCustomerGroup object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}
}
