package com.nitya.accounter.web.client.ui.edittable.tables;

import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientItem;
import com.nitya.accounter.web.client.core.ClientMeasurement;
import com.nitya.accounter.web.client.core.ClientPayee;
import com.nitya.accounter.web.client.core.ClientPriceLevel;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.Features;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.edittable.DeleteColumn;
import com.nitya.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.nitya.accounter.web.client.ui.edittable.ItemNameColumn;
import com.nitya.accounter.web.client.ui.edittable.NewQuantityColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionClassColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionTaxableColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionUnitPriceListColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class CustomerItemTransactionTable extends
		CustomerTransactionTable {

	private ClientPriceLevel priceLevel;
	private TransactionUnitPriceListColumn transactionUnitPriceListColumn;

	/**
	 * Creates the instance
	 */
	public CustomerItemTransactionTable(boolean enableTax, boolean showTaxCode,
			ICurrencyProvider currencyProvider) {
		this(true, enableTax, showTaxCode, currencyProvider);
	}

	public CustomerItemTransactionTable(boolean enableTax, boolean showTaxCode,
			boolean enableDisCount, boolean showDisCount, boolean isTrackClass,
			boolean isClassPerDetailLine, ICurrencyProvider currencyProvider) {
		super(1, enableDisCount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableDisCount = enableDisCount;
		this.showDiscount = showDisCount;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
		addEmptyRecords();
	}

	public CustomerItemTransactionTable(boolean needDiscount,
			boolean enableTax, boolean showTaxCode,
			ICurrencyProvider currencyProvider) {
		super(1, needDiscount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		addEmptyRecords();
	}

	@Override
	public ClientTransactionItem getEmptyRow() {
		ClientTransactionItem item = new ClientTransactionItem();
		item.setType(ClientTransactionItem.TYPE_ITEM);
		return item;
	}

	@Override
	protected void initColumns() {

		this.addColumn(new ItemNameColumn(isSales(), currencyProvider) {

			@Override
			public ListFilter<ClientItem> getItemsFilter() {
				return new ListFilter<ClientItem>() {

					@Override
					public boolean filter(ClientItem e) {
						return e.isISellThisItem() && e.getIncomeAccount() != 0;
					}
				};
			}

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientItem newValue) {
				row.setAmountIncludeTAX(isShowPriceWithVat());
				super.setPriceLevel(priceLevel);
				super.setValue(row, newValue);
				updateDiscountValues(row);
				if (newValue != null
						&& (newValue.getType() == ClientItem.TYPE_INVENTORY_PART || newValue
								.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY)) {
					ClientMeasurement measurement = Accounter.getCompany()
							.getMeasurement(newValue.getMeasurement());
					if (row.getQuantity() == null) {
						ClientQuantity quantity = new ClientQuantity();
						quantity.setValue(1.0);
						row.setQuantity(quantity);
					}
					if (row.getUnitPrice() == null) {
						row.setUnitPrice(new Double(0));
					}
					if (row.getDiscount() == null) {
						row.setDiscount(new Double(0));
					}
					row.getQuantity().setUnit(
							measurement.getDefaultUnit().getId());
					row.setWareHouse(newValue.getWarehouse());
					row.setDescription(newValue.getSalesDescription());
				}
				update(row);
				// applyPriceLevel(row);
			}

			@Override
			protected String getDiscription(ClientItem item) {
				return item.getSalesDescription();
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return "";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new DescriptionEditColumn() {
			@Override
			public int getWidth() {
				return -1;
			}
		});

		this.addColumn(new NewQuantityColumn(false) {
			@Override
			protected void setValue(ClientTransactionItem row, String value) {
				super.setValue(row, value);
				update(row);
			}
		});
		transactionUnitPriceListColumn = new TransactionUnitPriceListColumn(
				true);

		if (Accounter.hasPermission(Features.HOSTORICAL_UNITPRICES)) {
			this.addColumn(transactionUnitPriceListColumn);
		} else {
			this.addColumn(new TransactionUnitPriceColumn(currencyProvider) {
				@Override
				public void setValue(ClientTransactionItem row, String value) {
					super.setValue(row, value);
					update(row);
				}
			});
		}

		if (needDiscount) {
			if (showDiscount) {
				this.addColumn(new TransactionDiscountColumn(currencyProvider));
			}
		}

		if (enableClass) {
			if (showClass) {
				this.addColumn(new TransactionClassColumn<ClientTransactionItem>() {

					@Override
					protected ClientAccounterClass getValue(
							ClientTransactionItem row) {
						return Accounter.getCompany().getAccounterClass(
								row.getAccounterClass());
					}

					@Override
					protected void setValue(ClientTransactionItem row,
							ClientAccounterClass newValue) {
						if (newValue != null) {
							row.setAccounterClass(newValue.getID());
							getTable().update(row);
						}
					}

					@Override
					public String getValueAsString(ClientTransactionItem row) {
						return "";
					}

					@Override
					public int insertNewLineNumber() {
						return 1;
					}

				});
			}
		}
		this.addColumn(new TransactionTotalColumn(currencyProvider, true));

		// if (getCompany().getPreferences().isChargeSalesTax()) {

		if (enableTax) {
			if (showTaxCode) {
				this.addColumn(new TransactionVatCodeColumn() {

					@Override
					protected ListFilter<ClientTAXCode> getTaxCodeFilter() {
						return new ListFilter<ClientTAXCode>() {

							@Override
							public boolean filter(ClientTAXCode e) {
								if (!e.isTaxable()
										|| e.getTAXItemGrpForSales() != 0) {
									return true;
								}
								return false;
							}
						};
					}

					@Override
					protected void setValue(ClientTransactionItem row,
							ClientTAXCode newValue) {
						super.setValue(row, newValue);
						update(row);
					}

					@Override
					protected boolean isSales() {
						return true;
					}

					@Override
					public String getValueAsString(ClientTransactionItem row) {
						return "";
					}

					@Override
					public int insertNewLineNumber() {
						return 1;
					}
				});

				this.addColumn(new TransactionVatColumn(currencyProvider));
			} else {
				this.addColumn(new TransactionTaxableColumn());
			}
		}

		// }

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

	@Override
	public void updateAmountsFromGUI() {
		super.updateAmountsFromGUI();

	}

	public void setPricingLevel(ClientPriceLevel priceLevel) {
		this.priceLevel = priceLevel;
	}

	public void setPayee(ClientPayee payee) {
		if (transactionUnitPriceListColumn != null) {
			transactionUnitPriceListColumn.setPayee(payee);
		}
	}
}
