package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.settings.WareHouseItemsListView;
import com.nitya.accounter.web.client.ui.settings.WareHouseTransferView;
import com.nitya.accounter.web.client.ui.settings.WareHouseView;
import com.nitya.accounter.web.client.ui.settings.WarehouseListView;
import com.nitya.accounter.web.client.ui.settings.WarehouseTransferListView;

public class WarehouseActions extends Action {

	public static final int NEW_WAREHOUSE = 1;
	public static final int WAREHOUSE_TRANSFER = 2;
	public static final int WAREHOUSES_LIST = 3;
	public static final int WAREHOUSE_TRANSFERS_LIST = 4;
	public static final int WAREHOUSE_ITEMS_LIST = 5;

	private int type;
	private long warehouse;

	public WarehouseActions(int type) {
		super();
		this.type = type;
		this.catagory = messages.inventory();
	}

	public WarehouseActions(int type, long warehouseID) {
		this(type);
		this.warehouse = warehouseID;
	}

	@Override
	public void run() {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractBaseView view = null;
				switch (type) {
				case NEW_WAREHOUSE:
					view = GWT.create(WareHouseView.class);
					break;
				case WAREHOUSE_TRANSFER:
					view = new WareHouseTransferView();
					break;
				case WAREHOUSES_LIST:
					view = new WarehouseListView();
					break;
				case WAREHOUSE_TRANSFERS_LIST:
					view = new WarehouseTransferListView();
					break;
				case WAREHOUSE_ITEMS_LIST:
					view = new WareHouseItemsListView(warehouse);
					break;
				}
				if (view != null) {
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, WarehouseActions.this);
				}

			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getHistoryToken() {
		switch (type) {
		case NEW_WAREHOUSE:
			return "wareHouse";
		case WAREHOUSE_TRANSFER:
			return "wareHouseTransfer";
		case WAREHOUSES_LIST:
			return "WarehouseList";
		case WAREHOUSE_TRANSFERS_LIST:
			return "WarehouseTransferList";
		case WAREHOUSE_ITEMS_LIST:
			return "wareHouseItems";
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case NEW_WAREHOUSE:
			return "warehouse";
		case WAREHOUSE_TRANSFER:
			return "wareHouseTransfer";
		case WAREHOUSES_LIST:
			return "WarehouseList";
		case WAREHOUSE_TRANSFERS_LIST:
			return "WarehouseTransferList";
		case WAREHOUSE_ITEMS_LIST:
			return "wareHouseItems";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case NEW_WAREHOUSE:
			return messages.wareHouse();
		case WAREHOUSE_TRANSFER:
			return messages.wareHouseTransfer();
		case WAREHOUSES_LIST:
			return messages.warehouseList();
		case WAREHOUSE_TRANSFERS_LIST:
			return messages.warehouseTransferList();
		case WAREHOUSE_ITEMS_LIST:
			return messages.wareHouseItems();
		}
		return null;
	}

	public static WarehouseActions newWarehouse() {
		return new WarehouseActions(NEW_WAREHOUSE);
	}

	public static WarehouseActions warehouseTransfer() {
		return new WarehouseActions(WAREHOUSE_TRANSFER);
	}

	public static WarehouseActions warehousesList() {
		return new WarehouseActions(WAREHOUSES_LIST);
	}

	public static WarehouseActions warehouseTransfersList() {
		return new WarehouseActions(WAREHOUSE_TRANSFERS_LIST);
	}

	public static WarehouseActions warehouseItemsList(long warehouseID) {
		return new WarehouseActions(WAREHOUSE_ITEMS_LIST, warehouseID);
	}

}
