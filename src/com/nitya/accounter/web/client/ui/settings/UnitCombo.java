package com.nitya.accounter.web.client.ui.settings;

import com.nitya.accounter.web.client.core.ClientUnit;
import com.nitya.accounter.web.client.ui.combo.CustomCombo;

public class UnitCombo extends CustomCombo<ClientUnit> {

	public UnitCombo(String title) {
		super(title, false, 1, "UnitCombo");
	}

	@Override
	protected String getDisplayName(ClientUnit object) {
		return object.getType();
	}

	@Override
	protected String getColumnData(ClientUnit object, int col) {
		switch (col) {
		case 0:
			return object.getType();
		}
		return null;
	}

	@Override
	public String getDefaultAddNewCaption() {
		return null;
	}

	@Override
	public void onAddNew() {
	}

}
