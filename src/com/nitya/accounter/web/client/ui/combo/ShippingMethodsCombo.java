package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.core.ClientShippingMethod;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.vendors.ManageSupportListView;

public class ShippingMethodsCombo extends CustomCombo<ClientShippingMethod> {

	public ShippingMethodsCombo(String title) {
		super(title, "ShippingMethodsCombo");
		initCombo(getCompany().getShippingMethods());
	}

	public ShippingMethodsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "ShippingMethodsCombo");
		initCombo(getCompany().getShippingMethods());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.shippingMethod();
	}

	@Override
	protected String getDisplayName(ClientShippingMethod object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		ManageSupportListView priceLevelDialog = new ManageSupportListView(
				IAccounterCore.SHIPPING_METHOD);
		priceLevelDialog.setVisible(false);
		priceLevelDialog.setCallback(createAddNewCallBack());
		priceLevelDialog.showAddEditGroupDialog(null);
	}

	@Override
	protected String getColumnData(ClientShippingMethod object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
