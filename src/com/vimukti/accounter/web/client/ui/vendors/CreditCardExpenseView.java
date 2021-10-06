package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.ValueCallBack;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
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
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.GwtDisclosurePanel;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.banking.AbstractBankTransactionView;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.ContactCombo;
import com.vimukti.accounter.web.client.ui.combo.CreditCardAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.ActionCallback;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class CreditCardExpenseView extends
		AbstractBankTransactionView<ClientCreditCardCharge> implements
		IPrintableView {

	protected List<String> selectedComboList;
	protected DateField date, delivDate;;
	protected TextItem cheqNoText;
	AmountField totText;
	List<String> idPhoneNumberForContacts = new ArrayList<String>();
	List<String> idNamesForContacts = new ArrayList<String>();

	protected DynamicForm vendorForm, addrForm, phoneForm, termsForm, memoForm;
	// protected SelectCombo payMethSelect;
	protected CreditCardAccountCombo creditCardCombo;

	protected String selectPaymentMethod;

	private DynamicForm totForm;

	private StyledPanel botPanel;
	StyledPanel totPanel;

	private StyledPanel leftVLay;

	private ArrayList<DynamicForm> listforms;
	protected Label titlelabel;
	// protected TextAreaItem billToAreaItem;
	private List<ClientAccount> listOfAccounts;

	private VendorAccountTransactionTable vendorAccountTransactionTable;
	private VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private GwtDisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;

	private TAXCodeCombo taxCodeSelect;
	private StyledPanel accountFlowPanel;
	private StyledPanel itemsFlowPanel;

	public CreditCardExpenseView() {
		super(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
		this.getElement().setId("CreditCardExpenseView");
	}

	@Override
	protected void createControls() {
		String vendorString = null;

		vendorString = messages.payeeName(Global.get().Vendor());

		vendorCombo = new VendorCombo(vendorString, true) {
			@Override
			public void initCombo(List<ClientVendor> list) {
				List<ClientVendor> ccVendors = new ArrayList<ClientVendor>();
				for (ClientVendor vdr : list) {
					// if (vdr.getVendorGroup() != 0) {
					// ClientVendorGroup vendorGrougp = Accounter.getCompany()
					// .getVendorGroup(vdr.getVendorGroup());
					// if (vendorGrougp.getName().equals(
					// AccounterClientConstants.CREDIT_CARD_COMPANIES)) {
					ccVendors.add(vdr);
					// }
					// }
				}
				super.initCombo(ccVendors);
			}

			@Override
			public void onAddNew() {
				NewVendorAction action = new NewVendorAction();

				action.setCallback(new ActionCallback<ClientVendor>() {

					@Override
					public void actionResult(ClientVendor result) {
						if (result.getName() != null)
							addItemThenfireEvent(result);

					}
				});
				action.setOpenedFrom(NewVendorAction.FROM_CREDIT_CARD_EXPENSE);
				action.run(null, true);

			}
		};
		vendorCombo.setRequired(false);
		if (!isTaxPerDetailLine())
			taxCodeSelect = createTaxCodeSelectItem();
		vendorCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

					@Override
					public void selectedComboBoxItem(ClientVendor selectItem) {
						selectedVendor = selectItem;
						vendorItemTransactionTable.setPayee(selectedVendor);
						vendorCombo.setComboItem(selectItem);
						addPhonesContactsAndAddress();
						long code = selectedVendor.getTAXCode();
						if (taxCodeSelect != null) {
							if (code == 0)
								code = Accounter.getCompany()
										.getDefaultTaxCode();
							taxCodeSelect.setComboItem(getCompany().getTAXCode(
									code));
						}
						// ClientCurrency currency = getCurrency(selectedVendor
						// .getCurrency());
						// if (currency.getID() != 0) {
						// currencyWidget.setSelectedCurrencyFactorInWidget(
						// currency, transactionDateItem.getDate()
						// .getDate());
						// } else {
						// currencyWidget
						// .setSelectedCurrency(getBaseCurrency());
						// }
						// modifyForeignCurrencyTotalWidget();
					}

				});

		String listString[] = new String[] { messages.cash(),
				UIUtils.getpaymentMethodCheckBy_CompanyType(messages.check()),
				messages.creditCard(), messages.directDebit(),
				messages.masterCard(), messages.onlineBanking(),
				messages.standingOrder(), messages.switchMaestro() };

		if (isInViewMode()) {
			ClientCreditCardCharge creditCardCharge = transaction;
			vendorCombo.setComboItem(getCompany().getVendor(
					creditCardCharge.getVendor()));
			vendorCombo.setEnabled(false);
		}
		// vendorForm.setFields(vendorCombo, contactNameSelect, phoneSelect,
		// billToAreaItem);
		// vendorForm.getCellFormatter().setWidth(0, 0, "180px");
		// vendorForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
		// StyledPanel verticalPanel = (StyledPanel) vendorForm.getParent();
		// vendorForm.removeFromParent();
		// verticalPanel.add(vendorForm);
		// verticalPanel.setSpacing(10);

		titlelabel = new Label(messages.creditCardExpense());
		titlelabel.removeStyleName("gwt-Label");
		titlelabel.addStyleName("label-title");
		// titlelabel.setHeight("35px");
		transactionDateItem = createTransactionDateItem();
		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm("dateNoForm");
		dateNoForm.setStyleName("datenumber-panel");
		if (isTemplate) {
			dateNoForm.add(transactionNumber);
		} else {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");

		StyledPanel regPanel = new StyledPanel("regPanel");
		regPanel.add(dateNoForm);

		labeldateNoLayout.add(regPanel);

		contactCombo = new ContactCombo(messages.contact(), true);
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
		contactCombo.setEnabled(!isInViewMode());

		// formItems.add(contactCombo);

		// contactNameSelect = new
		// SelectCombo(messages.contactName());
		// contactNameSelect.setHelpInformation(true);
		// contactNameSelect
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<String>() {
		//
		// @Override
		// public void selectedComboBoxItem(String selectItem) {
		// contactNameSelect.setSelected(selectItem);
		//
		// int i = 0;
		// while (i < idNamesForContacts.size()) {
		// String s = idNamesForContacts.get(i);
		// if (s.equals(selectItem))
		// phoneSelect.setValue(idPhoneNumberForContacts
		// .get(i));
		//
		// i++;
		// }
		//
		// }
		// });
		// contactNameSelect.setWidth(100);
		// formItems.add(contactNameSelect);
		// billToCombo = createBillToComboItem();

		// billToAreaItem = new TextAreaItem(messages.billTo());
		// billToAreaItem.setWidth(100);
		// billToAreaItem.setDisabled(true);
		// formItems.add(billToCombo);

		creditCardCombo = createCreditCardItem();
		// creditCardCombo.setWidth("100%");
		// creditCardCombo.setPopupWidth("450px");
		// creditCardCombo.setTitle(messages.payFrom());

		phoneSelect = new TextItem(messages.phone(), "phoneSelect");
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		// phoneSelect.setWidth(100);
		// formItems.add(phoneSelect);

		vendorForm = UIUtils.form(messages.Vendor());
		// vendorForm.setWidth("100%");
		vendorForm.add(creditCardCombo, vendorCombo, contactCombo, phoneSelect
		/* billToAreaItem */);
		// vendorForm.getCellFormatter().setWidth(0, 0, "180px");
		//
		// payMethSelect = new SelectCombo(messages.paymentMethod());
		// payMethSelect.setRequired(true);
		// payMethSelect.setWidth(100);
		// List<String> paymentMthds = new ArrayList<String>();
		// paymentMthds.add(messages.creditCard());
		// payMethSelect.initCombo(paymentMthds);
		// payMethSelect.setDefaultToFirstOption(true);
		// payMethSelect.setDisabled(true);
		// payMethSelect.setComboItem(UIUtils
		// .getpaymentMethodCheckBy_CompanyType(messages
		// .check()));

		payFromAccount = 0;

		// formItems.add(payFrmSelect)
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
		if (!isTemplate) {
			// termsForm.setFields(delivDate);
		}
		// termsForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "203px");

		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
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

		// vatinclusiveCheck = new CheckboxItem(messages
		// .amountIncludesVat());
		vatinclusiveCheck = getVATInclusiveCheckBox();

		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), isTrackClass(),
				isClassPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CreditCardExpenseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CreditCardExpenseView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CreditCardExpenseView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CreditCardExpenseView.this.updateNonEditableItems();
			}
		};

		vendorAccountTransactionTable.setEnabled(!isInViewMode());

		this.accountFlowPanel = new StyledPanel("accountFlowPanel");
		accountsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		accountsDisclosurePanel.setTitle(messages.ItemizebyAccount());
		accountFlowPanel.add(vendorAccountTransactionTable);

		accountsDisclosurePanel.setContent(accountFlowPanel);
		accountsDisclosurePanel.setOpen(true);
		// accountsDisclosurePanel.setWidth("100%");
		vendorItemTransactionTable = new VendorItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), isTrackClass(),
				isClassPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CreditCardExpenseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CreditCardExpenseView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CreditCardExpenseView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CreditCardExpenseView.this.updateNonEditableItems();
			}
		};

		vendorItemTransactionTable.setEnabled(!isInViewMode());

		this.itemsFlowPanel = new StyledPanel("itemsFlowPanel");
		itemsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		itemsDisclosurePanel.setTitle(messages.ItemizebyProductService());
		itemsFlowPanel.add(vendorItemTransactionTable);

		itemsDisclosurePanel.setContent(itemsFlowPanel);
		// itemsDisclosurePanel.setWidth("100%");
		memoTextAreaItem = createMemoTextAreaItem();
		// memoTextAreaItem.setWidth(100);
		memoTextAreaItem.setDisabled(false);

		// refText = new TextItem(messages.reference());
		//
		// refText.setWidth(100);
		// refText.setDisabled(false);

		DynamicForm memoForm = new DynamicForm("memoForm");
		// memoForm.setWidth("100%");
		memoForm.add(memoTextAreaItem);

		DynamicForm vatCheckform = new DynamicForm("vatCheckform");
		// vatCheckform.setFields(vatinclusiveCheck);

		StyledPanel totalForm = new StyledPanel("totalForm");
		totalForm.setStyleName("boldtext");
		// totText = new AmountField(FinanceApplication.constants()
		// .total());
		// totText.setWidth(100);

		totForm = new DynamicForm("totForm");
		totForm.addStyleName("unused-payments");
		totForm.getElement().getStyle().setMarginTop(10, Unit.PX);

		botPanel = new StyledPanel("botPanel");

		StyledPanel bottompanel = new StyledPanel("bottompanel");
		// bottompanel.setWidth("100%");
		currencyWidget = createCurrencyFactorWidget();

		discountField = getDiscountField();

		DynamicForm transactionTotalForm = new DynamicForm(
				"transactionTotalForm");

		DynamicForm form = new DynamicForm("form");

		if (isTrackTax()) {
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

			}
			form.add(vatinclusiveCheck);
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					form.add(discountField);
				}
			}
			botPanel.add(form);
			botPanel.add(totalForm);

			bottompanel.add(vPanel);
			bottompanel.add(botPanel);

		} else {
			totForm.add(transactionTotalBaseCurrencyText);

			if (isMultiCurrencyEnabled())
				totForm.add(foreignCurrencyamountLabel);

			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					form.add(discountField);
					botPanel.add(form);
				}
			}
			StyledPanel hPanel = new StyledPanel("hpanel");
			hPanel.add(memoForm);
			hPanel.add(botPanel);
			hPanel.add(totForm);

			StyledPanel vpanel = new StyledPanel("vpanel");
			vpanel.add(hPanel);

			bottompanel.add(vpanel);
		}

		leftVLay = new StyledPanel("leftVLay");
		// leftVLay.setWidth("80%");
		leftVLay.add(vendorForm);

		StyledPanel rightHLay = new StyledPanel("rightHLay");
		// rightHLay.setWidth("80%");
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
		// vLay1.add(lab2);
		// vLay1.add(addButton);
		// multi currency combo
		vLay1.add(accountsDisclosurePanel.getPanel());
		vLay1.add(itemsDisclosurePanel.getPanel());
		// vLay1.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);
		// vLay1.setWidth("100%");
		vLay1.add(bottompanel);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		// mainVLay.setSize("100%", "100%");
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

		if (isInViewMode()) {
			creditCardCombo.setComboItem(getCompany().getAccount(
					transaction.getPayFrom()));
		}

	}

	private CreditCardAccountCombo createCreditCardItem() {
		CreditCardAccountCombo creditCardAccountCombo = new CreditCardAccountCombo(
				messages.payFrom());
		creditCardAccountCombo.setRequired(true);
		// payFrmSelect.setWidth("*");

		// payFrmSelect.setWidth("*");
		// payFrmSelect.setWrapTitle(false);
		creditCardAccountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						creditCardAccountSelected(selectItem);
					}

				});
		creditCardAccountCombo.setEnabled(!isInViewMode());
		// payFrmSelect.setShowDisabled(false);
		return creditCardAccountCombo;
	}

	protected void creditCardAccountSelected(ClientAccount selectItem) {
		payFromAccountSelected(selectItem.getID());
		ClientCurrency currency = getCurrency(selectItem.getCurrency());
		if (currency.getID() != 0) {
			currencyWidget.setSelectedCurrencyFactorInWidget(currency,
					transactionDateItem.getDate().getDate());
		} else {
			currencyWidget.setSelectedCurrency(getBaseCurrency());
		}
		super.setCurrency(currency);
		modifyForeignCurrencyTotalWidget();
		updateAmountsFromGUI();
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDateItem, messages.invalidateDate());
		}

		// if (vendorCombo.getSelectedValue() == null)
		// result.addError(
		// vendorCombo,
		// messages.pleaseSelectVendor(
		// Global.get().vendor()));
		result.add(vendorAccountTransactionTable.validateGrid());
		result.add(vendorItemTransactionTable.validateGrid());
		if (creditCardCombo.getSelectedValue() == null) {
			result.addError(creditCardCombo,
					messages.pleaseSelect(messages.payFrom()));
			creditCardCombo.highlight();
		} else {
			// ClientAccount bankAccount = creditCardCombo.getSelectedValue();
			// check if the currency of accounts is valid or not

			// ClientCurrency bankCurrency = getCurrency(bankAccount
			// .getCurrency());
			ClientVendor vendor = vendorCombo.getSelectedValue();
			if (vendor != null) {
				ClientCurrency vendorCurrency = getCompany().getCurrency(
						vendor.getCurrency());
				if (!(vendorCurrency.equals(getBaseCurrency()))
						&& !(vendorCurrency.equals(currency))) {
					result.addError(vendorCombo, messages.selectProperVendor());
				} else {
					vendorCombo.removeStyleName("highlightedFormItem");
				}
			}
		}

		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				if (taxCodeSelect != null
						&& taxCodeSelect.getSelectedValue() == null) {
					result.addError(taxCodeSelect,
							messages.pleaseSelect(messages.taxCode()));
					taxCodeSelect.highlight();
				} else {
					taxCodeSelect.removeStyleName("highlightedFormItem");
				}
			}
		}

		return result;
	}

	private void resetFormView() {
		// vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// refText.setWidth("200px");
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
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setEnabled(!isInViewMode());
			}
			if (transaction.getTransactionItems() != null
					&& !transaction.getTransactionItems().isEmpty()) {
				this.vendorAccountTransactionTable
						.setAllRows(getAccountTransactionItems(transaction
								.getTransactionItems()));
				this.vendorItemTransactionTable
						.setAllRows(getItemTransactionItems(transaction
								.getTransactionItems()));
			}
			// if (vendor != null) {
			// vendorNameSelect.setComboItem(vendor);
			// phoneSelect.setValue(vendor.getPhoneNo());
			// }
			vendorSelected(getCompany().getVendor(transaction.getVendor()));
			vendorCombo.setComboItem(getCompany().getVendor(
					transaction.getVendor()));
			vendorCombo.setEnabled(!isInViewMode());
			transactionDateItem.setValue(transaction.getDate());
			contact = transaction.getContact();
			if (contact != null) {
				contactCombo.setValue(contact.getName());
			}
			transactionDateItem.setValue(transaction.getDate());
			transactionDateItem.setEnabled(!isInViewMode());
			transactionNumber.setValue(transaction.getNumber());
			transactionNumber.setEnabled(!isInViewMode());
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
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(isAmountIncludeTAX());
				}
			}
			if (isTrackClass() && !isClassPerDetailLine()) {
				this.accounterClass = getClassForTransactionItem(transaction
						.getTransactionItems());
				if (accounterClass != null) {
					this.classListCombo.setComboItem(accounterClass);
					classSelected(accounterClass);
				}
			}
			if (transaction.getTransactionItems() != null) {
				if (isTrackDiscounts()) {
					if (!isDiscountPerDetailLine()) {
						this.discountField
								.setPercentage(getdiscount(transaction
										.getTransactionItems()));
					}
				}
			}

			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));

			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			if (transaction.getPayFrom() != 0)
				payFromAccountSelected(transaction.getPayFrom());
			creditCardCombo.setComboItem(getCompany()
					.getAccount(payFromAccount));
			creditCardCombo.setEnabled(!isInViewMode());
			cheqNoText.setEnabled(!isInViewMode());
			cheqNoText.setValue(transaction.getCheckNumber());
			paymentMethodSelected(transaction.getPaymentMethod());
			// payMethSelect.setComboItem(transaction.getPaymentMethod());
			// payMethSelect.setDisabled(isInViewMode());
			cheqNoText.setEnabled(!isInViewMode());
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
		listOfAccounts = creditCardCombo.getAccounts();

		creditCardCombo.initCombo(listOfAccounts);
		creditCardCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		creditCardCombo.setAccounts();
		creditCardCombo.setEnabled(!isInViewMode());

		account = creditCardCombo.getSelectedValue();

		if (account != null)
			creditCardCombo.setComboItem(account);
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
		vendorCombo.initCombo(result);

		if (isInViewMode()) {
			if (selectedVendor != null) {
				vendorCombo.setComboItem(selectedVendor);
				billToaddressSelected(selectedVendor.getSelectedAddress());
				vendorItemTransactionTable.setPayee(selectedVendor);
			}
			addPhonesContactsAndAddress();
		}
		vendorCombo.setEnabled(!isInViewMode());
	}

	protected void addPhonesContactsAndAddress() {
		// Set<Address> allAddress = selectedVendor.getAddress();
		if (selectedVendor != null) {
			addressList = selectedVendor.getAddress();
			initBillToCombo();
			// billToCombo.setDisabled(isEdit);
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
		// phoneSelect.initCombo(phones);

		// ClientVendor cv = FinanceApplication.getCompany().getVendor(
		// creditCardChargeTaken.getVendor());
		if (transaction.getContact() != null)
			contactCombo.setSelected(transaction.getContact().getName());
		if (transaction.getPhone() != null)
			// FIXME check and fix the below code
			phoneSelect.setValue(transaction.getPhone());

		contactCombo.setEnabled(!isInViewMode());
		phoneSelect.setEnabled(!isInViewMode());
		return;
	}

	private void resetElements() {
		selectedVendor = null;
		// transaction = null;
		billingAddress = null;
		addressList = null;
		// billToCombo.setDisabled(isEdit);
		paymentMethod = UIUtils.getpaymentMethodCheckBy_CompanyType(messages
				.check());
		payFromAccount = 0;
		// phoneSelect.setValueMap("");
		setMemoTextAreaItem("");
		// refText.setValue("");
		cheqNoText.setValue("");

	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);

		// setting date
		if (transactionDateItem != null)

			transaction.setDate((transactionDateItem.getValue()).getDate());
		// setting number
		if (transactionNumber != null)
			transaction.setNumber(transactionNumber.getValue().toString());
		ClientVendor vendor = vendorCombo.getSelectedValue();
		if (vendor != null)
			transaction.setVendor(vendor.getID());
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

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);

		// setting phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());

		// Setting payment method

		transaction.setPaymentMethod(messages.creditCard());

		// Setting pay from
		if (creditCardCombo.getSelectedValue() != null)
			payFromAccount = creditCardCombo.getSelectedValue().getID();
		if (payFromAccount != 0)
			transaction.setPayFrom(getCompany().getAccount(payFromAccount)
					.getID());

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
		// transaction.setReference(UIUtils.toStr(refText.getValue()));
		taxCodeSelected(taxCode);
		if (selectedVendor != null) {

			// setting vendor
			transaction.setVendor(selectedVendor.getID());

			// setting contact
			if (contact != null) {
				transaction.setContact(contact);
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

		if (isTrackClass() && !isClassPerDetailLine() && accounterClass != null
				&& transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setAccounterClass(accounterClass.getID());
			}
		}

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	public void createAlterObject() {
		saveOrUpdate(transaction);

	}

	/*
	 * @Override public ValidationResult validate() { ValidationResult result =
	 * super.validate();
	 * 
	 * if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
	 * result.addError(transactionDate, messages.invalidateTransactionDate()); }
	 * 
	 * if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
	 * result.addError(transactionDate, messages.invalidateTransactionDate()); }
	 * 
	 * result.add(vendorForm.validate()); result.add(termsForm.validate()); if
	 * (AccounterValidator.isBlankTransaction(vendorTransactionGrid)) {
	 * result.addError(vendorTransactionGrid, messages.blankTransaction()); }
	 * result.add(vendorTransactionGrid.validateGrid()); return result; }
	 */

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
		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		// payMethSelect.setDisabled(isEdit);
		if (paymentMethod.equals(messages.check())
				|| paymentMethod.equals(messages.cheque())) {
			cheqNoText.setEnabled(!isInViewMode());
		} else {
			cheqNoText.setEnabled(isInViewMode());
		}
		delivDate.setEnabled(!isInViewMode());
		// billToCombo.setDisabled(isEdit);
		vendorCombo.setEnabled(!isInViewMode());
		contactCombo.setEnabled(!isInViewMode());
		phoneSelect.setEnabled(!isInViewMode());
		creditCardCombo.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		vendorAccountTransactionTable.setEnabled(!isInViewMode());
		vendorItemTransactionTable.setEnabled(!isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		discountField.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (taxCodeSelect != null)
			taxCodeSelect.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		super.onEdit();
	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button, messages.Accounts(),
				messages.productOrServiceItem());
	}

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
	protected String getViewTitle() {
		return messages.creditCardExpense();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(isInViewMode());
		setMemoTextAreaItem(transaction.getMemo());
	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		// TODO Auto-generated method stub

	}

	@Override
	public void updateNonEditableItems() {
		if (vendorAccountTransactionTable == null
				|| vendorItemTransactionTable == null) {
			return;
		}
		if (taxCodeSelect != null) {
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
		}
		double lineTotal = vendorAccountTransactionTable.getLineTotal()
				+ vendorItemTransactionTable.getLineTotal();
		double grandTotal = vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal();

		transactionTotalBaseCurrencyText
				.setAmount(getAmountInBaseCurrency(grandTotal));
		foreignCurrencyamountLabel.setAmount(grandTotal);
		netAmount.setAmount(lineTotal);
		if (getPreferences().isTrackPaidTax()) {
			if (transaction.getTransactionItems() != null && !isInViewMode()) {
				transaction.setTransactionItems(vendorAccountTransactionTable
						.getTransactionItems());
				transaction.getTransactionItems().addAll(
						vendorItemTransactionTable.getTransactionItems());
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			vatTotalNonEditableText.setTransaction(transaction);
		}

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	public ClientVendor getSelectedVendor() {
		return selectedVendor;
	}

	/**
	 * @param value
	 */
	protected void addContactToVendor(final ClientContact contact) {
		final ClientVendor selectedVendor = vendorCombo.getSelectedValue();
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
				selectedVendor.setVersion(selectedVendor.getVersion() + 1);
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
		vendorAccountTransactionTable.updateTotals();
		vendorItemTransactionTable.updateTotals();
	}

	@Override
	public void setFocus() {
		this.creditCardCombo.setFocus();
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

		if (taxCodeSelect == null) {
			return;
		}

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
		vendorAccountTransactionTable.updateAmountsFromGUI();
		vendorItemTransactionTable.updateAmountsFromGUI();
		modifyForeignCurrencyTotalWidget();
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

	@Override
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
	protected void classSelected(ClientAccounterClass accounterClass) {
		this.accounterClass = accounterClass;
		if (accounterClass != null) {
			classListCombo.setComboItem(accounterClass);
			vendorAccountTransactionTable
					.setClass(accounterClass.getID(), true);
			vendorItemTransactionTable.setClass(accounterClass.getID(), true);
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
		return false;
	}

	@Override
	public void print() {
		updateTransaction();
		UIUtils.downloadAttachment(transaction.getID(),
				ClientTransaction.TYPE_CREDIT_CARD_EXPENSE);
	}

	@Override
	protected void createButtons() {
		super.createButtons();
		accountTableButton = getAccountAddNewButton();
		itemTableButton = getItemAddNewButton();
		addButton(accountFlowPanel, accountTableButton);
		addButton(itemsFlowPanel, itemTableButton);
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(itemsFlowPanel, itemTableButton);
		removeButton(accountFlowPanel, accountTableButton);
	}
}
