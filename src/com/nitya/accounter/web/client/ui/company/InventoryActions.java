package com.nitya.accounter.web.client.ui.company;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.ui.AbstractBaseView;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.BuildAssemblyView;
import com.nitya.accounter.web.client.ui.HistoryTokens;
import com.nitya.accounter.web.client.ui.InventoryAssemblyView;
import com.nitya.accounter.web.client.ui.MainFinanceWindow;
import com.nitya.accounter.web.client.ui.core.Action;
import com.nitya.accounter.web.client.ui.settings.AddMeasurementView;
import com.nitya.accounter.web.client.ui.settings.InventoryCentreView;
import com.nitya.accounter.web.client.ui.settings.InventoryItemsListView;
import com.nitya.accounter.web.client.ui.settings.MeasurementListView;
import com.nitya.accounter.web.client.ui.settings.StockAdjustmentView;
import com.nitya.accounter.web.client.ui.settings.StockAdjustmentsListView;

public class InventoryActions extends Action {

	public static final int TYPE_NEW_ASSEMBLY = 1;
	public static final int TYPE_BUILD_ASSEMBLY = 2;
	public static final int TYPE_STOCK_ADJUSTMENT = 3;
	public static final int TYPE_INVETORY_ITEMS = 4;
	public static final int TYPE_STOCK_ADJUSTMENTS_LIST = 6;
	public static final int TYPE_MEASUREMENT = 7;
	public static final int TYPE_MEASUREMENTS_LIST = 8;
	public static final int TYPE_INVENTORY_CENTRE = 9;

	private int type;

	private String itemname;
	private boolean isItemEditable;

	public InventoryActions(int type) {
		super();
		this.type = type;
		catagory = messages.inventory();
	}

	@Override
	public void run() {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractBaseView view = null;
				switch (type) {
				case TYPE_NEW_ASSEMBLY:
					InventoryAssemblyView iView = GWT
							.create(InventoryAssemblyView.class);
					iView.setItemName(itemname);
					view = iView;
					break;
				case TYPE_BUILD_ASSEMBLY:
					view = new BuildAssemblyView();
					break;
				case TYPE_STOCK_ADJUSTMENT:
					view = new StockAdjustmentView();
					break;
				case TYPE_INVETORY_ITEMS:
					view = new InventoryItemsListView();
					break;
				case TYPE_STOCK_ADJUSTMENTS_LIST:
					view = new StockAdjustmentsListView();
					break;
				case TYPE_MEASUREMENT:
					view = new AddMeasurementView();
					break;
				case TYPE_MEASUREMENTS_LIST:
					view = new MeasurementListView();
					break;
				case TYPE_INVENTORY_CENTRE:
					view = new InventoryCentreView();
					break;
				}
				if (view != null) {
					MainFinanceWindow.getViewManager().showView(view, data,
							isDependent, InventoryActions.this);
					if (isItemEditable) {
						view.onEdit();
					}
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
		case TYPE_NEW_ASSEMBLY:
			return "inventoryassembly";
		case TYPE_BUILD_ASSEMBLY:
			return "buildassembly";
		case TYPE_STOCK_ADJUSTMENT:
			return "stockAdjustment";
		case TYPE_INVETORY_ITEMS:
			return HistoryTokens.INVENTORYITEMS;
		case TYPE_STOCK_ADJUSTMENTS_LIST:
			return "stockAdjustments";
		case TYPE_MEASUREMENT:
			return "addMeasurement";
		case TYPE_MEASUREMENTS_LIST:
			return "MeasurementList";
		case TYPE_INVENTORY_CENTRE:
			return "inventoryCentre";
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_NEW_ASSEMBLY:
			return "inventoryassembly";
		case TYPE_BUILD_ASSEMBLY:
			return "Build-Assembly";
		case TYPE_STOCK_ADJUSTMENT:
			return "stockAdjustment";
		case TYPE_INVETORY_ITEMS:
			return "inventoryItem";
		case TYPE_STOCK_ADJUSTMENTS_LIST:
			return "stockAdjustments";
		case TYPE_MEASUREMENT:
			return "add-measurement";
		case TYPE_MEASUREMENTS_LIST:
			return "MeasurementList";
		case TYPE_INVENTORY_CENTRE:
			return "inventoryCentre";
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_NEW_ASSEMBLY:
			return messages.inventoryAssembly();
		case TYPE_BUILD_ASSEMBLY:
			return messages.buildAssembly();
		case TYPE_STOCK_ADJUSTMENT:
			return messages.stockAdjustment();
		case TYPE_INVETORY_ITEMS:
			return messages.inventoryItems();
		case TYPE_STOCK_ADJUSTMENTS_LIST:
			return messages.stockAdjustments();
		case TYPE_MEASUREMENT:
			return messages.addMeasurementName();
		case TYPE_MEASUREMENTS_LIST:
			return messages.measurementList();
		case TYPE_INVENTORY_CENTRE:
			return messages.inventoryCentre();

		}
		return null;
	}

	public void setItemText(String itemname) {
		this.itemname = itemname;
	}

	public void setisItemEditable(boolean isItemEditable) {
		this.isItemEditable = isItemEditable;
	}

	public static InventoryActions newAssembly() {
		return new InventoryActions(TYPE_NEW_ASSEMBLY);
	}

	public static InventoryActions buildAssembly() {
		return new InventoryActions(TYPE_BUILD_ASSEMBLY);
	}

	public static InventoryActions stockAdjustment() {
		return new InventoryActions(TYPE_STOCK_ADJUSTMENT);
	}

	public static InventoryActions inventoryItems() {
		return new InventoryActions(TYPE_INVETORY_ITEMS);
	}

	public static InventoryActions stockAdjustments() {
		return new InventoryActions(TYPE_STOCK_ADJUSTMENTS_LIST);
	}

	public static InventoryActions measurement() {
		return new InventoryActions(TYPE_MEASUREMENT);
	}

	public static InventoryActions measurementsList() {
		return new InventoryActions(TYPE_MEASUREMENTS_LIST);
	}

	public static InventoryActions inventoyCentre() {
		return new InventoryActions(TYPE_INVENTORY_CENTRE);
	}

}
