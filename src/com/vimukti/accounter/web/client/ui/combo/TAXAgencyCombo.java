package com.vimukti.accounter.web.client.ui.combo;

import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.ui.company.NewTAXAgencyAction;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;

public class TAXAgencyCombo extends CustomCombo<ClientTAXAgency> {

	public TAXAgencyCombo(String title) {
		this(title, true);
	}

	public TAXAgencyCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1, "TAXAgencyCombo");
		initCombo(getCompany().getActiveTAXAgencies());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.taxAgency();

	}

	@Override
	public void onAddNew() {
		NewTAXAgencyAction action = new NewTAXAgencyAction();
		action.setCallback(new ActionCallback<ClientTAXAgency>() {

			@Override
			public void actionResult(ClientTAXAgency result) {
				if (result.getName() != null)
					addItemThenfireEvent(result);

			}
		});

		action.run(null, true);
	}

	@Override
	protected String getDisplayName(ClientTAXAgency object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientTAXAgency object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
