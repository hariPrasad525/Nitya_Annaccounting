package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.core.ClientSalesPerson;
import com.nitya.accounter.web.client.ui.company.NewSalesperSonAction;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class SalesPersonCombo extends CustomCombo<ClientSalesPerson> {

	public SalesPersonCombo(String title) {
		super(title, "SalesPersonCombo");
	}

	public SalesPersonCombo(String title, boolean isAddNewRequired) {
		super(title, isAddNewRequired, 1, "SalesPersonCombo");

		// initCombo(getCompany().getActiveSalesPersons());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.salesPerson();
	}

	@Override
	protected String getDisplayName(ClientSalesPerson object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		NewSalesperSonAction action = new NewSalesperSonAction();
		action.setCallback(new ActionCallback<ClientSalesPerson>() {

			@Override
			public void actionResult(ClientSalesPerson result) {
				if (result.getFirstName() != null
						|| result.getLastName() != null)
					addItemThenfireEvent(result);

			}
		});

		action.run(null, true);
	}

	@Override
	protected String getColumnData(ClientSalesPerson object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}
}
