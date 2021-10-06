package com.nitya.accounter.web.client.ui.edittable.tables;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.ui.CheckBox;
import com.google.gwt.user.client.ui.IsWidget;
import com.google.gwt.user.client.ui.TextBoxBase;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCreditsAndPayments;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.nitya.accounter.web.client.core.ClientTransactionPayBill;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.CashDiscountDialog;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.ICurrencyProvider;
import com.nitya.accounter.web.client.ui.core.InputDialogHandler;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.customers.NewApplyCreditsDialog;
import com.nitya.accounter.web.client.ui.edittable.AmountColumn;
import com.nitya.accounter.web.client.ui.edittable.AnchorEditColumn;
import com.nitya.accounter.web.client.ui.edittable.CheckboxEditColumn;
import com.nitya.accounter.web.client.ui.edittable.EditTable;
import com.nitya.accounter.web.client.ui.edittable.RenderContext;
import com.nitya.accounter.web.client.ui.edittable.TextEditColumn;
import com.nitya.accounter.web.client.ui.widgets.DateUtills;

public abstract class TransactionPayBillTable extends
		EditTable<ClientTransactionPayBill> {
	private final boolean canEdit;
	private final List<Integer> selectedValues = new ArrayList<Integer>();
	private boolean gotCreditsAndPayments;
	public List<ClientCreditsAndPayments> creditsAndPayments = new ArrayList<ClientCreditsAndPayments>();

	public boolean isAlreadyOpened;
	private final ICurrencyProvider currencyProvider;
	private ClientTAXItem tdsCode;
	private final boolean enableDiscount;
	private long transactionId;

	public TransactionPayBillTable(boolean enableDiscount, boolean canEdit,
			ICurrencyProvider currencyProvider) {
		this.currencyProvider = currencyProvider;
		this.canEdit = canEdit;
		this.enableDiscount = enableDiscount;
	}

	public void setTranactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	@Override
	protected void initColumns() {
		this.addColumn(new CheckboxEditColumn<ClientTransactionPayBill>() {

			@Override
			protected void onChangeValue(boolean value,
					ClientTransactionPayBill row) {
				// if (!getPreferences().isCreditsApplyAutomaticEnable()) {
				// row.setPayment(row.getAmountDue() - row.getAppliedCredits());
				// }
				onSelectionChanged(row, value);
			}

			@Override
			public void render(IsWidget widget,
					RenderContext<ClientTransactionPayBill> context) {
				super.render(widget, context);
				if (isInViewMode()) {
					((CheckBox) widget).setValue(true);
				}
			}

			@Override
			public String getValueAsString(ClientTransactionPayBill row) {
				return "Dont know what is this";
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		});

		if (canEdit) {
			TextEditColumn<ClientTransactionPayBill> dueDate = new TextEditColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return DateUtills.getDateAsString(new ClientFinanceDate(row
							.getDueDate()).getDateAsObject());
				}

				@Override
				protected void setValue(ClientTransactionPayBill row,
						String value) {
					// No need
				}

				@Override
				public int getWidth() {
					return 90;
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
				public String getValueAsString(ClientTransactionPayBill row) {
					return messages.dueDate() + getValue(row).toString();
				}

				@Override
				public int insertNewLineNumber() {
					return 4;
				}
			};
			this.addColumn(dueDate);
		}

		TextEditColumn<ClientTransactionPayBill> billNo = new TextEditColumn<ClientTransactionPayBill>() {

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				return row.getBillNumber();
			}

			@Override
			protected void setValue(ClientTransactionPayBill row, String value) {
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
				return messages.billNo();
			}

			@Override
			public String getValueAsString(ClientTransactionPayBill row) {
				return messages.billNo() + getValue(row).toString();
			}

			@Override
			public int insertNewLineNumber() {
				return 1;
			}
		};
		this.addColumn(billNo);

		if (canEdit) {
			this.addColumn(new AmountColumn<ClientTransactionPayBill>(
					currencyProvider, false) {

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
					return getColumnNameWithCurrency(messages.originalAmount());
				}

				@Override
				protected Double getAmount(ClientTransactionPayBill row) {
					return row.getOriginalAmount();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						Double value) {

				}

				@Override
				public String getValueAsString(ClientTransactionPayBill row) {
					return getColumnNameWithCurrency(messages.originalAmount())
							+ getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}
			});

			this.addColumn(new AmountColumn<ClientTransactionPayBill>(
					currencyProvider, false) {

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
					return getColumnNameWithCurrency(messages.amountDue());
				}

				@Override
				protected Double getAmount(ClientTransactionPayBill row) {
					return row.getAmountDue();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						Double value) {

				}

				@Override
				public String getValueAsString(ClientTransactionPayBill row) {
					return getColumnNameWithCurrency(messages.amountDue())
							+ getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}
			});
		} else {
			this.addColumn(new AmountColumn<ClientTransactionPayBill>(
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
					return messages.billAmount();
				}

				@Override
				protected Double getAmount(ClientTransactionPayBill row) {
					return row.getOriginalAmount();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						Double value) {
					//

				}

				@Override
				public String getValueAsString(ClientTransactionPayBill row) {
					return messages.billAmount() + getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}
			});
		}

		TextEditColumn<ClientTransactionPayBill> discountDateColumn = new TextEditColumn<ClientTransactionPayBill>() {

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				return DateUtills.getDateAsString(row.getDiscountDate());
			}

			@Override
			protected void setValue(ClientTransactionPayBill row, String value) {
				// No Need
			}

			@Override
			protected boolean isEnable() {
				return false;
			}

			@Override
			public int getWidth() {
				return 95;
			}

			@Override
			protected String getColumnName() {
				return messages.discountDate();
			}

			@Override
			public String getValueAsString(ClientTransactionPayBill row) {
				return messages.discountDate() + getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}
		};

		AnchorEditColumn<ClientTransactionPayBill> discountColumn = new AnchorEditColumn<ClientTransactionPayBill>(
				currencyProvider) {

			@Override
			protected void onClick(ClientTransactionPayBill row) {
				openCashDiscountDialog(row);
			}

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				return DataUtils.getAmountAsStringInCurrency(
						row.getCashDiscount(), null);
			}

			@Override
			public int getWidth() {
				return 100;
			}

			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.discount());
			}

			@Override
			protected boolean isEnable(ClientTransactionPayBill row) {
				return selectedValues.contains(indexOf(row));
			}

			@Override
			public String getValueAsString(ClientTransactionPayBill row) {
				return getColumnNameWithCurrency(messages.discount())
						+ getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}

		};
		if (enableDiscount) {
			this.addColumn(discountDateColumn);
			this.addColumn(discountColumn);
		}

		this.addColumn(new AnchorEditColumn<ClientTransactionPayBill>(
				currencyProvider) {

			@Override
			protected void onClick(ClientTransactionPayBill row) {
				openCreditsDialog(row);
			}

			@Override
			public int getWidth() {
				return 90;
			}

			@Override
			protected String getValue(ClientTransactionPayBill row) {
				return DataUtils.getAmountAsStringInCurrency(
						row.getAppliedCredits(), null);
			}

			@Override
			protected String getColumnName() {
				return getColumnNameWithCurrency(messages.credits());
			}

			@Override
			protected boolean isEnable(ClientTransactionPayBill row) {
				return selectedValues.contains(indexOf(row));
			}

			@Override
			public String getValueAsString(ClientTransactionPayBill row) {
				return getColumnNameWithCurrency(messages.credits())
						+ getValue(row);
			}

			@Override
			public int insertNewLineNumber() {
				return 4;
			}

		});

		if (canEdit) {

			this.addColumn(new AmountColumn<ClientTransactionPayBill>(
					currencyProvider, true) {

				@Override
				public void render(IsWidget widget,
						RenderContext<ClientTransactionPayBill> context) {
					TextBoxBase box = (TextBoxBase) widget;
					String value = getValue(context.getRow());
					box.setEnabled(isEnable(context.getRow())
							&& !context.isDesable());
					box.setText(value);
				}

				private boolean isEnable(ClientTransactionPayBill row) {
					return selectedValues.contains(indexOf(row));
				}

				@Override
				protected String getColumnName() {
					return getColumnNameWithCurrency(messages.payments());
				}

				@Override
				public int getWidth() {
					return 110;
				}

				@Override
				protected Double getAmount(ClientTransactionPayBill row) {
					return row.getPayment();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						Double value) {
					double previous = row.getPayment();
					row.setPayment(value);
					double totalValue = getTotalValue(row);
					if (DecimalUtil.isGreaterThan(totalValue,
							row.getAmountDue())) {
						Accounter.showError(messages
								.totalPaymentNotExceedDueForSelectedRecords());
						row.setPayment(previous);
					}
					updateValue(row);
					adjustAmountAndEndingBalance();
					updateFootervalues(row, canEdit);
					update(row);
				}

				@Override
				public String getValueAsString(ClientTransactionPayBill row) {
					return getColumnNameWithCurrency(messages.payments())
							+ getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					return 4;
				}
			});
			addTdsColumn();
		}

		if (!canEdit) {
			this.addColumn(new TextEditColumn<ClientTransactionPayBill>() {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return "";
				}

				@Override
				protected void setValue(ClientTransactionPayBill row,
						String value) {
					// No Need
				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.referenceNo();
				}

				@Override
				public String getValueAsString(ClientTransactionPayBill row) {
					// TODO Auto-generated method stub
					return messages.referenceNo() + getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					// TODO Auto-generated method stub
					return 1;
				}
			});
			this.addColumn(new AmountColumn<ClientTransactionPayBill>(
					currencyProvider, false) {

				@Override
				protected String getValue(ClientTransactionPayBill row) {
					return DataUtils.getAmountAsStrings(row.getPayment());
					// return DataUtils.getAmountAsStringInPrimaryCurrency(row
					// .getPayment());

				}

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.amountPaid();
				}

				@Override
				protected Double getAmount(ClientTransactionPayBill row) {
					return row.getPayment();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						Double value) {

				}

				@Override
				public String getValueAsString(ClientTransactionPayBill row) {
					return messages.amountPaid() + getValue(row);
				}

				@Override
				public int insertNewLineNumber() {
					return 1;
				}
			});
		}
		if (!canEdit) {
			addTdsColumn();
		}

	}

	protected void selectAllRows(boolean value) {
		List<ClientTransactionPayBill> allRows = getAllRows();
		for (ClientTransactionPayBill row : allRows) {
			row.setPayment(row.getAmountDue());
			onSelectionChanged(row, value);
		}
	}

	private void onSelectionChanged(ClientTransactionPayBill obj,
			boolean isChecked) {
		int row = indexOf(obj);
		if (isChecked && !selectedValues.contains(row)) {
			selectedValues.add(row);
			updateValue(obj);
		} else {
			selectedValues.remove((Integer) row);
			resetValue(obj);
		}
		super.checkColumn(row, 0, isChecked);
	}

	/*
	 * This method invoked each time when record(s) get selected & it updates
	 * the footer values
	 */
	public void updateValue(ClientTransactionPayBill obj) {
		// obj.setPayment(obj.getAmountDue());
		updateTotalPayment(obj);
		calculateUnusedCredits();
		updatesAmounts(obj);
		adjustAmountAndEndingBalance();

		update(obj);
	}

	protected abstract void updateTotalPayment(ClientTransactionPayBill obj);

	public int indexOf(ClientTransactionPayBill selectedObject) {
		return getAllRows().indexOf(selectedObject);
	}

	private void openCreditsDialog(final ClientTransactionPayBill ctpb) {
		if (gotCreditsAndPayments) {
			final NewApplyCreditsDialog dialog = new NewApplyCreditsDialog(
					creditsAndPayments,
					ctpb.getTransactionCreditsAndPayments(),
					ctpb.getAmountDue(), this.currencyProvider);

			dialog.addInputDialogHandler(new InputDialogHandler() {

				@Override
				public void onCancel() {
				}

				@Override
				public boolean onOK() {
					ctpb.updatePayment();

					adjustPaymentValue(ctpb);
					updateValue(ctpb);

					updateFootervalues(ctpb, canEdit);

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

	public List<ClientTransactionPayBill> getSelectedRecords() {
		return super.getSelectedRecords(0);
	}

	private void openCashDiscountDialog(
			final ClientTransactionPayBill selectedObject) {
		// if (cashDiscountDialog == null) {
		// ClientAccount discountAccount = null;
		// if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_US) {
		// discountAccount = getCompany().getAccountByName(
		// companyConstants.cashDiscountTaken());
		// } else if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_UK) {
		// discountAccount = getCompany().getAccountByName(
		// companyConstants.discounts());
		// }
		final CashDiscountDialog cashDiscountDialog = new CashDiscountDialog(
				canEdit, selectedObject.getCashDiscount(), getCompany()
						.getAccount(selectedObject.getDiscountAccount()),
				currencyProvider);
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
				try {
					selectedObject.setCashDiscount(cashDiscountDialog
							.getCashDiscount());

					selectedObject.setDiscountAccount(cashDiscountDialog
							.getSelectedDiscountAccount().getID());
					// TODO setAttribute("cashAccount",
					// cashDiscountDialog.selectedDiscountAccount
					// .getName(), currentRow);
					selectedObject.updatePayment();
					updateValue(selectedObject);

					adjustPaymentValue(selectedObject);
					updateFootervalues(selectedObject, canEdit);
				} catch (Exception e) {
					Accounter.showError(e.getMessage());
					return false;
				}
				adjustAmountAndEndingBalance();
				return true;
			}
		});
		ViewManager.getInstance().showDialog(cashDiscountDialog);
	}

	protected abstract void adjustPaymentValue(
			ClientTransactionPayBill selectedObject);

	private ClientCompany getCompany() {
		return Accounter.getCompany();
	}

	protected abstract void updateFootervalues(ClientTransactionPayBill row,
			boolean canEdit);

	protected abstract void adjustAmountAndEndingBalance();

	private void addTdsColumn() {

		if (isTDSEnabled()) {
			this.addColumn(new AmountColumn<ClientTransactionPayBill>(
					currencyProvider, true) {

				@Override
				protected boolean isEnable() {
					return false;
				}

				@Override
				protected String getColumnName() {
					return messages.tds();
				}

				@Override
				protected Double getAmount(ClientTransactionPayBill row) {
					return row.getTdsAmount();
				}

				@Override
				protected void setAmount(ClientTransactionPayBill row,
						Double value) {
					// No Need

				}

				@Override
				public String getValueAsString(ClientTransactionPayBill row) {
					// TODO Auto-generated method stub
					return null;
				}

				@Override
				public int insertNewLineNumber() {
					// TODO Auto-generated method stub
					return 0;
				}
			});
		}
	}

	public void setTds(ClientTAXItem item) {
		if (item == null) {
			return;
		}
		this.tdsCode = item;
		for (ClientTransactionPayBill bill : getSelectedRecords()) {
			updateValue(bill);
		}
	}

	private double calculateTDS(double amount) {
		if (!isTDSEnabled()) {
			return 0.00D;
		}
		return amount * (tdsCode.getTaxRate() / 100);
	}

	public ValidationResult validateGrid() {
		ValidationResult result = new ValidationResult();
		if (this.getSelectedRecords().size() == 0) {
			result.addError(this,
					messages.pleaseSelectAnyOneOfTheTransactions());
		}

		// validates receive payment amount excesses due amount or not
		for (ClientTransactionPayBill transactionPayBill : this
				.getSelectedRecords()) {

			double totalValue = getTotalValue(transactionPayBill);
			if (DecimalUtil.isEquals(totalValue, 0)) {
				result.addError(this,
						messages.totalPaymentNotZeroForSelectedRecords());
			} else if (DecimalUtil.isGreaterThan(totalValue,
					transactionPayBill.getAmountDue())) {
				result.addError(this,
						messages.totalPaymentNotExceedDueForSelectedRecords());
			}
		}

		return result;
	}

	public void initCreditsAndPayments(final ClientVendor vendor) {
		if (isTDSEnabled()) {
			ClientTAXItem tdsCode = Accounter.getCompany().getTaxItem(
					vendor.getTaxItemCode());
			setTds(tdsCode);
		}
		Accounter
				.createHomeService()
				.getVendorCreditsAndPayments(
						vendor.getID(),
						transactionId,
						new AccounterAsyncCallback<ArrayList<ClientCreditsAndPayments>>() {

							@Override
							public void onException(AccounterException caught) {
								Accounter.showInformation(messages
										.failedTogetCreditsListAndPayments(vendor
												.getName()));

								gotCreditsAndPayments = false;
								return;

							}

							@Override
							public void onResultSuccess(
									ArrayList<ClientCreditsAndPayments> result) {
								if (result == null)
									onFailure(null);

								addCreditsAndPayments(result);

								adjustAmountAndEndingBalance();
								calculateUnusedCredits();

								gotCreditsAndPayments = true;

							}

						});

	}

	protected abstract void calculateUnusedCredits();

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

	public void resetValues() {
		/* Revert all credits to its original state */
		if (creditsAndPayments == null) {
			return;
		}
		for (ClientCreditsAndPayments crdt : creditsAndPayments) {
			if (crdt.getAmtTouse() > 0) {
				crdt.setBalance(crdt.getRemaoningBalance()
						+ crdt.getActualAmt());
				crdt.setRemaoningBalance(crdt.getBalance());
				crdt.setAmtTouse(0);
			}
		}
		for (ClientTransactionPayBill obj : this.getAllRows()) {

			obj.setPayment(0.0);
			obj.setCashDiscount(0);
			// NO NEED TO APPLY CREDITS HERE
			obj.setAppliedCredits(0, false);
			selectedValues.remove((Integer) indexOf(obj));
			update(obj);
		}
		updateFootervalues(null, canEdit);
		resetTotlas();
		calculateUnusedCredits();
	}

	protected abstract void resetTotlas();

	private void resetValue(ClientTransactionPayBill obj) {

		deleteTotalPayment(obj);
		obj.setPayment(0.0d);
		// obj.setCashDiscount(0.0d);
		// What ever credits we applied to it, reset them
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
		obj.setAppliedCredits(0.0d, false);
		obj.setCashDiscount(0.00D);
		obj.setTdsAmount(0.00D);
		update(obj);
		adjustAmountAndEndingBalance();
		updateFootervalues(obj, canEdit);
		calculateUnusedCredits();
	}

	protected abstract void deleteTotalPayment(ClientTransactionPayBill obj);

	private double getTotalValue(ClientTransactionPayBill payment) {
		double totalValue = payment.getCashDiscount()
				+ payment.getAppliedCredits() + payment.getPayment();
		return totalValue;
	}

	public void setRecords(List<ClientTransactionPayBill> records) {
		setAllRows(records);
		if (isTDSEnabled()) {
			setTds(tdsCode);
		}
	}

	public List<ClientCreditsAndPayments> getUpdatedCustomerCreditsAndPayments() {
		return creditsAndPayments;
	}

	public void removeAllRecords() {
		clear();
	}

	public void addLoadingImagePanel() {
		// TODO Auto-generated method stub

	}

	public void selectRow(int count) {
		onSelectionChanged(getRecords().get(count), true);
	}

	public List<ClientTransactionPayBill> getRecords() {
		return getAllRows();
	}

	@Override
	protected abstract boolean isInViewMode();

	public double getTDSTotal() {
		double tdsAmount = 0.00D;
		for (ClientTransactionPayBill bill : getSelectedRecords()) {
			tdsAmount += bill.getTdsAmount();
		}
		return tdsAmount;
	}

	private boolean isTDSEnabled() {
		if (getVendor() != null) {
			return (getCompany().getPreferences().isTDSEnabled() && getVendor()
					.isTdsApplicable());
		} else {
			return getCompany().getPreferences().isTDSEnabled();
		}
	}

	public abstract ClientVendor getVendor();

	private void updatesAmounts(ClientTransactionPayBill bill) {
		double tdsToPay;
		tdsToPay = calculateTDS(bill.getPayment());
		bill.setTdsAmount(tdsToPay);
	}

	public void updateAmountsFromGUI() {
		for (ClientTransactionPayBill item : this.getAllRows()) {
			updateFromGUI(item);
		}
		updateColumnHeaders();
	}

	public void addCreditsAndPayments(List<ClientCreditsAndPayments> credits) {
		if (credits == null) {
			return;
		}
		creditsAndPayments = credits;
		for (ClientTransactionPayBill ctpb : this.getRecords()) {
			for (ClientCreditsAndPayments ccap : this.creditsAndPayments) {
				double balance = ccap.getBalance();
				double usedAmount = 0.0;
				for (ClientTransactionCreditsAndPayments ctcap : ctpb
						.getTransactionCreditsAndPayments()) {
					if (ctcap.getCreditsAndPayments() == ccap.getID()) {
						usedAmount += ctcap.getAmountToUse();
					}
				}
				ccap.setBalance(balance + usedAmount);
			}
			ctpb.getTransactionCreditsAndPayments().clear();
		}

	}

}
