package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientAddress;

public class AddressCombo extends CustomCombo<ClientAddress> {

	public AddressCombo(String title) {
		super(title,"addressCombo");
		super.setToolTip("");
	}

	public AddressCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1,"addressCombo");
		super.setToolTip("");

	}

	// AddressCombo dont have "AddNew" option
	@Override
	public String getDefaultAddNewCaption() {
		return null;
	}

	@Override
	protected String getDisplayName(ClientAddress object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		// TODO Auto-generated method stub

	}

	public void setDefaultToFirstOption(boolean b) {
		if (b) {
			this.setComboItem(comboItems.get(0));
		}
	}

	@Override
	protected String getColumnData(ClientAddress object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
