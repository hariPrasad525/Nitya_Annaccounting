package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientVendorGroup;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.vendors.ManageSupportListView;

public class VendorGroupCombo extends CustomCombo<ClientVendorGroup> {

	public VendorGroupCombo(String title) {
		super(title, "VendorGroupCombo");
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.payeeGroup(Global.get().Vendor());
	}

	@Override
	protected String getDisplayName(ClientVendorGroup object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		ManageSupportListView vendorGroup = new ManageSupportListView(
				IAccounterCore.VENDOR_GROUP);
		vendorGroup.setVisible(false);
		vendorGroup.setCallback(createAddNewCallBack());
		vendorGroup.showAddEditGroupDialog(null);
	}

	@Override
	protected String getColumnData(ClientVendorGroup object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
