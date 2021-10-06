package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.ui.vendors.ManageSupportListView;

public class ShippingTermsCombo extends CustomCombo<ClientShippingTerms> {

	public ShippingTermsCombo(String title) {
		super(title, "ShippingTermsCombo");
		initCombo(getCompany().getShippingTerms());
	}

	public ShippingTermsCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "ShippingTermsCombo");
		initCombo(getCompany().getShippingTerms());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.shippingTerms();
	}

	@Override
	protected String getDisplayName(ClientShippingTerms object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		ManageSupportListView priceLevelDialog = new ManageSupportListView(
				IAccounterCore.SHIPPING_TERMS);
		priceLevelDialog.setVisible(false);
		priceLevelDialog.setCallback(createAddNewCallBack());
		priceLevelDialog.showAddEditGroupDialog(null);
	}

	@Override
	protected String getColumnData(ClientShippingTerms object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
