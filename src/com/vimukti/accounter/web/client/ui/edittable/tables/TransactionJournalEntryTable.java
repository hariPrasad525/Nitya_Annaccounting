package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AbstractDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.AccountDropDownTable;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.ComboColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.TextEditColumn;

public abstract class TransactionJournalEntryTable extends
		EditTable<ClientTransactionItem> {

	/*
	 * holds all records debit n credit totals
	 */
	private double creditTotal;
	private double debitTotal;
	private ICurrencyProvider currencyProvider;

	public TransactionJournalEntryTable(ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
	}

	protected void initColumns() {
		// TextEditColumn<ClientEntry> voucherNumber = new
		// TextEditColumn<ClientEntry>() {
		//
		// @Override
		// protected String getValue(ClientEntry row) {
		// return row.getVoucherNumber();
		// }
		//
		// @Override
		// protected boolean isEnable() {
		// return false;
		// }
		//
		// @Override
		// public int getWidth() {
		// return 85;
		// }
		//
		// @Override
		// protected String getColumnName() {
		// return messages.voucherNo();
		// }
		//
		// @Override
		// protected void setValue(ClientEntry row, String value) {
		// row.setVoucherNumber(value);
		// }
		// };
		// this.addColumn(voucherNumber);

		// DateColumn<ClientTransactionItem> dateColumn = new
		// DateColumn<ClientTransactionItem>() {
		//
		// @Override
		// protected ClientFinanceDate getValue(ClientTransactionItem row) {
		// return new ClientFinanceDate(row.getEntryDate());
		// }
		//
		// @Override
		// public int getWidth() {
		// return 132;
		// }
		//
		// @Override
		// protected String getColumnName() {
		// return messages.date();
		// }
		//
		// @Override
		// protected void setValue(ClientEntry row, ClientFinanceDate value) {
		// row.setEntryDate(value.getDate());
		// }
		// };
		// this.addColumn(dateColumn);

		final AccountDropDownTable accountDropDownTable = new AccountDropDownTable(
				new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount e) {
						return true;
					}

				}, null);

		this.addColumn(new ComboColumn<ClientTransactionItem, IAccounterCore>() {

			@Override
			protected IAccounterCore getValue(ClientTransactionItem obj) {
				return Accounter.getCompany().getAccount(obj.getAccount());
			}

			@Override
			protected void setValue(ClientTransactionItem row,
					IAccounterCore newValue) {
				// MultiCurrency supporting
				ClientCurrency transactionCurrency = currencyProvider
						.getTransactionCurrency();
				ClientAccount account = Accounter.getCompany().getAccount(
						newValue.getID());
				if (account.getCurrency() != transactionCurrency.getID()
						&& account.getCurrency() != Accounter.getCompany()
								.getPrimaryCurrency().getID()) {
					Accounter
							.showError("Should have the selected accounts must be in base Currency or transaction Currency");
				}
				row.setAccount(newValue.getID());
				if (row.getLineTotal() == null) {
					row.setLineTotal(new Double(0));
				}
				update(row);
			}

			@SuppressWarnings({ "unchecked" })
			@Override
			public AbstractDropDownTable getDisplayTable(
					ClientTransactionItem row) {
				return accountDropDownTable;
			}

			@Override
			protected String getColumnName() {
				return messages.Account();
			}

			@Override
			public int getWidth() {
				return 180;
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return messages.Account() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		TextEditColumn<ClientTransactionItem> memoColumn = new TextEditColumn<ClientTransactionItem>() {

			@Override
			protected String getValue(ClientTransactionItem row) {
				return row.getDescription();
			}

			@Override
			public int getWidth() {
				return -1;
			}

			@Override
			protected String getColumnName() {
				return messages.memo();
			}

			@Override
			protected void setValue(ClientTransactionItem row, String value) {
				row.setDescription(value);
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return messages.memo() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 3;
			}
		};
		this.addColumn(memoColumn);

		AmountColumn<ClientTransactionItem> debitColumn = new AmountColumn<ClientTransactionItem>(
				currencyProvider, false) {

			@Override
			protected Double getAmount(ClientTransactionItem row) {
				if (row.getLineTotal() != null
						&& DecimalUtil.isGreaterThan(row.getLineTotal(), 0)) {
					return row.getLineTotal();
				}
				return row.getLineTotal() == null ? null : new Double(0);
			}

			@Override
			protected void setAmount(ClientTransactionItem row, Double value) {
				row.setLineTotal(value);
				refreshTotals();
				getTable().update(row);
			}

			@Override
			protected String getColumnName() {
				return messages.debit();
			}

			@Override
			public int getWidth() {
				return 110;
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return messages.debit() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		};
		this.addColumn(debitColumn);

		AmountColumn<ClientTransactionItem> creditColumn = new AmountColumn<ClientTransactionItem>(
				currencyProvider, false) {

			@Override
			protected Double getAmount(ClientTransactionItem row) {
				if (row.getLineTotal() != null
						&& DecimalUtil.isLessThan(row.getLineTotal(), 0)) {
					return -1 * row.getLineTotal();
				}
				return row.getLineTotal() == null ? null : new Double(0);
			}

			@Override
			protected void setAmount(ClientTransactionItem row, Double value) {
				row.setLineTotal(-1 * value);
				refreshTotals();
				getTable().update(row);
			}

			@Override
			protected String getColumnName() {
				return messages.credit();
			}

			@Override
			public int getWidth() {
				return 110;
			}

			@Override
			public String getValueAsString(ClientTransactionItem row) {
				return messages.credit() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		};
		this.addColumn(creditColumn);

		this.addColumn(new DeleteColumn<ClientTransactionItem>());
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();

		if (this.getTransactionItems().size() == 0) {
			result.addError(this,
					messages.thereisNoRecordsTosave(messages.journalEntry()));
		}
		// Validates account name
		List<ClientTransactionItem> entrylist = this.getTransactionItems();
		for (ClientTransactionItem entry : entrylist) {
			long accountId = entry.getAccount();
			for (ClientTransactionItem entry2 : entrylist) {
				long accountId2 = entry2.getAccount();
				if (!entry.equals(entry2) && accountId == accountId2) {
					result.addError(this, messages
							.shouldntSelectSameAccountInMultipleEntries());
				}
			}
		}
		for (ClientTransactionItem clientTransactionItem : entrylist) {
			ClientCurrency transactionCurrency = currencyProvider
					.getTransactionCurrency();
			ClientAccount account = Accounter.getCompany().getAccount(
					clientTransactionItem.getAccount());
			if (account.getCurrency() != transactionCurrency.getID()
					&& account.getCurrency() != Accounter.getCompany()
							.getPrimaryCurrency().getID()) {
				result.addError(
						this,
						"Should have the selected accounts must be in base Currency or transaction Currency");
			}
		}

		for (ClientTransactionItem entry : entrylist) {
			if (entry.isEmpty()) {
				continue;
			}
			if (entry.getAccount() == 0) {
				result.addError(this, messages.pleaseEnter(messages.account()));
			}
		}

		return result;
	}

	public boolean isValidTotal() {
		if (DecimalUtil.isEquals(getTotalCredittotal(), getTotalDebittotal()))
			return true;
		else {
			return false;
		}
	}

	public double getTotalCredittotal() {
		return creditTotal;
	}

	public void setCreditTotal(double creditTotal) {
		this.creditTotal = creditTotal;
	}

	public double getTotalDebittotal() {
		return debitTotal;
	}

	public void setDebitTotal(double debitTotal) {
		this.debitTotal = debitTotal;
	}

	/**
	 * This method is used to refresh the total with existing added entries.
	 */
	public void refreshTotals() {
		creditTotal = 0;
		debitTotal = 0;
		for (ClientTransactionItem rec : getAllRows()) {
			if (rec.getLineTotal() != null) {
				if (DecimalUtil.isGreaterThan(rec.getLineTotal(), 0)) {
					debitTotal += rec.getLineTotal();
				} else {
					creditTotal += (-1 * rec.getLineTotal());
				}
			}
		}
		updateNonEditableItems();
	}

	public abstract void updateNonEditableItems();

	@Override
	public void setAllRows(List<ClientTransactionItem> rows) {
		for (ClientTransactionItem item : rows) {
			item.setID(0);
			item.taxRateCalculationEntriesList.clear();
		}
		super.setAllRows(rows);
		refreshTotals();
	}

	@Override
	public void delete(ClientTransactionItem row) {
		super.delete(row);
		refreshTotals();
	}

	public List<ClientTransactionItem> getTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem item : getAllRows()) {
			if (!item.isEmpty()) {
				list.add(item);
			}
		}
		return list;
	}
}
