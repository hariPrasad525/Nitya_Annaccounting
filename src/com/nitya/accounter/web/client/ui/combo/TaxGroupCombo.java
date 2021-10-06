package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.core.ClientTAXGroup;
import com.nitya.accounter.web.client.core.ClientTAXItemGroup;
import com.nitya.accounter.web.client.ui.company.ManageSupportListAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class TaxGroupCombo extends CustomCombo<ClientTAXItemGroup> {

	public TaxGroupCombo(String title) {
		super(title, "TaxGroupCombo");

	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.taxGroup();
	}

	@Override
	protected String getDisplayName(ClientTAXItemGroup object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		ManageSupportListAction action = ManageSupportListAction
				.salesTaxItems();
		action.setCallback(new ActionCallback<ClientTAXGroup>() {

			@Override
			public void actionResult(ClientTAXGroup result) {
				addItemThenfireEvent(result);

			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientTAXItemGroup object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
