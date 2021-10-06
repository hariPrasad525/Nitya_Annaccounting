package com.vimukti.accounter.web.client.ui.combo;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.company.WarehouseActions;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;

public class WarehouseCombo extends CustomCombo<ClientWarehouse> {

	public WarehouseCombo(String title) {
		super(title, "WarehouseCombo");
		initCombo(Accounter.getCompany().getWarehouses());
	}

	public WarehouseCombo(String title, boolean isAddNewRequire,
			boolean isneedAll) {
		super(title, isAddNewRequire, 1, "WarehouseCombo");
		ArrayList<ClientWarehouse> warehouses = new ArrayList<ClientWarehouse>();
		ClientWarehouse warehouse = new ClientWarehouse();
		warehouse.setName(messages.all());
		warehouses.add(warehouse);
		warehouses.addAll(Accounter.getCompany().getWarehouses());
		initCombo(warehouses);
	}

	public WarehouseCombo(String title, boolean isAddNewRequire) {
		super(title, isAddNewRequire, 1, "WarehouseCombo");
		initCombo(Accounter.getCompany().getWarehouses());
	}

	@Override
	public String getDefaultAddNewCaption() {
		return messages.wareHouse();
	}

	@Override
	protected String getDisplayName(ClientWarehouse object) {
		if (object != null)
			return object.getName() != null ? object.getName() : "";
		else
			return "";
	}

	@Override
	public void onAddNew() {
		Action<ClientWarehouse> action = WarehouseActions.newWarehouse();
		action.setCallback(new ActionCallback<ClientWarehouse>() {

			@Override
			public void actionResult(ClientWarehouse result) {
				addItemThenfireEvent(result);

			}
		});
		action.run(null, true);

	}

	@Override
	protected String getColumnData(ClientWarehouse object, int col) {
		switch (col) {
		case 0:
			return object.getName();
		}
		return null;
	}

}
