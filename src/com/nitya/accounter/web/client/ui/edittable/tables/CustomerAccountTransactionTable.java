package com.nitya.accounter.web.client.ui.edittable.tables;

import java.util.Arrays;
import java.util.List;

import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientQuantity;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.edittable.AccountNameColumn;
import com.nitya.accounter.web.client.ui.edittable.DeleteColumn;
import com.nitya.accounter.web.client.ui.edittable.DescriptionEditColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionClassColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionDiscountColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionTaxableColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionTotalColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionUnitPriceColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionVatCodeColumn;
import com.nitya.accounter.web.client.ui.edittable.TransactionVatColumn;

public abstract class CustomerAccountTransactionTable extends
		CustomerTransactionTable {

	public CustomerAccountTransactionTable(boolean enableTax,
			boolean showTaxCode, ICurrencyProvider currencyProvider) {
		this(true, enableTax, showTaxCode, currencyProvider);
	}

	public CustomerAccountTransactionTable(boolean enableTax,
			boolean showTaxCode, boolean enableDisCount, boolean showDisCount,
			boolean isTrackClass, boolean isClassPerDetailLine,
			ICurrencyProvider currencyProvider) {
		super(1, enableDisCount, currencyProvider);
		this.enableTax = enableTax;
		this.showTaxCode = showTaxCode;
		this.enableDisCount = enableDisCount;
		this.showDiscount = showDisCount;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
		addEmptyRecords();
	}

	public CustomerAccountTransactionTable(boolean needDiscount,
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
		item.setType(ClientTransactionItem.TYPE_ACCOUNT);
		return item;
	}

	@Override
	protected void initColumns() {
		this.addColumn(new AccountNameColumn<ClientTransactionItem>() {

			@Override
			public ListFilter<ClientAccount> getAccountsFilter() {
				return new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						if (account.getType() != ClientAccount.TYPE_CASH
								&& account.getType() != ClientAccount.TYPE_BANK
								&& account.getType() != ClientAccount.TYPE_CREDIT_CARD
								&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
								&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
								&& account.getType() != ClientAccount.TYPE_EXPENSE
								&& account.getType() != ClientAccount.TYPE_OTHER_EXPENSE
								&& account.getType() != ClientAccount.TYPE_COST_OF_GOODS_SOLD
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_ASSET
								&& account.getType() != ClientAccount.TYPE_OTHER_CURRENT_LIABILITY
								&& account.getType() != ClientAccount.TYPE_LONG_TERM_LIABILITY
								&& account.getType() != ClientAccount.TYPE_OTHER_ASSET
								&& account.getType() != ClientAccount.TYPE_EQUITY) {
							return true;
						}
						return false;
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
				// applyPriceLevel(row);
			}

			@Override
			public List<Integer> getCanAddedAccountTypes() {
				return Arrays.asList(ClientAccount.TYPE_INCOME,
						ClientAccount.TYPE_FIXED_ASSET);
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
				// TODO Auto-generated method stub
				return 1;
			}
		});

		this.addColumn(new DescriptionEditColumn());

		// this.addColumn(new TransactionQuantityColumn());

		this.addColumn(new TransactionUnitPriceColumn(currencyProvider) {
			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.amount());
			}
		});

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
						return messages.className() + getValue(row);
					}

					@Override
					public int insertNewLineNumber() {
						// TODO Auto-generated method stub
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
						return "Vat Code" + getValue(row);
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

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

}
