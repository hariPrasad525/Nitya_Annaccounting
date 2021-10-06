package com.vimukti.accounter.web.client.ui.edittable.tables;

import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientItem;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.ClientMeasurement;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.Features;
import com.vimukti.accounter.web.client.core.ItemUnitPrice;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.CustomerColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.ItemNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.JobColumn;
import com.vimukti.accounter.web.client.ui.edittable.NewQuantityColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionBillableColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionClassColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTaxableColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceListColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class VendorItemTransactionTable extends VendorTransactionTable {

	private TransactionUnitPriceListColumn transactionUnitPriceListColumn;

	public VendorItemTransactionTable(boolean enableTax, boolean showTaxCode,
			boolean isTrackClass, boolean isClassPerDetailLine,
			ICurrencyProvider currencyProvider) {
		this(true, enableTax, showTaxCode, isTrackClass, isClassPerDetailLine,
				currencyProvider);
	}

	public VendorItemTransactionTable(boolean enableTax, boolean showTaxCode,
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

	public VendorItemTransactionTable(boolean enableTax, boolean showTaxCode,
			boolean enableDisCount, boolean showDisCount,
			ICurrencyProvider currencyProvider, boolean isCustomerAllowedToAdd,
			boolean isTrackClass, boolean isClassPerDetailLine) {
		super(1, enableDisCount, isCustomerAllowedToAdd, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableDisCount = enableDisCount;
		this.showDiscount = showDisCount;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
		addEmptyRecords();
	}

	public VendorItemTransactionTable(boolean needDiscount, boolean enableTax,
			boolean showTaxCode, boolean isTrackClass,
			boolean isClassPerDetailLine, ICurrencyProvider currencyProvider) {
		super(1, needDiscount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
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

		ItemNameColumn transactionItemNameColumn = new ItemNameColumn(
				isSales(), currencyProvider) {
			@Override
			public int getWidth() {
				if (isCustomerAllowedToAdd && isTrackJob()) {
					return 100;
				} else if (isCustomerAllowedToAdd) {
					return 90;
				}
				return super.getWidth();
			}

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientItem newValue) {
				row.setAmountIncludeTAX(isShowPriceWithVat());
				super.setValue(row, newValue);
				updateDiscountValues(row);
				// Unit Price is different. So that overriden the code here
				if (newValue != null) {
					// row.setUnitPrice(newValue.getPurchasePrice());
					row.setTaxable(newValue.isTaxable());
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
					double lt = row.getQuantity().getValue()
							* row.getUnitPrice();
					double disc = row.getDiscount();
					row.setLineTotal(DecimalUtil.isGreaterThan(disc, 0) ? (lt - (lt
							* disc / 100))
							: lt);
					if (newValue.getType() == ClientItem.TYPE_INVENTORY_PART
							|| newValue.getType() == ClientItem.TYPE_INVENTORY_ASSEMBLY) {
						ClientMeasurement measurement = Accounter.getCompany()
								.getMeasurement(newValue.getMeasurement());
						row.getQuantity().setUnit(
								measurement.getDefaultUnit().getId());
						row.setWareHouse(newValue.getWarehouse());
						row.setDescription(newValue.getPurchaseDescription());
					}

				}
				update(row);
			}

			@Override
			public ListFilter<ClientItem> getItemsFilter() {
				return new ListFilter<ClientItem>() {

					@Override
					public boolean filter(ClientItem e) {
						return e.isIBuyThisItem();
					}
				};
			}

			@Override
			protected String getDiscription(ClientItem item) {
				return item.getPurchaseDescription();
			}

			@Override
			protected int getTransactionType() {
				return VendorItemTransactionTable.this.getTransactionType();
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return getValue(row).toString();
			}

			@Override
			public int insertNewLineNumber() {
				// TODO Auto-generated method stub
				return 3;
			}
		};
		transactionItemNameColumn.setItemForCustomer(false);

		this.addColumn(transactionItemNameColumn);

		this.addColumn(new DescriptionEditColumn());

		this.addColumn(new NewQuantityColumn(true) {
			@Override
			public int getWidth() {
				if (isCustomerAllowedToAdd && isTrackJob() && enableClass
						&& enableDisCount) {
					return 60;
				} else if ((isCustomerAllowedToAdd && enableClass)
						|| (isCustomerAllowedToAdd && enableDisCount)
						|| (isCustomerAllowedToAdd && showTaxCode)
						|| (enableClass && enableDisCount)
						|| (enableClass && showTaxCode)
						|| (enableDisCount && showTaxCode)) {
					return 60;
				}
				return super.getWidth();
			}
		});

		transactionUnitPriceListColumn = new TransactionUnitPriceListColumn(
				false) {
			@Override
			public int getWidth() {
				if ((isCustomerAllowedToAdd && showClass)
						|| (isCustomerAllowedToAdd && showDiscount)
						|| (isCustomerAllowedToAdd && showTaxCode)
						|| (showClass && showDiscount)
						|| (showClass && showTaxCode)
						|| (showDiscount && showTaxCode)) {
					return 75;
				}
				return super.getWidth();
			}

			@Override
			protected void setValue(ClientTransactionItem row,
					ItemUnitPrice newValue) {
				super.setValue(row, newValue);
				update(row);
			}
		};

		if (Accounter.hasPermission(Features.HOSTORICAL_UNITPRICES)) {
			this.addColumn(transactionUnitPriceListColumn);
		} else {
			this.addColumn(new TransactionUnitPriceColumn(currencyProvider) {
				@Override
				public int getWidth() {
					if ((isCustomerAllowedToAdd && showClass)
							|| (isCustomerAllowedToAdd && showDiscount)
							|| (isCustomerAllowedToAdd && showTaxCode)
							|| (showClass && showDiscount)
							|| (showClass && showTaxCode)
							|| (showDiscount && showTaxCode)) {
						return 60;
					}
					return super.getWidth();
				}

				@Override
				public void setValue(ClientTransactionItem row, String newValue) {
					super.setValue(row, newValue);
					update(row);
				}
			});
		}

		if (needDiscount) {
			if (showDiscount) {
				this.addColumn(new TransactionDiscountColumn(currencyProvider) {
					@Override
					public int getWidth() {
						if ((isCustomerAllowedToAdd && showClass)
								|| (isCustomerAllowedToAdd && showDiscount)
								|| (isCustomerAllowedToAdd && showTaxCode)
								|| (showClass && showDiscount)
								|| (showClass && showTaxCode)
								|| (showDiscount && showTaxCode)) {
							return 55;
						}
						return super.getWidth();
					}
				});
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
					public int getWidth() {
						if (isCustomerAllowedToAdd && isTrackJob()) {
							return 75;
						}
						return super.getWidth();
					}

					@Override
					public String getValueAsString(ClientTransactionItem row) {
						return getValue(row).toString();
					}

					@Override
					public int insertNewLineNumber() {
						return 1;
					}

				});
			}
		}
		this.addColumn(new TransactionTotalColumn(currencyProvider, true) {
			@Override
			public int getWidth() {
				if ((isCustomerAllowedToAdd && showClass)
						|| (isCustomerAllowedToAdd && showDiscount)
						|| (isCustomerAllowedToAdd && showTaxCode)
						|| (showClass && showDiscount)
						|| (showClass && showTaxCode)
						|| (showDiscount && showTaxCode)) {
					return 70;
				}
				return super.getWidth();
			}
		});

		if (enableTax) {
			if (showTaxCode) {
				this.addColumn(new TransactionVatCodeColumn() {

					@Override
					protected ListFilter<ClientTAXCode> getTaxCodeFilter() {
						return new ListFilter<ClientTAXCode>() {

							@Override
							public boolean filter(ClientTAXCode e) {
								if (!e.isTaxable()
										|| e.getTAXItemGrpForPurchases() != 0) {
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
						return false;
					}

					@Override
					public int getWidth() {
						if (isCustomerAllowedToAdd && isTrackJob()) {
							return 100;
						} else if (isCustomerAllowedToAdd) {
							return 110;
						}
						return super.getWidth();
					}

					@Override
					public String getValueAsString(ClientTransactionItem row) {
						return getValue(row).toString();
					}

					@Override
					public int insertNewLineNumber() {
						return 1;
					}
				});

				this.addColumn(new TransactionVatColumn(currencyProvider) {
					@Override
					public int getWidth() {
						if (isCustomerAllowedToAdd && isTrackJob()
								&& enableClass && enableDisCount && showTaxCode) {
							return 35;
						} else if ((isCustomerAllowedToAdd && showClass)
								|| (isCustomerAllowedToAdd && showDiscount)
								|| (isCustomerAllowedToAdd && showTaxCode)
								|| (showClass && showDiscount)
								|| (showClass && showTaxCode)
								|| (showDiscount && showTaxCode)) {
							return 40;
						}
						return super.getWidth();
					}
				});
			} else {
				this.addColumn(new TransactionTaxableColumn());
			}
		}
		final JobColumn<ClientTransactionItem> jobColumn = new JobColumn<ClientTransactionItem>() {

			@Override
			protected ClientJob getValue(ClientTransactionItem row) {
				return Accounter.getCompany().getjob(row.getJob());
			}

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientJob newValue) {
				if (newValue == null) {
					return;
				}
				row.setJob(newValue.getID());
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return getValue(row).toString();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		if (isCustomerAllowedToAdd) {
			this.addColumn(new CustomerColumn<ClientTransactionItem>() {

				@Override
				protected ClientCustomer getValue(ClientTransactionItem row) {
					return Accounter.getCompany()
							.getCustomer(row.getCustomer());
				}

				@Override
				protected void setValue(ClientTransactionItem row,
						ClientCustomer newValue) {
					if (newValue == null) {
						return;
					}
					row.setCustomer(newValue.getID());
					jobColumn.setcustomerId(newValue.getID());
				}

				@Override
				public int getWidth() {
					if (isTrackJob()) {
						return 100;
					}
					return super.getWidth();
				}

				@Override
				public String getValueAsString(ClientTransactionItem row) {
					return getValue(row).toString();
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}
			});
			if (isTrackJob()) {
				this.addColumn(jobColumn);
			}
			this.addColumn(new TransactionBillableColumn() {
				@Override
				public int getWidth() {
					if ((isCustomerAllowedToAdd && showClass)
							|| (isCustomerAllowedToAdd && showDiscount)
							|| (isCustomerAllowedToAdd && showTaxCode)
							|| (showClass && showDiscount)
							|| (showClass && showTaxCode)
							|| (showDiscount && showTaxCode)) {
						return 55;
					}
					return super.getWidth();
				}
			});
		}

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

	protected boolean isTrackJob() {
		return false;
	}

	protected int getTransactionType() {
		return 0;
	}

	public void setPayee(ClientPayee payee) {
		if (transactionUnitPriceListColumn != null) {
			transactionUnitPriceListColumn.setPayee(payee);
		}
	}
}
