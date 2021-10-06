package com.nitya.accounter.web.client.ui.grids;

import com.google.gwt.user.client.ui.CheckBox;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientInventoryAssembly;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientMeasurement;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.ItemListView;
import com.nitya.accounter.web.client.ui.company.InventoryActions;
import com.nitya.accounter.web.client.ui.company.NewItemAction;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class InventoryItemsListGrid extends BaseListGrid<ClientItem> {

	private ClientCurrency currency = getCompany().getPrimaryCurrency();
	private boolean isGeneratedFromCus;

	public InventoryItemsListGrid(boolean isMultiSelectionEnable) {
		super(isMultiSelectionEnable);
		this.getElement().setId("InventoryItemsListGrid");
	}

	public InventoryItemsListGrid(boolean isMultiSelectionEnable,
			boolean isGeneratedFromCus) {
		this(isMultiSelectionEnable);
		this.isGeneratedFromCus = isGeneratedFromCus;
	}

	@Override
	protected int[] setColTypes() {
		return new int[] { ListGrid.COLUMN_TYPE_CHECK,
				ListGrid.COLUMN_TYPE_LINK, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_TEXT,
				ListGrid.COLUMN_TYPE_TEXT, ListGrid.COLUMN_TYPE_DECIMAL_TEXT,
				ListGrid.COLUMN_TYPE_DECIMAL_TEXT, ListGrid.COLUMN_TYPE_IMAGE };
	}

	@Override
	protected int getCellWidth(int index) {
		if (index == 0)
			return 40;
		else if (index == 1)
			return 130;
		else if (index == 2)
			return 100;
		else if (index == 3)
			return 150;
		else if (index == 4)
			return 100;
		else if (index == 5)
			return 100;
		else if (index == 6)
			return 100;
		else if (index == 7)
			return 100;
		else {
			return 25;
		}
	}

	@Override
	protected Object getColumnValue(ClientItem obj, int col) {
		return getInventoryItemColumnValue(obj, col);

	}

	/**
	 * 
	 * @param obj
	 * @return
	 */
	private String getspaces(ClientItem obj) {
		// Element element = this.getCellByWidget(view.getGrid().getWidget(1))
		// .getElement();
		// Element element = view.getGrid().cellFormatter.getElement(view
		// .getGrid().getCurrentRow(), view.getGrid().getCurrentColumn());
		int depth = obj.getDepth();
		// // for adding space
		// element.addClassName("depth" + depth);
		cellFormatter.addStyleName(currentRow, currentCol, "depth" + depth);
		return obj.getName();
	}

	private Object getInventoryItemColumnValue(ClientItem obj, int col) {
		ClientMeasurement measurement = getCompany().getMeasurement(
				obj.getMeasurement());
		switch (col) {
		case 0:
			return obj.isActive();
		case 1:
			return obj.getName() != null ? getspaces(obj) : "";
		case 2:
			if (!ItemListView.isPurchaseType) {
				return obj.getSalesDescription() != null ? obj
						.getSalesDescription() : "";
			} else
				return obj.getPurchaseDescription() != null ? obj
						.getPurchaseDescription() : "";

		case 3:
			return Utility.getItemTypeText(obj) != null ? Utility
					.getItemTypeText(obj) : "";
		case 4:

			return obj.getOnhandQty() != null ? DecimalUtil.round(obj
					.getOnhandQty().getValue())
					+ "("
					+ measurement.getDefaultUnit().getType() + ")" : "";
		case 5:
			return obj.getReorderPoint() != null ? DecimalUtil.round(obj
					.getReorderPoint().getValue())
					+ "("
					+ measurement.getDefaultUnit().getType() + ")" : "";
		case 6:
			return DataUtils.amountAsStringWithCurrency(obj.getSalesPrice(),
					currency) != null ? DataUtils.amountAsStringWithCurrency(
					obj.getSalesPrice(), currency) : "";
		case 7:
			return DataUtils.amountAsStringWithCurrency(obj.getPurchasePrice(),
					currency) != null ? DataUtils.amountAsStringWithCurrency(
					obj.getPurchasePrice(), currency) : "";

		case 8:
			return Accounter.getFinanceMenuImages().delete();
		}
		return null;
	}

	@Override
	protected String[] getColumns() {
		return new String[] { messages.active(), messages.itemName(),
				messages.description(), messages.type(), messages.onHandQty(),
				messages.reorderPoint(), messages.salesPrice(),
				messages.purchasePrice(), "" };

	}

	@Override
	public void onDoubleClick(ClientItem obj) {
		if (isCanOpenTransactionView(0, IAccounterCore.ITEM)) {
			if (obj.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
				InventoryActions.newAssembly().run(
						(ClientInventoryAssembly) obj, false);
			} else {
				NewItemAction itemAction = new NewItemAction(
						ItemListView.isSalesType);
				itemAction.setType(obj.getType());
				itemAction.run(obj, false);
			}
		}
	}

	protected void onClick(ClientItem item, int row, int col) {
		if (!isCanOpenTransactionView(0, IAccounterCore.ITEM)) {
			return;
		}

		if (getColumnType(col) == COLUMN_TYPE_IMAGE) {
			if (item != null)
				showWarnDialog(item);
		}

	}

	protected void executeDelete(final ClientItem recordToBeDeleted) {
		deleteObject(recordToBeDeleted);
	}

	@Override
	protected int sort(ClientItem item1, ClientItem item2, int index) {
		if (ItemListView.isPurchaseType && ItemListView.isSalesType) {
			if (index == 4) {
				Double price1 = item1.getSalesPrice();
				Double price2 = item2.getSalesPrice();
				return price1.compareTo(price2);
			} else if (index == 5) {
				Double price1 = item1.getPurchasePrice();
				Double price2 = item2.getPurchasePrice();
				return price1.compareTo(price2);
			}
		} else {
			switch (index) {
			case 1:
				return item1.getName().toLowerCase()
						.compareTo(item2.getName().toLowerCase());
			case 2:
				if (!ItemListView.isPurchaseType) {
					String obj1 = item1.getSalesDescription() != null ? item1
							.getSalesDescription() : "";
					String obj2 = item2.getSalesDescription() != null ? item2
							.getSalesDescription() : "";
					return obj1.toLowerCase().compareTo(obj2.toLowerCase());
				} else {
					String obj1 = item1.getPurchaseDescription() != null ? item1
							.getPurchaseDescription() : "";
					String obj2 = item2.getPurchaseDescription() != null ? item2
							.getPurchaseDescription() : "";
					return obj1.toLowerCase().compareTo(obj2.toLowerCase());
				}

			case 3:
				String type1 = Utility.getItemTypeText(item1) != null ? Utility
						.getItemTypeText(item1) : "";
				String type2 = Utility.getItemTypeText(item2) != null ? Utility
						.getItemTypeText(item2) : "";
				return type1.compareTo(type2);

			case 4:
				Double avgCost1 = item1.getAverageCost();
				Double avgCost2 = item2.getAverageCost();
				return avgCost1.compareTo(avgCost2);
			case 5:
				if (!ItemListView.isPurchaseType) {
					Double price1 = item1.getSalesPrice();
					Double price2 = item2.getSalesPrice();
					return price1.compareTo(price2);
				} else {
					Double price1 = item1.getPurchasePrice();
					Double price2 = item2.getPurchasePrice();
					return price1.compareTo(price2);
				}

			default:
				break;
			}
		}
		return 0;
	}

	public AccounterCoreType getType() {
		return AccounterCoreType.ITEM;
	}

	@Override
	public void addData(ClientItem obj) {
		super.addData(obj);
		((CheckBox) this.getWidget(currentRow, 0)).setEnabled(false);
	}

	@Override
	public void headerCellClicked(int colIndex) {
		super.headerCellClicked(colIndex);
		for (int i = 0; i < this.getTableRowCount(); i++) {
			((CheckBox) this.getWidget(i, 0)).setEnabled(false);
		}
	}

	@Override
	protected String[] setHeaderStyle() {
		return new String[] { "active", "itemName", "description", "type",
				"onHandQty", "reorderpoint", "salesPrice", "purchasePrice",
				"col-last" };
	}

	@Override
	protected String[] setRowElementsStyle() {
		return new String[] { "active-value", "itemName-value",
				"description-value", "type-value", "onHandQty-value",
				"reorderpoint-value", "salesPrice-value",
				"purchasePrice-value", "col-last-value" };
	}
}
