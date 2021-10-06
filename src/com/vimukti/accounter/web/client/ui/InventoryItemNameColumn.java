package com.vimukti.accounter.web.client.ui;

import com.vimukti.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.ItemsDropDownTable;

public class InventoryItemNameColumn extends
		ComboColumn<ClientInventoryAssemblyItem, ClientItem> {

	ItemsDropDownTable itemsList = new ItemsDropDownTable(getItemsFilter());
	private final ICurrencyProvider currencyProvider;
	private ClientPriceLevel priceLevel;

	public InventoryItemNameColumn(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
	}

	@Override
	protected ClientItem getValue(ClientInventoryAssemblyItem row) {
		return Accounter.getCompany().getItem(row.getInventoryItem());
	}

	public ListFilter<ClientItem> getItemsFilter() {
		return new ListFilter<ClientItem>() {

			@Override
			public boolean filter(ClientItem e) {
				return (e.getType() == ClientItem.TYPE_INVENTORY_PART
						|| e.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY
						|| e.getType() == ClientItem.TYPE_NON_INVENTORY_PART || e
							.getType() == ClientItem.TYPE_SERVICE);
			}
		};
	}

	@Override
	@SuppressWarnings({ "unchecked", "rawtypes" })
	public AbstractDropDownTable getDisplayTable(ClientInventoryAssemblyItem row) {
		itemsList = new ItemsDropDownTable(getItemsFilter()) {
		};
		return itemsList;
	}

	@Override
	public int getWidth() {
		return 150;
	}

	@Override
	protected void setValue(ClientInventoryAssemblyItem row, ClientItem newValue) {
		if (newValue != null) {
			Double unitPrice = 0.0;
			Double itemPrice = newValue.getAverageCost();
			if (newValue.getType() == ClientItem.TYPE_NON_INVENTORY_PART) {
				itemPrice = newValue.getAverageCost();
			}

			if (!isSameItems(row, newValue)) {
				unitPrice = itemPrice / currencyProvider.getCurrencyFactor();
			} else {
				unitPrice = newValue.getAverageCost();
			}

			if (unitPrice.equals((0.0 / 0.0))) {
				unitPrice = 0.0;
			}
			if (getPreferences().isPricingLevelsEnabled()) {
				if (priceLevel != null && !priceLevel.isDefault()) {
					unitPrice = getPriceLevel(unitPrice);
				}
			}

			row.setPurchaseCost(unitPrice);
		}
		row.setInventoryItem(newValue.getID());
		onValueChange(row);
		row.setDescription(getDiscription(newValue));
		ClientQuantity quantity = new ClientQuantity();
		quantity.setValue(1.0);
		row.setQuantity(quantity);
		getTable().update(row);
	}

	private boolean isSameItems(ClientInventoryAssemblyItem row,
			ClientItem newValue) {
		if (row.getInventoryItem() == newValue.getID()) {
			return true;
		}
		return false;
	}

	protected String getDiscription(ClientItem newValue) {
		return newValue.getPurchaseDescription();
	}

	@Override
	protected String getColumnName() {
		return messages.name();
	}

	public void setItemForCustomer(boolean isForCustomer) {
		itemsList.setForCustomer(isForCustomer);
	}

	protected Double getPriceLevel(Double unitPrice) {
		double amount = 0.0;
		if (unitPrice == 0) {
			return unitPrice;
		} else {
			if (priceLevel.isPriceLevelDecreaseByThisPercentage()) {
				amount = unitPrice
						- (priceLevel.getPercentage() * (unitPrice / 100));
			} else {
				amount = unitPrice
						+ (priceLevel.getPercentage() * (unitPrice / 100));
			}

		}
		return amount;
	}

	public void setPriceLevel(ClientPriceLevel priceLevel) {
		this.priceLevel = priceLevel;
	}

	@Override
	public boolean isPrimaryColumn() {
		return true;
	}

	@Override
	public int insertNewLineNumber() {
		return 1;
	}

	@Override
	public String getValueAsString(ClientInventoryAssemblyItem row) {
		return getValue(row).toString();
	}

}