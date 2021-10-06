package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientCreditsAndPayments;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPayBill;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXItem;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionCreditsAndPayments;
import com.nitya.accounter.web.client.core.ClientTransactionPayBill;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.Lists.PayBillTransactionList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.combo.TaxItemCombo;
import com.nitya.accounter.web.client.ui.combo.VendorCombo;
import com.nitya.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.edittable.tables.TransactionPayBillTable;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.SelectItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class PayBillView extends AbstractTransactionBaseView<ClientPayBill>
		implements IPrintableView {

	AmountField endBalText;
	DateField date;
	DateField dueDate;
	protected String vendorPaymentMethod;
	private TransactionPayBillTable grid;
	protected AmountField cashDiscountTextItem;
	protected AmountField creditTextItem;
	public AmountLabel unUsedCreditsText, amountLabelForeign, amountLableBase;

	TaxItemCombo tdsCombo;
	protected SelectCombo vendorPaymentMethodCombo;
	protected List<PayBillTransactionList> paybillTransactionList;
	protected List<PayBillTransactionList> filterList;
	protected List<PayBillTransactionList> tempList;
	private ClientFinanceDate dueDateOnOrBefore;
	private DynamicForm payForm, filterForm, textForm;
	private int size;
	// public double transactionTotal = 0.0d;
	public double totalAmountDue = 0.0d;
	public double totalCashDiscount = 0.0d;
	public double totalOriginalAmount = 0.0d;
	public double totalOrginalAmt = 0.0D, totalDueAmt = 0.0D,
			totalPayment = 0.0D, cashDiscount = 0.0d;
	private ArrayList<DynamicForm> listforms;
	private StyledPanel gridLayout;
	private TextItem checkNoText;
	private CheckboxItem printCheck;
	private boolean isChecked = false;

	// Upate
	protected PayFromAccountsCombo payFromCombo;
	protected ClientAccount payFromAccount;
	protected ClientVendor vendor;
	protected List<ClientVendor> vendors;
	protected VendorCombo vendorCombo;
	private AmountLabel tdsPayableAmount;
	private AmountLabel amountToVendor;
	private double toBeSetEndingBalance;

	private ClientCurrency vendorCurrency;

	public PayBillView() {
		super(ClientTransaction.TYPE_PAY_BILL);
		this.getElement().setId("paybillview");
	}

	/*
	 * This method invoked when all records in grid are de-selected.It resets
	 * the non-editable fields
	 */
	public void resetTotlas() {
		amountLabelForeign.setAmount(0.0);
		amountLableBase.setAmount(0.0);
		toBeSetEndingBalance = payFromCombo.getSelectedValue() != null ? payFromCombo
				.getSelectedValue().getTotalBalance() : 0.0;
		endBalText.setAmount(toBeSetEndingBalance);
	}

	/*
	 * This method invoked eachtime when there is a change in records and it
	 * updates the noneditable amount fields
	 */
	public void adjustAmountAndEndingBalance() {
		List<ClientTransactionPayBill> selectedRecords = grid
				.getSelectedRecords();
		double toBeSetAmount = 0.0;
		for (ClientTransactionPayBill rec : selectedRecords) {
			toBeSetAmount += rec.getPayment();
		}
		if (this.transaction != null) {
			Double totalPayment = toBeSetAmount;
			Double tdsTotal = grid.getTDSTotal();

			amountToVendor.setAmount(totalPayment - tdsTotal);
			tdsPayableAmount.setAmount(tdsTotal);
			amountLabelForeign.setAmount(totalPayment);
			amountLableBase.setAmount(getAmountInBaseCurrency(toBeSetAmount));

			if (payFromAccount != null) {
				if (isInViewMode()) {
					endBalText.setAmount(payFromAccount.getTotalBalance());
				} else {
					if (payFromAccount.isIncrease()) {
						toBeSetEndingBalance = payFromAccount.getTotalBalance()
								+ amountToVendor.getAmount();
					} else {
						toBeSetEndingBalance = payFromAccount.getTotalBalance()
								- amountToVendor.getAmount();
					}
				}
			}
		}
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;

		if (paymentMethod != null) {
			this.paymentMethod = paymentMethod;
			if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
				printCheck.setEnabled(true);
				checkNoText.setEnabled(true);
			} else {
				// paymentMethodCombo.setComboItem(paymentMethod);
				printCheck.setEnabled(false);
				checkNoText.setEnabled(false);
			}
		}

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type of Enter Bill
		transaction.setType(ClientTransaction.TYPE_PAY_BILL);
		transaction.setPayBillType(ClientPayBill.TYPE_PAYBILL);

		// transaction.setTaxItem(taxItem)

		// Setting Date
		transaction.setDate(date.getEnteredDate().getDate());

		// Setting Pay From
		if (payFromAccount != null)
			transaction.setPayFrom(payFromAccount);

		// Setting payment method
		if (paymentMethod != null) {
			transaction.setPaymentMethod(paymentMethod);
			if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
				if (checkNoText.getValue() != null
						&& !checkNoText.getValue().equals("")) {
					String value = String.valueOf(checkNoText.getValue());
					transaction.setCheckNumber(value);
				} else {
					transaction.setCheckNumber("");

				}
			} else {
				transaction.setCheckNumber("");
			}
		}

		// transaction.setIsToBePrinted(isChecked);

		// Setting Bill due or before
		if (dueDate.getEnteredDate() != null)
			transaction.setBillDueOnOrBefore(dueDate.getEnteredDate());
		// Setting Vendor
		if (getVendor() != null)
			transaction.setVendor(getVendor());
		// Setting Amount

		transaction.setTotal(amountLabelForeign.getAmount());
		if (getPreferences().isTDSEnabled() && vendor.isTdsApplicable()) {
			ClientTAXItem taxItem = tdsCombo.getSelectedValue();
			if (taxItem != null) {
				transaction.setTdsTaxItem(taxItem.getID());
			}
		}

		// Setting ending Balance
		transaction.setEndingBalance(toBeSetEndingBalance);

		transaction.setMemo(memoTextAreaItem.getValue().toString());
		if (getPreferences().isTDSEnabled() && getVendor().isTdsApplicable()) {
			transaction.setTdsTotal(grid.getTDSTotal());
		}
		// Setting Transactions
		List<ClientTransactionPayBill> selectedRecords = grid
				.getSelectedRecords();
		List<ClientTransactionPayBill> transactionPayBill = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill tpbRecord : selectedRecords) {
			tpbRecord.setID(0);
			transactionPayBill.add(tpbRecord);
		}

		transaction.setTransactionPayBill(transactionPayBill);

		transaction.setUnUsedCredits(this.unUsedCreditsText.getAmount());

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	private String getCheckValue() {
		String value;
		if (!isInViewMode()) {
			if (checkNoText.getValue().equals(messages.toBePrinted())) {
				value = String.valueOf(messages.toBePrinted());

			} else
				value = String.valueOf(checkNoText.getValue());
		} else {
			String checknumber;
			checknumber = this.checkNumber;
			if (checknumber == null) {
				checknumber = messages.toBePrinted();
			}
			if (checknumber.equals(messages.toBePrinted()))
				value = messages.toBePrinted();
			else
				value = String.valueOf(checknumber);
		}
		return value;
	}

	private void initListGrid() {
		if (grid != null) {
			grid.removeFromParent();
		}
		grid = new TransactionPayBillTable(isTrackDiscounts(), !isInViewMode(),
				this) {

			@Override
			protected void updateFootervalues(ClientTransactionPayBill row,
					boolean canEdit) {
				PayBillView.this.updateFootervalues(row, canEdit);
			}

			@Override
			protected void setUnUsedCreditsTextAmount(Double totalBalances) {
				PayBillView.this.setUnUsedCredits(totalBalances);
			}

			@Override
			protected void resetTotlas() {
				PayBillView.this.resetTotlas();
			}

			@Override
			protected void deleteTotalPayment(ClientTransactionPayBill obj) {
				PayBillView.this.deleteTotalPayment(obj);
			}

			@Override
			protected void updateTotalPayment(ClientTransactionPayBill obj) {
				PayBillView.this.updateTotalPayment(obj);
			}

			@Override
			protected void calculateUnusedCredits() {
				PayBillView.this.calculateUnusedCredits();
			}

			@Override
			protected void adjustPaymentValue(
					ClientTransactionPayBill selectedObject) {
				PayBillView.this.adjustPaymentValue(selectedObject);
			}

			@Override
			protected void adjustAmountAndEndingBalance() {
				PayBillView.this.adjustAmountAndEndingBalance();
			}

			@Override
			protected boolean isInViewMode() {
				return PayBillView.this.isInViewMode();
			}

			@Override
			public ClientVendor getVendor() {
				return PayBillView.this.vendorCombo.getSelectedValue();
			}
		};
		grid.setEnabled(!isInViewMode());
		gridLayout.insert(grid, 1);
	}

	/**
	 * @param obj
	 */
	protected void updateTotalPayment(ClientTransactionPayBill obj) {
		ClientTransaction transactionObject = getTransactionObject();
		// paybillView.gettrantransactionTotal = 0.0;
		transactionObject.setTotal(0.0);
		for (ClientTransactionPayBill rec : grid.getSelectedRecords()) {
			transactionObject.setTotal(transactionObject.getTotal()
					+ rec.getPayment());
			totalCashDiscount += rec.getCashDiscount();
		}
		adjustPaymentValue(obj);
	}

	/*
	 * This method invoked each time when there is a change in cashdicount or
	 * credits or on record's paymentvalue change
	 */
	public void adjustPaymentValue(ClientTransactionPayBill rec) {

		Double amountDue = rec.getAmountDue();
		Double cashDiscount = rec.getCashDiscount();
		Double credit = rec.getAppliedCredits();
		Double payments = amountDue - (cashDiscount + credit);
		if (rec.getPayment() == 0
				&& !getPreferences().isCreditsApplyAutomaticEnable()) {
			rec.setPayment(payments);
		}

		rec.setCashDiscount(cashDiscount);

		grid.update(rec);
		adjustAmountAndEndingBalance();
	}

	private ClientTransactionPayBill getTransactionPayBillByTransaction(
			int transactionType, long transactionId) {
		for (ClientTransactionPayBill bill : transaction
				.getTransactionPayBill()) {
			if ((transactionType == ClientTransaction.TYPE_ENTER_BILL && bill
					.getEnterBill() == transactionId)
					|| (transactionType == ClientTransaction.TYPE_TRANSFER_FUND && bill
							.getTransactionMakeDeposit() == transactionId)
					|| (transactionType == ClientTransaction.TYPE_JOURNAL_ENTRY && bill
							.getJournalEntry() == transactionId)) {
				return bill;
			}
		}
		return null;
	}

	public void updateFooterValues() {
		/* if no records are there */
		totalOrginalAmt = 0.0D;
		totalDueAmt = 0.0D;
		cashDiscount = 0.0D;
		// if (!isEdit)
		// gridView.updateFooterValues(DataUtils
		// .getAmountAsString(totalOrginalAmt), 2);
		// if (!isEdit)
		// gridView.updateFooterValues(DataUtils
		// .getAmountAsString(totalDueAmt), 3);
		// gridView.updateFooterValues(DataUtils.getAmountAsString(totalPayment),
		// isEdit ? 6 : 7);
		// if (!isEdit)
		// gridView.updateFooterValues(DataUtils
		// .getAmountAsString(cashDiscount), 5);
	}

	private ClientVendor getVendorById(long id) {
		for (ClientVendor temp : vendors) {
			if (temp.getID() == id)
				return temp;
		}
		return null;
	}

	@Override
	protected void createControls() {

		listforms = new ArrayList<DynamicForm>();

		Label lab = new Label(messages.payBill());
		lab.removeStyleName("gwt-Label");
		lab.addStyleName("label-title");

		locationCombo = createLocationCombo();
		date = new DateField(messages.date(), "date");
		date.setToolTip(messages.selectDateWhenTransactioCreated(this
				.getAction().getViewName()));
		date.setEnteredDate(new ClientFinanceDate());
		setTransactionDate(new ClientFinanceDate());
		date.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate changedDate) {
				if (changedDate != null) {
					onDateChanged(changedDate);
				}
			}

		});
		transactionNumber = createTransactionNumberItem();
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setDefaultValue(messages.onlineBanking());

		printCheck = new CheckboxItem(messages.toBePrinted(), "printCheck");
		printCheck.setValue(true);
		printCheck.setEnabled(false);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNoText.setValue(messages.toBePrinted());
						checkNoText.setEnabled(false);
					} else {
						if (payFromCombo.getValue() == null)
							checkNoText.setValue(messages.toBePrinted());
						else if (transaction != null) {
							checkNoText.setValue(transaction.getCheckNumber());
						}
					}
				} else
					checkNoText.setValue("");
				checkNoText.setEnabled(true);

			}
		});

		checkNoText = new TextItem(messages.chequeNo(), "checkNoText");
		checkNoText.setValue(messages.toBePrinted());
		// checkNoText.setHelpInformation(true);
		// checkNoText.setWidth(100);

		if (paymentMethodCombo.getSelectedValue() != null
				&& !paymentMethodCombo.getSelectedValue().equals(
						UIUtils.getpaymentMethodCheckBy_CompanyType(messages
								.check()))) {
			checkNoText.setEnabled(false);
		} else {
			if (paymentMethodCombo.getSelectedValue() != null
					&& !paymentMethodCombo
							.getSelectedValue()
							.equals(UIUtils
									.getpaymentMethodCheckBy_CompanyType(messages
											.check()))) {
				checkNoText.setEnabled(false);
			}
		}

		checkNoText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNoText.getValue().toString();
			}
		});

		vendorCombo = createVendorComboItem(messages.payeeName(Global.get()
				.Vendor()));
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						grid.isAlreadyOpened = false;

						vendorSelected(selectItem);
					}
				});

		payFromCombo = createPayFromCombo(messages.payFrom());
		payFromCombo.setPopupWidth("500px");

		dueDate = new DateField(messages.filterByBilldueonorbefore(), "dueDate");
		// dueDate.setHelpInformation(true);
		dueDate.setValue(new ClientFinanceDate());
		// dueDate.setUseTextField(true);
		// dueDate.setWidth(100);

		dueDate.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (paybillTransactionList != null
						&& (dueDateOnOrBefore != null && !dueDateOnOrBefore
								.equals(date))) {
					// if (event.getSource() != null) {
					// dueDateOnOrBefore = ((DateField) event.getSource())
					// .getValue();
					dueDateOnOrBefore = dueDate.getValue();
					clearGrid();
					filterGrid();
					grid.resetValues();
					// }
				}

			}
		});

		vendorPaymentMethodCombo = createPaymentMethodSelectItem();
		vendorPaymentMethodCombo.setRequired(false);
		vendorPaymentMethodCombo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (vendorPaymentMethodCombo.getSelectedValue() != null)
					vendorPaymentMethod = ((SelectItem) event.getSource())
							.getValue().toString();
				if (paybillTransactionList != null) {
					clearGrid();
					filterGrid();
				}

			}
		});

		// filterForm = new DynamicForm();
		// filterForm.setIsGroup(true);
		// filterForm.setGroupTitle(messages.Filter());
		// filterForm.setFields(dueDate);

		DynamicForm dateForm = new DynamicForm("dateForm");
		// dateForm.setNumCols(6);
		dateForm.setStyleName("datenumber-panel");
		dateForm.add(date, transactionNumber);

		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateForm);

		payForm = new DynamicForm("payForm");
		// payForm.setIsGroup(true);
		// payForm.setGroupTitle(messages.payment());
		payForm.add(vendorCombo, payFromCombo, paymentMethodCombo, dueDate);

		endBalText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency(), "endBalText");
		// endBalText.setHelpInformation(true);
		// endBalText.setWidth(100);
		endBalText.setValue("" + UIUtils.getCurrencySymbol() + "0.00");
		endBalText.setEnabled(false);
		tdsCombo = new TaxItemCombo(messages.tds(),
				ClientTAXAgency.TAX_TYPE_TDS);
		tdsCombo.setEnabled(!isInViewMode());
		tdsCombo.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXItem>() {

			@Override
			public void selectedComboBoxItem(ClientTAXItem selectItem) {
				grid.setTds(selectItem);
			}
		});

		DynamicForm balForm = new DynamicForm("balForm");
		// balForm.setIsGroup(true);
		if (locationTrackingEnabled)
			balForm.add(locationCombo);
		// balForm.setGroupTitle(messages.balances());
		balForm.add(endBalText, tdsCombo, printCheck, checkNoText);
		if (!getCompany().getPreferences().isTDSEnabled()) {
			tdsCombo.setVisible(false);
		}
		// if (getPreferences().isClassTrackingEnabled()
		// && getPreferences().isClassOnePerTransaction()) {
		// classListCombo = createAccounterClassListCombo();
		// balForm.setFields(classListCombo);
		// }

		Label lab1 = new Label(messages.billsDue());
		lab1.addStyleName("editTableTitle");
		memoTextAreaItem = createMemoTextAreaItem();

		DynamicForm memoForm = new DynamicForm("memoForm");
		memoForm.add(memoTextAreaItem);
		unUsedCreditsText = new AmountLabel(
				messages.unusedCreditsWithCurrencyName(getBaseCurrency()
						.getFormalName()), getBaseCurrency());
		unUsedCreditsText.setEnabled(false);

		amountLabelForeign = new AmountLabel(
				messages.currencyTotal(getBaseCurrency().getFormalName()),
				getBaseCurrency());

		amountLableBase = new AmountLabel(
				messages.currencyTotal(getBaseCurrency().getFormalName()),
				getBaseCurrency());

		currencyWidget = createCurrencyFactorWidget();

		this.tdsPayableAmount = new AmountLabel(
				messages.tdsAmountWithCurrencyName(getBaseCurrency()
						.getFormalName()), getBaseCurrency());

		this.amountToVendor = new AmountLabel(
				messages.payeePaymentWithCurrencyName(Global.get().Vendor(),
						getBaseCurrency().getFormalName()), getBaseCurrency());

		textForm = new DynamicForm("textForm");

		addTextFormFields();

		StyledPanel bottompanel = new StyledPanel("bottompanel");
		bottompanel.add(memoForm);
		bottompanel.add(textForm);

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		leftVLay.add(payForm);
		StyledPanel vpPanel = new StyledPanel("vpPanel");
		vpPanel.add(balForm);

		if (isMultiCurrencyEnabled()) {
			vpPanel.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}

		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("fields-panel");
		topHLay.add(leftVLay);
		topHLay.add(vpPanel);

		gridLayout = new StyledPanel("gridLayout");
		gridLayout.add(lab1);
		initListGrid();
		gridLayout.add(bottompanel);

		this.add(lab);
		this.add(voidedPanel);
		this.add(datepanel);
		this.add(topHLay);
		this.add(gridLayout);

		/* Adding dynamic forms in list */
		listforms.add(payForm);
		listforms.add(balForm);
		listforms.add(textForm);
		// setTabindexes();

	}

	protected void onDateChanged(ClientFinanceDate changedDate) {
		try {
			ClientFinanceDate newDate = date.getValue();
			if (newDate != null) {
				setTransactionDate(newDate);
				if (vendor != null && !isInViewMode()) {
					getTransactionPayBills(vendor);
				}
			}
		} catch (Exception e) {
			Accounter.showError(messages.invalidTransactionDate());
		}

	}

	private void addTextFormFields() {
		// textForm.setNumCols(2);
		textForm.setStyleName("unused-payments");
		if (!isInViewMode()) {
			textForm.add(unUsedCreditsText, amountToVendor, tdsPayableAmount,
					amountLableBase);
		} else {
			textForm.add(amountToVendor, tdsPayableAmount, amountLableBase);
		}
		if (isMultiCurrencyEnabled()) {
			textForm.add(amountLabelForeign);
		}
		if (!getPreferences().isTDSEnabled()) {
			amountToVendor.setVisible(false);
			tdsPayableAmount.setVisible(false);
		}

		if (isMultiCurrencyEnabled()) {
			if (!isInViewMode()) {
				amountLabelForeign.hide();
			}
		}
	}

	private void setUnUsedCredits(Double unusedCredits) {
		unUsedCreditsText.setAmount(unusedCredits);
	}

	public void calculateUnusedCredits() {

		if (getCompany().getPreferences().isCreditsApplyAutomaticEnable()) {
			List<ClientTransactionPayBill> allRows = grid.getSelectedRecords();
			// First add all credits back
			for (ClientTransactionPayBill c : allRows) {
				if (c.creditsAppliedManually) {
					continue;
				}
				// First add all credits back
				for (ClientTransactionCreditsAndPayments ctcap : c
						.getTransactionCreditsAndPayments()) {
					for (ClientCreditsAndPayments ccap : grid.creditsAndPayments) {
						if (ctcap.getCreditsAndPayments() == ccap.getID()) {
							ccap.setBalance(ccap.getBalance()
									+ ctcap.getAmountToUse());
						}
					}
				}
				// If we have got credits by this time then clear all
				// transaction cap
				if (grid.creditsAndPayments.size() > 0) {
					c.getTransactionCreditsAndPayments().clear();
				}
			}
			// now distribute again
			for (ClientTransactionPayBill c : allRows) {
				if (c.creditsAppliedManually) {
					continue;
				}

				double due = c.getAmountDue();
				if (due <= 0) {
					continue;
				}
				double creditsApplied = 0.0;
				List<ClientTransactionCreditsAndPayments> transactionCreditsAndPayments = c
						.getTransactionCreditsAndPayments();
				for (ClientCreditsAndPayments ccap : grid.creditsAndPayments) {
					if (ccap.getBalance() > 0 && due > 0) {
						ClientTransactionCreditsAndPayments ctcap = new ClientTransactionCreditsAndPayments();
						double amountToUse = Math.min(due, ccap.getBalance());
						ctcap.setAmountToUse(amountToUse);
						ctcap.setCreditsAndPayments(ccap.getID());
						transactionCreditsAndPayments.add(ctcap);
						ccap.setBalance(ccap.getBalance() - amountToUse);
						due -= amountToUse;
						creditsApplied += amountToUse;
					}
				}
				c.setAppliedCredits(creditsApplied, false);
				c.setCreditsApplied(true);
				if (c.getPayment() == 0) {
					c.setPayment(due);
				}
				grid.update(c);
			}
			adjustAmountAndEndingBalance();
		}

		this.unUsedCreditsText.setAmount(grid.getUnusedCredits());

	}

	@Override
	protected void vendorSelected(final ClientVendor vendor) {

		long currency = vendor.getCurrency();
		if (currency != 0) {
			vendorCurrency = getCurrency(currency);
		} else {
			vendorCurrency = getCompany().getPrimaryCurrency();
		}

		// endBalText.setCurrency(vendorCurrency);

		amountLabelForeign.setTitle(messages.currencyTotal(vendorCurrency
				.getFormalName()));
		amountLabelForeign.setCurrency(vendorCurrency);

		tdsPayableAmount.setTitle(messages
				.tdsAmountWithCurrencyName(vendorCurrency.getFormalName()));
		tdsPayableAmount.setCurrency(vendorCurrency);

		amountToVendor.setTitle(messages.payeePaymentWithCurrencyName(Global
				.get().Vendor(), vendorCurrency.getFormalName()));
		amountToVendor.setCurrency(vendorCurrency);

		unUsedCreditsText.setTitle(messages
				.unusedCreditsWithCurrencyName(vendorCurrency.getFormalName()));
		unUsedCreditsText.setCurrency(vendorCurrency);

		grid.resetValues();
		grid.creditsAndPayments.clear();
		this.setVendor(vendor);
		transaction.setVendor(vendor);
		if (!isInViewMode()) {
			updateTDSFields();
			paymentMethodCombo.setComboItem(vendor.getPaymentMethod());
		}

		initListGrid();

		if (!isInViewMode()) {
			grid.initCreditsAndPayments(vendor);
		}

		if (transaction.id == 0) {
			grid.addLoadingImagePanel();
			getTransactionPayBills(vendor);
		}
		initTransactionTotalNonEditableItem();

		if (vendorCurrency.getID() != 0) {
			currencyWidget.setSelectedCurrencyFactorInWidget(vendorCurrency,
					date.getValue().getDate());
		} else {
			currencyWidget.setSelectedCurrency(getBaseCurrency());
		}

		if (isMultiCurrencyEnabled() && !isInViewMode()) {
			super.setCurrency(vendorCurrency);
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
		calculateUnusedCredits();
	}

	private void updateTDSFields() {
		if (isTDSEnable()) {
			tdsCombo.setVisible(true);
			amountToVendor.setVisible(true);
			tdsPayableAmount.setVisible(true);
			ClientTAXItem taxItem = getCompany().getTAXItem(
					vendor.getTaxItemCode());
			if (transaction.getTdsTaxItem() == 0 && taxItem != null) {
				tdsCombo.setComboItem(taxItem);
			}
		} else {
			tdsCombo.setVisible(false);
			amountToVendor.setVisible(false);
			tdsPayableAmount.setVisible(false);
		}

	}

	private void getTransactionPayBills(ClientVendor vendor) {

		if (date.getDate().getDate() == 0) {

			Accounter.showInformation(messages.pleaseSelect(messages.date()));
			return;
		}

		ClientFinanceDate paymentDate = date.getDate();

		this.rpcUtilService
				.getTransactionPayBills(
						vendor.getID(),
						paymentDate,
						new AccounterAsyncCallback<ArrayList<PayBillTransactionList>>() {

							@Override
							public void onException(AccounterException caught) {
								grid.addEmptyMessage(messages.noRecordsToShow());
							}

							@Override
							public void onResultSuccess(
									ArrayList<PayBillTransactionList> result) {
								if (result == null) {
									result = new ArrayList<PayBillTransactionList>();
								}
								onGetPayBillTransactionList(result);
							}

						});

	}

	protected void onGetPayBillTransactionList(
			ArrayList<PayBillTransactionList> result) {
		paybillTransactionList = result;
		grid.removeAllRecords();
		if (!result.isEmpty() || !transaction.getTransactionPayBill().isEmpty()) {
			dueDateOnOrBefore = dueDate.getValue();
			clearGrid();
			filterGrid();
		} else {
			grid.addEmptyMessage(messages.noRecordsToShow());
			updateFooterValues();
		}
	}

	@Override
	protected void accountSelected(ClientAccount account) {

		if (account == null)
			return;
		this.payFromAccount = account;
		endBalText.setAmount(payFromAccount.getCurrentBalance());
		endBalText.setCurrency(getCurrency(payFromAccount.getCurrency()));
		adjustAmountAndEndingBalance();
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			ClientPayBill clientPayBill = new ClientPayBill();
			clientPayBill.setAmountIncludeTDS(true);
			setData(clientPayBill);
		} else {
			grid.setTranactionId(transaction.id);
			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setEnabled(!isInViewMode());
			}
			this.setVendor(getCompany().getVendor(transaction.getVendor()));
			vendorCombo.setComboItem(getVendor());
			vendorSelected(getCompany().getVendor(transaction.getVendor()));
			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			paymentMethodCombo.setEnabled(!isInViewMode());

			if (transaction.getCheckNumber() != null) {
				if (transaction.getCheckNumber().equals(messages.toBePrinted())) {
					checkNoText.setValue(messages.toBePrinted());
					printCheck.setValue(true);
				} else {
					checkNoText.setValue(transaction.getCheckNumber());
					printCheck.setValue(false);
				}
			}

			this.transactionItems = transaction.getTransactionItems();

			payFromCombo.setComboItem(getCompany().getAccount(
					transaction.getPayFrom()));

			date.setDateWithNoEvent(transaction.getDate());
			date.setEnabled(!isInViewMode());
			accountSelected(getCompany().getAccount(transaction.getPayFrom()));

			dueDate.setValue(new ClientFinanceDate(transaction
					.getBillDueOnOrBefore()));
			dueDate.setEnabled(!isInViewMode());

			transactionNumber.setValue(transaction.getNumber());
			transactionNumber.setEnabled(!isInViewMode());

			amountLabelForeign.setAmount(transaction.getNetAmount());

			amountLableBase.setAmount(getAmountInBaseCurrency(transaction
					.getNetAmount()));

			endBalText.setAmount(transaction.getEndingBalance());
			initListGridData(this.transaction.getTransactionPayBill());
			initTransactionTotalNonEditableItem();
			initMemoAndReference();
			adjustAmountAndEndingBalance();
			ClientTAXItem taxItem = getCompany().getTAXItem(
					transaction.getTdsTaxItem());

			if (tdsCombo != null && taxItem != null) {
				tdsCombo.setComboItem(taxItem);
			}

			tdsCombo.setVisible(isTDSEnable());
		}

		initVendors();
		initTransactionTotalNonEditableItem();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));

		initPayFromAccounts();
		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(isInViewMode());
		memoTextAreaItem.setValue(transaction.getMemo());
	}

	private void initListGridData(List<ClientTransactionPayBill> list) {
		int count = 0;
		double totalOrgAmt = 0.0;
		// double totalDueAmount = 0.0;
		// double cashDiscount = 0.0;
		// double credits = 0.0;
		// double payment = 0.0;
		// grid.setRecords(list);
		for (ClientTransactionPayBill receivePayment : list) {
			totalOrgAmt += receivePayment.getOriginalAmount();
			this.grid.add(receivePayment);
			// this.grid.selectRow(count);
			count++;
		}
	}

	protected void initTransactionTotalNonEditableItem() {
		if (transaction == null)
			return;
		ClientPayBill paybill = transaction;
		Double unusedCredits = paybill.getUnUsedCredits();
		setUnUsedCredits(unusedCredits);
	}

	@Override
	public void updateNonEditableItems() {

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (vendorCombo.getSelectedValue() == null) {
			vendorCombo.setValue("");
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}
		ValidationResult payFormValidationResult = payForm.validate();
		if (payFormValidationResult.haveErrors()
				|| payFormValidationResult.haveWarnings()) {
			result.add(payFormValidationResult);
			return result;
		}

		if (filterForm != null) {
			result.add(filterForm.validate());
			return result;
		}
		if (!isInViewMode()) {
			if (grid.getAllRows().isEmpty()) {
				result.addError(grid,
						messages.noBillsAreAvailableFirstAddABill(Global.get()
								.Vendor()));
			}

			else {

				result.add(grid.validateGrid());
			}
		}

		if (isTDSEnable()) {
			ClientTAXItem selectedValue = tdsCombo.getSelectedValue();
			if (selectedValue == null) {
				result.addError(tdsCombo, messages.pleaseSelectTDS());
			}
		}
		ClientAccount bankAccount = payFromCombo.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());
			if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
				result.addError(payFromCombo,
						messages.selectProperBankAccount());
			}
		}
		return result;
	}

	public ClientPayBill saveView() {
		// ClientPayBill saveView = super.saveView();
		// if (saveView != null) {
		// updateTransaction();
		// }
		return null;
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(transaction);

	}

	protected void clearGrid() {
		grid.removeAllRecords();
	}

	protected void filterGrid() {
		filterList = new ArrayList<PayBillTransactionList>();
		tempList = new ArrayList<PayBillTransactionList>();

		filterList.addAll(paybillTransactionList);

		if (dueDateOnOrBefore != null) {
			for (PayBillTransactionList cont : filterList) {
				if (cont.getDueDate().before(dueDateOnOrBefore)
						|| cont.getDueDate().equals(dueDateOnOrBefore))
					tempList.add(cont);

			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		if (getVendor() != null) {

			for (PayBillTransactionList cont : filterList) {
				if (getVendor().getName().toString()
						.equalsIgnoreCase(cont.getVendorName().toString())) {

					tempList.add(cont);
				}
			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		List<ClientTransactionPayBill> records = new ArrayList<ClientTransactionPayBill>();
		totalOrginalAmt = 0.0d;
		totalDueAmt = 0.0d;
		totalPayment = 0.0d;
		cashDiscount = 0.0d;

		for (PayBillTransactionList cont : filterList) {
			ClientTransactionPayBill record = getTransactionPayBillByTransaction(
					cont.getType(), cont.getTransactionId());
			double amountDue = 0.00D;
			if (record == null) {
				record = new ClientTransactionPayBill();
				if (cont.getType() == ClientTransaction.TYPE_ENTER_BILL) {
					record.setEnterBill(cont.getTransactionId());
				} else if (cont.getType() == ClientTransaction.TYPE_TRANSFER_FUND) {
					record.setTransactionMakeDeposit(cont.getTransactionId());
				} else if (cont.getType() == ClientTransaction.TYPE_JOURNAL_ENTRY) {
					record.setJournalEntry(cont.getTransactionId());
				}
			} else {
				transaction.getTransactionPayBill().remove(record);
				amountDue = record.getPayment() + record.getAppliedCredits()
						+ record.getCashDiscount();
			}
			amountDue += cont.getAmountDue();
			record.setAmountDue(amountDue);
			record.setDummyDue(amountDue);

			record.setBillNumber(cont.getBillNumber());
			record.setCashDiscount(record.getCashDiscount()
					+ cont.getCashDiscount());

			record.setAppliedCredits(cont.getCredits(), false);

			record.setDiscountDate(cont.getDiscountDate() != null ? cont
					.getDiscountDate().getDate() : 0);

			record.setDueDate(cont.getDueDate() != null ? cont.getDueDate()
					.getDate() : 0);

			record.setOriginalAmount(cont.getOriginalAmount());
			record.setPayment(cont.getPayment());
			record.setDiscountAccount(getCompany().getCashDiscountAccount());

			totalOrginalAmt += record.getOriginalAmount();
			totalDueAmt += record.getAmountDue();
			totalPayment += record.getPayment();
			cashDiscount += record.getCashDiscount();

			record.setTdsAmount(0.00D);
			record.setPayment(0.00D);

			records.add(record);
		}
		for (ClientTransactionPayBill bill : transaction
				.getTransactionPayBill()) {
			if (DecimalUtil.isEquals(bill.getAmountDue(), 0.00D)) {
				bill.setAmountDue(bill.getPayment() + bill.getAppliedCredits()
						+ bill.getCashDiscount());
			}
			bill.setPayment(0.00D);
			bill.setTdsAmount(0.00D);
			bill.setAppliedCredits(0.00D, false);
			bill.setCashDiscount(0);
			bill.setDiscountAccount(0);

			records.add(bill);
		}

		grid.setRecords(records);
		size = records.size();
		if (size == 0) {
			grid.addEmptyMessage(messages.noRecordsToShow());
		} else {
			grid.resetValues();
		}
	}

	@Override
	public List<DynamicForm> getForms() {

		return listforms;
	}

	/**
	 * call this method to set focus in View
	 */
	@Override
	public void setFocus() {
		this.vendorCombo.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// Nothing To Do

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// Nothing TO DO

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {

		if (!transaction.isVoid()) {
			AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

				@Override
				public void onFailure(Throwable caught) {
					if (caught instanceof InvocationException) {
						Accounter.showMessage(messages.sessionExpired());
					} else {
						int errorCode = ((AccounterException) caught)
								.getErrorCode();
						Accounter.showError(AccounterExceptions
								.getErrorString(errorCode));

					}
				}

				@Override
				public void onSuccess(Boolean result) {
					if (result)
						enableFormItems();
				}

			};

			AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
					.getType());
			this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
		} else if (transaction.isVoid() || transaction.isDeleted()) {
			Accounter.showError(messages.failedtovoidTransaction());
		}
	}

	public void updateFootervalues(ClientTransactionPayBill obj, boolean canEdit) {
		ClientTransaction transactionObject = this.getTransactionObject();
		if (canEdit) {
			transactionObject.setTotal(0.0);
			this.totalCashDiscount = 0.0;
			for (ClientTransactionPayBill rec : grid.getSelectedRecords()) {
				transactionObject.setTotal(transactionObject.getTotal()
						+ rec.getPayment());
				this.totalCashDiscount += rec.getCashDiscount();
			}
		} else {
			transactionObject.setTotal(0.0);
			this.totalCashDiscount = 0.0;
			this.totalOriginalAmount = 0.0;
			for (ClientTransactionPayBill rec : grid.getRecords()) {
				this.totalOriginalAmount += rec.getOriginalAmount();
				transactionObject.setTotal(transactionObject.getTotal()
						+ rec.getPayment());
				this.totalCashDiscount += rec.getCashDiscount();
			}
		}
	}

	private void enableFormItems() {

		setMode(EditMode.EDIT);
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		date.setEnabled(!isInViewMode());
		// vendorCombo.setEnabled((!isInViewMode() ? true :
		// !getData().isDraft()));
		payFromCombo.setEnabled(!isInViewMode());

		if (paymentMethodCombo.getSelectedValue() != null
				&& !paymentMethodCombo.getSelectedValue().equals(
						UIUtils.getpaymentMethodCheckBy_CompanyType(messages
								.check()))) {
			checkNoText.setEnabled(false);
		} else {
			checkNoText.setEnabled(!isInViewMode());
		}

		paymentMethodCombo.setEnabled(!isInViewMode());
		dueDate.setEnabled(!isInViewMode());
		grid.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNoText.setValue(messages.toBePrinted());
		}
		super.onEdit();
		initListGrid();
		grid.setTranactionId(transaction.id);
		getTransactionPayBills(this.getVendor());
		memoTextAreaItem.setDisabled(isInViewMode());
		// transaction = new ClientPayBill();
		transaction.setAmountIncludeTDS(true);
		data = transaction;
		tdsCombo.setEnabled(!isInViewMode());
		textForm.clear();
		addTextFormFields();
		grid.initCreditsAndPayments(this.getVendor());
		grid.setTds(getCompany().getTAXItem(transaction.getTdsTaxItem()));
		updateTDSFields();
	}

	@Override
	public void print() {
		updateTransaction();
		UIUtils.downloadAttachment(transaction.getID(),
				ClientTransaction.TYPE_PAY_BILL);
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	protected Double getTransactionTotal() {
		return this.amountLabelForeign.getAmount();
	}

	@Override
	protected String getViewTitle() {
		return messages.payBills();
	}

	// Update methods

	@Override
	public ClientVendor getVendor() {
		return vendor;
	}

	@Override
	public void setVendor(ClientVendor vendor) {
		this.vendor = vendor;
	}

	@Override
	public VendorCombo createVendorComboItem(String title) {

		VendorCombo vendorCombo = new VendorCombo(title != null ? title
				: Global.get().Vendor());
		// vendorCombo.setHelpInformation(true);
		vendorCombo.setRequired(true);
		vendorCombo.setEnabled(!isInViewMode());
		// vendorCombo.setShowDisabled(false);
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						vendorSelected(selectItem);

					}

				});

		// vendorCombo.setShowDisabled(false);
		return vendorCombo;

	}

	public void deleteTotalPayment(ClientTransactionPayBill rec) {
		ClientTransaction transactionObject = getTransactionObject();
		// paybillView.transactionTotal -= rec.getPayment();
		transactionObject.setTotal(transactionObject.getTotal()
				- rec.getPayment());
		this.totalCashDiscount -= rec.getCashDiscount();

		/* updating payment column's footer */
		// this.updateFooterValues(DataUtils
		// .getAmountAsString(paybillView.transactionTotal), canEdit ? 7
		// : 6);
		// if (!DecimalUtil.isLessThan(paybillView.totalCashDiscount, 0))
		// this.updateFooterValues(DataUtils
		// .getAmountAsString(paybillView.totalCashDiscount),
		// canEdit ? 5 : 3);
	}

	@Override
	public PayFromAccountsCombo createPayFromCombo(String title) {

		PayFromAccountsCombo payFromCombo = new PayFromAccountsCombo(title);
		// payFromCombo.setHelpInformation(true);
		payFromCombo.setRequired(true);
		payFromCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountSelected(selectItem);
					}

				});
		payFromCombo.setEnabled(!isInViewMode());
		// payFromCombo.setShowDisabled(false);
		// formItems.add(payFromCombo);
		return payFromCombo;
	}

	protected void initVendors() {

		if (vendorCombo == null)
			return;
		List<ClientVendor> result = getCompany().getActiveVendors();
		vendors = result;

		vendorCombo.initCombo(result);
		vendorCombo.setEnabled(!isInViewMode());

		if (getVendor() != null)
			vendorCombo.setComboItem(getVendor());
	}

	@Override
	protected void initPayFromAccounts() {
		// getPayFromAccounts();
		// payFromCombo.initCombo(payFromAccounts);
		// payFromCombo.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.payFromCombo));
		payFromCombo.setAccounts();
		payFromCombo.setEnabled(!isInViewMode());
		payFromAccount = payFromCombo.getSelectedValue();
		if (payFromAccount != null)
			payFromCombo.setComboItem(payFromAccount);
	}

	@Override
	protected void refreshTransactionGrid() {

	}

	private void setTabindexes() {
		vendorCombo.setTabIndex(1);
		payFromCombo.setTabIndex(2);
		vendorPaymentMethodCombo.setTabIndex(3);
		dueDate.setTabIndex(4);
		date.setTabIndex(5);
		transactionNumber.setTabIndex(6);
		endBalText.setTabIndex(8);
		memoTextAreaItem.setTabIndex(9);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(10);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(11);
		cancelButton.setTabIndex(12);

	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		this.grid.updateAmountsFromGUI();

	}

	public void modifyForeignCurrencyTotalWidget() {

		if (currencyWidget.isShowFactorField()) {
			amountLabelForeign.hide();
		} else {
			amountLabelForeign.show();
			amountLabelForeign.setTitle(messages.currencyTotal(currencyWidget
					.getSelectedCurrency().getFormalName()));
			amountLabelForeign
					.setCurrency(currencyWidget.getSelectedCurrency());
		}
	}

	private boolean isTDSEnable() {

		if (transaction != null && transaction.getVendor() != 0) {
			ClientVendor vendor2 = getCompany().getVendor(
					transaction.getVendor());
			return (getPreferences().isTDSEnabled() && vendor2
					.isTdsApplicable());
		} else {
			return getPreferences().isTDSEnabled();
		}
	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {

	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return false;
	}

	@Override
	public boolean canPrint() {
		EditMode mode = getMode();
		if (mode == EditMode.CREATE || mode == EditMode.EDIT
				|| data.getSaveStatus() == ClientTransaction.STATUS_DRAFT) {
			return false;
		} else {
			return true;
		}
	}

	@Override
	public boolean canExportToCsv() {
		// TODO Auto-generated method stub
		return false;
	}

}
