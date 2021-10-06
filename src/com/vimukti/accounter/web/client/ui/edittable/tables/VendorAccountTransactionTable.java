package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.Arrays;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientQuantity;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AccountNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionClassColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTaxableColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class VendorAccountTransactionTable extends
		VendorTransactionTable {

	public VendorAccountTransactionTable(boolean enableTax,
			boolean showTaxCode, boolean isTrackClass,
			boolean isClassPerDetailLine, ICurrencyProvider currencyProvider) {
		this(true, enableTax, showTaxCode, isTrackClass, isClassPerDetailLine,
				currencyProvider);
	}

	public VendorAccountTransactionTable(boolean enableTax,
			boolean showTaxCode, boolean enableDisCount, boolean showDiscount,
			boolean isTrackClass, boolean isClassPerDetailLine,
			ICurrencyProvider currencyProvider) {
		super(1, enableDisCount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableDisCount = enableDisCount;
		this.showDiscount = showDiscount;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
		addEmptyRecords();
	}

	public VendorAccountTransactionTable(boolean enableTax,
			boolean showTaxCode, boolean enableDisCount, boolean showDiscount,
			ICurrencyProvider currencyProvider, boolean isCustomerAllowedToAdd,
			boolean isTrackClass, boolean isClassPerDetailLine) {
		super(1, enableDisCount, isCustomerAllowedToAdd, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableDisCount = enableDisCount;
		this.showDiscount = showDiscount;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
		addEmptyRecords();
	}

	public VendorAccountTransactionTable(boolean needDiscount,
			boolean enableTax, boolean showTaxCode, boolean isTrackClass,
			boolean isClassPerDetailLine, ICurrencyProvider currencyProvider) {
		super(1, needDiscount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
		addEmptyRecords();
	}

	public ClientTransactionItem getEmptyRow() {
		ClientTransactionItem item = new ClientTransactionItem();
		item.setType(ClientTransactionItem.TYPE_ACCOUNT);
		return item;
	};

	@Override
	protected void initColumns() {

		AccountNameColumn<ClientTransactionItem> transactionItemNameColumn = new AccountNameColumn<ClientTransactionItem>() {
			@Override
			public int getWidth() {
				if (isCustomerAllowedToAdd && isTrackJob()) {
					return 110;
				}
				return super.getWidth();
			}

			@Override
			public ListFilter<ClientAccount> getAccountsFilter() {
				return new ListFilter<ClientAccount>() {
					@Override
					public boolean filter(ClientAccount account) {

						return VendorAccountTransactionTable.this
								.isAccountAllowed(account);
					}
				};

			}

			@Override
			protected void setValue(ClientTransactionItem row,
					ClientAccount newValue) {
				row.setAmountIncludeTAX(isShowPriceWithVat());
				updateDiscountValues(row);
				if (newValue != null) {
					row.setAccountable(newValue);
					row.setDescription(newValue.getComment());
					row.setTaxable(true);
					onValueChange(row);
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
					getTable().update(row);
				}
				update(row);
			}

			@Override
			public List<Integer> getCanAddedAccountTypes() {
				return Arrays.asList(ClientAccount.TYPE_COST_OF_GOODS_SOLD,
						ClientAccount.TYPE_OTHER_EXPENSE,
						ClientAccount.TYPE_FIXED_ASSET,
						ClientAccount.TYPE_EXPENSE);
			}

			@Override
			protected ClientAccount getValue(ClientTransactionItem row) {
				return (ClientAccount) row.getAccountable();
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
		this.addColumn(transactionItemNameColumn);

		this.addColumn(new DescriptionEditColumn());

		// this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn(currencyProvider) {
			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.amount());
			}

			@Override
			public int getWidth() {
				if (isCustomerAllowedToAdd && isTrackJob()) {
					return 85;
				}
				return super.getWidth();
			}
		});

		if (needDiscount) {
			if (showDiscount) {
				this.addColumn(new TransactionDiscountColumn(currencyProvider) {
					@Override
					public int getWidth() {
						if (isCustomerAllowedToAdd && isTrackJob()) {
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
							return 60;
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
					public int getWidth() {
						if (isCustomerAllowedToAdd && isTrackJob()) {
							return 90;
						}
						return super.getWidth();
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
								&& enableClass && showDiscount) {
							return 45;
						} else if ((isCustomerAllowedToAdd && showClass)
								|| (isCustomerAllowedToAdd && showDiscount)
								|| (isCustomerAllowedToAdd && showTaxCode)
								|| (showClass && showDiscount)
								|| (showClass && showTaxCode)
								|| (showDiscount && showTaxCode)) {
							return 60;
						}
						return super.getWidth();
					}
				});
			} else {
				this.addColumn(new TransactionTaxableColumn());
			}
		}
		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

	protected boolean isAccountAllowed(ClientAccount account) {
		if (account.getType() != ClientAccount.TYPE_CASH
				&& account.getType() != ClientAccount.TYPE_BANK
				&& account.getType() != ClientAccount.TYPE_CREDIT_CARD
				&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
				&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
				&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
				&& account.getType() != ClientAccount.TYPE_INCOME
				&& account.getType() != ClientAccount.TYPE_OTHER_INCOME
				&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
				&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
				&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
				&& account.getType() != ClientAccount.TYPE_EQUITY
				&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY) {
			return true;
		} else {
			return false;
		}
	}

	protected boolean isTrackJob() {
		return false;
	}

	@Override
	public void updateAmountsFromGUI() {
		super.updateAmountsFromGUI();
	}
}
