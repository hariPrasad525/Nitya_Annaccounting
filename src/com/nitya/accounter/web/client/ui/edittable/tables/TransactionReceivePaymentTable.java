package com.nitya.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCreditsAndPayments;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.nitya.accounter.web.client.core.ClientTransactionReceivePayment;
import com.nitya.accounter.web.client.core.ListFilter;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.CashDiscountDialog;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.core.InputDialogHandler;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.customers.NewApplyCreditsDialog;
import com.nitya.accounter.web.client.ui.customers.WriteOffDialog;
import com.nitya.accounter.web.client.ui.edittable.AmountColumn;
import com.nitya.accounter.web.client.ui.edittable.AnchorEditColumn;
import com.nitya.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.RenderContext;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;
import com.nitya.accounter.web.client.ui.widgets.DateUtills;

public abstract class TransactionReceivePaymentTable extends
		EditTable<ClientTransactionReceivePayment> {
	private ClientCompany company;
	private boolean canEdit;

	ClientCustomer customer;
	List<Integer> selectedValues = new ArrayList<Integer>();
	protected boolean gotCreditsAndPayments;
	private CashDiscountDialog cashDiscountDialog;
	private WriteOffDialog writeOffDialog;

	// public CustomerCreditsAndPaymentsDialiog creditsAndPaymentsDialiog;
	public List<ClientCreditsAndPayments> creditsAndPayments = new ArrayList<ClientCreditsAndPayments>();

	List<ClientTransactionReceivePayment> tranReceivePayments = new ArrayList<ClientTransactionReceivePayment>();
	private ICurrencyProvider currencyProvider;
	private boolean enableDiscount;
	private long transactionId;

	public TransactionReceivePaymentTable(boolean enableDisCount,
			boolean canEdit, ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		this.canEdit = canEdit;
		this.enableDiscount = enableDisCount;
		this.company = Accounter.getCompany();
	}

	public void setTranactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	protected void initColumns() {
		this.addColumn(new CheckboxEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void onChangeValue(boolean value,
					ClientTransactionReceivePayment row) {
				onSelectionChanged(row, value);
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientTransactionReceivePayment> context) {
				super.render(widget, context);
				if (isInViewMode()) {
					((CheckBox) widget).setValue(true);
				}
			}

			@Override
			public String getValueAsString(ClientTransactionReceivePayment row) {
				return "";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});
		if (canEdit) {
			TextEditColumn<ClientTransactionReceivePayment> dateCoulmn = new TextEditColumn<ClientTransactionReceivePayment>() {

				@Override
				protected void setValue(ClientTransactionReceivePayment row,
						String value) {
				}

				@Override
				protected String getValue(ClientTransactionReceivePayment row) {
					return DateUtills.getDateAsString(new ClientFinanceDate(row
							.getDueDate()).getDateAsObject());
				}

				@Override
				public int getWidth() {
					return 100;
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.dueDate();
				}

				@Override
				public String getValueAsString(
						ClientTransactionReceivePayment row) {
					return messages.dueDate() + getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					return 4;
				}
			};
			this.addColumn(dateCoulmn);
		}

		TextEditColumn<ClientTransactionReceivePayment> invoiceNumber = new TextEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return row.getNumber();
			}

			@Override
			protected void setValue(ClientTransactionReceivePayment row,
					String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages.invoice();
			}

			@Override
			public String getValueAsString(ClientTransactionReceivePayment row) {
				return getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				// TODO Auto-generated method stub
				return 1;
			}
		};
		this.addColumn(invoiceNumber);

		AmountColumn<ClientTransactionReceivePayment> invoiceAmountColumn = new AmountColumn<ClientTransactionReceivePayment>(
				currencyProvider, false) {

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.invoiceAmount());
			}

			@Override
			protected Double getAmount(ClientTransactionReceivePayment row) {
				return row.getInvoiceAmount();
			}

			@Override
			protected void setAmount(ClientTransactionReceivePayment row,
					Double value) {

			}

			@Override
			public String getValueAsString(ClientTransactionReceivePayment row) {
				return getColumnNameWithCurrency(messages.invoiceAmount())
						+ " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(invoiceAmountColumn);

		if (canEdit) {
			AmountColumn<ClientTransactionReceivePayment> amountDueColumn = new AmountColumn<ClientTransactionReceivePayment>(
					currencyProvider, false) {

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				public int getWidth() {
					return 100;
				}

				@Override
				protected String getColumnName() {
					return getColumnNameWithCurrency(messages.amountDue());
				}

				@Override
				protected Double getAmount(ClientTransactionReceivePayment row) {
					return row.getAmountDue();
				}

				@Override
				protected void setAmount(ClientTransactionReceivePayment row,
						Double value) {

				}

				@Override
				public String getValueAsString(
						ClientTransactionReceivePayment row) {
					return getColumnNameWithCurrency(messages.amountDue())
							+ " : " + getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}
			};
			this.addColumn(amountDueColumn);
		}

		TextEditColumn<ClientTransactionReceivePayment> discountDateColumn = new TextEditColumn<ClientTransactionReceivePayment>() {

			@Override
			protected void setValue(ClientTransactionReceivePayment row,
					String value) {
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return "";// row.getDiscountDate();
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return messages.discountDate();
			}

			@Override
			public String getValueAsString(ClientTransactionReceivePayment row) {
				return messages.discountDate() + " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		};
		// this.addColumn(discountDateColumn);

		AnchorEditColumn<ClientTransactionReceivePayment> cashDiscountColumn = new AnchorEditColumn<ClientTransactionReceivePayment>(
				currencyProvider) {

			@Override
			protected void onClick(ClientTransactionReceivePayment row) {
				openCashDiscountDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return DataUtils.getAmountAsStringInCurrency(
						row.getCashDiscount(), null);
			}

			@Override
			public int getWidth() {
				return 93;
			}

			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.cashDiscount());
			}

			@Override
			protected boolean isEnable(ClientTransactionReceivePayment row) {
				return selectedValues.contains(indexOf(row));
			}

			@Override
			public String getValueAsString(ClientTransactionReceivePayment row) {
				return getColumnNameWithCurrency(messages.cashDiscount())
						+ " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				// TODO Auto-generated method stub
				return 2;
			}
		};
		if (enableDiscount) {
			this.addColumn(cashDiscountColumn);
		}

		AnchorEditColumn<ClientTransactionReceivePayment> writeOffColumn = new AnchorEditColumn<ClientTransactionReceivePayment>(
				currencyProvider) {

			@Override
			protected void onClick(ClientTransactionReceivePayment row) {
				openWriteOffDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return DataUtils.getAmountAsStringInCurrency(row.getWriteOff(),
						null);
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.writeOff());
			}

			@Override
			protected boolean isEnable(ClientTransactionReceivePayment row) {
				return selectedValues.contains(indexOf(row));
			}

			@Override
			public String getValueAsString(ClientTransactionReceivePayment row) {
				return getColumnNameWithCurrency(messages.writeOff()) + " : "
						+ getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		};
		this.addColumn(writeOffColumn);

		AnchorEditColumn<ClientTransactionReceivePayment> appliedCreditsColumn = new AnchorEditColumn<ClientTransactionReceivePayment>(
				currencyProvider) {

			@Override
			protected void onClick(ClientTransactionReceivePayment row) {
				openCreditsDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionReceivePayment row) {
				return DataUtils.getAmountAsStringInCurrency(
						row.getAppliedCredits(), null);
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.appliedCredits());
			}

			@Override
			protected boolean isEnable(ClientTransactionReceivePayment row) {
				return selectedValues.contains(indexOf(row));
			}

			@Override
			public String getValueAsString(ClientTransactionReceivePayment row) {
				return getColumnNameWithCurrency(messages.appliedCredits())
						+ " : " + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		};
		this.addColumn(appliedCreditsColumn);

		TextEditColumn<ClientTransactionReceivePayment> paymentColumn = new AmountColumn<ClientTransactionReceivePayment>(
				currencyProvider, false) {

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientTransactionReceivePayment> context) {
				TextBoxBase box = (TextBoxBase) widget;
				String value = getValue(context.getRow());
				box.setEnabled(isEnable(context.getRow())
						&& !context.isDesable());
				box.setText(value);
			}

			private boolean isEnable(ClientTransactionReceivePayment row) {
				return selectedValues.contains(indexOf(row));
			}

			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.payment());
			}

			@Override
			protected Double getAmount(ClientTransactionReceivePayment row) {
				return row.getPayment();
			}

			@Override
			protected void setAmount(ClientTransactionReceivePayment item,
					Double value) {
				if (isInViewMode()) {
					return;
				}
				double amt, originalPayment;
				try {
					originalPayment = item.getPayment();
					amt = value;
					if (!isSelected(item)) {
						item.setPayment(item.getAmountDue());
						onSelectionChanged(item, true);
					}
					item.setPayment(amt);
					updateAmountDue(item);

					double totalValue = item.getCashDiscount()
							+ item.getWriteOff() + item.getAppliedCredits()
							+ item.getPayment();
					if (AccounterValidator.validatePayment(item.getAmountDue(),
							totalValue)) {
						recalculateGridAmounts();
						updateTotalPayment(0.0);
					} else {
						item.setPayment(originalPayment);
					}
					update(item);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}

			@Override
			public String getValueAsString(ClientTransactionReceivePayment row) {
				return getColumnNameWithCurrency(messages.payment()) + " : "
						+ getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		};
		this.addColumn(paymentColumn);
	}

	protected abstract boolean isInViewMode();

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		// Validates receive amount exceeds due amount
		for (ClientTransactionReceivePayment transactionReceivePayment : this
				.getSelectedRecords()) {
			double totalValue = getTotalValue(transactionReceivePayment);
			if (DecimalUtil.isLessThan(totalValue, 0.00)) {
				result.addError(this,
						messages.valueCannotBe0orlessthan0(messages.amount()));
			} else if (DecimalUtil.isGreaterThan(totalValue,
					transactionReceivePayment.getAmountDue())
					|| DecimalUtil.isEquals(totalValue, 0)) {
				result.addError(this, messages.receivePaymentExcessDue());
			}
		}
		return result;
	}

	private double getTotalValue(ClientTransactionReceivePayment payment) {
		double totalValue = payment.getWriteOff() + payment.getAppliedCredits()
				+ payment.getPayment();
		if (enableDiscount) {
			totalValue += payment.getCashDiscount();
		}
		return totalValue;
	}

	public void initCreditsAndPayments(final ClientCustomer customer) {

		Accounter
				.createHomeService()
				.getCustomerCreditsAndPayments(
						customer.getID(),
						transactionId,
						new AccounterAsyncCallback<ArrayList<ClientCreditsAndPayments>>() {

							public void onException(AccounterException caught) {
								Accounter.showInformation(messages
										.failedTogetCreditsListAndPayments(customer
												.getName()));

								gotCreditsAndPayments = false;
								return;

							}

							public void onResultSuccess(
									ArrayList<ClientCreditsAndPayments> result) {
								if (result == null)
									onFailure(null);

								addCreditsAndPayments(result);
								gotCreditsAndPayments = true;
								calculateUnusedCredits();
							}

						});

	}

	protected abstract void calculateUnusedCredits();

	public void openCashDiscountDialog(
			final ClientTransactionReceivePayment selectedObject) {
		cashDiscountDialog = new CashDiscountDialog(canEdit,
				selectedObject.getCashDiscount(),
				getCashDiscountAccount(selectedObject), currencyProvider);
		// } else {
		// cashDiscountDialog.setCanEdit(canEdit);
		// cashDiscountDialog.setCashDiscountValue(selectedObject
		// .getCashDiscount());
		// cashDiscountDialog.
		// }
		cashDiscountDialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public void onCancel() {

			}

			@Override
			public boolean onOK() {
				if (canEdit) {
					if (cashDiscountDialog.getSelectedDiscountAccount() != null) {
						selectedObject.setPayment(0.0);
						selectedObject.setCashDiscount(cashDiscountDialog
								.getCashDiscount());
						selectedObject.setDiscountAccount(cashDiscountDialog
								.getSelectedDiscountAccount().getID());
						if (validatePaymentValue(selectedObject)) {
							updatePayment(selectedObject);
						} else {
							selectedObject.setCashDiscount(0.0D);
							updatePayment(selectedObject);
						}
						recalculateGridAmounts();

						update(selectedObject);
					} else
						return false;
				}
				return true;
			}
		});
		ViewManager.getInstance().showDialog(cashDiscountDialog);
	}

	private ClientAccount getCashDiscountAccount(
			ClientTransactionReceivePayment selectedObject) {
		ClientAccount cashDiscountAccount = this.company
				.getAccount(selectedObject.getDiscountAccount());
		return cashDiscountAccount;
	}

	private ClientAccount getWriteOffAccount(
			ClientTransactionReceivePayment selectedObject) {
		ClientAccount writeOffAccount = this.company.getAccount(selectedObject
				.getWriteOffAccount());
		return writeOffAccount;
	}

	public void openCreditsDialog(final ClientTransactionReceivePayment ctrp) {
		if (gotCreditsAndPayments) {
			final NewApplyCreditsDialog dialog = new NewApplyCreditsDialog(
					creditsAndPayments,
					ctrp.getTransactionCreditsAndPayments(),
					ctrp.getAmountDue(), this.currencyProvider);

			dialog.addInputDialogHandler(new InputDialogHandler() {

				@Override
				public void onCancel() {
				}

				@Override
				public boolean onOK() {
					ctrp.updatePayment();

					// adjustPaymentValue(ctrp);
					updateValue(ctrp);
					recalculateGridAmounts();
					// updateFootervalues(ctrp, canEdit);

					setUnUsedCreditsTextAmount(getUnusedCredits());
					return true;
				}
			});
			ViewManager.getInstance().showDialog(dialog);

		} else {
			Accounter.showInformation(messages.noCreditsForThisVendor(Global
					.get().vendor()));
		}

	}

	public double getUnusedCredits() {
		double unusedCredits = 0.0;
		for (ClientCreditsAndPayments ccap : this.creditsAndPayments) {
			unusedCredits += ccap.getBalance();
		}
		return unusedCredits;
	}

	protected abstract void setUnUsedCreditsTextAmount(Double totalBalances);

	public void checkBalance(double amount) throws Exception {
		if (DecimalUtil.isEquals(amount, 0))
			throw new Exception(messages.youdnthaveBalToApplyCredits());
	}

	public class TempCredit {
		double remainingBalance;
		double amountToUse;

		public double getRemainingBalance() {
			return remainingBalance;
		}

		public void setRemainingBalance(double remainingBalance) {
			this.remainingBalance = remainingBalance;
		}

		public double getAmountToUse() {
			return amountToUse;
		}

		public void setAmountToUse(double amountToUse) {
			this.amountToUse = amountToUse;
		}

	}

	private ClientCreditsAndPayments getCreditsAndPayment(long id) {
		for (ClientCreditsAndPayments ccap : this.creditsAndPayments) {
			if (ccap.getID() == id) {
				return ccap;
			}
		}
		return null;
	}

	public void setTranReceivePayments(
			List<ClientTransactionReceivePayment> recievePayments) {
		for (ClientTransactionReceivePayment payment : recievePayments) {
			payment.setID(0);
		}
		this.tranReceivePayments = recievePayments;
	}

	protected boolean validatePaymentValue(
			ClientTransactionReceivePayment selectedObject) {
		double totalValue = getTotalValue(selectedObject);
		if (AccounterValidator.isValidReceive_Payment(
				selectedObject.getAmountDue(), totalValue,
				messages.receiveAmountPayDue())) {
			return true;
		} else
			return false;

	}

	public void updatePayment(ClientTransactionReceivePayment payment) {
		payment.setPayment(0);
		double paymentValue = payment.getAmountDue() - getTotalValue(payment);
		payment.setPayment(paymentValue);
		updateAmountDue(payment);
		updateTotalPayment(payment.getPayment());
	}

	public void openWriteOffDialog(
			final ClientTransactionReceivePayment selectedObject) {
		writeOffDialog = new WriteOffDialog(
				getAccounts(this.company.getActiveAccounts()), selectedObject,
				canEdit, getWriteOffAccount(selectedObject), currencyProvider);
		writeOffDialog.addInputDialogHandler(new InputDialogHandler() {

			@Override
			public void onCancel() {

			}

			@Override
			public boolean onOK() {
				if (canEdit) {
					if (writeOffDialog.getSelectedWriteOffAccount() != null) {
						selectedObject.setPayment(0.0);
						selectedObject.setWriteOff(writeOffDialog
								.getCashDiscountValue());
						selectedObject.setWriteOffAccount(writeOffDialog
								.getSelectedWriteOffAccount().getID());
						if (validatePaymentValue(selectedObject)) {
							updatePayment(selectedObject);
						} else {
							selectedObject.setWriteOff(0.0D);
							updatePayment(selectedObject);
						}
						recalculateGridAmounts();
						update(selectedObject);
					}

				}
				return true;
			}
		});
		ViewManager.getInstance().showDialog(writeOffDialog);
	}

	private List<ClientAccount> getAccounts(
			ArrayList<ClientAccount> activeAccounts) {
		return Utility.filteredList(new ListFilter<ClientAccount>() {

			@Override
			public boolean filter(ClientAccount e) {
				if (e != null && e.getType() != ClientAccount.TYPE_CASH
						&& e.getType() != ClientAccount.TYPE_BANK
						&& e.getType() != ClientAccount.TYPE_CREDIT_CARD
						&& e.getType() != ClientAccount.TYPE_PAYPAL) {
					return true;
				}
				return false;
			}
		}, activeAccounts);
	}

	public void updateValue(ClientTransactionReceivePayment obj) {
		updateTotalPayment(obj.getPayment());
		obj.setDummyDue(obj.getAmountDue() - obj.getPayment());
		update(obj);
	}

	public abstract void updateTotalPayment(Double payment);

	private void onHeaderCheckBoxClick(boolean isChecked) {
		resetValues();
		if (isChecked) {
			List<ClientTransactionReceivePayment> allRows = getAllRows();
			for (ClientTransactionReceivePayment row : allRows) {
				onSelectionChanged(row, true);
			}
		}
	}

	private void resetValues() {
		for (ClientCreditsAndPayments crdt : creditsAndPayments) {
			crdt.setBalance(crdt.getActualAmt());
			crdt.setRemaoningBalance(crdt.getBalance());
			crdt.setAmtTouse(0);
		}
		for (ClientTransactionReceivePayment obj : this.getRecords()) {
			resetValue(obj);
			recalculateGridAmounts();
			selectedValues.remove((Integer) indexOf(obj));
		}
		cashDiscountDialog = null;
		writeOffDialog = null;
	}

	protected abstract void recalculateGridAmounts();

	public int indexOf(ClientTransactionReceivePayment selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	public void resetValue(ClientTransactionReceivePayment obj) {
		// setAccountDefaultValues(obj);
		for (ClientCreditsAndPayments ccap : this.creditsAndPayments) {
			double balance = ccap.getBalance();
			double usedAmount = 0.0;
			for (ClientTransactionCreditsAndPayments ctcap : obj
					.getTransactionCreditsAndPayments()) {
				if (ctcap.getCreditsAndPayments() == ccap.getID()) {
					usedAmount += ctcap.getAmountToUse();
				}
			}
			ccap.setBalance(balance + usedAmount);
		}
		obj.getTransactionCreditsAndPayments().clear();
		deleteTotalPayment(obj.getPayment());
		obj.setPayment(0.0d);
		// obj.setCashDiscount(0.0d);
		obj.setWriteOff(0.0d);
		obj.setAppliedCredits(0.0d, false);
		obj.setDummyDue(obj.getAmountDue());
	}

	protected abstract void deleteTotalPayment(double d);

	public List<ClientTransactionReceivePayment> getSelectedRecords() {
		return super.getSelectedRecords(0);
	}

	public void updateAmountDue(ClientTransactionReceivePayment item) {
		double totalValue = item.getCashDiscount() + item.getWriteOff()
				+ item.getAppliedCredits() + item.getPayment();
		double amount = item.getAmountDue();
		if (!DecimalUtil.isGreaterThan(totalValue, amount)) {
			if (!DecimalUtil.isLessThan(item.getPayment(), 0.00))
				item.setDummyDue(amount - totalValue);
			else
				item.setDummyDue(amount + item.getPayment() - totalValue);

		}
	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer;
	}

	public void removeAllRecords() {
		this.clear();
	}

	public void selectRow(int count) {
		onSelectionChanged(getAllRows().get(count), true);
	}

	private void onSelectionChanged(ClientTransactionReceivePayment obj,
			boolean isChecked) {
		recalculateGridAmounts();
		int row = indexOf(obj);
		if (isChecked) {
			selectedValues.add(row);
			if (!isInViewMode()) {
				updatePayment(obj);
			}
		} else {
			selectedValues.remove((Integer) row);
			resetValue(obj);
		}
		update(obj);
		super.checkColumn(row, 0, isChecked);
		recalculateGridAmounts();
	}

	// private void updateAmountReceived() {
	// double toBeSetAmount = 0.0;
	// for (ClientTransactionReceivePayment receivePayment :
	// getSelectedRecords()) {
	// toBeSetAmount += receivePayment.getPayment();
	// }
	// setAmountRecieved(toBeSetAmount);
	// }

	protected abstract void setAmountRecieved(double toBeSetAmount);

	public void addLoadingImagePanel() {
		// TODO Auto-generated method stub

	}

	public List<ClientTransactionReceivePayment> getRecords() {
		return getAllRows();
	}

	public boolean isSelected(ClientTransactionReceivePayment trprecord) {
		return super.isChecked(trprecord, 0);
	}

	public void updateAmountsFromGUI() {
		for (ClientTransactionReceivePayment item : this.getAllRows()) {
			if (selectedValues.contains(indexOf(item))) {
				updatePayment(item);
			}
			updateFromGUI(item);
		}
		updateColumnHeaders();
	}

	public void addCreditsAndPayments(List<ClientCreditsAndPayments> credits) {
		if (credits == null) {
			return;
		}
		creditsAndPayments = credits;
		for (ClientTransactionReceivePayment ctrp : this.getRecords()) {
			for (ClientCreditsAndPayments ccap : this.creditsAndPayments) {
				double balance = ccap.getBalance();
				double usedAmount = 0.0;
				for (ClientTransactionCreditsAndPayments ctcap : ctrp
						.getTransactionCreditsAndPayments()) {
					if (ctcap.getCreditsAndPayments() == ccap.getID()) {
						usedAmount += ctcap.getAmountToUse();
					}
				}
				ccap.setBalance(balance + usedAmount);
			}
			ctrp.getTransactionCreditsAndPayments().clear();
		}

	}

}
