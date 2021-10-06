package com.vimukti.accounter.web.client.ui.settings;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.core.ClientInventoryAssembly;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.ui.company.InventoryActions;
import com.vimukti.accounter.web.client.ui.company.NewItemAction;
import com.vimukti.accounter.web.client.ui.grids.BaseListGrid;
import com.vimukti.accounter.web.client.ui.grids.ListGrid;

public class InventoryCentreItemsListGrid extends BaseListGrid<ClientItem> {

	private ItemSelectionListener itemSelectionListener;
	private ClientItem selectedItem;
	ArrayList<ClientItem> listOfItems;

	public InventoryCentreItemsListGrid() {
		super(false, true);
		this.getElement().setId("InventoryCentreItemsListGrid");
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_TEXT };
	}

	@Override
	protected void executeDelete(ClientItem object) {
		// TODO Auto-generated method stub

	}

	@Override
	protected Object getColumnValue(ClientItem obj, int index) {
		return obj.getName();
	}

	@Override
	public void onDoubleClick(ClientItem obj) {
		if (Utility.isUserHavePermissions(obj.getObjectType())) {
			if (obj.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
				InventoryActions.newAssembly().run(
						(ClientInventoryAssembly) obj, false);
			} else {
				NewItemAction itemAction = new NewItemAction(true);
				itemAction.setType(obj.getType());
				itemAction.run(obj, false);
			}
		}
	}

	@Override
	protected void onClick(ClientItem obj, int row, int col) {
		setSelectedItem(obj);
		if (itemSelectionListener != null) {
			itemSelectionListener.itemSelected(obj);
		}
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.name() };
	}

	public ItemSelectionListener getItemSelectionListener() {
		return itemSelectionListener;
	}

	public void setItemSelectionListener(
			ItemSelectionListener itemSelectionListener) {
		this.itemSelectionListener = itemSelectionListener;
	}

	public ClientItem getSelectedItem() {
		return selectedItem;
	}

	public void setSelectedItem(ClientItem selectedItem) {
		this.selectedItem = selectedItem;
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "name" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "nameValue" };
	}

	@Override
	protected int sort(ClientItem obj1, ClientItem obj2, int index) {
		switch (index) {
		case 0:
			return obj1.getName().compareToIgnoreCase(obj2.getName());

		default:
			break;
		}

		return 0;
	}
}
