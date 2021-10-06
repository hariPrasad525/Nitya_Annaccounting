package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.AddNewButton;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientBrandingTheme;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientCustomerCreditMemo;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPriceLevel;
import com.nitya.accounter.web.client.core.ClientSalesPerson;
import com.nitya.accounter.web.client.core.ClientShippingTerms;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.GwtDisclosurePanel;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.SalesPersonCombo;
import com.nitya.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.nitya.accounter.web.client.ui.combo.TAXCodeCombo;
import com.nitya.accounter.web.client.ui.core.BrandingThemeComboAction;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TaxItemsForm;
import com.nitya.accounter.web.client.ui.edittable.tables.CustomerAccountTransactionTable;
import com.nitya.accounter.web.client.ui.edittable.tables.CustomerItemTransactionTable;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class CustomerCreditMemoView extends
		AbstractCustomerTransactionView<ClientCustomerCreditMemo> implements
		IPrintableView {
	private ShippingTermsCombo shippingTermsCombo;
	// private PriceLevelCombo priceLevelSelect;
	private TAXCodeCombo taxCodeSelect;
	private SalesPersonCombo salesPersonCombo;
	private Double salesTax = 0.0D;
	private ArrayList<DynamicForm> listforms;
	private TextAreaItem billToTextArea;
	private CustomerAccountTransactionTable customerAccountTransactionTable;
	private CustomerItemTransactionTable customerItemTransactionTable;
	protected ClientPriceLevel priceLevel;
	protected ClientSalesPerson salesPerson;
	private AmountLabel netAmountLabel;
	private TaxItemsForm taxTotalNonEditableText;
	private AddNewButton accountTableButton, itemTableButton;
	private GwtDisclosurePanel accountsDisclosurePanel;
	private GwtDisclosurePanel itemsDisclosurePanel;
	private StyledPanel accountStyledPanel;
	private StyledPanel itemsStyledPanel;

	public CustomerCreditMemoView() {
		super(ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO);
		this.getElement().setId("customercreditmemoview");
	}

	@Override
	protected void createControls() {

		Label lab1 = new Label(messages.customerCreditNote(Global.get()
				.Customer()));
		lab1.setStyleName("label-title");
		listforms = new ArrayList<DynamicForm>();

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							try {
								ClientFinanceDate newDate = transactionDateItem
										.getValue();
								if (newDate != null)
									setTransactionDate(newDate);
							} catch (Exception e) {
								Accounter.showError(messages
										.invalidTransactionDate());
								setTransactionDate(new ClientFinanceDate());
								transactionDateItem
										.setEnteredDate(getTransactionDate());
							}

						}
						updateNonEditableItems();
					}
				});

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(messages.creditNo());
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm("datenumber-panel");
		if (!isTemplate) {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}
		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateNoForm);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(messages.payeeName(Global.get()
				.Customer()));

		contactCombo = createContactComboItem();
		billToTextArea = new TextAreaItem(messages.billTo(), "billToTextArea");
		billToTextArea.setDisabled(true);

		custForm = UIUtils.form(Global.get().customer());
		custForm.add(customerCombo, contactCombo, billToTextArea);
		// custForm.getCellFormatter().getElement(0, 0)
		// .setAttribute(Global.get().constants().width(), "190px");
		// custForm.setWidth("100%");
		custForm.setStyleName("align-form");

		phoneSelect = new TextItem(messages.phone(), "phoneSelect");
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		phoneSelect.setEnabled(!isInViewMode());
		salesPersonCombo = createSalesPersonComboItem();

		DynamicForm phoneForm = UIUtils.form(messages.phoneNumber());
		// phoneForm.setWidth("100%");
		if (locationTrackingEnabled)
			phoneForm.add(locationCombo);
		phoneForm.setStyleName("align-form");
		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			phoneForm.add(classListCombo);
		}
		jobListCombo = createJobListCombo();
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setEnabled(false);
			phoneForm.add(jobListCombo);
		}

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setTitle(messages.reasonForIssue());

		taxCodeSelect = createTaxCodeSelectItem();

		DynamicForm prodAndServiceForm1 = new DynamicForm("prodAndServiceForm1");
		prodAndServiceForm1.add(memoTextAreaItem);

		taxTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableLabel();
		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getCompany()
				.getPrimaryCurrency());
		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		netAmountLabel = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();

		customerAccountTransactionTable = new CustomerAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), isTrackClass(),
				isClassPerDetailLine(), this) {

			@Override
			public void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CustomerCreditMemoView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CustomerCreditMemoView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CustomerCreditMemoView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CustomerCreditMemoView.this.updateNonEditableItems();
			}
		};
		customerAccountTransactionTable.setEnabled(!isInViewMode());

		this.accountStyledPanel = new StyledPanel("accountFlowPanel");
		accountsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		accountsDisclosurePanel.setTitle(messages.ItemizebyAccount());
		accountStyledPanel.add(customerAccountTransactionTable);

		accountsDisclosurePanel.setContent(accountStyledPanel);
		// accountsDisclosurePanel.setOpen(true);

		customerItemTransactionTable = new CustomerItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), isTrackClass(),
				isClassPerDetailLine(), this) {

			@Override
			public void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CustomerCreditMemoView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CustomerCreditMemoView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CustomerCreditMemoView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CustomerCreditMemoView.this.updateNonEditableItems();
			}
		};
		customerItemTransactionTable.setEnabled(!isInViewMode());

		currencyWidget = createCurrencyFactorWidget();
		this.itemsStyledPanel = new StyledPanel("itemsFlowPanel");
		itemsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		itemsDisclosurePanel.setTitle(messages2.consultantName());
		itemsStyledPanel.add(customerItemTransactionTable);

		itemsDisclosurePanel.setContent(itemsStyledPanel);

		DynamicForm form = new DynamicForm("boldtext");
		DynamicForm taxForm = new DynamicForm("taxForm");
		discountField = getDiscountField();

		if (isTrackTax()) {
			form.add(netAmountLabel);
			form.add(taxTotalNonEditableText);
			if (!isTaxPerDetailLine()) {
				taxForm.add(taxCodeSelect);
			}
			taxForm.add(vatinclusiveCheck);
		}

		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				taxForm.add(discountField);
			}
		}
		form.add(transactionTotalBaseCurrencyText);
		if (isMultiCurrencyEnabled()) {
			form.add(foreignCurrencyamountLabel);
		}

		StyledPanel prodAndServiceHLay = new StyledPanel("prodAndServiceHLay");

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(taxForm);
		prodAndServiceHLay.add(form);

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		leftVLay.add(custForm);

		StyledPanel rightVLay = new StyledPanel("rightVLay");
		rightVLay.add(phoneForm);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(voidedPanel);
		mainVLay.add(lab1);
		mainVLay.add(labeldateNoLayout);

		StyledPanel topHLay = getTopLayOut();
		if (topHLay != null) {
			topHLay.add(leftVLay);
			topHLay.add(rightVLay);
			mainVLay.add(topHLay);
		} else {
			mainVLay.add(leftVLay);
			mainVLay.add(rightVLay);
		}

		StyledPanel disPanel = new StyledPanel("dislosurePanel");
		disPanel.add(accountsDisclosurePanel.getPanel());
		disPanel.add(itemsDisclosurePanel.getPanel());

		StyledPanel mainControlsPanel = new StyledPanel("mainControlsPanel");
		mainControlsPanel.add(mainVLay);
		mainControlsPanel.add(disPanel);

		StyledPanel customerCreditMemoViewPanel = new StyledPanel(
				"customerCreditMemoViewPanel");
		customerCreditMemoViewPanel.add(mainControlsPanel);
		customerCreditMemoViewPanel.add(prodAndServiceHLay);
		this.add(customerCreditMemoViewPanel);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(phoneForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(taxForm);

		// settabIndexes();
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}
	}

	protected StyledPanel getTopLayOut() {
		return new StyledPanel("topHLay");
	}

	@Override
	protected void priceLevelSelected(ClientPriceLevel priceLevel) {

		this.priceLevel = priceLevel;
		// if (priceLevel != null && priceLevelSelect != null) {
		//
		// priceLevelSelect.setComboItem(getCompany().getPriceLevel(
		// priceLevel.getID()));
		//
		// }
		//
		if (transaction == null && customerItemTransactionTable != null) {
			customerItemTransactionTable.setPricingLevel(priceLevel);
			// customerAccountTransactionTable.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	protected void salesPersonSelected(ClientSalesPerson person) {
		this.salesPerson = person;
		if (salesPerson != null && salesPersonCombo != null) {

			salesPersonCombo.setComboItem(getCompany().getSalesPerson(
					salesPerson.getID()));

		}

	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(transaction);

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		result.add(customerAccountTransactionTable.validateGrid());
		result.add(customerItemTransactionTable.validateGrid());

		if (isTrackTax()) {
			if (!isTaxPerDetailLine()) {
				if (taxCodeSelect != null
						&& taxCodeSelect.getSelectedValue() == null) {
					result.addError(taxCodeSelect,
							messages.pleaseSelect(messages.taxCode()));
				}

			}
		}
		return result;
	}

	@Override
	public ClientCustomerCreditMemo saveView() {
		ClientCustomerCreditMemo saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		if (taxCode != null && transactionItems != null) {

			for (ClientTransactionItem item : transactionItems) {
				item.setTaxCode(taxCode.getID());

			}
		}
		if (!getPreferences().isClassPerDetailLine() && accounterClass != null
				&& transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setAccounterClass(accounterClass.getID());
			}
		}
		if (customer != null)
			transaction.setCustomer(getCustomer().getID());
		if (contact != null)
			transaction.setContact(contact);
		if (salesPerson != null)
			transaction.setSalesPerson(salesPerson.getID());
		if (phoneNo != null)
			transaction.setPhone(phoneNo);
		if (billingAddress != null)
			transaction.setBillingAddress(billingAddress);
		if (priceLevel != null)
			transaction.setPriceLevel(priceLevel.getID());
		transaction.setMemo(getMemoTextAreaItem());
		// transaction.setReference(getRefText());
		transaction.setNetAmount(netAmountLabel.getAmount());
		if (isTrackTax()) {
			setAmountIncludeTAX();
			transaction.setTaxTotal(this.salesTax);
		}
		if (isTrackDiscounts()) {
			if (discountField.getPercentage() != 0.0
					&& transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getPercentage());
				}
			}
		}
		transaction.setTotal(foreignCurrencyamountLabel.getAmount());

		if (getPreferences().isJobTrackingEnabled()) {
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
			setData(new ClientCustomerCreditMemo());
		} else {
			if (currencyWidget != null) {
				if (transaction.getCurrency() > 1) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				currencyWidget.setSelectedCurrency(this.currency);
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setEnabled(!isInViewMode());
			}
			initTransactionsItems();
			this.setCustomer(getCompany()
					.getCustomer(transaction.getCustomer()));
			customerItemTransactionTable.setPayee(customer);
			customerSelected(this.customer);
			this.billingAddress = transaction.getBillingAddress();
			this.contact = transaction.getContact();
			this.phoneNo = transaction.getPhone();
			phoneSelect.setValue(this.phoneNo);
			this.salesPerson = getCompany().getSalesPerson(
					transaction.getSalesPerson());
			this.priceLevel = getCompany().getPriceLevel(
					transaction.getPriceLevel());
			this.transactionItems = transaction.getTransactionItems();

			if (getCustomer() != null)
				customerCombo.setComboItem(getCustomer());
			// billToaddressSelected(this.billingAddress);
			contactSelected(this.contact);
			priceLevelSelected(this.priceLevel);
			salesPersonSelected(this.salesPerson);
			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(creditToBeEdited.getReference());
			if (billingAddress != null) {
				billToTextArea.setValue(getValidAddress(billingAddress));

			} else
				billToTextArea.setValue("");
			netAmountLabel.setAmount(transaction.getNetAmount());
			if (isTrackTax()) {
				if (!isTaxPerDetailLine()) {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						this.taxCodeSelect
								.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
						taxCodeSelected(taxCode);
					}
				}
				taxTotalNonEditableText.setTransaction(transaction);
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(isAmountIncludeTAX());
				}
			}
			if (isTrackClass()) {
				if (!isClassPerDetailLine()) {
					this.accounterClass = getClassForTransactionItem(this.transactionItems);
					if (accounterClass != null) {
						this.classListCombo.setComboItem(accounterClass);
						classSelected(accounterClass);
					}
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

			netAmountLabel.setAmount(transaction.getNetAmount());
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));

			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			memoTextAreaItem.setDisabled(true);
		}
		superinitTransactionViewData();
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		if (getPreferences().isJobTrackingEnabled()) {
			if (customer != null) {
				jobListCombo.setCustomer(customer);
			}
			jobSelected(Accounter.getCompany().getjob(transaction.getJob()));
			jobListCombo.setEnabled(false);
		}

		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
	}

	private void initCustomers() {
		List<ClientCustomer> result = getCompany().getActiveCustomers();
		customerCombo.initCombo(result);
		customerCombo.setEnabled(!isInViewMode());

	}

	private void superinitTransactionViewData() {

		initCustomers();

		ArrayList<ClientSalesPerson> salesPersons = getCompany()
				.getActiveSalesPersons();

		salesPersonCombo.initCombo(salesPersons);

		ArrayList<ClientPriceLevel> priceLevels = getCompany().getPriceLevels();

		// priceLevelSelect.initCombo(priceLevels);

		initSalesTaxNonEditableItem();

		initTransactionTotalNonEditableItem();

		initMemoAndReference();

		initTransactionsItems();

	}

	@Override
	protected void initMemoAndReference() {
		if (this.transaction != null) {

			ClientCustomerCreditMemo creditMemo = transaction;

			if (creditMemo.getMemo() != null) {

				memoTextAreaItem.setValue(creditMemo.getMemo());
				memoTextAreaItem.setDisabled(isInViewMode());
				// if (creditMemo.getReference() != null)
				// refText.setValue(creditMemo.getReference());

			}

		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {
		if (transaction != null) {
			Double salesTaxAmout = transaction.getTaxTotal();
			if (salesTaxAmout != null) {
				taxTotalNonEditableText.setTransaction(transaction);
			}

		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transaction != null) {
			Double transactionTotal = transaction.getTotal();
			if (transactionTotal != null) {
				transactionTotalBaseCurrencyText
						.setAmount(getAmountInBaseCurrency(transactionTotal));
				foreignCurrencyamountLabel.setAmount(transactionTotal);
			}

		}

	}

	@Override
	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null)
			updateNonEditableItems();
	}

	@Override
	public void updateNonEditableItems() {
		if (customerAccountTransactionTable == null
				|| customerItemTransactionTable == null)
			return;
		ClientTAXCode tax = taxCodeSelect.getSelectedValue();
		if (tax != null) {
			for (ClientTransactionItem item : customerAccountTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
			for (ClientTransactionItem item : customerItemTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
		}
		double total = customerAccountTransactionTable.getGrandTotal()
				+ customerItemTransactionTable.getGrandTotal();
		setTransactionTotal(total);
		if (isTrackTax()) {
			double totalTax = customerAccountTransactionTable.getTotalTax()
					+ customerItemTransactionTable.getTotalTax();

			setSalesTax(totalTax);

			// salesTax = Utility.getCalculatedSalesTax(transactionDateItem
			// .getEnteredDate(), taxableLineTotal, taxItemGroup);
			// setSalesTax(salesTax);
			//
			// this.salesTaxTextNonEditable.setAmount(salesTax != null ?
			// salesTax
			// : 0.0D);
			//
			// this.transactionTotalNonEditableText
			// .setAmount(customerTransactionGrid.getTotal()
			// + this.salesTax);
			double lineTotal = customerAccountTransactionTable.getLineTotal()
					+ customerItemTransactionTable.getLineTotal();
			netAmountLabel.setAmount(lineTotal);
			setSalesTax(total - lineTotal);

		}

		// this.paymentsNonEditableText.setValue(transactionGrid.);

		// this.balanceDueNonEditableText.setValue(""+UIUtils.getCurrencySymbol()
		// +"0.00");

	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		transactionTotalBaseCurrencyText
				.setAmount(getAmountInBaseCurrency(transactionTotal));
		foreignCurrencyamountLabel.setAmount(transactionTotal);
	}

	// @Override
	// public boolean validate() throws InvalidTransactionEntryException,
	// InvalidEntryException {
	// return super.validate();
	//
	// }
	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;

		if (taxTotalNonEditableText != null) {
			if (transaction.getTransactionItems() != null && !isInViewMode()) {
				transaction.setTransactionItems(customerAccountTransactionTable
						.getAllRows());
				transaction.getTransactionItems().addAll(
						customerItemTransactionTable.getAllRows());
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			taxTotalNonEditableText.setTransaction(transaction);
		}

	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (customer == null) {
			return;
		}
		// Job Tracking
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setValue("");
			jobListCombo.setCustomer(customer);
			jobListCombo.setEnabled(true);
		}
		customerItemTransactionTable.setPayee(customer);
		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientCustomerCreditMemo ent = this.transaction;

			if (ent != null && ent.getCustomer() == customer.getID()) {
				this.customerAccountTransactionTable
						.setAllRows(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.customerItemTransactionTable
						.setAllRows(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getCustomer() != customer.getID()) {
				// this.customerAccountTransactionTable.resetRecords();
				this.customerAccountTransactionTable.updateTotals();
				this.customerItemTransactionTable.updateTotals();
			} else if (ent == null)
				this.customerAccountTransactionTable.resetRecords();
		}
		super.customerSelected(customer);
		shippingTermSelected(shippingTerm);

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		if (this.taxCode != null && taxCodeSelect != null
				&& taxCodeSelect.getValue() != ""
				&& !taxCodeSelect.getTitle().equalsIgnoreCase(messages.none()))
			taxCodeSelect.setComboItem(this.taxCode);

		// if (this.priceLevel != null && priceLevelSelect != null)
		// priceLevelSelect.setComboItem(this.priceLevel);

		this.setCustomer(customer);
		if (customer != null) {
			customerCombo.setComboItem(customer);
		}

		this.addressListOfCustomer = customer.getAddress();
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		long currency = customer.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			if (clientCurrency != null) {
				currencyWidget
						.setSelectedCurrencyFactorInWidget(clientCurrency,
								transactionDateItem.getDate().getDate());
			}
		} else {
			ClientCurrency clientCurrency = getCompany().getPrimaryCurrency();
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(getCompany().getCurrency(customer.getCurrency()));
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
	}

	private void shippingTermSelected(ClientShippingTerms shippingTerm2) {
		this.shippingTerm = shippingTerm2;
		if (shippingTerm != null && shippingTermsCombo != null) {
			shippingTermsCombo.setComboItem(getCompany().getShippingTerms(
					shippingTerm.getID()));
			shippingTermsCombo.setEnabled(!isInViewMode());
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
		this.customerCombo.setFocus();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

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
				Accounter.showError(caught.getMessage());
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
		customerCombo.setEnabled(!isInViewMode());
		if (getPreferences().isSalesPersonEnabled())
			salesPersonCombo.setEnabled(!isInViewMode());
		// priceLevelSelect.setDisabled(isInViewMode());
		taxCodeSelect.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		customerAccountTransactionTable.setEnabled(!isInViewMode());
		customerItemTransactionTable.setEnabled(!isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		discountField.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}

		classListCombo.setEnabled(!isInViewMode());
		super.onEdit();
		jobListCombo.setEnabled(!isInViewMode());

	}

	@Override
	public void printPreview() {
		// NOTHING TO DO
	}

	@Override
	public void print() {
		updateTransaction();

		ArrayList<ClientBrandingTheme> themesList = Accounter.getCompany()
				.getBrandingTheme();
		if (themesList.size() > 1) {
			// if there are more than one branding themes, then show branding
			// theme combo box
			new BrandingThemeComboAction().run(transaction, false);
		} else {
			// if there is only one branding theme
			ClientBrandingTheme brandingTheme = themesList.get(0);
			UIUtils.downloadAttachment(transaction.getID(),
					ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO,
					brandingTheme.getID());

		}

	}

	public void resetFormView() {
		// custForm.getCellFormatter().setWidth(0, 1, "200px");
		// custForm.setWidth("75%");
		// refText.setWidth("200px");
		// priceLevelSelect.setWidth("150px");
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect
					.setComboItem(getCompany().getTAXCode(taxCode.getID()));
			customerAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			customerItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();

	}

	@Override
	protected String getViewTitle() {
		return messages.customerCreditNote(Global.get().Customer());
	}

	@Override
	public boolean canPrint() {
		EditMode mode = getMode();
		if (mode == EditMode.CREATE || mode == EditMode.EDIT) {
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
	protected void initTransactionsItems() {
		if (transaction.getTransactionItems() != null) {
			List<ClientTransactionItem> list = getAccountTransactionItems(transaction
					.getTransactionItems());
			if (!list.isEmpty()) {
				customerAccountTransactionTable.setAllRows(list);
			}
			list = getItemTransactionItems(transaction.getTransactionItems());
			if (!list.isEmpty()) {
				customerItemTransactionTable.setAllRows(list);
			}
		}
		accountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
		itemsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ITEM, false));
	}

	@Override
	protected boolean isBlankTransactionGrid() {
		return getAllTransactionItems().isEmpty();
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		customerAccountTransactionTable.add(transactionItem);
	}

	@Override
	protected void refreshTransactionGrid() {
		customerAccountTransactionTable.updateTotals();
		customerItemTransactionTable.updateTotals();
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(customerAccountTransactionTable.getRecords());
		list.addAll(customerItemTransactionTable.getRecords());
		return list;
	}

	public Double getSalesTax() {
		return salesTax;
	}

	private void settabIndexes() {
		customerCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		billToTextArea.setTabIndex(3);
		transactionDateItem.setTabIndex(4);
		transactionNumber.setTabIndex(5);
		memoTextAreaItem.setTabIndex(6);
		// menuButton.setTabIndex(7);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(8);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(9);
		cancelButton.setTabIndex(10);

	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		customerAccountTransactionTable.add(item);
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		customerItemTransactionTable.add(item);
	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		customerAccountTransactionTable.updateAmountsFromGUI();
		customerItemTransactionTable.updateAmountsFromGUI();
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
		netAmountLabel.setTitle(messages.currencyNetAmount(formalName));
		netAmountLabel.setCurrency(currencyWidget.getSelectedCurrency());
	}

	@Override
	protected void updateDiscountValues() {

		if (discountField.getPercentage() != null) {
			customerAccountTransactionTable.setDiscount(discountField
					.getPercentage());
			customerItemTransactionTable.setDiscount(discountField
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
			customerAccountTransactionTable.setClass(accounterClass.getID(),
					true);
			customerItemTransactionTable.setClass(accounterClass.getID(), true);
		} else {
			classListCombo.setValue("");
		}
	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return false;
	}

	@Override
	protected void createButtons() {
		super.createButtons();
		accountTableButton = getAccountAddNewButton();
		itemTableButton = getItemAddNewButton();
		addButton(accountStyledPanel, accountTableButton);
		addButton(itemsStyledPanel, itemTableButton);
	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(itemsStyledPanel, itemTableButton);
		removeButton(accountStyledPanel, accountTableButton);
	}
}
