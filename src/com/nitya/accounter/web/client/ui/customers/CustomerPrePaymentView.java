package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientCustomerPrePayment;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPriceLevel;
import com.nitya.accounter.web.client.core.ClientSalesPerson;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.JNSI;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.AddressCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.InvalidEntryException;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;

public class CustomerPrePaymentView extends
		AbstractCustomerTransactionView<ClientCustomerPrePayment> implements
		IPrintableView {

	// private CheckboxItem printCheck;
	private AmountField amountText, bankBalText, customerBalText;
	protected double enteredBalance;
	private DynamicForm payForm;
	Double toBeSetEndingBalance;
	Double toBeSetCustomerBalance;
	protected boolean isClose;
	protected String paymentMethod = UIUtils
			.getpaymentMethodCheckBy_CompanyType(messages.check());

	private ArrayList<DynamicForm> listforms;
	protected String checkNumber = null;
	protected TextItem checkNo;
	boolean isChecked = false;

	public CustomerPrePaymentView() {
		super(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
		this.getElement().setId("customerprepaymentview");
	}

	@Override
	protected void initMemoAndReference() {
		ClientCustomerPrePayment customerPrePayment = transaction;
		if (customerPrePayment != null) {
			memoTextAreaItem.setDisabled(isInViewMode());
			setMemoTextAreaItem(customerPrePayment.getMemo());
			// setRefText(customerPrePayment.getReference());
		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		if (AccounterValidator
				.isInPreventPostingBeforeDate(this.transactionDate)) {
			result.addError(transactionDateItem, messages.invalidateDate());
		}

		result.add(payForm.validate());

		if (!AccounterValidator.isPositiveAmount(amountText.getAmount())) {
			amountText.textBox.addStyleName("highlightedFormItem");
			result.addError(amountText,
					messages.valueCannotBe0orlessthan0(messages.amount()));
		}
		ClientAccount bankAccount = depositInCombo.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());
			ClientCurrency customerCurrency = getCurrency(customer
					.getCurrency());
			if (bankCurrency != getBaseCurrency()
					&& bankCurrency != customerCurrency) {
				result.addError(depositInCombo,
						messages.selectProperBankAccount());
			}
		}
		return result;
	}

	public static CustomerPrePaymentView getInstance() {

		return new CustomerPrePaymentView();

	}

	public void resetElements() {
		this.setCustomer(null);
		this.addressListOfCustomer = null;
		this.depositInAccount = null;
		this.paymentMethod = UIUtils
				.getpaymentMethodCheckBy_CompanyType(messages.check());
		amountText.setAmount(0D);
		// endBalText.setAmount(getAmountInTransactionCurrency(0D));
		// customerBalText.setAmount(getAmountInTransactionCurrency(0D));
		memoTextAreaItem.setValue("");
	}

	@Override
	public ClientCustomerPrePayment saveView() {
		ClientCustomerPrePayment saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();

		transaction.setNumber(transactionNumber.getValue().toString());
		if (customer != null)

			transaction.setCustomer(getCustomer().getID());

		if (billingAddress != null)
			transaction.setAddress(billingAddress);
		if (depositInAccount != null)
			transaction.setDepositIn(depositInAccount.getID());
		if (!DecimalUtil.isEquals(enteredBalance, 0.00))
			transaction.setTotal(enteredBalance);
		this.paymentMethod = paymentMethodCombo.getSelectedValue();
		if (paymentMethod != null) {
			transaction.setPaymentMethod(paymentMethod);
			if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
				if (checkNo.getValue() != null
						&& !checkNo.getValue().equals("")) {
					String value = String.valueOf(checkNo.getValue());
					transaction.setCheckNumber(value);
				} else {
					transaction.setCheckNumber("");

				}
			} else {
				transaction.setCheckNumber("");
			}
		}
		// if (transaction.getID() != 0)
		//
		// printCheck.setValue(transaction.isToBePrinted());
		// else
		// printCheck.setValue(true);

		if (transactionDate != null)
			transaction.setDate(transactionDateItem.getEnteredDate().getDate());
		transaction.setMemo(getMemoTextAreaItem());
		if (isTrackClass() && classListCombo.getSelectedValue() != null) {
			transaction.setAccounterClass(classListCombo.getSelectedValue()
					.getID());
		}

		// if (toBeSetEndingBalance != null)
		// transaction.setEndingBalance(toBeSetEndingBalance);
		if (toBeSetCustomerBalance != null)
			transaction.setCustomerBalance(toBeSetCustomerBalance);

		transaction.setType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);

		if (isTrackJob()) {
			if (jobListCombo.getSelectedValue() != null)
				transaction.setJob(jobListCombo.getSelectedValue().getID());
		}
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCustomerPrePayment());
			initDepositInAccounts();
		} else {

			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setEnabled(!isInViewMode());
			}
			ClientCompany comapny = getCompany();

			ClientCustomer customer = comapny.getCustomer(transaction
					.getCustomer());
			customerSelected(comapny.getCustomer(transaction.getCustomer()));
			this.billingAddress = transaction.getAddress();
			if (billingAddress != null)
				billToaddressSelected(billingAddress);
			amountText.setEnabled(!isInViewMode());
			amountText.setAmount(transaction.getTotal());
			if (customer != null) {
				customerBalText.setAmount(customer.getBalance());
			}
			// bankBalText.setAmount(getAmountInTransactionCurrency(transaction.g));
			paymentMethodSelected(transaction.getPaymentMethod());
			this.depositInAccount = comapny.getAccount(transaction
					.getDepositIn());
			if (depositInAccount != null) {
				depositInCombo.setComboItem(depositInAccount);
				bankBalText.setAmount(depositInAccount
						.getTotalBalanceInAccountCurrency());
				bankBalText.setCurrency(getCompany().getCurrency(
						depositInAccount.getCurrency()));
			}
			if (isTrackClass()) {
				classListCombo.setComboItem(getCompany().getAccounterClass(
						transaction.getAccounterClass()));
			}
			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			checkNo.setValue(transaction.getCheckNumber());
			// if (transaction.getPaymentMethod().equals(constants.check())) {
			// printCheck.setDisabled(isInViewMode());
			// checkNo.setDisabled(isInViewMode());
			// } else {
			// printCheck.setDisabled(true);
			// checkNo.setDisabled(true);
			// }

			// if (transaction.getCheckNumber() != null) {
			// if (transaction.getCheckNumber().equals(
			// messages.toBePrinted())) {
			// checkNo.setValue(messages.toBePrinted());
			// printCheck.setValue(true);
			// } else {
			// checkNo.setValue(transaction.getCheckNumber());
			// printCheck.setValue(false);
			// }
			// }
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		if (isTrackJob()) {
			if (customer != null) {
				jobListCombo.setCustomer(customer);
			}
			jobSelected(Accounter.getCompany().getjob(transaction.getJob()));
		}
		initMemoAndReference();
		initCustomers();
		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	private void initCustomers() {
		List<ClientCustomer> result = getCompany().getActiveCustomers();
		customerCombo.initCombo(result);
		customerCombo.setEnabled(!isInViewMode());
	}

	@Override
	protected void accountSelected(ClientAccount account) {
		if (account == null)
			return;
		this.depositInAccount = account;
		depositInCombo.setValue(depositInAccount);
		bankBalText.setAmount(depositInAccount.getTotalBalance());
		// if (account != null && !(Boolean) printCheck.getValue()) {
		// setCheckNumber();
		// } else if (account == null)
		// checkNo.setValue("");
		adjustBalance(amountText.getAmount());
	}

	private void adjustBalance(double amount) {
		ClientCustomerPrePayment customerPrePayment = transaction;
		enteredBalance = amount;

		if (DecimalUtil.isLessThan(enteredBalance, 0)
				|| DecimalUtil.isGreaterThan(enteredBalance, 1000000000000.00)) {
			amountText.setAmount(0D);
			enteredBalance = 0D;
		}
		if (getCustomer() != null) {
			if (isInViewMode()
					&& getCustomer().getID() == (customerPrePayment
							.getCustomer())
					&& !DecimalUtil.isEquals(enteredBalance, 0)) {
				double cusBal = DecimalUtil.isLessThan(getCustomer()
						.getBalance(), 0) ? -1 * getCustomer().getBalance()
						: getCustomer().getBalance();
				toBeSetCustomerBalance = (cusBal - transaction.getTotal())
						+ enteredBalance;
			} else {
				toBeSetCustomerBalance = getCustomer().getBalance()
						- enteredBalance;
			}
			// customerBalText.setAmount(toBeSetCustomerBalance);

		}
		if (depositInAccount != null) {
			double balanceToBeUpdate;
			if (depositInAccount.getCurrency() == getPreferences()
					.getPrimaryCurrency().getID()) {
				balanceToBeUpdate = enteredBalance;
			} else {
				balanceToBeUpdate = enteredBalance;
			}

			if (depositInAccount.isIncrease()) {
				toBeSetEndingBalance = depositInAccount
						.getTotalBalanceInAccountCurrency() - balanceToBeUpdate;
			} else {
				toBeSetEndingBalance = depositInAccount
						.getTotalBalanceInAccountCurrency() + balanceToBeUpdate;
			}
			if (isInViewMode()
					&& depositInAccount.getID() == (customerPrePayment
							.getDepositIn())
					&& !DecimalUtil.isEquals(balanceToBeUpdate, 0)) {
				toBeSetEndingBalance = toBeSetEndingBalance
						- transaction.getTotal();
			}
			// endBalText.setAmount(toBeSetEndingBalance);

		}
	}

	private void setCheckNumber() {
		rpcUtilService.getNextCheckNumber(depositInAccount.getID(),
				new AccounterAsyncCallback<Long>() {

					@Override
					public void onException(AccounterException t) {
						checkNo.setValue(messages.toBePrinted());
						return;
					}

					@Override
					public void onResultSuccess(Long result) {
						if (result == null)
							onFailure(null);

						checkNumber = String.valueOf(result);
						checkNo.setValue(result.toString());
					}

				});

	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void createControls() {
		Label lab1 = new Label(
				messages.payeePrePayment(Global.get().Customer()));
		lab1.setStyleName("label-title");
		// lab1.setHeight("35px");
		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm("datenumber-panel");
		dateNoForm.add(transactionDateItem, transactionNumber);

		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateNoForm);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
		labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);
		// customer and address
		customerCombo = createCustomerComboItem(messages.payeeName(Global.get()
				.Customer()));

		billToCombo = createBillToComboItem(messages.address());
		billToCombo.setEnabled(false);

		// Ending and Vendor Balance
		bankBalText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency(), "bankBalText");
		bankBalText.setEnabled(false);

		customerBalText = new AmountField(messages.payeeBalance(Global.get()
				.Customer()), this, getBaseCurrency(), "customerBalText");
		customerBalText.setEnabled(false);

		DynamicForm balForm = new DynamicForm("balForm");
		if (locationTrackingEnabled)
			balForm.add(locationCombo);
		classListCombo = createAccounterClassListCombo();
		if (isTrackClass()) {
			balForm.add(classListCombo);
		}
		jobListCombo = createJobListCombo();
		if (isTrackJob()) {
			jobListCombo.setEnabled(false);
			balForm.add(jobListCombo);
		}
		balForm.add(bankBalText, customerBalText);
		// balForm.getCellFormatter().setWidth(0, 0, "205px");

		// payment
		depositInCombo = createDepositInComboItem(bankBalText);
		// depositInCombo.setPopupWidth("500px");

		amountText = new AmountField(messages.amount(), this,
				getBaseCurrency(), "amountText");
		amountText.setRequired(true);
		amountText.addBlurHandler(getBlurHandler());

		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setComboItem(UIUtils
				.getpaymentMethodCheckBy_CompanyType(messages.check()));
		// printCheck = new CheckboxItem(messages.toBePrinted());
		// printCheck.setValue(true);
		// printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {
		//
		// @Override
		// public void onValueChange(ValueChangeEvent<Boolean> event) {
		// isChecked = (Boolean) event.getValue();
		// if (isChecked) {
		// if (printCheck.getValue().toString()
		// .equalsIgnoreCase("true")) {
		// checkNo.setValue(messages.toBePrinted());
		// checkNo.setDisabled(true);
		// } else {
		// if (depositInAccount == null)
		// checkNo.setValue(messages
		// .toBePrinted());
		// else if (isInViewMode()) {
		// checkNo.setValue(((ClientCustomerPrePayment) transaction)
		// .getCheckNumber());
		// }
		// }
		// } else
		// // setCheckNumber();
		// checkNo.setValue("");
		// checkNo.setDisabled(false);
		//
		// }
		// });
		checkNo = createCheckNumberItm();
		// checkNo.setValue(messages.toBePrinted());
		// checkNo.setDisabled(true);
		checkNo.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNo.getValue().toString();
			}
		});
		checkNo.setEnabled(!isInViewMode());
		currencyWidget = createCurrencyFactorWidget();
		payForm = UIUtils.form(messages.payment());
		memoTextAreaItem = createMemoTextAreaItem();
		// refText = createRefereceText();
		// refText.setWidth(100);
		payForm.add(customerCombo, billToCombo, depositInCombo, amountText,
				paymentMethodCombo, checkNo, memoTextAreaItem);
		// memo and Reference
		ClientAccount selectedValue = depositInCombo.getSelectedValue();
		if (selectedValue != null) {
			bankBalText.setAmount(selectedValue
					.getTotalBalanceInAccountCurrency());
			bankBalText.setCurrency(getCompany().getCurrency(
					selectedValue.getCurrency()));
		}

		// payForm.getCellFormatter().setWidth(0, 0, "160px");

		StyledPanel leftPanel = new StyledPanel("leftPanel");
		leftPanel.add(payForm);
		// leftPanel.add(payForm);
		// leftPanel.add(memoForm);

		StyledPanel rightPanel = new StyledPanel("rightPanel");
		rightPanel.add(balForm);
		if (isMultiCurrencyEnabled()) {
			rightPanel.add(currencyWidget);
		}

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(voidedPanel);
		mainVLay.add(labeldateNoLayout);
		StyledPanel hLay = getTopLayOut();
		if (hLay != null) {
			hLay.add(leftPanel);
			hLay.add(rightPanel);
			mainVLay.add(hLay);
		} else {
			mainVLay.add(leftPanel);
			mainVLay.add(rightPanel);
		}

		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(balForm);
		listforms.add(payForm);
		// settabIndexes();
	}

	protected StyledPanel getTopLayOut() {
		StyledPanel hLay = new StyledPanel("hLayPanel");
		hLay.addStyleName("fields-panel");
		return hLay;
	}

	private AddressCombo createBillToComboItem(String address) {
		AddressCombo addressCombo = new AddressCombo(messages.address(), false);
		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					@Override
					public void selectedComboBoxItem(ClientAddress selectItem) {

						billToaddressSelected(selectItem);

					}

				});

		addressCombo.setEnabled(!isInViewMode());

		// formItems.add(addressCombo);

		return addressCombo;

	}

	private TextItem createCheckNumberItm() {
		TextItem checkNoTextItem = new TextItem(
				UIUtils.getpaymentMethodCheckBy_CompanyType(messages.checkNo()),
				"checkNoTextItem");
		return checkNoTextItem;
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		saveOrUpdate(transaction);
	}

	private String getCheckNoValue() {
		return checkNumber;
	}

	private BlurHandler getBlurHandler() {
		BlurHandler blurHandler = new BlurHandler() {

			Object value = null;

			@Override
			public void onBlur(BlurEvent event) {
				try {

					value = amountText.getValue();

					if (value == null)
						return;

					Double amount = DataUtils.getAmountStringAsDouble(JNSI
							.getCalcultedAmount(value.toString()));
					if (DecimalUtil.isLessThan(amount, 0)) {
						Accounter.showError(messages.noNegativeAmounts());
						amountText.setAmount(0.00D);

					}

					amountText
							.setAmount(DataUtils.isValidAmount(amount + "") ? amount
									: 0.0);

					adjustBalance(amountText.getAmount());

				} catch (Exception e) {
					if (e instanceof InvalidEntryException) {
						Accounter.showError(e.getMessage());
					}
					amountText.setAmount(0.0);
				}

			}
		};

		return blurHandler;
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		this.paymentMethod = paymentMethod;
		if (paymentMethod == null) {
			return;
		}

		if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
			checkNo.setEnabled(!isInViewMode());
			checkNo.setVisible(true);
		} else {
			// paymentMethodCombo.setComboItem(paymentMethod);
			checkNo.setEnabled(false);
			checkNo.setVisible(false);
		}

	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (customer == null)
			return;

		// Job Tracking
		if (isTrackJob()) {
			jobListCombo.setValue("");
			jobListCombo.setEnabled(!isInViewMode());
			jobListCombo.setCustomer(customer);
		}
		ClientCurrency clientCurrency = getCurrency(customer.getCurrency());
		amountText.setCurrency(clientCurrency);
		bankBalText.setCurrency(clientCurrency);
		customerBalText.setCurrency(clientCurrency);

		this.setCustomer(customer);
		if (customerCombo != null) {
			customerCombo.setComboItem(customer);
		}
		this.addressListOfCustomer = customer.getAddress();
		initBillToCombo();
		customerBalText.setAmount(customer.getBalance());
		adjustBalance(amountText.getAmount());
		currencyWidget.setSelectedCurrencyFactorInWidget(clientCurrency,
				transactionDateItem.getDate().getDate());

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
		this.customerCombo.setFocus();
	}

	@Override
	public void onEdit() {
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

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		customerCombo.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		// printCheck.setDisabled(isInViewMode());
		amountText.setEnabled(!isInViewMode());
		paymentMethodCombo.setEnabled(!isInViewMode());
		paymentMethodSelected(paymentMethodCombo.getSelectedValue());
		// if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
		// checkNo.setValue(messages.toBePrinted());
		// checkNo.setDisabled(true);
		// }
		// if (paymentMethodCombo.getSelectedValue().equalsIgnoreCase(
		// messages.cheque())
		// && printCheck.getValue().toString().equalsIgnoreCase("true")) {
		// checkNo.setValue(messages.toBePrinted());
		checkNo.setEnabled(!isInViewMode());
		// }
		memoTextAreaItem.setDisabled(false);
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (isTrackClass())
			classListCombo.setEnabled(!isInViewMode());
		if (isTrackJob()) {
			jobListCombo.setEnabled(!isInViewMode());
		}
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		super.onEdit();
	}

	@Override
	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null) {
			// updateNonEditableItems();
		}
	}

	@Override
	public void updateNonEditableItems() {
		if (bankBalText != null)
			this.bankBalText.setAmount(toBeSetEndingBalance);
		if (customerBalText != null)
			this.customerBalText.setAmount(toBeSetCustomerBalance);
	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		// TODO Auto-generated method stub

	}

	@Override
	public void print() {
		updateTransaction();
		UIUtils.downloadAttachment(transaction.getID(),
				ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
	}

	@Override
	public void printPreview() {
		// NOTHING TO DO.
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected void depositInAccountSelected(ClientAccount depositInAccount2) {
		super.depositInAccountSelected(depositInAccount2);
		adjustBalance(amountText.getAmount());
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		// TODO Auto-generated method stub

	}

	@Override
	protected String getViewTitle() {
		return messages.payeePayment(Global.get().Customer());
	}

	@Override
	protected void initTransactionsItems() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean isBlankTransactionGrid() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void refreshTransactionGrid() {
		// TODO Auto-generated method stub

	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		return new ArrayList<ClientTransactionItem>();
	}

	private void settabIndexes() {
		customerCombo.setTabIndex(1);
		billToCombo.setTabIndex(2);
		depositInCombo.setTabIndex(3);
		amountText.setTabIndex(4);
		paymentMethodCombo.setTabIndex(5);
		// printCheck.setTabIndex(6);
		checkNo.setTabIndex(7);
		memoTextAreaItem.setTabIndex(8);
		transactionDateItem.setTabIndex(9);
		transactionNumber.setTabIndex(10);
		bankBalText.setTabIndex(11);
		customerBalText.setTabIndex(12);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(13);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(14);
		cancelButton.setTabIndex(15);
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
	public void updateAmountsFromGUI() {
		adjustBalance(amountText.getAmount());
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
		classListCombo.setComboItem(accounterClass);
	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return true;
	}

	@Override
	protected boolean needTransactionItems() {
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
		return false;
	}
}
