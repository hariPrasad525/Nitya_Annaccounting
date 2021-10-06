package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientInventoryAssemblyItem;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientMeasurement;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.edittable.DeleteColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.InventoryTotalColumn;

public abstract class InventoryAssemblyItemTable extends
		EditTable<ClientInventoryAssemblyItem> implements ICurrencyProvider {

	public static final int INVENTORY_ASSEMBLY_ITEM = 1;
	public static final int BUILD_ASSEMBLY = 2;

	double lineTotal;

	protected ICurrencyProvider currencyProvider;
	private int type;

	public InventoryAssemblyItemTable(int type,
			ICurrencyProvider currencyProvider) {
		this.type = type;
		this.currencyProvider = currencyProvider;
	}

	protected abstract void updateNonEditableItems();

	protected abstract long getAssembly();

	@Override
	protected void initColumns() {
		InventoryItemNameColumn transactionItemNameColumn = new InventoryItemNameColumn(
				currencyProvider) {

			@Override
			public ListFilter<ClientItem> getItemsFilter() {
				return new ListFilter<ClientItem>() {

					@Override
					public boolean filter(ClientItem e) {
						if (getAssembly() == e.getID()) {
							return false;
						}
						return (e.getType() == ClientItem.TYPE_INVENTORY_PART
								|| e.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY
								|| e.getType() == ClientItem.TYPE_NON_INVENTORY_PART || e
								.getType() == ClientItem.TYPE_SERVICE);
					}

				};
			}

			@Override
			protected void setValue(ClientInventoryAssemblyItem row,
					ClientItem newValue) {
				super.setValue(row, newValue);
				if (newValue != null) {
					ClientQuantity quantity;
					if (row.getQuantity() == null) {
						quantity = new ClientQuantity();
						quantity.setValue(1.0);
					} else {
						quantity = row.getQuantity();
					}
					row.setQuantity(quantity);
					long inventoryItem = row.getInventoryItem();
					double lt = 0;
					if (inventoryItem != 0) {
						lt = quantity.getValue()
								* Accounter.getCompany().getItem(inventoryItem)
										.getAverageCost();
					}
					row.setLineTotal(lt);
					ClientMeasurement measurement = Accounter.getCompany()
							.getMeasurement(newValue.getMeasurement());
					if (measurement != null) {
						row.getQuantity().setUnit(
								measurement.getDefaultUnit().getId());
					}
					row.setWareHouse(newValue.getWarehouse());
					row.setDescription(newValue.getPurchaseDescription());
				}
				if (newValue.getExpenseAccount() == 0) {
					Accounter.showError(messages
							.youCannotSelectTheItemWhichHaveNoExpenseAccount());
				}
				update(row);
				// if (getAllRows().indexOf(row) == getAllRows().size() - 1) {
				// addEmptyRowAtLast();
				// }
			}

			@Override
			protected String getDiscription(ClientItem item) {
				return item.getPurchaseDescription();
			}

		};
		transactionItemNameColumn.setItemForCustomer(false);

		this.addColumn(transactionItemNameColumn);

		this.addColumn(new InventoryDescriptionEditColumn());

		if (type == BUILD_ASSEMBLY) {
			this.addColumn(new InventoryQuantityColumn() {

				@Override
				protected ClientQuantity getQuantity(
						ClientInventoryAssemblyItem row) {
					ClientItem item = getCompany().getItem(
							row.getInventoryItem());
					return item.getOnhandQty();
				}

				@Override
				public int getWidth() {
					return 150;
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.quantityOnHand();
				}

			});
		}

		this.addColumn(new InventoryQuantityColumn() {
			@Override
			protected String getColumnName() {
				return messages.quantityNeeded();
			}

			@Override
			protected boolean isEnable() {
				return type != BUILD_ASSEMBLY;
			}
		});

		this.addColumn(new InventoryTransactionUnitPriceColumn(currencyProvider) {
			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected boolean isEnable() {
				return false;
			}
		});

		this.addColumn(new InventoryTotalColumn(currencyProvider) {
			@Override
			public int getWidth() {
				return 80;
			}
		});
		if (type == INVENTORY_ASSEMBLY_ITEM) {
			this.addColumn(new DeleteColumn<ClientInventoryAssemblyItem>());
		}
	}

	@Override
	protected ClientInventoryAssemblyItem getEmptyRow() {
		return new ClientInventoryAssemblyItem();
	}

	public void updateTotals() {

		List<ClientInventoryAssemblyItem> allrecords = getAllRows();
		lineTotal = 0.0;

		for (ClientInventoryAssemblyItem record : allrecords) {
			if (!record.isEmpty()) {

				Double lineTotalAmt = record.getLineTotal();
				if (lineTotalAmt != null) {
					lineTotal += lineTotalAmt;
				}
			}
		}

		updateNonEditableItems();

	}

	@Override
	public void delete(ClientInventoryAssemblyItem row) {
		super.delete(row);
		updateTotals();
	}

	@Override
	public void setAllRows(List<ClientInventoryAssemblyItem> rows) {
		createColumns();
		for (ClientInventoryAssemblyItem item : rows) {
			item.setID(0);
		}
		super.setAllRows(rows);

	}

	@Override
	public void addRows(List<ClientInventoryAssemblyItem> rows) {
		for (ClientInventoryAssemblyItem item : getRecords()) {
			if (item.getInventoryItem() == 0) {
				delete(item);
			}
		}
		createColumns();
		super.addRows(rows);
		List<ClientInventoryAssemblyItem> itemList = new ArrayList<ClientInventoryAssemblyItem>();
		if (getAllRows().size() < 4) {
			for (int ii = 0; ii < (4 - getAllRows().size()); ii++) {
				ClientInventoryAssemblyItem item = new ClientInventoryAssemblyItem();
				itemList.add(item);
			}
			createColumns();
			super.addRows(itemList);
		}
	}

	protected ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	@Override
	public void update(ClientInventoryAssemblyItem row) {
		super.update(row);
		updateTotals();
	}

	public double getLineTotal() {
		return lineTotal;
	}

	public void removeAllRecords() {
		clear();
	}

	public void setRecords(List<ClientInventoryAssemblyItem> transactionItems) {
		if (!transactionItems.isEmpty()) {
			setAllRows(transactionItems);
			updateTotals();
		} else {
			clear();
			addEmptyRecords();
		}
	}

	public void resetRecords() {
		clear();
		addEmptyRecords();
	}

	public List<ClientInventoryAssemblyItem> getRecords() {
		return getAllRows();
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		// Validations
		// 1. checking for the name of the transaction item
		// 2. checking for the vat code if the company is of type UK
		// TODO::: check whether this validation is working or not
		int i = 0;
		for (ClientInventoryAssemblyItem transactionItem : this.getRecords()) {
			if (transactionItem.isEmpty()) {
				continue;
			}
			if (transactionItem.getInventoryItem() == 0) {
				result.addError("GridItem-",
						messages.pleaseSelect(messages.inventoryItem()));
			}
			if (transactionItem.getInventoryItem() != 0
					&& getCompany().getItem(transactionItem.getInventoryItem())
							.getExpenseAccount() == 0) {
				result.addError("transactionItem " + i, messages
						.youCannotSelectTheItemWhichHaveNoExpenseAccount());
			}
			i++;
		}
		if (DecimalUtil.isLessThan(lineTotal, 0.0)) {
			result.addError(this, messages.invalidTransactionAmount());
		}
		return result;
	}

	public void updateAmountsFromGUI() {
		updateTotals();
		for (ClientInventoryAssemblyItem item : this.getAllRows()) {
			updateFromGUI(item);
			update(item);
		}
		updateColumnHeaders();
	}

	public void updateRecords() {
		for (ClientInventoryAssemblyItem item : this.getAllRows()) {
			update(item);
		}
	}

	@Override
	protected abstract boolean isInViewMode();

	public boolean isEmpty() {
		List<ClientInventoryAssemblyItem> records = getRecords();
		boolean isEmpty = true;
		for (ClientInventoryAssemblyItem item : records) {
			if (!item.isEmpty()) {
				isEmpty = false;
				break;
			}
		}
		return isEmpty;
	}

	public List<ClientInventoryAssemblyItem> getTransactionItems() {
		List<ClientInventoryAssemblyItem> list = new ArrayList<ClientInventoryAssemblyItem>();
		for (ClientInventoryAssemblyItem item : getRecords()) {
			if (!item.isEmpty()) {
				list.add(item);
			}
		}
		return list;
	}

	@Override
	public ClientCurrency getTransactionCurrency() {
		return getCompany().getPrimaryCurrency();
	}

	@Override
	public Double getCurrencyFactor() {
		return 1.0;
	}

	@Override
	public Double getAmountInBaseCurrency(Double amount) {
		return amount;
	}

}
