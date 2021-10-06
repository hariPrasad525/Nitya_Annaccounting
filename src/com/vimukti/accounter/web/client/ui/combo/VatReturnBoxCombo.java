package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientVATReturnBox;

public class VatReturnBoxCombo extends CustomCombo<ClientVATReturnBox> {

	public VatReturnBoxCombo(String title) {
		super(title,false,1,"VatReturnBoxCombo");
		initCombo(getCompany().getVatReturnBoxes());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return null;
	}

	@Override
	protected String getDisplayName(ClientVATReturnBox object) {
		return object.getName();
	}

	@Override
	public void onAddNew() {
		// do nothing
	}

	@Override
	protected String getColumnData(ClientVATReturnBox object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
