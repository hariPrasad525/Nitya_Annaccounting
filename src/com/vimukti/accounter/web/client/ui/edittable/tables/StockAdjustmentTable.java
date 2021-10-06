package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientUnit;
import com.vimukti.accounter.web.client.core.ClientWarehouse;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.DataUtils;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.ItemsDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.NewQuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class StockAdjustmentTable extends
		EditTable<ClientTransactionItem> {

	protected ICurrencyProvider currencyProvider;

	ItemsDropDownTable itemTable;

	public StockAdjustmentTable(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		addEmptyRecords();
	}

	protected abstract ClientWarehouse getSelectedWareHouse();

	@Override
	protected void initColumns() {
		itemTable = new ItemsDropDownTable(

		new ListFilter<ClientItem>() {

			@Override
			public boolean filter(ClientItem e) {
				if ((e.getType() == ClientItem.TYPE_INVENTORY_PART || e
						.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY)
						& (e.getWarehouse() == getSelectedWareHouse().getID())) {
					return true;
				} else {
					return false;
				}
			}
		});

		itemTable.setItemType(ClientItem.TYPE_INVENTORY_PART);

		ComboColumn<ClientTransactionItem, ClientItem> comboColumn = new ComboColumn<ClientTransactionItem, ClientItem>() {

			@Override
			protected ClientItem getValue(ClientTransactionItem row) {
				return getItem(row.getItem());
			}

			@Override
			protected void setValue(ClientTransactionItem row, ClientItem item) {
				row.setItem(item.getID());
				row.setType(ClientTransactionItem.TYPE_ITEM);
				ClientQuantity qty = new ClientQuantity();
				qty.setUnit(item.getOnhandQty().getUnit());
				row.setQuantity(qty);
				row.setUnitPrice(0.00D);
				row.setDiscount(0.00D);
				update(row);
			}

			@Override
			public AbstractDropDownTable<ClientItem> getDisplayTable(
					ClientTransactionItem row) {
				return itemTable;
			}

			@Override
			public int getWidth() {
				return 150;
			}

			@Override
			protected String getColumnName() {
				return messages.item();
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return messages.item() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

		};
		this.addColumn(comboColumn);

		this.addColumn(new TextEditColumn<ClientTransactionItem>() {

			@Override
			protected String getValue(ClientTransactionItem row) {
				return row.getDescription();
			}

			@Override
			protected void setValue(ClientTransactionItem row, String value) {
				row.setDescription(value);
			}

			@Override
			protected String getColumnName() {
				return messages.comment();
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 3;
			}
		});
		if (!isInViewMode()) {
			this.addColumn(new TextEditColumn<ClientTransactionItem>() {

				@Override
				protected String getValue(ClientTransactionItem row) {
					ClientItem item = getItem(row.getItem());
					if (item == null || item.getOnhandQty() == null
							|| row.getQuantity() == null) {
						return "";
					} else {
						ClientQuantity onhandQty = item.getOnhandQty();
						StringBuffer data = new StringBuffer();
						data.append(String.valueOf(onhandQty.getValue()));
						if (getPreferences().isUnitsEnabled()) {
							ClientUnit unit = getCompany().getUnitById(
									row.getQuantity().getUnit());
							data.append(" ");
							if (unit != null) {
								data.append(unit.getType());
							}
						}
						return data.toString();
					}
				}

				@Override
				protected void setValue(ClientTransactionItem row, String value) {
					// Nothing to do.
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.currentQty();
				}

				@Override
				public int getWidth() {
					return 100;
				}

				@Override
				public String getValueAsString(ClientTransactionItem row) {
					return messages.currentQty() + " : " + getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}
			});

			this.addColumn(new NewQuantityColumn(true) {

				@Override
				protected String getColumnName() {
					return messages.adjustedQty();
				}

				@Override
				protected boolean isMultipleWarehouseEnabled() {
					return false;
				}

				@Override
				protected boolean isUnitsEnabled() {
					return false;
				}

				@Override
				protected String getValue(ClientTransactionItem row) {
					ClientItem item = getItem(row.getItem());
					if (item == null || row.getQuantity() == null) {
						return "";
					}
					ClientQuantity onhandQty = item.getOnhandQty();
					ClientQuantity qty = DataUtils.addQuantities(onhandQty,
							row.getQuantity());
					StringBuffer data = new StringBuffer();
					data.append(String.valueOf(qty.getValue()));
					if (getPreferences().isUnitsEnabled()) {
						ClientUnit unit = getCompany().getUnitById(
								row.getQuantity().getUnit());
						data.append(" ");
						if (unit != null) {
							data.append(unit.getType());
						}
					}
					return data.toString();
				}

				@Override
				protected void setQuantity(ClientTransactionItem row,
						ClientQuantity quantity) {
					if (quantity == null || quantity.getValue() == 0) {
						return;
					}
					ClientItem item = getItem(row.getItem());
					ClientQuantity onhandQty = item.getOnhandQty();
					ClientQuantity qty = DataUtils.subtractQuantities(quantity,
							onhandQty);
					super.setQuantity(row, qty);
				}

				@Override
				protected ClientQuantity getQuantity(ClientTransactionItem row) {
					ClientItem item = getItem(row.getItem());
					if (item == null) {
						return row.getQuantity();
					}
					ClientQuantity onhandQty = item.getOnhandQty();
					ClientQuantity addQuantities = DataUtils.addQuantities(
							onhandQty, row.getQuantity());
					return addQuantities;
				}

			});

			this.addColumn(new AmountColumn<ClientTransactionItem>(
					currencyProvider, false) {

				@Override
				protected Double getAmount(ClientTransactionItem row) {
					ClientItem item = getItem(row.getItem());
					if (item != null) {
						return item.getAssetValue();
					}
					return 0.00D;
				}

				@Override
				protected void setAmount(ClientTransactionItem row, Double value) {
					// It won't come here
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.currentValue();
				}

				@Override
				public int getWidth() {
					return 100;
				}

				@Override
				public String getValueAsString(ClientTransactionItem row) {
					// TODO Auto-generated method stub
					return messages.currentValue() + " : " + getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}

			});
		} else {

			this.addColumn(new NewQuantityColumn(true) {

				@Override
				protected String getColumnName() {
					return messages.quantityDiff();
				}

				@Override
				protected boolean isMultipleWarehouseEnabled() {
					return false;
				}

				@Override
				protected String getValue(ClientTransactionItem row) {
					if (row.getQuantity() == null) {
						return "";
					}
					StringBuffer data = new StringBuffer();
					data.append(String.valueOf(row.getQuantity().getValue()));
					if (getPreferences().isUnitsEnabled()) {
						ClientUnit unit = getCompany().getUnitById(
								row.getQuantity().getUnit());
						data.append(" ");
						if (unit != null) {
							data.append(unit.getType());
						}
					}
					return data.toString();
				}

				@Override
				public int getWidth() {
					return 150;
				}

			});
		}

		this.addColumn(new AmountColumn<ClientTransactionItem>(
				currencyProvider, false) {

			@Override
			protected Double getAmount(ClientTransactionItem row) {
				return row.getUnitPrice();
			}

			@Override
			protected void setAmount(ClientTransactionItem row, Double value) {
				row.setUnitPrice(value);
				if (row.getQuantity() != null) {
					row.setLineTotal(row.getQuantity().getValue() * value);
				}
			}

			@Override
			protected String getColumnName() {
				return messages.salesOrPurchaseRate();
			}

			@Override
			public int getWidth() {
				return 150;
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return messages.salesOrPurchaseRate() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}

		});

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

	protected abstract String getAvailableQuantity(long item);

	public void refresh() {
		for (ClientTransactionItem row : getAllRows()) {
			update(row);
		}
	}

	@Override
	public void setAllRows(List<ClientTransactionItem> rows) {
		for (ClientTransactionItem row : rows) {
			row.setID(0);
		}
		super.setAllRows(rows);
	}

	@Override
	public List<ClientTransactionItem> getAllRows() {
		List<ClientTransactionItem> selectedRecords = new ArrayList<ClientTransactionItem>();
		List<ClientTransactionItem> allRows = super.getAllRows();
		for (ClientTransactionItem transactionItem : allRows) {
			if (transactionItem.getItem() != 0) {
				selectedRecords.add(transactionItem);
			}
		}
		return selectedRecords;
	}

	@Override
	protected ClientTransactionItem getEmptyRow() {
		ClientTransactionItem item = new ClientTransactionItem();
		item.setType(ClientTransactionItem.TYPE_ITEM);
		return item;
	}

	private ClientItem getItem(long itemId) {
		if (itemId == 0) {
			return null;
		}
		return getCompany().getItem(itemId);
	}

	private ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	public List<ClientTransactionItem> getRecords() {
		return getAllRows();
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		for (ClientTransactionItem transactionItem : this.getRecords()) {
			ClientItem item = getCompany().getItem(transactionItem.getItem());
			double onhandQty = item.getOnhandQty().getValue();
			double adjustQty = transactionItem.getQuantity().getValue();
			if ((-1 * onhandQty) > adjustQty) {
				result.addError(transactionItem.getQuantity(),
						messages.adjustedQtyNegative());
			}
			if (transactionItem.getUnitPrice() <= 0) {
				result.addError(transactionItem.getUnitPrice(),
						messages.pleaseEnter(messages.salesOrPurchaseRate()));
			}
		}
		return result;
	}

	public void wareHouseChanged() {
		clear();
		itemTable = new ItemsDropDownTable(

		new ListFilter<ClientItem>() {

			@Override
			public boolean filter(ClientItem e) {
				if ((e.getType() == ClientItem.TYPE_INVENTORY_PART || e
						.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY)
						& (e.getWarehouse() == getSelectedWareHouse().getID())) {
					return true;
				} else {
					return false;
				}
			}
		});

	}

}
