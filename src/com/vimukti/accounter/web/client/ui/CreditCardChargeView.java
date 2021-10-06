package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCreditCardCharge;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.externalization.AccounterMessages;
import com.vimukti.accounter.web.client.ui.banking.AbstractBankTransactionView;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.ContactCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PayFromAccountsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.CheckboxItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CreditCardChargeView extends
		AbstractBankTransactionView<ClientCreditCardCharge> {
	protected List<String> selectedComboList;
	protected DateField date, delivDate;;
	protected TextItem cheqNoText;
	AmountField totText;
	List<String> idPhoneNumberForContacts = new ArrayList<String>();
	List<String> idNamesForContacts = new ArrayList<String>();

	protected DynamicForm vendorForm, addrForm, phoneForm, termsForm, memoForm;
	protected SelectCombo payMethSelect;
	protected TextItem phoneSelect;

	VendorCombo vendorNameSelect;
	protected PayFromAccountsCombo payFrmSelect;

	protected String selectPaymentMethod;

	private DynamicForm totForm;

	private StyledPanel botPanel;
	StyledPanel totPanel;

	private StyledPanel leftVLay;

	private ArrayList<DynamicForm> listforms;
	protected Label titlelabel;
	protected TextAreaItem billToAreaItem;
	private List<ClientAccount> listOfAccounts;
	private boolean locationTrackingEnabled;
	private VendorAccountTransactionTable vendorAccountTransactionTable;
	private VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private TAXCodeCombo taxCodeSelect;
	private GwtDisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;
	private StyledPanel accountFlowPanel;
	private StyledPanel itemsFlowPanel;

	public CreditCardChargeView() {

		super(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
		this.getElement().setId("CreditCardChargeView");

	}

	protected CreditCardChargeView(int type) {

		super(type);
		this.getElement().setId("CreditCardChargeView");

	}

	protected void addPhonesContactsAndAddress() {
		Set<ClientAddress> allAddress = new HashSet<ClientAddress>();
		if (selectedVendor != null) {
			allAddress = getSelectedVendor().getAddress();
			addressList = selectedVendor.getAddress();
			initBillToCombo();
			// billToCombo.setEnabled(!isEdit);
			Set<ClientContact> allContacts;
			allContacts = selectedVendor.getContacts();
			Iterator<ClientContact> it = allContacts.iterator();
			// List<String> phones = new ArrayList<String>();
			ClientContact primaryContact = null;

			int i = 0;
			while (it.hasNext()) {
				ClientContact contact = it.next();
				if (contact.isPrimary())
					primaryContact = contact;
				idNamesForContacts.add(contact.getName());
				idPhoneNumberForContacts.add(contact.getBusinessPhone());
				// phones.add(contact.getBusinessPhone());
				i++;
			}

			contactCombo.initCombo(new ArrayList<ClientContact>(allContacts));

			contactCombo.setComboItem(primaryContact);
		}
		if (transaction.getContact() != null)
			contactCombo.setSelected(transaction.getContact().getName());
		if (transaction.getPhone() != null)
			// FIXME check and fix the below code
			phoneSelect.setValue(transaction.getPhone());

		contactCombo.setEnabled(!isInViewMode());
		phoneSelect.setEnabled(!isInViewMode());
		billToAreaItem.setValue(null);
		for (ClientAddress toBeShown : allAddress) {
			if (toBeShown.getType() == ClientAddress.TYPE_BILL_TO) {
				billToAreaItem.setValue(getValidAddress(toBeShown));
			}
		}
		return;
		// if (primaryContact == null) {
		// contactNameSelect.setSelected("");
		// phoneSelect.setValue("");
		// return;
		// }

		// contactNameSelect.setSelected(primaryContact.getName());
		// phoneSelect.setValue(primaryContact.getBusinessPhone());

	}

	private String getValidAddress(ClientAddress address) {
		String toToSet = new String();
		if (address.getAddress1() != null && !address.getAddress1().isEmpty()) {
			toToSet = address.getAddress1().toString() + "\n";
		}

		if (address.getStreet() != null && !address.getStreet().isEmpty()) {
			toToSet += address.getStreet().toString() + "\n";
		}

		if (address.getCity() != null && !address.getCity().isEmpty()) {
			toToSet += address.getCity().toString() + "\n";
		}

		if (address.getStateOrProvinence() != null
				&& !address.getStateOrProvinence().isEmpty()) {
			toToSet += address.getStateOrProvinence() + "\n";
		}
		if (address.getZipOrPostalCode() != null
				&& !address.getZipOrPostalCode().isEmpty()) {
			toToSet += address.getZipOrPostalCode() + "\n";
		}
		if (address.getCountryOrRegion() != null
				&& !address.getCountryOrRegion().isEmpty()) {
			toToSet += address.getCountryOrRegion();
		}
		return toToSet;
	}

	private void addVendorsList() {
		List<ClientVendor> result = getCompany().getActiveVendors();
		if (result != null) {
			initVendorsList(result);

		}
	}

	protected void initVendorsList(List<ClientVendor> result) {
		// First identify existing selected vendor
		for (ClientVendor vendor : result) {
			if (isInViewMode())
				if (vendor.getID() == transaction.getVendor()) {
					selectedVendor = vendor;
				}
		}
		vendorNameSelect.initCombo(result);
		vendorNameSelect.setEnabled(!isInViewMode());
		if (isInViewMode()) {
			if (selectedVendor != null) {
				vendorNameSelect.setComboItem(selectedVendor);
				billToaddressSelected(selectedVendor.getSelectedAddress());
				vendorItemTransactionTable.setPayee(selectedVendor);
			}
			addPhonesContactsAndAddress();
		}
	}

	public ClientContact getContactBasedOnId(String Id) {
		for (ClientContact cont : selectedVendor.getContacts()) {
			if (String.valueOf(cont.getID()).equalsIgnoreCase(Id))
				return cont;

		}

		return null;
	}

	@Override
	protected void initMemoAndReference() {
		if (isInViewMode()) {
			memoTextAreaItem.setEnabled(false);
			setMemoTextAreaItem(transaction.getMemo());
		}

	}

	@Override
	protected void paymentMethodSelected(String paymentMethod2) {
		super.paymentMethodSelected(paymentMethod2);
		AccounterMessages messages = Global.get().messages();
		if (paymentMethod != null
				&& (paymentMethod.equals(messages.check()) || paymentMethod
						.equals(messages.cheque()))) {
			if (isInViewMode()) {
				cheqNoText
						.setValue(transaction.getCheckNumber() != null ? transaction
								.getCheckNumber() : "");

			}
			cheqNoText.setEnabled(true);
		} else {
			cheqNoText.setValue("");
			cheqNoText.setEnabled(false);
		}
	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCreditCardCharge());
			resetElements();
			initpayFromAccountCombo();
		} else {
			if (currencyWidget != null) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setEnabled(!isInViewMode());
			}
			transactionDateItem.setValue(transaction.getDate());
			contact = transaction.getContact();
			delivDate.setValue(new ClientFinanceDate(transaction
					.getDeliveryDate()));
			delivDate.setEnabled(!isInViewMode());
			phoneSelect.setValue(transaction.getPhone());
			netAmount.setAmount(transaction.getNetAmount());
			if (getPreferences().isTrackPaidTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					vatTotalNonEditableText.setTransaction(transaction);
				} else {
					this.taxCode = getTaxCodeForTransactionItems(transaction
							.getTransactionItems());
					if (taxCode != null) {
						this.taxCodeSelect.setComboItem(taxCode);
					}
				}
			}
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(isAmountIncludeTAX());
			}
			if (transaction.getPayFrom() != 0)
				payFromAccountSelected(transaction.getPayFrom());
			payFrmSelect.setComboItem(getCompany().getAccount(payFromAccount));
			payFrmSelect.setEnabled(!isInViewMode());
			cheqNoText.setValue(transaction.getCheckNumber());
			cheqNoText.setEnabled(false);
			paymentMethodSelected(transaction.getPaymentMethod());
			payMethSelect.setComboItem(transaction.getPaymentMethod());
			vendorAccountTransactionTable
					.setAllRows(getAccountTransactionItems(transaction
							.getTransactionItems()));
			vendorItemTransactionTable
					.setAllRows(getItemTransactionItems(transaction
							.getTransactionItems()));

		}

		if (transaction.getTransactionItems() != null) {
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					this.discountField.setPercentage(getdiscount(transaction
							.getTransactionItems()));
				}
			}
		}

		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initMemoAndReference();
		addVendorsList();
		accountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
		itemsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ITEM, false));

		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	private void initpayFromAccountCombo() {

		// listOfAccounts = Utility.getPayFromAccounts(FinanceApplication
		// .getCompany());
		// getPayFromAccounts();
		listOfAccounts = payFrmSelect.getAccounts();

		payFrmSelect.initCombo(listOfAccounts);
		payFrmSelect.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		payFrmSelect.setAccounts();
		payFrmSelect.setEnabled(!isInViewMode());

		account = payFrmSelect.getSelectedValue();

		if (account != null)
			payFrmSelect.setComboItem(account);
	}

	private void resetElements() {
		selectedVendor = null;
		// transaction = null;
		billingAddress = null;
		addressList = null;
		// billToCombo.setEnabled(!isEdit);
		paymentMethod = UIUtils.getpaymentMethodCheckBy_CompanyType(messages
				.check());
		payFromAccount = 0;
		// phoneSelect.setValueMap("");
		setMemoTextAreaItem("");
		// refText.setValue("");
		cheqNoText.setValue("");

	}

	@Override
	protected void createControls() {
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();

		titlelabel = new Label(messages.creditCardCharge());
		titlelabel.removeStyleName("gwt-Label");
		titlelabel.addStyleName("label-title");
		// titlelabel.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();
		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm("dateNoForm");
		dateNoForm.setStyleName("datenumber-panel");
		dateNoForm.add(transactionDateItem, transactionNumber);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");

		StyledPanel regPanel = new StyledPanel("regPanel");
		regPanel.add(dateNoForm);

		labeldateNoLayout.add(regPanel);
		if (!isTaxPerDetailLine())
			taxCodeSelect = createTaxCodeSelectItem();
		vendorNameSelect = new VendorCombo(Global.get().messages()
				.payeeName(Global.get().Vendor()));
		// vendorNameSelect.setWidth(100);
		// vendorNameSelect.setRequired(true);
		vendorNameSelect.setEnabled(true);

		vendorNameSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectedVendor = selectItem;
						vendorItemTransactionTable.setPayee(selectedVendor);
						if (selectedVendor.getPaymentMethod() != null) {
							paymentMethodSelected(selectedVendor
									.getPaymentMethod());
							payMethSelect.setSelected(paymentMethod);
						}
						long code = selectedVendor.getTAXCode();
						if (taxCodeSelect != null) {
							if (code == 0)
								code = Accounter.getCompany()
										.getDefaultTaxCode();
							taxCodeSelect.setComboItem(getCompany().getTAXCode(
									code));
							taxCodeSelected(getCompany().getTAXCode(code));
						}

						contactCombo.setEnabled(true);
						addPhonesContactsAndAddress();
						initContacts(selectItem);

						ClientCurrency clientCurrency = getCompany()
								.getCurrency(selectedVendor.getCurrency());
						currencyWidget.setSelectedCurrency(clientCurrency);

						String formalName = currencyWidget
								.getSelectedCurrency().getFormalName();

						if (isMultiCurrencyEnabled()) {
							setCurrency(clientCurrency);
							setCurrencyFactor(1.0);
							updateAmountsFromGUI();
						}
					}

				});

		contactCombo = new ContactCombo(messages.contactName(), true);
		contactCombo.setEnabled(false);
		contactCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientContact>() {

					@Override
					public void selectedComboBoxItem(ClientContact selectItem) {
						contactSelected(selectItem);

					}
				});
		contactCombo.addNewContactHandler(new ValueCallBack<ClientContact>() {

			@Override
			public void execute(ClientContact value) {
				addContactToVendor(value);
			}
		});
		// contactNameSelect.setWidth(100);
		// formItems.add(contactNameSelect);
		// billToCombo = createBillToComboItem();
		billToAreaItem = new TextAreaItem(messages.billTo(), "billToAreaItem");
		// billToAreaItem.setWidth(100);
		billToAreaItem.setEnabled(!true);
		// formItems.add(billToCombo);
		phoneSelect = new TextItem(messages.phone(), "phoneSelect");
		phoneSelect.setToolTip(messages.phoneNumberOf(Global.get().vendor()));

		vendorForm = UIUtils.form(messages.Vendor());
		// vendorForm.setWidth("100%");
		vendorForm.add(vendorNameSelect, contactCombo, phoneSelect,
				billToAreaItem);

		payMethSelect = new SelectCombo(messages.paymentMethod());
		payMethSelect.setRequired(true);
		List<String> paymentMthds = new ArrayList<String>();
		paymentMthds.add(messages.creditCard());
		payMethSelect.initCombo(paymentMthds);
		payMethSelect.setDefaultToFirstOption(true);
		payMethSelect.setEnabled(false);
		// payMethSelect.setComboItem(UIUtils
		// .getpaymentMethodCheckBy_CompanyType(messages
		// .check()));

		payFrmSelect = createPayFromselectItem();
		payFrmSelect.setTitle(messages.payFrom());
		payFromAccount = 0;
		// formItems.add(payFrmSelect);

		cheqNoText = new TextItem(messages.chequeNo(), "cheqNoText");
		cheqNoText.setEnabled(!isInViewMode());
		cheqNoText.setWidth(100);
		// formItems.add(cheqNoText);

		delivDate = new DateField(messages.deliveryDate(), "delivDate");
		delivDate.setValue(new ClientFinanceDate());
		// formItems.add(delivDate);

		termsForm = UIUtils.form(messages.terms());
		// termsForm.setWidth("100%");
		if (locationTrackingEnabled)
			termsForm.add(locationCombo);

		termsForm.add(payMethSelect, payFrmSelect, delivDate);
		if (getPreferences().isClassTrackingEnabled()
				&& getPreferences().isClassOnePerTransaction()) {
			classListCombo = createAccounterClassListCombo();
			termsForm.add(classListCombo);
		}
		netAmount = new AmountLabel(
				messages.currencyNetAmount(getBaseCurrency().getFormalName()),
				getBaseCurrency());
		netAmount.setDefaultValue(String.valueOf(0.00));
		netAmount.setEnabled(false);

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getBaseCurrency());

		foreignCurrencyamountLabel = createTransactionTotalNonEditableLabel(getBaseCurrency());

		vatTotalNonEditableText = new TaxItemsForm();

		vatinclusiveCheck = new CheckboxItem(messages.amountIncludesVat(),
				"vatinclusiveCheck");
		vatinclusiveCheck = getVATInclusiveCheckBox();

		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax() && isTrackPaidTax(), isTaxPerDetailLine(),
				isTrackDiscounts(), isDiscountPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				CreditCardChargeView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CreditCardChargeView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CreditCardChargeView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CreditCardChargeView.this.updateNonEditableItems();
			}
		};

		vendorAccountTransactionTable.setEnabled(!isInViewMode());

		this.accountFlowPanel = new StyledPanel("StyledPanel");

		accountsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		accountsDisclosurePanel.setTitle(messages.ItemizebyAccount());
		accountFlowPanel.add(vendorAccountTransactionTable);
		accountsDisclosurePanel.setContent(accountFlowPanel);

		vendorItemTransactionTable = new VendorItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				CreditCardChargeView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CreditCardChargeView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CreditCardChargeView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CreditCardChargeView.this.updateNonEditableItems();
			}
		};

		vendorItemTransactionTable.setEnabled(!isInViewMode());

		this.itemsFlowPanel = new StyledPanel("itemsFlowPanel");

		itemsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		accountsDisclosurePanel.setTitle(messages.ItemizebyProductService());
		itemsFlowPanel.add(vendorItemTransactionTable);

		itemsDisclosurePanel.setContent(itemsFlowPanel);

		memoTextAreaItem = createMemoTextAreaItem();
		// memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setEnabled(!false);

		// refText = new TextItem(messages.reference());
		//
		// refText.setWidth(100);
		// refText.setEnabled(!false);
		currencyWidget = createCurrencyFactorWidget();
		DynamicForm memoForm = new DynamicForm("memoForm");
		memoForm.add(memoTextAreaItem);

		DynamicForm vatCheckform = new DynamicForm("vatCheckform");
		// vatCheckform.add(vatinclusiveCheck);

		StyledPanel totalForm = new StyledPanel("totalForm");
		totalForm.setStyleName("boldtext");
		// totText = new AmountField(FinanceApplication.constants()
		// .total());
		// totText.setWidth(100);

		totForm = new DynamicForm("totForm");
		totForm.addStyleName("boldtext");

		botPanel = new StyledPanel("botPanel");

		StyledPanel bottompanel = new StyledPanel("bottompanel");

		DynamicForm transactionTotalForm = new DynamicForm(
				"transactionTotalForm");

		discountField = getDiscountField();

		DynamicForm form = new DynamicForm("form");

		if (isTrackPaidTax()) {
			DynamicForm netAmountForm = new DynamicForm("netAmountForm");
			netAmountForm.add(netAmount);

			totalForm.add(netAmountForm);
			totalForm.add(vatTotalNonEditableText);

			if (isMultiCurrencyEnabled()) {
				transactionTotalForm.add(transactionTotalBaseCurrencyText,
						foreignCurrencyamountLabel);
			} else {
				transactionTotalForm.add(transactionTotalBaseCurrencyText);
			}
			totalForm.add(transactionTotalForm);

			StyledPanel vPanel = new StyledPanel("vPanel");
			vPanel.add(totalForm);
			botPanel.add(memoForm);
			if (!isTaxPerDetailLine()) {
				form.add(taxCodeSelect);
				botPanel.add(form);
			}
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					form.add(discountField);
					botPanel.add(form);
				}
			}
			botPanel.add(totalForm);

			bottompanel.add(vPanel);
			bottompanel.add(botPanel);

		} else {
			if (isMultiCurrencyEnabled()) {
				totForm.add(foreignCurrencyamountLabel,
						transactionTotalBaseCurrencyText);
			} else {
				totForm.add(transactionTotalBaseCurrencyText);
			}

			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					form.add(discountField);
					botPanel.add(form);
				}
			}

			StyledPanel hPanel = new StyledPanel("hPanel");
			hPanel.add(memoForm);
			hPanel.add(totForm);
			StyledPanel vpanel = new StyledPanel("vpanel");
			vpanel.add(hPanel);

			bottompanel.add(vpanel);
		}

		leftVLay = new StyledPanel("leftVLay");
		leftVLay.add(vendorForm);

		StyledPanel rightHLay = new StyledPanel("rightHLay");
		rightHLay.add(termsForm);
		if (isMultiCurrencyEnabled()) {
			rightHLay.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}

		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("fields-panel");
		// topHLay.setWidth("100%");
		topHLay.add(leftVLay);
		topHLay.add(rightHLay);

		StyledPanel vLay1 = new StyledPanel("vLay1");
		vLay1.add(accountsDisclosurePanel.getPanel());
		vLay1.add(itemsDisclosurePanel.getPanel());
		vLay1.add(bottompanel);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(titlelabel);
		mainVLay.add(voidedPanel);
		mainVLay.add(labeldateNoLayout);
		mainVLay.add(topHLay);
		mainVLay.add(vLay1);

		this.add(mainVLay);

		// setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(transactionTotalForm);
		listforms.add(totForm);
		initViewType();

		// if (UIUtils.isMSIEBrowser())
		// resetFormView();

		if (isInViewMode()) {
			payFrmSelect.setComboItem(getCompany().getAccount(
					transaction.getPayFrom()));
		}
		// settabIndexes();
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}
	}

	// protected void payFromMethodSelected(Account account2) {
	// this.account = account2;
	//
	// }

	@Override
	public ClientCreditCardCharge saveView() {
		ClientCreditCardCharge saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
			transaction.setNetAmount(netAmount.getAmount());
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();

		transaction.setNetAmount(netAmount.getAmount());
		// creditCardCharge.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		super.saveAndUpdateView();

		createAlterObject();
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);

		// setting date
		if (transactionDateItem != null)
			transaction.setDate(transactionDateItem.getValue().getDate());
		// setting number
		if (transactionNumber != null)
			transaction.setNumber(transactionNumber.getValue().toString());

		if (selectedVendor != null) {
			// setting vendor
			transaction.setVendor(selectedVendor.getID());

			// setting contact
			if (contact != null) {
				transaction.setContact(contact);
			}
			// if (contactNameSelect.getValue() != null) {
			// // ClientContact contact = getContactBasedOnId(contactNameSelect
			// // .getValue().toString());
			// transaction
			// .setContact(getContactBasedOnId(contactNameSelect
			// .getValue().toString()));
			//
			// }
		}

		if (!getPreferences().isClassPerDetailLine() && accounterClass != null
				&& transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setAccounterClass(accounterClass.getID());
			}
		}

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);

		// setting phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());

		// Setting payment method

		transaction.setPaymentMethod(payMethSelect.getSelectedValue());

		// Setting pay from
		if (payFrmSelect.getSelectedValue() != null)
			payFromAccount = payFrmSelect.getSelectedValue().getID();
		if (payFromAccount != 0) {

			transaction.setPayFrom(getCompany().getAccount(payFromAccount)
					.getID());
		}

		// setting check no
		if (cheqNoText.getValue() != null)
			transaction.setCheckNumber(cheqNoText.getValue().toString());

		setAmountIncludeTAX();

		// setting delivery date
		transaction.setDeliveryDate(UIUtils.toDate(delivDate.getValue()));

		// setting total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());
		// setting memo
		transaction.setMemo(getMemoTextAreaItem());
		// setting ref
		// creditCardCharge.setReference(UIUtils.toStr(refText.getValue()));

		if (discountField.getPercentage() != 0.0 && transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setDiscount(discountField.getPercentage());
			}

		}

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	public void createAlterObject() {
		saveOrUpdate(transaction);

	}

	@Override
	public void updateNonEditableItems() {
		if (vendorAccountTransactionTable == null
				|| vendorItemTransactionTable == null) {
			return;
		}
		ClientTAXCode tax = taxCodeSelect.getSelectedValue();
		if (tax != null) {
			for (ClientTransactionItem item : vendorAccountTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
			for (ClientTransactionItem item : vendorItemTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
		}
		double lineTotal = vendorAccountTransactionTable.getLineTotal()
				+ vendorItemTransactionTable.getLineTotal();
		double grandTotal = vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal();

		transactionTotalBaseCurrencyText
				.setAmount(getAmountInBaseCurrency(grandTotal));
		foreignCurrencyamountLabel.setAmount(grandTotal);
		netAmount.setAmount(lineTotal);
		if (isTrackTax()) {
			if ((transaction.getTransactionItems() != null && transaction
					.getTransactionItems().isEmpty()) && !isInViewMode()) {
				transaction.setTransactionItems(vendorAccountTransactionTable
						.getAllRows());
				transaction.getTransactionItems().addAll(
						vendorItemTransactionTable.getAllRows());
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			vatTotalNonEditableText.setTransaction(transaction);
		}
	}

	@Override
	public ValidationResult validate() {
		try {
			ValidationResult result = super.validate();
			if (AccounterValidator
					.isInPreventPostingBeforeDate(transactionDate)) {
				result.addError(transactionDate, messages.invalidateDate());
			}

			result.add(vendorForm.validate());
			result.add(termsForm.validate());
			if (getAllTransactionItems().isEmpty()) {
				result.addError(vendorAccountTransactionTable,
						messages.blankTransaction());
			} else {
				result.add(vendorAccountTransactionTable.validateGrid());
				result.add(vendorItemTransactionTable.validateGrid());
			}
			return result;
		} catch (Exception e) {
			System.err.println(e);
		}
		return null;

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
		this.vendorNameSelect.setFocus();
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
		if (type != null) {
			this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
		}

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		// payMethSelect.setEnabled(!isEdit);
		if (paymentMethod.equals(messages.check())
				|| paymentMethod.equals(messages.cheque())) {
			cheqNoText.setEnabled(!isInViewMode());
		} else {
			cheqNoText.setEnabled(!!isInViewMode());
		}
		delivDate.setEnabled(!isInViewMode());
		// billToCombo.setEnabled(!isEdit);
		vendorNameSelect.setEnabled(!isInViewMode());
		contactCombo.setEnabled(!isInViewMode());
		phoneSelect.setEnabled(!isInViewMode());
		payFrmSelect.setEnabled(!isInViewMode());
		memoTextAreaItem.setEnabled(!isInViewMode());
		vendorAccountTransactionTable.setEnabled(!isInViewMode());
		vendorItemTransactionTable.setEnabled(!isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		taxCodeSelect.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		classListCombo.setEnabled(!isInViewMode());
		super.onEdit();

	}

	protected void initViewType() {

	}

	@Override
	public void print() {
		// TODO Auto-generated method stub

	}

	@Override
	public void printPreview() {
		// its not using any where

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		// its not using any where

	}

	private void resetFormView() {
		// vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// refText.setWidth("200px");
	}

	@Override
	protected String getViewTitle() {
		return messages.creditCardCharge();
	}

	public ClientVendor getSelectedVendor() {
		return selectedVendor;
	}

	/**
	 * @param value
	 */
	protected void addContactToVendor(final ClientContact contact) {
		ClientVendor selectedVendor = vendorNameSelect.getSelectedValue();
		if (selectedVendor == null) {
			return;
		}
		selectedVendor.addContact(contact);
		AccounterAsyncCallback<Long> asyncallBack = new AccounterAsyncCallback<Long>() {

			@Override
			public void onException(AccounterException caught) {
				caught.printStackTrace();
			}

			@Override
			public void onResultSuccess(Long result) {
				contactSelected(contact);
			}

		};
		Accounter.createCRUDService().update(selectedVendor, asyncallBack);
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorAccountTransactionTable.add(transactionItem);

	}

	@Override
	protected void refreshTransactionGrid() {

	}

	private void settabIndexes() {
		vendorNameSelect.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		billToAreaItem.setTabIndex(4);
		transactionDateItem.setTabIndex(5);
		transactionNumber.setTabIndex(6);
		payMethSelect.setTabIndex(7);
		payFrmSelect.setTabIndex(8);
		delivDate.setTabIndex(9);
		memoTextAreaItem.setTabIndex(10);
		// menuButton.setTabIndex(11);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(12);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(13);
		cancelButton.setTabIndex(14);

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		vendorAccountTransactionTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		vendorItemTransactionTable.add(item);
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			vendorAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			vendorItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else {
			taxCodeSelect.setValue("");
		}
	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		this.vendorItemTransactionTable.updateAmountsFromGUI();
		this.vendorAccountTransactionTable.updateAmountsFromGUI();

	}

	public void modifyForeignCurrencyTotalWidget() {
		String formalName = currencyWidget.getSelectedCurrency()
				.getFormalName();
		if (currencyWidget.isShowFactorField()) {
			foreignCurrencyamountLabel.hide();
		} else {
			foreignCurrencyamountLabel.show();
			foreignCurrencyamountLabel.setTitle(messages
					.currencyTotal(formalName));
			foreignCurrencyamountLabel.setCurrency(currencyWidget
					.getSelectedCurrency());
		}
		netAmount.setTitle(messages.currencyNetAmount(formalName));
		netAmount.setCurrency(currencyWidget.getSelectedCurrency());
	}

	protected void updateDiscountValues() {
		if (discountField.getPercentage() != null) {
			vendorItemTransactionTable.setDiscount(discountField
					.getPercentage());
			vendorAccountTransactionTable.setDiscount(discountField
					.getPercentage());
		} else {
			discountField.setPercentage(0d);
		}
	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return false;
	}

	@Override
	protected void createButtons() {
		super.createButtons();
		itemTableButton = getItemAddNewButton();
		accountTableButton = getAccountAddNewButton();
		addButton(itemsFlowPanel, itemTableButton);
		addButton(accountFlowPanel, accountTableButton);
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(itemsFlowPanel, itemTableButton);
		removeButton(accountFlowPanel, accountTableButton);
	}

}
