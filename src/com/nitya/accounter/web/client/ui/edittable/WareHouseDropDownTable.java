package com.nitya.accounter.web.client.ui.edittable;

import java.util.List;

import com.google.gwt.user.cellview.client.TextColumn;
import com.nitya.accounter.web.client.core.ClientWarehouse;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.company.WarehouseActions;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.core.ActionCallback;

public class WareHouseDropDownTable extends
		AbstractDropDownTable<ClientWarehouse> {

	public WareHouseDropDownTable() {
		super(Accounter.getCompany().getWarehouses(), true);
	}

	@Override
	public List<ClientWarehouse> getTotalRowsData() {
		return Accounter.getCompany().getWarehouses();
	}

	@Override
	protected ClientWarehouse getAddNewRow() {
		ClientWarehouse wareHouse = new ClientWarehouse();
		wareHouse.setName(messages.addNewWareHouse());
		return wareHouse;
	}

	@Override
	public void initColumns() {
		TextColumn<ClientWarehouse> textColumn = new TextColumn<ClientWarehouse>() {

			@Override
			public String getValue(ClientWarehouse object) {
				return object.getDisplayName();
			}
		};
		this.addColumn(textColumn);
	}

	@Override
	protected boolean filter(ClientWarehouse t, String string) {
		return t.getDisplayName().toLowerCase().startsWith(string);
	}

	@Override
	protected String getDisplayValue(ClientWarehouse value) {
		return value.getDisplayName();
	}

	@Override
	protected void addNewItem(String text) {
		Action<ClientWarehouse> action = WarehouseActions.newWarehouse();
		action.setCallback(new ActionCallback<ClientWarehouse>() {

			@Override
			public void actionResult(ClientWarehouse result) {
				selectRow(result);
			}
		});
		action.run(null, true);
	}

	@Override
	protected void addNewItem() {
		addNewItem("");
	}

}
