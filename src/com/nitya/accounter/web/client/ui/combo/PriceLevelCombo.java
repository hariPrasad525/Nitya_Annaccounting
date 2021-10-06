package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.core.ClientPriceLevel;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.ui.vendors.ManageSupportListView;

public class PriceLevelCombo extends CustomCombo<ClientPriceLevel> {

	public PriceLevelCombo(String title) {
		super(title, "PriceLevelCombo");
		initCombo(getCompany().getPriceLevels());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.priceLevel();
	}

	@Override
	protected String getDisplayName(ClientPriceLevel object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		ManageSupportListView priceLevelDialog = new ManageSupportListView(
				IAccounterCore.PRICE_LEVEL);
		priceLevelDialog.setVisible(false);
		priceLevelDialog.setCallback(createAddNewCallBack());
		priceLevelDialog.showAddEditGroupDialog(null);
	}

	@Override
	protected String getColumnData(ClientPriceLevel object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
