package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientStatementRecord;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ClientTransferFund;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.banking.AbstractBankTransactionView;
import com.nitya.accounter.web.client.ui.combo.AccountCombo;
import com.nitya.accounter.web.client.ui.combo.CashBackAccountsCombo;
import com.nitya.accounter.web.client.ui.combo.CustomCombo;
import com.nitya.accounter.web.client.ui.combo.CustomerCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.MakeDepositAccountCombo;
import com.nitya.accounter.web.client.ui.combo.OtherAccountsCombo;
import com.nitya.accounter.web.client.ui.combo.TAXCodeCombo;
import com.nitya.accounter.web.client.ui.combo.VendorCombo;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.forms.DateItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class TransferFundView extends
		AbstractBankTransactionView<ClientTransferFund> {
	DateItem date;

	DynamicForm depoForm;
	TextAreaItem memoText;

	AmountField cashBackAmountText;
	TAXCodeCombo taxCodeSelect;
	TextItem cashBackMemoText;
	DynamicForm memoForm, totForm;
	DynamicForm form1, form2;
	private AmountField amtText;

	protected boolean isClose;

	private int TYPE_FINANCIAL_ACCOUNT = 1;
	private int TYPE_VENDOR = 2;
	private int TYPE_CUSTOMER = 3;

	private MakeDepositAccountCombo depositInSelect, depositFromSelect;
	private CashBackAccountsCombo cashBackAccountSelect;
	private OtherAccountsCombo financeAccountSelect;
	private CustomerCombo customerSelect;
	private CustomCombo<ClientVendor> vendorSelect;

	private ClientAccount selectedDepositInAccount, selectedDepositFromAccount;
	private ClientAccount selectedCashBackAccount;

	private String selectedItemId;

	// private ClientAccount selectedAccount;

	protected ClientCustomer customer;

	protected ClientVendor vendor;

	protected Long nextTransactionNumber;

	protected Long TransactionNumber;

	private Double calculatedTotal;

	private boolean isListEmpty;

	protected boolean isSelected;
	// private MakeDeposit transactionObject;

	private List<ClientAccount> listOfAccounts;

	private StyledPanel bot1Panel;

	private ArrayList<DynamicForm> listforms;

	// private StyledPanel botRightPanel;

	private String transactionNo;

	// For Reconciliation
	private ClientAccount reconcilationAccount;

	private double reconcilAmount;

	private ClientStatementRecord statementRecord;

	public TransferFundView() {
		super(ClientTransaction.TYPE_TRANSFER_FUND);
		this.getElement().setId("MakeDepositView");
		calculatedTotal = 0D;
	}

	// For Reconciliation
	public TransferFundView(ClientAccount reconcilationAccount,
			double reconcilAmount, ClientStatementRecord statementRecord) {
		super(ClientTransaction.TYPE_MAKE_DEPOSIT);
		this.getElement().setId("MakeDepositView");
		calculatedTotal = reconcilAmount;
		this.reconcilationAccount = reconcilationAccount;
		this.reconcilAmount = reconcilAmount;
		this.statementRecord = statementRecord;
	}

	private void setTransactionNumberToMakeDepositObject() {
		AccounterAsyncCallback<String> getTransactionNumberCallback = new AccounterAsyncCallback<String>() {

			@Override
			public void onException(AccounterException caught) {

			}

			@Override
			public void onResultSuccess(String result) {

				if (result != null)
					setTransactionNumber(result);
				else
					onFailure(new Exception());

			}

		};

		Accounter.createHomeService().getNextTransactionNumber(
				ClientTransaction.TYPE_TRANSFER_FUND,
				getTransactionNumberCallback);
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNo = transactionNumber;
	}

	protected void validateCashBackAmount() {

		String amount = cashBackAmountText.getAmount().toString();
		if (amount.substring(0, 1)
				.equals("" + UIUtils.getCurrencySymbol() + "")) {
			amount = amount.substring(1);
		}
		try {
			Double cashBackAmount = Double.parseDouble(amount);
			if (DecimalUtil.isEquals(cashBackAmount, 0.00)) {

				if (DecimalUtil.isLessThan(cashBackAmount, 0.00)
						|| DecimalUtil.isGreaterThan(cashBackAmount,
								calculatedTotal)) {
					Accounter.showError(messages.cashBackAmountErrorMsg());
					cashBackAmount = 0.00;
					// cashBackAmountText.setValue("$0.00");
					cashBackAmountText.setAmount(0.00);
				} /*
				 * else if (cashBackAmount > 1000000000000.00) { SC.say(
				 * "Cash-back Amount should not exceed +UIUtils.getCurrencySymbol() +"
				 * 1,000,000,000,000.00" ); cashBackAmount = 0.00;
				 * cashBackAmountText.setValue(""+UIUtils.getCurrencySymbol()
				 * +"0.00"); }
				 */

			}

			Double diff = calculatedTotal.doubleValue()
					- cashBackAmount.doubleValue();
			// cashBackAmountText.setValue(UIUtils.format(cashBackAmount));
			cashBackAmountText.setAmount(cashBackAmount);
			// totText.setValue(UIUtils.format(diff));
			// totText.setAmount(getAmountInTransactionCurrency(diff));
		} catch (Exception e) {
			Accounter.showError(messages.enterValidAmount());
			// cashBackAmountText.setValue("$0.00");
			cashBackAmountText.setAmount(0.00);

		}

	}

	// public void initVendorCombo() {
	// List<ClientVendor> result = getCompany().getActiveVendors();
	// if (result != null) {
	// allVendors = result;
	// vendorSelect.initCombo(result);
	//
	// }
	//
	// }
	//
	// private void initCustomerCombo() {
	// List<ClientCustomer> result = getCompany().getActiveCustomers();
	// if (result != null) {
	// allCustomers = result;
	// customerSelect.initCombo(result);
	// }
	// }

	public void getDepositInAccounts() {
		listOfAccounts = depositInSelect.getAccounts();
		depositInSelect.initCombo(listOfAccounts);
		depositFromSelect.initCombo(listOfAccounts);
	}

	// protected boolean validateForm() {
	// boolean flag = true;
	// // if (UIUtils.unFormat(UIUtils.toStr(cashBackAmountText.getValue())) !=
	// // 0.00
	// // && selectedCashBackAccount == null) {
	// if (!DecimalUtil.isEquals(cashBackAmountText.getAmount(), 0.00)
	// && selectedCashBackAccount == null) {
	//
	// flag = false;
	// Accounter.showError(messages
	// .cashBackAccountShouldBeSelected());
	//
	// }
	//
	// return checkTotalAmount() && checkLastRecord() && flag
	// && depoForm.validate(false);
	// }

	private boolean checkTotalAmount() {

		// if (UIUtils.unFormat(UIUtils.toStr(cashBackAmountText.getValue())) ==
		// 0.00)
		if (!DecimalUtil.isEquals(cashBackAmountText.getAmount(), 0.00))
			return true;

		// if (UIUtils.unFormat(UIUtils.toStr(cashBackAmountText.getValue())) >
		// calculatedTotal) {
		if (DecimalUtil.isGreaterThan(cashBackAmountText.getAmount(),
				calculatedTotal)) {
			Accounter.showError(messages
					.cashBackAmountShouldnotBeGreaterthanDepositedAmount());
			return false;
		}
		return true;
	}

	@Override
	public ClientTransferFund saveView() {
		ClientTransferFund saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(transaction);

	}

	public String getSelectedItemId() {
		return selectedItemId;
	}

	public void setSelectedItemId(String selectedItemId) {
		this.selectedItemId = selectedItemId;
	}

	@Override
	public void init() {
		super.init();
		// createControls();
		// setSize("100%", "100%");
		// setOverflow(Overflow.AUTO);
	}

	@Override
	public void initData() {
		super.initData();
		getDepositInAccounts();
		depositInSelect.setComboItem(getCompany().getAccount(
				transaction.getDepositIn()));
		this.selectedDepositInAccount = getCompany().getAccount(
				transaction.getDepositIn());
		// for reconcilation
		if (reconcilationAccount != null) {
			depositFromSelect.setComboItem(reconcilationAccount);
			this.selectedDepositFromAccount = reconcilationAccount;
			depositFromSelect.setEnabled(false);
			this.amtText.setAmount(reconcilAmount);
			amtText.setEnabled(false);
			date.setValue(statementRecord.getStatementDate());
		} else {
			depositFromSelect.setComboItem(getCompany().getAccount(
					transaction.getDepositFrom()));
			this.selectedDepositFromAccount = getCompany().getAccount(
					transaction.getDepositFrom());
		}

		initFianancialAccounts();
		initCashBackAccounts();

		setTransactionNumberToMakeDepositObject();
		// addTracsactionMakeDepositsToGrid();
		// initVendorCombo();
		// initCustomerCombo();

	}

	private void initCashBackAccounts() {
		accountsList = new ArrayList<ClientAccount>();
		for (ClientAccount account : getCompany().getActiveAccounts()) {

			if (account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
					&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE) {
				accountsList.add(account);
			}

		}
		cashBackAccountSelect.initCombo(accountsList);

	}

	@Override
	protected void initMemoAndReference() {

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientTransferFund());
		} else {
			selectedDepositFromAccount = getCompany().getAccount(
					transaction.getDepositFrom());
			selectedDepositInAccount = getCompany().getAccount(
					transaction.getDepositIn());
			depositFromSelect.setComboItem(selectedDepositFromAccount);
			depositInSelect.setComboItem(selectedDepositInAccount);
			if (currencyWidget != null) {
				setCurrency(transaction.getCurrency() != 0 ? getCurrency(transaction
						.getCurrency()) : getCompany().getPrimaryCurrency());
				checkForCurrencyType();
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setEnabled(!isInViewMode());
			}

			date.setValue(transaction.getDate());
			memoText.setValue(transaction.getMemo());
			transactionNumber.setValue(transaction.getNumber());
			this.transactionItems = transaction.getTransactionItems();
			cashBackAmountText.setValue(DataUtils
					.getAmountAsStringInPrimaryCurrency(transaction
							.getCashBackAmount()));
			cashBackMemoText.setValue(transaction.getCashBackMemo());
			cashBackAccountSelect.setValue(transaction.getCashBackAccount());
			// totText
			// .setValue(DataUtils
			// .getAmountAsString(getAmountInTransactionCurrency(transaction
			// .getTotal())));
			if (selectedDepositFromAccount != null) {
				if (transaction.getCurrency() == selectedDepositFromAccount
						.getCurrency()) {
					amtText.setAmount(transaction.getTotal());
				} else {
					amtText.setAmount(transaction.getTotal());
				}
				amtText.setCurrency(getCurrency(selectedDepositFromAccount
						.getCurrency()));
			}
			// gridView.setCanEdit(false);
			updateTotals();
		}
		// FIXME--need to implement this feature
		// gridView.setEnableMenu(false);

		// gridView.canDelete(true);
		// FIMXE--need to add this type
		// gridView.setEditEvent();
		if (isTrackClass()) {
			classListCombo.setComboItem(getCompany().getAccounterClass(
					transaction.getAccounterClass()));
		}

		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		// if (isMultiCurrencyEnabled()) {
		// modifyForeignCurrencyTotalWidget();
		// foreignCurrencyamountLabel
		// .setAmount(getAmountInTransactionCurrency(transaction
		// .getTotal()));
		// }
		super.initTransactionViewData();

	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();
		Label lab = new Label(messages.transferFund());
		// lab.removeStyleName("gwt-Label");
		lab.setStyleName("label-title");
		// lab.setHeight("50px");
		date = UIUtils.date(messages.date(), this);
		date.setEnabled(!isInViewMode());
		// set the transactionDate while creation
		setTransactionDate(date.getValue());

		date.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (date != null) {
					setTransactionDate(date);
				}
			}
		});
		locationCombo = createLocationCombo();
		// date.setWidth(100);
		transactionNumber = createTransactionNumberItem();
		DynamicForm dateForm = new DynamicForm("dateForm");
		dateForm.setStyleName("datenumber-panel");
		if (!isTemplate) {
			dateForm.add(date, transactionNumber);
		}

		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateForm);
		currencyWidget = createCurrencyFactorWidget();

		depositInSelect = new MakeDepositAccountCombo(messages.transferTo()) {
			@Override
			public void addItemThenfireEvent(ClientAccount obj) {
				super.addItemThenfireEvent(obj);
				List<ClientAccount> deposiInAccounts = new ArrayList<ClientAccount>();
				for (ClientAccount account : getCompany().getActiveAccounts()) {
					if (Arrays.asList(ClientAccount.SUBBASETYPE_CURRENT_ASSET,
							ClientAccount.SUBBASETYPE_CURRENT_LIABILITY,
							ClientAccount.SUBBASETYPE_EQUITY).contains(
							account.getSubBaseType())) {

						deposiInAccounts.add(account);

					}
				}

				if (depositFromSelect != null) {
					depositFromSelect.initCombo(deposiInAccounts);
				}
				if (depositInSelect != null) {
					depositInSelect.initCombo(deposiInAccounts);
				}
			}
		};
		depositInSelect.setRequired(true);
		depositInSelect.setEnabled(!isInViewMode());
		depositInSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedDepositInAccount = selectItem;
						checkForCurrencyType();

					}

				});

		depositFromSelect = new MakeDepositAccountCombo(messages.transferFrom()) {
			@Override
			public void addItemThenfireEvent(ClientAccount obj) {
				super.addItemThenfireEvent(obj);
				List<ClientAccount> deposiInAccounts = new ArrayList<ClientAccount>();
				for (ClientAccount account : getCompany().getActiveAccounts()) {
					if (Arrays.asList(ClientAccount.SUBBASETYPE_CURRENT_ASSET,
							ClientAccount.SUBBASETYPE_CURRENT_LIABILITY,
							ClientAccount.SUBBASETYPE_EQUITY).contains(
							account.getSubBaseType())) {

						deposiInAccounts.add(account);

					}
				}

				if (depositFromSelect != null) {
					depositFromSelect.initCombo(deposiInAccounts);
				}
				if (depositInSelect != null) {
					depositInSelect.initCombo(deposiInAccounts);
				}
			}
		};
		depositFromSelect.setRequired(true);
		depositFromSelect.setEnabled(!isInViewMode());
		depositFromSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedDepositFromAccount = selectItem;
						checkForCurrencyType();
					}

				});

		vendorSelect = new VendorCombo("");

		financeAccountSelect = new OtherAccountsCombo("");

		customerSelect = new CustomerCombo("");

		vendorSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectedVendor = selectItem;

					}

				});
		financeAccountSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
					}
				});

		customerSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

					@Override
					public void selectedComboBoxItem(ClientCustomer selectItem) {
						// selectedCustomer = selectItem;

					}
				});
		amtText = new AmountField(messages.amount(), this, getBaseCurrency(),
				"amtText");
		amtText.setEnabled(!isInViewMode());
		amtText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				updateTotals();
			}
		});
		memoText = createMemoTextAreaItem();
		memoText.setEnabled(!isInViewMode());

		memoForm = new DynamicForm("memoForm");
		memoForm.add(memoText);

		depoForm = new DynamicForm("depoForm");
		depoForm.add(depositFromSelect, depositInSelect, amtText);
		classListCombo = createAccounterClassListCombo();
		if (getPreferences().isClassTrackingEnabled()) {
			depoForm.add(classListCombo);
		}

		// Label lab1 = new Label(FinanceApplication.constants()
		// .paymentsReceived());

		cashBackAccountSelect = new CashBackAccountsCombo(
				messages.cashBackAccount());
		cashBackAccountSelect.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.CASH_BACK_ACCOUNTS_COMBO));
		// cashBackAccountSelect.setRequired(true);
		cashBackAccountSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedCashBackAccount = selectItem;

					}

				});

		cashBackMemoText = new TextItem(messages.cashBackMemo(),
				"cashBackMemoText");
		cashBackMemoText.setWidth(100);

		form1 = new DynamicForm("form1");
		form1.add(cashBackMemoText, cashBackAccountSelect);
		// form1.getCellFormatter().setWidth(0, 0, "180px");
		form1.setWidth("70%");

		cashBackAmountText = new AmountField(messages.cashBackAmount(), this,
				getBaseCurrency(), "cashBackAmountText");
		cashBackAmountText.setDefaultValue("" + UIUtils.getCurrencySymbol()
				+ "0.00");
		cashBackAmountText.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				if (event != null)
					validateCashBackAmount();
			}
		});

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getBaseCurrency());
		foreignCurrencyamountLabel = createTransactionTotalNonEditableLabel(getBaseCurrency());

		// transactionTotalBaseCurrencyText.setWidth("100px");
		transactionTotalBaseCurrencyText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + "0.00");
		transactionTotalBaseCurrencyText.setEnabled(!true);

		form2 = new DynamicForm("form2");

		form2.add(transactionTotalBaseCurrencyText);
		if (isMultiCurrencyEnabled()) {
			form2.add(foreignCurrencyamountLabel);
		}

		form2.addStyleName("textbold");
		// form2.setWidth("50%");

		StyledPanel topHLay = new StyledPanel("topHLay");
		/* topHLay.setWidth("100%"); */
		topHLay.addStyleName("fields-panel");
		topHLay.add(depoForm);

		DynamicForm dynamicForm = new DynamicForm("dynamicForm");

		locationCombo.addStyleName("locationCombo");

		if (locationTrackingEnabled) {
			dynamicForm.add(locationCombo);
		}

		StyledPanel currencyPanel = new StyledPanel("currencyPanel");

		currencyPanel.add(dynamicForm);
		currencyPanel.add(currencyWidget);
		topHLay.add(currencyPanel);

		StyledPanel panel = new StyledPanel("panel");
		// panel.add(addButton);

		StyledPanel botHLay = new StyledPanel("botHLay");
		botHLay.add(memoForm);
		botHLay.add(form2);

		StyledPanel vPanel = new StyledPanel("vPanel");
		vPanel.add(panel);
		vPanel.add(botHLay);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab);
		mainVLay.add(voidedPanel);
		mainVLay.add(datepanel);
		mainVLay.add(topHLay);
		// mainVLay.add(lab1);
		// mainVLay.add(addButton);

		// mainVLay.add(gridView);

		mainVLay.add(vPanel);

		// if (UIUtils.isMSIEBrowser()) {
		// resetFormView();
		// }

		this.add(mainVLay);

		// setSize("700", "600");

		/* Adding dynamic forms in list */
		listforms.add(depoForm);
		listforms.add(memoForm);
		// listforms.add(totForm);
		// listforms.add(form1);
		listforms.add(form2);
		// settabIndexes();

		if (isMultiCurrencyEnabled()) {
			modifyForeignCurrencyTotalWidget();
		}

	}

	protected void updateTotals() {
		Double amount = amtText.getAmount();
		if (amtText.getCurrency().getID() != getCompany().getPrimaryCurrency()
				.getID()) {
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(amount));
			if (isMultiCurrencyEnabled()) {
				foreignCurrencyamountLabel.setAmount(amount);
			}
		} else {
			transactionTotalBaseCurrencyText.setAmount(amount);
			if (isMultiCurrencyEnabled()) {
				foreignCurrencyamountLabel
						.setAmount(calculateTransactionCurrencyAmount(amount));
			}
		}
	}

	private double calculateTransactionCurrencyAmount(Double amount) {
		if (currency != null && amount != null) {
			if (currencyFactor < 0.0) {
				currencyFactor = 1.0;
			}
			return amount / currencyFactor;
		} else {
			return amount;
		}
	}

	private void initFianancialAccounts() {
		financeAccountSelect.initCombo(getCompany().getActiveAccounts());
	}

	@Override
	public void setData(ClientTransferFund data) {

		super.setData(data);
		if (isInViewMode() && (!transaction.isTransferFund()))
			try {
				throw new Exception(messages.unabletoLoadTheRequiredDeposit());
			} catch (Exception e) {
				e.printStackTrace();
			}

	}

	@Override
	public void updateNonEditableItems() {
		// totText.setAmount(getAmountInTransactionCurrency(gridView.getTotal()
		// - getAmountInBaseCurrency(cashBackAmountText.getAmount())));
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		// Validations
		// 1. if(isBlackTransaction(gridView))
		// ERROR
		// else
		// gridView validation
		// 2. if(!isValidTransactionDate(transactionDate)) ERROR
		// 3. if(isInPreventPostingBeforeDate(transactionDate)) ERROR
		// 4. depoForm validation
		// 5. if(!validMakeDepositCombo(selectedDepositInAccount, gridAcconts))
		// ERROR

		result.add(depoForm.validate());

		// if ((gridView == null)
		// || (gridView != null && gridView.getRecords().isEmpty())) {
		// result.addError(gridView, messages.blankTransaction());
		// } else {
		// ClientAccount selectedValue = depositInSelect.getSelectedValue();
		// long depositIn = selectedValue != null ? selectedValue.getID() : 0;
		// if(selectedValue1==null )
		// result.addError(selectedValue, "please select deposit in account");

		// result.add(gridView.validateGrid(depositIn));
		// }

		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDateItem,
		// messages.invalidateTransactionDate());
		// }

		if (this.date != null && this.date.getDate().getDate() == 0) {
			result.addError(date,
					messages.pleaseEnter(messages.transactionDate()));
			return result;
		}

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDateItem, messages.invalidateDate());
		}

		// result.add(depoForm.validate());

		if (!AccounterValidator.isPositiveAmount(amtText.getAmount())) {
			amtText.textBox.addStyleName("highlightedFormItem");
			result.addError(amtText,
					messages.valueCannotBe0orlessthan0(messages.amount()));
		}
		// else if (!AccounterValidator.isValidMakeDeposit_CashBackAmount(
		// cashBackAmountText.getAmount().doubleValue(),
		// totText.getAmount())) {
		// result.addError(cashBackAmountText, messages
		// .makeDepositCashBackAmount());
		// }

		// if (!AccounterValidator.validate_MakeDeposit_accountcombo(
		// selectedDepositInAccount, gridView)) {
		// result.addError(gridView, messages
		// .makedepositAccountValidation());
		// }
		return result;

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
		this.depositInSelect.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);

	}

	@Override
	public void onEdit() {
		// if (transactionObject.canEdit) {
		// Accounter.showWarning(AccounterWarningType.MAKEDEPOSIT_EDITING,
		// AccounterType.WARNING, new ErrorDialogHandler() {
		//
		// @Override
		// public boolean onYesClick()
		// throws InvalidEntryException {
		// voidTransaction();
		// return true;
		// }
		//
		// private void voidTransaction() {
		// AccounterAsyncCallback<Boolean> callback = new
		// AccounterAsyncCallback<Boolean>() {
		//
		// @Override
		// public void onException(AccounterException caught) {
		// Accounter
		// .showError("Failed to void Make Deposit");
		//
		// }
		//
		// @Override
		// public void onSuccess(Boolean result) {
		// if (result) {
		// enableFormItems();
		// } else
		//
		// onFailure(new Exception());
		// }
		//
		// };
		// makeDepositEdited = (ClientMakeDeposit) transactionObject;
		// if (makeDepositEdited != null) {
		// AccounterCoreType type = UIUtils
		// .getAccounterCoreType(makeDepositEdited
		// .getType());
		// rpcDoSerivce.voidTransaction(type,
		// makeDepositEdited.id, callback);
		// }
		//
		// }
		//
		// @Override
		// public boolean onNoClick() throws InvalidEntryException {
		//
		// return true;
		// }
		//
		// @Override
		// public boolean onCancelClick()
		// throws InvalidEntryException {
		//
		// return true;
		// }
		// });
		// }
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = ((AccounterException) caught).getErrorCode();
				Accounter.showError(AccounterExceptions
						.getErrorString(errorCode));

			}

			@Override
			public void onResultSuccess(Boolean result) {
				if (result)
					enableFormItems();
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);

	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		date.setEnabled(!isInViewMode());
		depositInSelect.setEnabled(!isInViewMode());
		depositFromSelect.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		cashBackMemoText.setEnabled(!isInViewMode());
		cashBackAmountText.setEnabled(!isInViewMode());
		cashBackAccountSelect.setEnabled(!isInViewMode());
		memoText.setEnabled(!isInViewMode());
		amtText.setEnabled(!isInViewMode());

		// For deleting the transctionItems after we edit
		// transactionObject = null;
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());

		if (isMultiCurrencyEnabled()) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		if (isTrackClass())
			classListCombo.setEnabled(!isInViewMode());
	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		// NOTHING TO DO.
	}

	public void resetFormView() {

		// form1.getCellFormatter().setWidth(0, 1, "200px");
		// form1.getCellFormatter().setWidth(0, 1, "200px");
		// form2.getCellFormatter().setWidth(0, 1, "200px");
		// form2.getCellFormatter().setWidth(0, 1, "200px");
	}

	@Override
	protected String getViewTitle() {
		return messages.transferFund();
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting date
		if (date != null) {
			transaction.setDate(date.getValue().getDate());
		}
		// Setting Deposit in
		if (depositInSelect.getSelectedValue() != null)
			transaction.setDepositIn(selectedDepositInAccount.getID());

		if (depositFromSelect.getSelectedValue() != null)
			transaction.setDepositFrom(selectedDepositFromAccount.getID());
		// Setting Memo
		if (memoText.getValue() != null)
			transaction.setMemo(UIUtils.toStr(memoText.getValue()));

		if (transactionNumber.getValue() != null)
			transaction.setNumber(transactionNumber.getValue());

		transaction.setTotal(amtText.getAmount());

		// Setting Cash back account
		transaction
				.setCashBackAccount(selectedCashBackAccount != null ? selectedCashBackAccount
						.getID() : 0);
		if (cashBackMemoText.getValue() != null)
			transaction.setCashBackMemo(cashBackMemoText.getValue().toString());

		// Setting Cash back amount
		transaction.setCashBackAmount(cashBackAmountText.getAmount());
		// Setting Total amount
		if (isTrackClass() && classListCombo.getSelectedValue() != null) {
			transaction.setAccounterClass(classListCombo.getSelectedValue()
					.getID());
		}
		// Setting Total
		transaction.setTotal(amtText.getAmount());

		// Setting Transaction type
		transaction.setType(ClientTransaction.TYPE_TRANSFER_FUND);

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
		// For Reconcilation
		if (statementRecord != null) {
			statementRecord.setMatched(true);
			transaction.setStatementRecord(statementRecord);
		}
		super.saveAndUpdateView();

	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
		// TODO Auto-generated method stub

	}

	private void settabIndexes() {
		depositInSelect.setTabIndex(1);
		date.setTabIndex(2);
		transactionNumber.setTabIndex(3);
		memoText.setTabIndex(4);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(6);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(7);
		cancelButton.setTabIndex(8);

	}

	private void checkForCurrencyType() {
		ClientAccount depositIn = depositInSelect.getSelectedValue();
		ClientAccount depositFrom = depositFromSelect.getSelectedValue();
		if (depositIn != null && depositFrom != null) {

			long toCurrencyID = depositIn.getCurrency();
			long fromCurrencyID = depositFrom.getCurrency();

			ClientCurrency toCurrency = getCompany().getCurrency(toCurrencyID);
			ClientCurrency fromCurrency = getCompany().getCurrency(
					fromCurrencyID);

			if (depositIn.getID() == depositFrom.getID()) {
				Accounter.showError(messages
						.dipositAccountAndTransferAccountShouldBeDiff());
				depositInSelect.setComboItem(null);
			}
			if (toCurrency == getBaseCurrency()
					|| fromCurrency == getBaseCurrency()
					|| (toCurrencyID == fromCurrencyID)) {
				if (toCurrencyID != fromCurrencyID) {
					if (toCurrencyID != getBaseCurrency().getID()) {
						currencyWidget.setSelectedCurrency(toCurrency);
						setCurrency(toCurrency);
					} else {
						currencyWidget.setSelectedCurrency(fromCurrency);
						setCurrency(fromCurrency);
					}
				} else {
					currencyWidget.setSelectedCurrency(toCurrency);
					setCurrency(toCurrency);
				}
				amtText.setCurrency(fromCurrency);
			} else {
				Accounter.showError(messages
						.oneOfTheAccountCurrencyShouldBePrimaryCurrency());
				depositInSelect.setComboItem(null);
			}
			updateTotals();
		} else if (depositFrom != null) {
			ClientCurrency fromCurrency = getCompany().getCurrency(
					depositFrom.getCurrency());
			currencyWidget.setSelectedCurrency(fromCurrency);
			setCurrency(fromCurrency);
			amtText.setCurrency(fromCurrency);
		} else if (depositIn != null) {
			ClientCurrency toCurrency = getCompany().getCurrency(
					depositIn.getCurrency());
			if (toCurrency.getID() != getBaseCurrency().getID()) {
				currencyWidget.setSelectedCurrency(toCurrency);
				setCurrency(toCurrency);
				amtText.setCurrency(toCurrency);
			}
		}
		modifyForeignCurrencyTotalWidget();
	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		updateTotals();
	}

	public void modifyForeignCurrencyTotalWidget() {
		if (currencyWidget.isShowFactorField()) {
			foreignCurrencyamountLabel.hide();
		} else {
			foreignCurrencyamountLabel.show();
			foreignCurrencyamountLabel.setTitle(messages
					.currencyTotal(currencyWidget.getSelectedCurrency()
							.getFormalName()));
			foreignCurrencyamountLabel.setCurrency(currencyWidget
					.getSelectedCurrency());
		}
	}

	@Override
	protected ValidationResult validateBaseRequirement() {
		updateTransaction();
		ValidationResult result = new ValidationResult();
		if ((depositFromSelect.getSelectedValue() == null || depositInSelect
				.getSelectedValue() == null)
				&& !AccounterValidator.isPositiveAmount(amtText.getAmount())) {
			result.addError(this,
					messages.youCannotSaveAblankRecurringTemplate());
		}
		return result;
	}

	@Override
	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {
		if (clientAccounterClass != null) {
			classListCombo.setComboItem(clientAccounterClass);
		}
	}

	@Override
	protected boolean needTransactionItems() {
		return false;
	}
}
