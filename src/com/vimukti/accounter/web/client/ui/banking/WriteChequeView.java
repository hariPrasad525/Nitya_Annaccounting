package com.vimukti.accounter.web.client.ui.banking;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientPayee;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientTAXAgency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientWriteCheck;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.GwtDisclosurePanel;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.PayeeCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class WriteChequeView extends
		AbstractBankTransactionView<ClientWriteCheck> implements IPrintableView {

	private PayFromAccountsCombo bankAccSelect;

	private PayeeCombo paytoSelect;
	private TextItem inFavourOf;
	private AmountField balText, amtText;
	private DynamicForm bankAccForm;

	private StyledPanel labelLayout;
	public AmountLabel netAmount;

	TaxItemsForm vatTotalNonEditableText;
	private TextAreaItem addrArea;
	private DynamicForm payForm;
	private TextItem checkNumberField;

	protected ClientAccount selectBankAcc;
	protected ClientAccount selectPayToMethod;
	protected boolean isClose;

	private CheckboxItem toprintCheck;
	protected ClientCustomer selectedCustomer;
	protected ClientVendor selectedVendor;

	protected ClientSalesPerson selectedSalesPerson;
	protected ClientAccount selectedBalance;

	protected ClientPayee payee;
	private StyledPanel mainVLay, nHPanel;

	private LinkedHashMap<String, ClientAddress> billToAddress;

	protected ClientTAXAgency selectedTaxAgency;

	private DateField date;

	// private DynamicForm amtForm;

	private final ClientCompany company;
	private List<ClientAccount> payFromAccounts;

	private DynamicForm numForm;

	private ArrayList<DynamicForm> listforms;

	private StyledPanel vatPanel;
	private StyledPanel amountPanel;

	private VendorAccountTransactionTable transactionVendorAccountTable;
	private AddNewButton accountTableButton;
	private GwtDisclosurePanel vendorAccountsDisclosurePanel;

	private TAXCodeCombo taxCodeSelect;

	private boolean isAmountChange;
	private AmountLabel unassignedAmount;
	private double previousValue = 0.00D;

	private StyledPanel unassignedAmountPanel;

	private StyledPanel vendorAccountFlowPanel;

	// private CurrencyFactorWidget currencyWidget;

	public WriteChequeView() {
		super(ClientTransaction.TYPE_WRITE_CHECK);
		this.company = getCompany();
		this.getElement().setId("writechequeview");
	}

	protected void updateAddressAndGrid() {
		// Set<Address> add = null;
		long currency = getBaseCurrency().getID();

		if (payee instanceof ClientCustomer) {
			selectedCustomer = (ClientCustomer) payee;
			addressList = selectedCustomer.getAddress();
			currency = selectedCustomer.getCurrency();
		} else if (payee instanceof ClientVendor) {

			selectedVendor = (ClientVendor) payee;
			addressList = selectedVendor.getAddress();
			currency = selectedVendor.getCurrency();

		} else if (payee instanceof ClientTAXAgency) {
			selectedTaxAgency = (ClientTAXAgency) payee;
			addressList = selectedTaxAgency.getAddress();

		}
		ClientCurrency clientCurrency = getCompany().getCurrency(currency);
		currencyWidget.setSelectedCurrency(clientCurrency);

		String formalName = currencyWidget.getSelectedCurrency()
				.getFormalName();

		transactionVendorAccountTable.updateTotals();
		this.transactionVendorAccountTable.updateAmountsFromGUI();

		foreignCurrencyamountLabel.setTitle(messages.currencyTotal(formalName));
		foreignCurrencyamountLabel.setCurrency(currencyWidget
				.getSelectedCurrency());

		transactionTotalBaseCurrencyText.setTitle(messages
				.currencyTotal(getBaseCurrency().getFormalName()));
		transactionTotalBaseCurrencyText.setCurrency(getBaseCurrency());

		amtText.setCurrency(clientCurrency);
		// getAddreses(add);
		if (isInViewMode()) {
			if (transaction.getAddress() != null)
				billToaddressSelected(getAddressById(transaction.getAddress()
						.getID()));
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(clientCurrency);
			setCurrencyFactor(transaction.getCurrencyFactor());
			updateAmountsFromGUI();
			modifyForeignCurrencyTotalWidget();
		}

		initBillToCombo();

	}

	@Override
	protected void reload() {
		if (!saveAndClose)
			try {
				new WriteChecksAction().run(null, true);
			} catch (Throwable e) {

				// //UIUtils.logError("Failed to load Write Check View", e);
			}

	}

	protected void updateBalance() {
		if (selectBankAcc != null) {
			balText.setAmount(selectBankAcc.getTotalBalance());
		}
	}

	protected void getAddreses(Set<ClientAddress> allAddress) {
		ClientAddress toBeShown = null;

		if (isInViewMode() && transaction.getAddress() != null) {
			addressList = payee.getAddress();
			toBeShown = getAddressById(transaction.getAddress().getID());
		} else {
			for (ClientAddress to : allAddress) {
				if (to.getType() == ClientAddress.TYPE_BUSINESS) {
					toBeShown = to;
					break;
				}
			}
		}

		if (toBeShown != null) {
			billToAddress.put("1", toBeShown);
			String toToSet = new String();
			if (toBeShown.getStreet() != null) {
				toToSet = toBeShown.getStreet().toString() + ",\n";
			}
			if (toBeShown.getCity() != null) {
				toToSet += toBeShown.getCity().toString() + ",\n";
			}

			if (toBeShown.getStateOrProvinence() != null) {
				toToSet += toBeShown.getStateOrProvinence() + ",\n";
			}
			if (toBeShown.getZipOrPostalCode() != null) {
				toToSet += toBeShown.getZipOrPostalCode() + ",\n";
			}
			if (toBeShown.getCountryOrRegion() != null) {
				toToSet += toBeShown.getCountryOrRegion();
			}

			addrArea.setValue(toToSet);
		}
	}

	public void initBankaccountCombo() {
		payFromAccounts = bankAccSelect.getAccounts();

		bankAccSelect.initCombo(payFromAccounts);
		if (isInViewMode() || transaction.getBankAccount() != 0) {

			selectBankAcc = this.company.getAccount(transaction
					.getBankAccount());

		}
		// else if (takenPaySalesTax != null) {
		//
		// selectBankAcc = getCompany().getAccount(
		// takenPaySalesTax.getPayFrom());
		//
		// }
		if (selectBankAcc != null) {

			bankAccSelect.setComboItem(selectBankAcc);
			updateBalance();
		}

		bankAccSelect.setEnabled(!isInViewMode());
		// bankAccSelect.setShowDisabled(false);

	}

	public void initPayToCombo() {
		List<ClientPayee> payees = getCompany().getActivePayees();

		if (payees != null) {

			paytoSelect.initCombo(payees);

			if (isInViewMode() || transaction.getPayToType() != 0) {
				ClientCustomer customer = null;
				if (transaction.getPayToType() == ClientPayee.TYPE_CUSTOMER) {
					customer = getCompany().getCustomer(
							transaction.getCustomer());
					payee = customer;
					paytoSelect.setComboItem(customer);

					if (isTrackJob()) {
						jobListCombo.setVisible(true);
						jobSelected(company.getjob(transaction.getJob()));
					}

				} else if (transaction.getPayToType() == ClientPayee.TYPE_VENDOR) {
					ClientVendor vendor2 = getCompany().getVendor(
							transaction.getVendor());
					payee = vendor2;
					paytoSelect.setComboItem(vendor2);
				} else if (transaction.getPayToType() == ClientPayee.TYPE_TAX_AGENCY) {
					ClientTAXAgency taxAgency = getCompany().getTaxAgency(
							transaction.getTaxAgency());
					payee = taxAgency;
					paytoSelect.setComboItem(taxAgency);
				}
				paytoSelect.setEnabled(!isInViewMode());
				transactionVendorAccountTable
						.setRecords(getAccountTransactionItems(transaction
								.getTransactionItems()));
				return;
			}

			newPayToMethod();
		}
	}

	protected void newPayToMethod() {
		if (isInViewMode()) {
			switch (transaction.getPayToType()) {
			case ClientWriteCheck.TYPE_VENDOR:
				paytoSelect.setComboItem(getCompany().getVendor(
						transaction.getVendor()));
				payee = this.company.getVendor(transaction.getVendor());

				break;
			case ClientWriteCheck.TYPE_CUSTOMER:
				ClientCompany clientCompany = this.company;
				paytoSelect.setComboItem(clientCompany.getCustomer(transaction
						.getCustomer()));
				payee = clientCompany.getCustomer(transaction.getCustomer());

				break;
			case ClientWriteCheck.TYPE_TAX_AGENCY:
				paytoSelect.setComboItem(getCompany().getTaxAgency(
						transaction.getTaxAgency()));
				payee = this.company.getTaxAgency(transaction.getTaxAgency());
				break;
			}
			paytoSelect.setEnabled(!isInViewMode());
			paytoSelect.setEnabled(!false);
			updateAddressAndGrid();

		}
	}

	private void setDisableFields() {
		if (isInViewMode()) {
			payForm.setEnabled(!true);
			bankAccForm.setEnabled(!true);
		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (isInViewMode()) {
			Double transactionTotal = transaction.getTotal();
			if (transactionTotal != null && !isAmountChange) {
				amtText.setAmount(transactionTotal.doubleValue());
			}

		}

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		result.add(DynamicForm.validate(payForm));
		result.add(DynamicForm.validate(bankAccForm));

		// FIXME Need to validate grids.
		if (transactionVendorAccountTable.getAllRows().isEmpty()) {
			result.addError(transactionVendorAccountTable,
					messages.blankTransaction());
		} else {
			result.add(transactionVendorAccountTable.validateGrid());
		}

		// if (!validateAmount()) {
		// result.addError(memoTextAreaItem,
		// messages.transactiontotalcannotbe0orlessthan0());
		// }

		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				if (taxCodeSelect != null
						&& taxCodeSelect.getSelectedValue() == null) {
					result.addError(taxCodeSelect,
							messages.pleaseSelect(messages.taxCode()));
				}

			}
		}
		if (unassignedAmountPanel.isVisible()) {
			result.addError(unassignedAmountPanel,
					messages.amountAndTotalShouldEqual());
		}

		ClientAccount bankAccount = bankAccSelect.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());
			if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
				result.addError(bankAccSelect,
						messages.selectProperBankAccount());
			}
		}

		return result;
	}

	private boolean validateAmount() {
		double total = 0.0;
		total += transactionVendorAccountTable.getGrandTotal();
		return AccounterValidator.isPositiveAmount(total);
	}

	@Override
	public ClientWriteCheck saveView() {
		ClientWriteCheck saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {

		if (isInViewMode()
				&& transaction.getSaveStatus() != ClientTransaction.STATUS_DRAFT) {
			updatePaySalesTax();
			return;
		}

		updateTransaction();

		saveOrUpdate(transaction);

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type of the write check
		transaction.setType(ClientTransaction.TYPE_WRITE_CHECK);

		transaction.setInFavourOf(inFavourOf.getValue());

		transaction.setTransactionDate(transactionDate.getDate());

		// Setting Bank account
		selectBankAcc = bankAccSelect.getSelectedValue();
		if (selectBankAcc != null) {
			transaction.setBankAccount(selectBankAcc.getID());
		}
		if (!getPreferences().isClassPerDetailLine() && accounterClass != null
				&& transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setAccounterClass(accounterClass.getID());
			}
		}
		// setting paymentmethod
		transaction.setPaymentMethod(messages.check());

		// Setting Address
		// transaction.setAddress(billingAddress);

		// Setting Transactions
		// FIXME Need to assign transaction Items from to tables.
		if (payee != null) {
			// In Edit mode If payee was changed to customer to vendor or
			// anything else
			// previous object should become null;
			transaction.setCustomer(0);
			transaction.setVendor(0);
			transaction.setTaxAgency(0);

			switch (payee.getType()) {
			case ClientPayee.TYPE_CUSTOMER:
				transaction.setCustomer(selectedCustomer.getID());
				transaction.setPayToType(ClientWriteCheck.TYPE_CUSTOMER);
				break;
			case ClientPayee.TYPE_VENDOR:
				transaction.setVendor(selectedVendor.getID());
				transaction.setPayToType(ClientWriteCheck.TYPE_VENDOR);
				break;

			case ClientPayee.TYPE_TAX_AGENCY:
				transaction.setTaxAgency(selectedTaxAgency.getID());
				transaction.setPayToType(ClientWriteCheck.TYPE_TAX_AGENCY);

				break;
			}
		}
		transaction.setTotal(amtText.getAmount());
		transaction.setAmount(amtText.getAmount());
		transaction.setInWords(amtText.getValue().toString());
		transaction.setNetAmount(netAmount.getAmount());

		// Setting Date
		if (date != null)
			transaction.setDate(date.getValue().getDate());
		// setting transactoin number
		transaction.setNumber(transactionNumber.getValue().toString());

		// setting checknumber
		transaction.setCheckNumber(getCheckValue());

		// setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		setAmountIncludeTAX();

		if (isTrackJob()) {
			if (jobListCombo.getSelectedValue() != null) {
				transaction.setJob(jobListCombo.getSelectedValue().getID());
			}
		}

		if (isTrackDiscounts()) {
			if (discountField.getPercentage() != 0.0
					&& transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getPercentage());
				}
			}
		}

		if (toprintCheck.getValue() != null) {
			transaction.setToBePrinted(toprintCheck.getValue());

		}
		if (currency != null) {
			transaction.setCurrency(currency.getID());
			transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
		}
	}

	private String getCheckValue() {
		String value;
		if (!isInViewMode()) {
			if (checkNumberField.getValue().equals(messages.toBePrinted())) {
				value = String.valueOf(messages.toBePrinted());
			} else {
				value = String.valueOf(checkNumberField.getValue());
			}
		} else {
			String checknumber = this.checkNumber;
			if (checknumber == null) {
				checknumber = messages.toBePrinted();
			}
			if (checknumber.equals(messages.toBePrinted())) {
				value = messages.toBePrinted();
			} else {
				value = String.valueOf(checknumber);
			}
		}
		return value;
	}

	private void updatePaySalesTax() {
		transactionSuccess(transaction);

	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();

		// setTitle(bankingConstants.writeCheck());
		Label titleLabel = new Label(messages.writeCheck() + "("
				+ getTransactionStatus() + ")");
		titleLabel.removeStyleName("gwt-Label");
		titleLabel.addStyleName("label-title");

		transactionNumber = createTransactionNumberItem();
		locationCombo = createLocationCombo();
		date = createTransactionDateItem();
		date.setShowTitle(false);
		date.setEnabled(!isInViewMode());

		numForm = new DynamicForm("numForm");
		numForm.addStyleName("datenumber-panel");
		if (isTemplate) {
			numForm.add(transactionNumber);
		} else {
			numForm.add(date, transactionNumber);
		}

		nHPanel = new StyledPanel("nHPanel");
		nHPanel.add(numForm);

		labelLayout = new StyledPanel("labelLayout");
		labelLayout.add(nHPanel);
		// labelLayout.setCellHorizontalAlignment(nHPanel,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		balText = new AmountField(messages.balance(), this, getBaseCurrency(),
				"balText");
		// balText.setWidth(100);
		balText.setEnabled(!true);

		bankAccSelect = new PayFromAccountsCombo(messages.bankAccount());
		// bankAccSelect.setWidth(100);
		bankAccSelect.setRequired(true);
		bankAccSelect.setEnabled(!isInViewMode());
		bankAccSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectBankAcc = selectItem;
						balText.setAmount(selectBankAcc
								.getTotalBalanceInAccountCurrency());
						balText.setCurrency(getCurrency(selectItem
								.getCurrency()));
					}

				});
		bankAccSelect.setDefaultPayFromAccount();

		bankAccForm = new DynamicForm("bankAccForm");
		if (locationTrackingEnabled)
			bankAccForm.add(locationCombo);
		jobListCombo = createJobListCombo();
		if (isTrackJob()) {
			jobListCombo.setVisible(false);
			bankAccForm.add(jobListCombo);
		}
		jobListCombo.setVisible(false);
		checkNumberField = new TextItem(messages.checkNumber(),
				"chekNumberField");
		checkNumberField.setValue(messages.toBePrinted());
		toprintCheck = new CheckboxItem(messages.toBePrinted(), "toprintCheck");
		toprintCheck.setEnabled(!false);
		toprintCheck.setValue(true);

		toprintCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				if (toprintCheck.getValue()) {
					if (toprintCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNumberField.setValue(messages.toBePrinted());
						checkNumberField.setEnabled(false);
					} else {
						if (payFromCombo.getValue() == null)
							checkNumberField.setValue(messages.toBePrinted());
						else if (transaction != null) {
							checkNumberField.setValue(transaction
									.getCheckNumber());
						}
					}
				} else {
					checkNumberField.setValue("");
				}
				checkNumberField.setEnabled(true);

			}
		});

		bankAccForm.add(bankAccSelect, balText, toprintCheck, checkNumberField);

		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			bankAccForm.add(classListCombo);
		}
		// bankAccForm.getCellFormatter().setWidth(0, 0, "232px");
		// forms.add(bankAccForm);
		taxCodeSelect = createTaxCodeSelectItem();
		paytoSelect = new PayeeCombo(messages.payTo());
		// paytoSelect.setWidth(100);
		// paytoSelect.setRequired(true);
		paytoSelect.setEnabled(!isInViewMode());
		paytoSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPayee>() {
					@Override
					public void selectedComboBoxItem(ClientPayee selectItem) {
						amtText.setValue("0.00");
						inFavourOf.setValue("");
						inFavourOf.setValue(selectItem.getName());
						inFavourOf.setEnabled(!true);
						if (payee != null) {

							transactionVendorAccountTable.resetRecords();
							transaction
									.setTransactionItems(transactionVendorAccountTable
											.getRecords());
							if (taxCodeSelect != null
									&& taxCodeSelect.getSelectedValue() != null) {
								transactionVendorAccountTable.setTaxCode(
										taxCodeSelect.getSelectedValue()
												.getID(), true);
							}
							if (currency != null) {
								transaction.setCurrency(currency.getID());
							}
							vatTotalNonEditableText.setTransaction(transaction);

							// } else if (payee instanceof ClientTAXAgency)
							// {
							// taxAgencyGrid.removeAllRecords();
						}

						if (isInViewMode() && payee != null) {
							if (payee.getType() != selectItem.getType()) {
								Accounter.showError(messages
										.youcannotchangeaCustomertypetoVendortype(
												Global.get().customer(), Global
														.get().Vendor()));
								paytoSelect.setComboItem(payee);
							} else {

								PayToSelected(selectItem);
								// Job Tracking

								if (isTrackJob()) {
									if (selectItem instanceof ClientCustomer) {
										jobListCombo.setValue("");
										// jobListCombo.setEnabled(!false);
										jobListCombo
												.setCustomer((ClientCustomer) selectItem);
										jobListCombo.setVisible(false);
									} else {
										jobListCombo.setVisible(false);
									}
								}
								payee = selectItem;
							}
						} else {
							if (selectItem instanceof ClientCustomer
									&& isTrackJob()) {
								jobListCombo.setValue("");
								// jobListCombo.setEnabled(!false);
								jobListCombo
										.setCustomer((ClientCustomer) selectItem);
								jobListCombo.setVisible(false);
							} else {
								jobListCombo.setVisible(false);
							}
							payee = selectItem;
						}
						updateAddressAndGrid();

					}

				});
		amtText = new AmountField(messages.amount(), this, getBaseCurrency(),
				"amtText");
		// amtText.setWidth(100);
		amtText.setAmount(0.00);
		amtText.setEnabled(!isInViewMode());
		amtText.addBlurHandler(new BlurHandler() {

			@Override
			public void onBlur(BlurEvent event) {
				if ((amtText.getAmount() != 0)
						&& !(DecimalUtil.isEquals(previousValue,
								amtText.getAmount()))) {
					previousValue = amtText.getAmount();
					isAmountChange = true;
					validateAmountAndTotal();
				}
			}
		});

		memoTextAreaItem = createMemoTextAreaItem();
		// memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setEnabled(!false);

		DynamicForm memoForm = new DynamicForm(" memoForm");
		// memoForm.setWidth("100%");
		memoForm.add(memoTextAreaItem);
		// memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		payForm = new DynamicForm("payForm");
		// payForm.setWidth("100%");
		inFavourOf = new TextItem(messages.inFavourOf(), "inFavourOf");
		inFavourOf.setRequired(true);
		inFavourOf.setEnabled(!isInViewMode());
		payForm.add(paytoSelect, inFavourOf, amtText);
		// payForm.getCellFormatter().setWidth(0, 0, "170px");

		currencyWidget = createCurrencyFactorWidget();

		StyledPanel currencyPanel = new StyledPanel("currencyPanel");
		// currencyPanel.setWidth("100%");
		currencyPanel.add(bankAccForm);
		if (isMultiCurrencyEnabled()) {
			currencyPanel.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}

		StyledPanel accPanel = new StyledPanel("accPanel");
		accPanel.addStyleName("fields-panel");
		accPanel.add(payForm);
		accPanel.add(currencyPanel);

		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.add(labelLayout);
		topHLay.add(accPanel);

		vatPanel = new StyledPanel("vatPanel");
		amountPanel = new StyledPanel("amountPanel");
		// vatPanel.setWidth("100%");
		// amountPanel.setWidth("100%");
		vatinclusiveCheck = getVATInclusiveCheckBox();
		foreignCurrencyamountLabel = createTransactionTotalNonEditableLabel(getBaseCurrency());

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getBaseCurrency());

		vatTotalNonEditableText = new TaxItemsForm();

		netAmount = new AmountLabel(
				messages.currencyNetAmount(getBaseCurrency().getFormalName()),
				getBaseCurrency());

		StyledPanel bottomPanel = new StyledPanel("bottomPanel");
		bottomPanel.add(memoForm);
		StyledPanel totalForm = new StyledPanel("totalForm");

		DynamicForm netAmountForm = new DynamicForm("netAmountForm");
		// netAmountForm.setNumCols(2);
		netAmountForm.add(netAmount);
		totalForm.add(netAmountForm);
		discountField = getDiscountField();

		DynamicForm form = new DynamicForm("form");
		if (isTrackTax()) {
			totalForm.add(vatTotalNonEditableText);
			if (!isTaxPerDetailLine()) {
				// taxCodeSelect.setVisible(isInViewMode());
				form.add(taxCodeSelect);
				// vatPanel.setCellHorizontalAlignment(form, ALIGN_CENTER);
				// vatPanel.setCellHorizontalAlignment(form, ALIGN_RIGHT);
			}
			if (isTrackPaidTax()) {
				form.add(vatinclusiveCheck);
			}
		}

		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				form.add(discountField);
			}
		}
		if (isTrackTax() || isTrackDiscounts()) {
			vatPanel.add(form);
		}
		DynamicForm transactionTotalForm = new DynamicForm(
				"transactionTotalForm");
		// transactionTotalForm.setNumCols(2);

		transactionTotalForm.add(foreignCurrencyamountLabel);

		if (isMultiCurrencyEnabled()) {
			transactionTotalForm.add(transactionTotalBaseCurrencyText);
		}
		totalForm.add(transactionTotalForm);
		totalForm.addStyleName("boldtext");

		unassignedAmountPanel = new StyledPanel("unassignedAmountPanel");

		amountPanel.add(unassignedAmountPanel);
		amountPanel.add(totalForm);

		Button recalculateButton = new Button(messages.recalculate());

		recalculateButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				isAmountChange = false;
				updateNonEditableItems();
				hideUnassignedFields();
			}

		});

		DynamicForm unassignedAmountForm = new DynamicForm(
				"unassignedAmountForm");
		unassignedAmount = new AmountLabel(
				messages.unassignedAmountWithCurrencyName(getBaseCurrency()
						.getFormalName()), getBaseCurrency());
		unassignedAmountForm.add(unassignedAmount);

		unassignedAmountPanel.add(recalculateButton);
		unassignedAmountPanel.add(unassignedAmountForm);

		// amountPanel.setCellHorizontalAlignment(totalForm, ALIGN_RIGHT);
		// amountPanel.setHorizontalAlignment(ALIGN_RIGHT);
		hideUnassignedFields();

		mainVLay = new StyledPanel("mainVLay");

		// For Editing writeCheck or PaySalesTax.
		// if (transactionObject != null) {
		// transactionItems = transactionObject.getTransactionItems();
		// if (takenPaySalesTax != null) {
		// paytoSelect.setRequired(false);
		// memoTextAreaItem.setEnabled(!true);
		// toprintCheck.setEnabled(!true);
		// transactionNumber = takenPaySalesTax.getNumber();
		// amtText.setAmount(takenPaySalesTax.getTotal());
		// date.setValue(takenPaySalesTax.getDate());
		// text.setValue(Utility.getNumberInWords(String
		// .valueOf(takenPaySalesTax.getTotal())));
		//
		// setGridType(TAXAGENCY_TRANSACTION_GRID);
		// taxAgencyGrid = new TaxAgencyTransactionGrid();
		// taxAgencyGrid.setTransactionView(this);
		// taxAgencyGrid.setCanEdit(true);
		// taxAgencyGrid.setEditEventType(ListGrid.EDIT_EVENT_DBCLICK);
		// taxAgencyGrid.init();
		// taxAgencyGrid.setVisible(true);
		// taxAgencyGrid.setTransactionPaySalesTax(takenPaySalesTax
		// .getTransactionPaySalesTax());
		// taxAgencyGrid.setVisible(false);
		// taxAgencyGrid = new TaxAgencyTransactionGrid();
		// taxAgencyGrid.init();
		// taxAgencyGrid.setVisible(true);
		// // taxAgencyGrid.setTransactionPaySalesTax(takenPaySalesTax
		// // .getTransactionPaySalesTax());
		// taxAgencyGrid.setVisible(true);
		// mainVLay.add(lab1);
		// nText
		// .setValue(writeCheckTaken.isToBePrinted() ? bankingConstants
		// .toBePrinted()
		// : transactionNumber != null ? transactionNumber
		// : "");
		// mainVLay.add(labelLayout);
		// mainVLay.add(topHLay);
		// mainVLay.add(taxAgencyGrid);
		//
		// }
		// if{

		transactionVendorAccountTable = new VendorAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this, isCustomerAllowedToAdd(),
				isTrackClass(), isClassPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				WriteChequeView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return WriteChequeView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return WriteChequeView.this.isInViewMode();
			}

			@Override
			protected boolean isTrackJob() {
				return WriteChequeView.this.isTrackJob();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				WriteChequeView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isAccountAllowed(ClientAccount account) {
				if (account.getType() != ClientAccount.TYPE_CASH
						&& account.getType() != ClientAccount.TYPE_BANK
						&& account.getType() != ClientAccount.TYPE_CREDIT_CARD
						&& account.getType() != ClientAccount.TYPE_INVENTORY_ASSET
						&& account.getType() != ClientAccount.TYPE_ACCOUNT_RECEIVABLE
						&& account.getType() != ClientAccount.TYPE_ACCOUNT_PAYABLE
						&& account.getType() != ClientAccount.TYPE_INCOME
						&& account.getType() != ClientAccount.TYPE_OTHER_INCOME) {
					return true;
				} else {
					return false;
				}
			}

		};

		transactionVendorAccountTable.setEnabled(!isInViewMode());

		this.vendorAccountFlowPanel = new StyledPanel("vendorAccountFlowPanel");
		vendorAccountsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		vendorAccountsDisclosurePanel.setTitle(messages.ItemizebyAccount());
		vendorAccountFlowPanel.add(transactionVendorAccountTable);
		vendorAccountsDisclosurePanel.setContent(vendorAccountFlowPanel);
		vendorAccountsDisclosurePanel.setOpen(true);
		// vendorAccountsDisclosurePanel.setWidth("100%");

		if (isInViewMode()) {
			transactionItems = transaction.getTransactionItems();
			transactionNumber.setValue(transaction.getNumber());

			amtText.setAmount(transaction.getTotal());
			memoTextAreaItem.setValue(transaction.getMemo());
			date.setValue(transaction.getDate());
			toprintCheck.setValue(transaction.isToBePrinted());
			toprintCheck.setEnabled(!true);
			// nText.setValue(writeCheckTaken.isToBePrinted() ? bankingConstants
			// .toBePrinted()
			// : transactionNumber != null ? transactionNumber : "");
			// }

		}
		mainVLay.add(titleLabel);
		mainVLay.add(voidedPanel);
		mainVLay.add(topHLay);
		mainVLay.add(vendorAccountsDisclosurePanel.getPanel());

		StyledPanel vPanel = new StyledPanel("vPanel");
		bottomPanel.add(vatPanel);
		bottomPanel.add(amountPanel);

		vPanel.add(bottomPanel);

		mainVLay.add(vPanel);

		this.add(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(numForm);

		listforms.add(bankAccForm);
		listforms.add(payForm);

		// settabIndexes();

		if (isMultiCurrencyEnabled() && !isInViewMode()) {
			transactionTotalBaseCurrencyText.hide();
		}

	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setEnabled(!isInViewMode());
		setMemoTextAreaItem(transaction.getMemo());

	}

	private boolean isCustomerAllowedToAdd() {
		if (transaction != null) {
			List<ClientTransactionItem> transactionItems = transaction
					.getTransactionItems();
			for (ClientTransactionItem clientTransactionItem : transactionItems) {
				if (clientTransactionItem.isBillable()
						|| clientTransactionItem.getCustomer() != 0) {
					return true;
				}
			}
		}
		if (getPreferences().isBillableExpsesEnbldForProductandServices()
				&& getPreferences()
						.isProductandSerivesTrackingByCustomerEnabled()) {
			return true;
		}
		return false;
	}

	@Override
	public void updateNonEditableItems() {

		if (transactionVendorAccountTable == null) {
			return;
		}
		ClientTAXCode tax = taxCodeSelect.getSelectedValue();
		if (tax != null) {
			for (ClientTransactionItem item : transactionVendorAccountTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
		}
		double total = transactionVendorAccountTable.getGrandTotal();

		if (!isAmountChange) {
			this.amtText.setAmount(total);
			previousValue = amtText.getAmount();
		}
		double grandTotal = transactionVendorAccountTable.getLineTotal();
		if (getPreferences().isTrackPaidTax()) {
			if (transaction.getTransactionItems() != null && !isInViewMode()) {
				transaction.setTransactionItems(transactionVendorAccountTable
						.getAllRows());
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			vatTotalNonEditableText.setTransaction(transaction);
		}
		netAmount.setAmount(grandTotal);

		foreignCurrencyamountLabel.setAmount(total);

		transactionTotalBaseCurrencyText
				.setAmount(getAmountInBaseCurrency(total));

		if (amtText.getAmount() == 0) {
			amtText.setAmount(foreignCurrencyamountLabel.getAmount());
			previousValue = amtText.getAmount();
		}
		if (isAmountChange)
			validateAmountAndTotal();

	}

	private void validateAmountAndTotal() {
		unassignedAmount.setAmount(amtText.getAmount()
				- foreignCurrencyamountLabel.getAmount());
		if (unassignedAmount.getAmount() != 0) {
			showUnassignedFields();
		} else {
			hideUnassignedFields();
		}
	}

	// @Override
	// public void onDraw() {
	// this.nText.setEnabled(!true);
	// }

	private void showUnassignedFields() {
		unassignedAmountPanel.setVisible(true);
	}

	private void hideUnassignedFields() {
		unassignedAmountPanel.setVisible(false);
	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button, messages.Account(),
				messages.productOrServiceItem());

	}

	@Override
	protected void onAddNew(String item) {
		// FIXME Need to add new transaction item to appropriate grid.
		ClientTransactionItem transactionItem = new ClientTransactionItem();
		if (payee != null) {
			if (payee.getType() == ClientWriteCheck.TYPE_CUSTOMER) {
				if (item.equals(messages.Accounts())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				} else if (item.equals(messages.productOrServiceItem())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ITEM);

				} else if (item.equals(messages.comment())) {
					transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
				}
				// else if (item.equals("Sales Tax")) {
				// transactionItem
				// .setType(ClientTransactionItem.TYPE_SALESTAX);
				// }
				// transactionCustomerTable.add(transactionItem);
			} else if (payee.getType() == ClientWriteCheck.TYPE_VENDOR
					|| payee.getType() == ClientWriteCheck.TYPE_TAX_AGENCY) {
				if (item.equals(messages.Accounts())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
				} else if (item.equals(messages.productOrServiceItem())) {
					transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
				} else if (item.equals(messages.comment())) {
					transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
				}
				// transactionVendorTable.add(transactionItem);
			}
		} else {
			if (item.equals(messages.Accounts())) {
				transactionItem.setType(ClientTransactionItem.TYPE_ACCOUNT);
			} else if (item.equals(messages.productOrServiceItem())) {
				transactionItem.setType(ClientTransactionItem.TYPE_ITEM);
			} else if (item.equals(messages.comment())) {
				transactionItem.setType(ClientTransactionItem.TYPE_COMMENT);
			}
			// else if (item.equals(Accounter.constants().salesTax())) {
			// transactionItem.setType(ClientTransactionItem.TYPE_SALESTAX);
			// }
			// transactionCustomerTable.add(transactionItem);
		}
	}

	@Override
	public void onAddNew() {
		// ClientTransactionItem transactionItem = new ClientTransactionItem();
		// taxAgencyGrid.addData(transactionItem);
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
		this.paytoSelect.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
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
		date.setEnabled(!isInViewMode());
		paytoSelect.setEnabled(!isInViewMode());
		// billToCombo.setEnabled(!isInViewMode());
		inFavourOf.setEnabled(!isInViewMode());
		amtText.setEnabled(!isInViewMode());
		toprintCheck.setEnabled(!isInViewMode());
		bankAccSelect.setEnabled(!isInViewMode());
		if (transactionVendorAccountTable != null)
			transactionVendorAccountTable.setEnabled(!isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		memoTextAreaItem.setEnabled(!false);
		discountField.setEnabled(!isInViewMode());
		checkNumberField.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (isTrackJob()) {
			jobListCombo.setEnabled(!isInViewMode());
			if (payee != null) {
				if (payee instanceof ClientCustomer) {
					jobListCombo.setCustomer((ClientCustomer) payee);
					jobListCombo.setComboItem(company.getjob(transaction
							.getJob()));
				}
			}
		}
		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				taxCodeSelect.setEnabled(!isInViewMode());
			}
		}
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		if (classListCombo != null) {
			classListCombo.setEnabled(!isInViewMode());
		}
		super.onEdit();

	}

	@Override
	public void print() {
		updateTransaction();
		UIUtils.downloadAttachment(transaction.getID(),
				ClientTransaction.TYPE_WRITE_CHECK);
	}

	@Override
	public void printPreview() {

	}

	public void PayToSelected(ClientPayee selectItem) {

		ClientCurrency payeeCurrency = getCompany().getCurrency(
				selectItem.getCurrency());

		amtText.setCurrency(payeeCurrency);

		// ClientWriteCheck check = (ClientWriteCheck) this.transaction;

		// FIXME Need to set transaction items.
		// if (selectItem instanceof ClientCustomer) {
		// transactionCustomerTable.removeAllRecords();
		// if (check.getCustomer() == selectItem.getID()) {
		// transactionCustomerTable
		// .setRecords(check.getTransactionItems());
		// }
		//
		// } else if (selectItem instanceof ClientVendor) {
		// transactionVendorTable.removeAllRecords();
		// if (check.getVendor() == selectItem.getID()) {
		// transactionVendorTable.setRecords(check.getTransactionItems());
		// }
		//
		// } else if (selectItem instanceof ClientTAXAgency) {
		// transactionVendorTable.removeAllRecords();
		// if (check.getTaxAgency() == selectItem.getID()) {
		// transactionVendorTable.setRecords(check.getTransactionItems());
		// }
		//
		// }

		if (currency.getID() != 0) {
			currencyWidget.setSelectedCurrency(payeeCurrency);
		} else {
			currencyWidget.setSelectedCurrency(getBaseCurrency());
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(1.0);
			updateAmountsFromGUI();
			modifyForeignCurrencyTotalWidget();
		}

	}

	@Override
	protected String getViewTitle() {
		return messages.writeCheck();
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientWriteCheck());
		} else {

			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				if (currency != null)
					currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
			}
			// if (getPreferences().isTrackPaidTax()) {
			// netAmount.setAmount(transaction.getNetAmount());
			// vatTotalNonEditableText.setAmount(transaction.getTotal()
			// - transaction.getNetAmount());
			// }
			netAmount.setAmount(transaction.getNetAmount());
			if (isTrackTax()) {
				if (isTaxPerDetailLine()) {
					vatTotalNonEditableText.setTransaction(transaction);
				} else {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						taxCodeSelected(taxCode);
					}
				}
			}
			if (isTrackClass()) {
				if (!isClassPerDetailLine()) {
					this.accounterClass = getClassForTransactionItem(transaction
							.getTransactionItems());
					if (accounterClass != null) {
						this.classListCombo.setComboItem(accounterClass);
						classSelected(accounterClass);
					}
				}
			}
			if (transaction.getTransactionItems() != null) {
				this.transactionItems = transaction.getTransactionItems();
				transactionVendorAccountTable
						.setRecords(getAccountTransactionItems(transaction
								.getTransactionItems()));
				if (isTrackDiscounts()) {
					if (!isDiscountPerDetailLine()) {
						this.discountField
								.setPercentage(getdiscount(this.transactionItems));
					}
				}
			}

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(isAmountIncludeTAX());

			}
			initMemoAndReference();
		}
		initPayToCombo();

		setDisableFields();
		initBankaccountCombo();
		updateAddressAndGrid();
		amtText.setAmount(transaction.getAmount());
		inFavourOf.setValue(transaction.getInFavourOf());
		if (transaction.getCheckNumber() != null) {
			if (transaction.getCheckNumber().equals(messages.toBePrinted())) {
				checkNumberField.setValue(messages.toBePrinted());
				toprintCheck.setValue(true);
			} else {
				checkNumberField.setValue(transaction.getCheckNumber());
				toprintCheck.setValue(false);
			}
		}

		if (transaction.getAmount() != 0) {
			validateAmountAndTotal();
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));

		vendorAccountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(transactionVendorAccountTable.getAllRows());
		return list;

	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		// FIXME
		// if (ClientWriteCheck.TYPE_CUSTOMER == transaction.getPayToType()) {
		// transactionCustomerTable.add(transactionItem);
		// } else {
		// transactionVendorTable.add(transactionItem);
		// }
	}

	@Override
	protected void refreshTransactionGrid() {
		if (transactionVendorAccountTable != null) {
			transactionVendorAccountTable.updateTotals();
		}
	}

	private void settabIndexes() {
		paytoSelect.setTabIndex(1);
		amtText.setTabIndex(3);
		date.setTabIndex(4);
		transactionNumber.setTabIndex(5);
		bankAccSelect.setTabIndex(6);
		balText.setTabIndex(7);
		memoTextAreaItem.setTabIndex(8);
		vatinclusiveCheck.setTabIndex(9);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(11);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(12);
		cancelButton.setTabIndex(13);

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		transactionVendorAccountTable.add(item);
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			transactionVendorAccountTable.setTaxCode(taxCode.getID(), true);
		} else {
			taxCodeSelect.setValue("");
		}

	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		this.transactionVendorAccountTable.updateAmountsFromGUI();
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub
	}

	public void modifyForeignCurrencyTotalWidget() {
		String formalName = currencyWidget.getSelectedCurrency()
				.getFormalName();
		if (currencyWidget.isShowFactorField()) {
			transactionTotalBaseCurrencyText.hide();
		} else {
			transactionTotalBaseCurrencyText.show();
			transactionTotalBaseCurrencyText.setTitle(messages
					.currencyTotal(getBaseCurrency().getFormalName()));
			foreignCurrencyamountLabel.setCurrency(currencyWidget
					.getSelectedCurrency());
		}
		netAmount.setTitle(messages.currencyNetAmount(formalName));
		netAmount.setCurrency(currencyWidget.getSelectedCurrency());
		unassignedAmount.setTitle(messages
				.unassignedAmountWithCurrencyName(formalName));
		unassignedAmount.setCurrency(currencyWidget.getSelectedCurrency());
	}

	@Override
	protected void updateDiscountValues() {

		if (discountField.getPercentage() != null) {
			transactionVendorAccountTable.setDiscount(discountField
					.getPercentage());
		} else {
			discountField.setPercentage(0d);
		}
	}

	@Override
	protected void classSelected(ClientAccounterClass accounterClass) {
		this.accounterClass = accounterClass;
		if (accounterClass != null) {
			classListCombo.setComboItem(accounterClass);
			transactionVendorAccountTable
					.setClass(accounterClass.getID(), true);
		} else {
			classListCombo.setValue("");
		}
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

	@Override
	protected void createButtons() {
		super.createButtons();
		accountTableButton = getAccountAddNewButton();
		addButton(vendorAccountFlowPanel, accountTableButton);
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(vendorAccountFlowPanel, accountTableButton);
	}
}
