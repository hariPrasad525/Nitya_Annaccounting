package com.vimukti.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientJob;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientTransactionDepositItem;
import com.vimukti.accounter.web.client.core.ListFilter;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.ICurrencyProvider;
import com.vimukti.accounter.web.client.ui.edittable.AccountNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.AmountColumn;
import com.vimukti.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.CustomerColumn;
import com.vimukti.accounter.web.client.ui.edittable.DeleteColumn;
import com.vimukti.accounter.web.client.ui.edittable.EditTable;
import com.vimukti.accounter.web.client.ui.edittable.JobColumn;
import com.vimukti.accounter.web.client.ui.edittable.PayeeNameColumn;
import com.vimukti.accounter.web.client.ui.edittable.RenderContext;
import com.vimukti.accounter.web.client.ui.edittable.TextAreaEditColumn;
import com.vimukti.accounter.web.client.ui.edittable.TransactionClassColumn;

public abstract class TransactionDepositTable extends
		EditTable<ClientTransactionDepositItem> {

	private double total;
	private boolean isCustomerAllowedToAdd;
	private ICurrencyProvider currencyProvider;
	protected boolean enableClass;
	protected boolean showClass;

	public TransactionDepositTable(boolean isCustomerAllowedToAdd,
			boolean isTrackClass, boolean isClassPerDetailLine,
			ICurrencyProvider currencyProvider) {
		super();
		this.isCustomerAllowedToAdd = isCustomerAllowedToAdd;
		this.currencyProvider = currencyProvider;
		this.enableClass = isTrackClass;
		this.showClass = isClassPerDetailLine;
		addEmptyRecords();
	}

	@Override
	protected void initColumns() {
		this.addColumn(new PayeeNameColumn<ClientTransactionDepositItem>() {

			@Override
			protected ClientPayee getValue(ClientTransactionDepositItem row) {
				return Accounter.getCompany().getPayee(row.getReceivedFrom());
			}

			@Override
			protected void setValue(ClientTransactionDepositItem row,
					ClientPayee newValue) {
				if (newValue != null) {
					row.setReceivedFrom(newValue.getID());
				}
			}

			@Override
			public String getValueAsString(ClientTransactionDepositItem row) {
				return getValue(row).toString();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new AccountNameColumn<ClientTransactionDepositItem>() {

			@Override
			protected String getColumnName() {
				return messages.depositFrom();
			}

			@Override
			public List<Integer> getCanAddedAccountTypes() {
				ArrayList<Integer> arrayList = new ArrayList<Integer>();
				for (Integer integer : UIUtils.accountTypes) {
					arrayList.add(integer);
				}
				return arrayList;
			}

			@Override
			public ListFilter<ClientAccount> getAccountsFilter() {
				return new ListFilter<ClientAccount>() {

					@Override
					public boolean filter(ClientAccount account) {
						return true;
					}
				};
			}

			@Override
			protected void setValue(ClientTransactionDepositItem row,
					ClientAccount newValue) {
				if (newValue != null) {
					row.setAccount(newValue.getID());
					update(row);
				}
			}

			@Override
			protected ClientAccount getValue(ClientTransactionDepositItem row) {
				return Accounter.getCompany().getAccount(row.getAccount());
			}

			@Override
			public String getValueAsString(ClientTransactionDepositItem row) {
				return getValue(row).toString();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		this.addColumn(new TextAreaEditColumn<ClientTransactionDepositItem>() {

			@Override
			protected String getValue(ClientTransactionDepositItem row) {
				return row.getDescription();
			}

			@Override
			protected void setValue(ClientTransactionDepositItem row,
					String value) {
				row.setDescription(value);
			}

			@Override
			protected String getColumnName() {
				return messages.description();
			}

			@Override
			public String getValueAsString(ClientTransactionDepositItem row) {
				return getValue(row).toString();
			}

			@Override
			public int insertNewLineNumber() {
				// TODO Auto-generated method stub
				return 3;
			}
		});

		this.addColumn(new AmountColumn<ClientTransactionDepositItem>(
				currencyProvider, isCustomerAllowedToAdd) {

			@Override
			protected Double getAmount(ClientTransactionDepositItem row) {
				return row.getTotal();
			}

			@Override
			protected void setAmount(ClientTransactionDepositItem row,
					Double value) {
				row.setTotal(value);
				getTable().update(row);
			}

			@Override
			protected String getColumnName() {
				return messages.total();
			}

			@Override
			public int getWidth() {
				return 150;
			}

			@Override
			public String getValueAsString(ClientTransactionDepositItem row) {
				return "Amount: " + getValue(row).toString();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		if (enableClass) {
			if (showClass) {
				this.addColumn(new TransactionClassColumn<ClientTransactionDepositItem>() {

					@Override
					protected ClientAccounterClass getValue(
							ClientTransactionDepositItem row) {
						return Accounter.getCompany().getAccounterClass(
								row.getAccounterClass());
					}

					@Override
					protected void setValue(ClientTransactionDepositItem row,
							ClientAccounterClass newValue) {
						if (newValue != null) {
							row.setAccounterClass(newValue.getID());
							getTable().update(row);
						}
					}

					@Override
					public String getValueAsString(
							ClientTransactionDepositItem row) {
						return "Class: " + getValue(row).toString();
					}

					@Override
					public int insertNewLineNumber() {
						// TODO Auto-generated method stub
						return 1;
					}
				});
			}
		}
		if (isCustomerAllowedToAdd) {
			final JobColumn<ClientTransactionDepositItem> jobColumn = new JobColumn<ClientTransactionDepositItem>() {

				@Override
				protected ClientJob getValue(ClientTransactionDepositItem row) {
					return Accounter.getCompany().getjob(row.getJob());
				}

				@Override
				protected void setValue(ClientTransactionDepositItem row,
						ClientJob newValue) {
					if (newValue == null) {
						return;
					}
					row.setJob(newValue.getID());
				}

				@Override
				public String getValueAsString(ClientTransactionDepositItem row) {
					return "Job: " + getValue(row).toString();
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}

			};

			this.addColumn(new CustomerColumn<ClientTransactionDepositItem>() {

				@Override
				protected ClientCustomer getValue(
						ClientTransactionDepositItem row) {
					return Accounter.getCompany()
							.getCustomer(row.getCustomer());
				}

				@Override
				protected void setValue(ClientTransactionDepositItem row,
						ClientCustomer newValue) {
					if (newValue != null) {
						row.setCustomer(newValue.getID());
						row.setJob(0);
						jobColumn.setcustomerId(newValue.getID());
						update(row);
					}
				}

				@Override
				public String getValueAsString(ClientTransactionDepositItem row) {
					return getValue(row).toString();
				}

				@Override
				public int insertNewLineNumber() {
					// TODO Auto-generated method stub
					return 1;
				}

			});

			if (isTrackJob()) {
				this.addColumn(jobColumn);
			}

			this.addColumn(new CheckboxEditColumn<ClientTransactionDepositItem>() {

				@Override
				protected void onChangeValue(boolean value,
						ClientTransactionDepositItem row) {
					row.setIsBillable(value);
					(getTable()).update(row);
				}

				@Override
				public IsWidget getHeader() {
					return new Label(messages.isBillable());
				}

				@Override
				public void render(IsWidget widget,
						RenderContext<ClientTransactionDepositItem> context) {
					super.render(widget, context);
					CheckBox box = (CheckBox) widget;
					box.setValue(context.getRow().isBillable());
				}

				@Override
				public int getWidth() {
					return 41;
				}

				@Override
				public String getValueAsString(ClientTransactionDepositItem row) {
					if (row.isBillable()) {
						return "Is Billable";
					} else {
						return "Not Billable";
					}
				}

				@Override
				public int insertNewLineNumber() {
					return 2;
				}
			});
		}

		this.addColumn(new DeleteColumn<ClientTransactionDepositItem>());
	}

	protected boolean isTrackJob() {
		return false;
	}

	@Override
	public void update(ClientTransactionDepositItem row) {
		super.update(row);
		updateTotals();
	}

	protected abstract boolean isInViewMode();

	@Override
	protected ClientTransactionDepositItem getEmptyRow() {
		return new ClientTransactionDepositItem();
	}

	@Override
	public void updateFromGUI(ClientTransactionDepositItem row) {
		super.updateFromGUI(row);
	}

	public void updateAmountsFromGUI() {
		updateTotals();
		for (ClientTransactionDepositItem item : this.getAllRows()) {
			updateFromGUI(item);
		}
		updateColumnHeaders();
	}

	public double getLineTotal() {
		return total;
	}

	public List<ClientTransactionDepositItem> getRecords() {
		return getAllRows();
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		for (ClientTransactionDepositItem transactionItem : getRecords()) {
			if (transactionItem.isEmpty()) {
				continue;
			}
			if (transactionItem.getAccount() == 0) {
				result.addError("GridItem-",
						messages.pleaseSelect(messages.Account()));
			}

			if (transactionItem.isBillable()) {
				if (transactionItem.getCustomer() == 0) {
					result.addError("Customer",
							messages.mustSelectCustomerForBillable());
				}
			}
		}
		// if (DecimalUtil.isLessThan(total, 0.0)) {
		// result.addError(this, messages.invalidTransactionAmount());
		// }
		return result;
	}

	public void updateTotals() {

		List<ClientTransactionDepositItem> allrecords = getAllRows();
		total = 0.0;

		for (ClientTransactionDepositItem record : allrecords) {
			if (record.getAccount() != 0) {

				Double totalAmt = record.getTotal();
				if (totalAmt != null) {
					total += totalAmt;
				}
			}
		}

		updateNonEditableItems();

	}

	protected abstract void updateNonEditableItems();

	@Override
	public void setAllRows(List<ClientTransactionDepositItem> rows) {
		createColumns();
		for (ClientTransactionDepositItem item : rows) {
			item.setID(0);
		}
		super.setAllRows(rows);

	}

	public void setRecords(List<ClientTransactionDepositItem> transactionItems) {
		setAllRows(transactionItems);
		updateTotals();
	}

	public void resetRecords() {
		clear();
		addEmptyRecords();
		updateTotals();
	}

	public void setClass(long classId, boolean force) {
		for (ClientTransactionDepositItem item : getAllRows()) {
			if ((item.getAccounterClass() == 0) || force) {
				item.setAccounterClass(classId);
			}
			update(item);
		}
	}

}
