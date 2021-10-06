package com.nitya.accounter.web.client.ui.combo;

import com.nitya.accounter.web.client.core.ClientMeasurement;
import com.nitya.accounter.web.client.ui.company.InventoryActions;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class MeasurementCombo extends CustomCombo<ClientMeasurement> {

	public MeasurementCombo(String title) {
		super(title, "MeasurementCombo");
		initCombo(getCompany().getMeasurements());
	}

	@Override
	protected String getDisplayName(ClientMeasurement object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	protected String getColumnData(ClientMeasurement object, int col) {
		return object.getName();
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.measurement();
	}

	@Override
	public void onAddNew() {
		InventoryActions action = InventoryActions.measurement();
		action.setCallback(new ActionCallback<ClientMeasurement>() {

			@Override
			public void actionResult(ClientMeasurement result) {
				if (result.getDisplayName() != null)
					addItemThenfireEvent(result);
			}
		});
		action.run(null, true);
	}
}
