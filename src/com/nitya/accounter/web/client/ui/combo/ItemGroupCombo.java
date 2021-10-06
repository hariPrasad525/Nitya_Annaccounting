package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.core.ClientItemGroup;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.vendors.ManageSupportListView;

public class ItemGroupCombo extends CustomCombo<ClientItemGroup> {

	public ItemGroupCombo(String title) {
		super(title, "ItemGroupCombo");
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.itemGroup();
	}

	@Override
	protected String getDisplayName(ClientItemGroup object) {
		return object != null ? object.getName() != null ? object.getName()
				: "" : "";
	}

	@Override
	public void onAddNew() {
		ManageSupportListView priceLevelDialog = new ManageSupportListView(
				IAccounterCore.ITEM_GROUP);
		priceLevelDialog.setVisible(false);
		priceLevelDialog.setCallback(createAddNewCallBack());
		priceLevelDialog.showAddEditGroupDialog(null);

	}

	@Override
	protected String getColumnData(ClientItemGroup object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}
}
