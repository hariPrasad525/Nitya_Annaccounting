//package com.vimukti.accounter.web.client.ui.customers;
//
//import java.util.ArrayList;
//import java.util.List;
//
//import com.google.gwt.dom.client.Style.Unit;
//import com.google.gwt.dom.client.Style.VerticalAlign;
//import com.google.gwt.event.dom.client.ChangeEvent;
//import com.google.gwt.event.dom.client.ChangeHandler;
//import com.google.gwt.event.dom.client.ClickEvent;
//import com.google.gwt.event.dom.client.ClickHandler;
//import com.google.gwt.user.client.ui.HasHorizontalAlignment;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.google.gwt.user.client.ui.Label;
//import com.google.gwt.user.client.ui.StyledPanel;
//import com.vimukti.accounter.web.client.AccounterAsyncCallback;
//import com.vimukti.accounter.web.client.Global;
//import com.vimukti.accounter.web.client.core.AccounterCoreType;
//import com.vimukti.accounter.web.client.core.AddNewButton;
//import com.vimukti.accounter.web.client.core.ClientAddress;
//import com.vimukti.accounter.web.client.core.ClientCompany;
//import com.vimukti.accounter.web.client.core.ClientCurrency;
//import com.vimukti.accounter.web.client.core.ClientCustomer;
//import com.vimukti.accounter.web.client.core.ClientEstimate;
//import com.vimukti.accounter.web.client.core.ClientFinanceDate;
//import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
//import com.vimukti.accounter.web.client.core.ClientPriceLevel;
//import com.vimukti.accounter.web.client.core.ClientSalesPerson;
//import com.vimukti.accounter.web.client.core.ClientShippingTerms;
//import com.vimukti.accounter.web.client.core.ClientTAXCode;
//import com.vimukti.accounter.web.client.core.ClientTransaction;
//import com.vimukti.accounter.web.client.core.ClientTransactionItem;
//import com.vimukti.accounter.web.client.core.IAccounterCore;
//import com.vimukti.accounter.web.client.core.Utility;
//import com.vimukti.accounter.web.client.core.ValidationResult;
//import com.vimukti.accounter.web.client.exception.AccounterException;
//import com.vimukti.accounter.web.client.ui.Accounter;
//import com.vimukti.accounter.web.client.ui.ShipToForm;
//import com.vimukti.accounter.web.client.ui.UIUtils;
//import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
//import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
//import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
//import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
//import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
//import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
//import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
//import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
//import com.vimukti.accounter.web.client.ui.core.DateField;
//import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
//import com.vimukti.accounter.web.client.ui.core.EditMode;
//import com.vimukti.accounter.web.client.ui.edittable.tables.SalesOrderTable;
//import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
//import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
//import com.vimukti.accounter.web.client.ui.forms.FormItem;
//import com.vimukti.accounter.web.client.ui.forms.LabelItem;
//import com.vimukti.accounter.web.client.ui.forms.LinkItem;
//import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
//import com.vimukti.accounter.web.client.ui.forms.TextItem;
//
//public class SalesOrderView extends
//		AbstractCustomerTransactionView<ClientEstimate> {
//	private ShippingTermsCombo shippingTermsCombo;
//	// private PriceLevelCombo priceLevelSelect;
//	private SalesPersonCombo salesPersonCombo;
//	private TAXCodeCombo taxCodeSelect;
//	private PaymentTermsCombo payTermsSelect;
//	private DateField dueDateItem;
//	private LabelItem quoteLabel;
//	private SalesQuoteListDialog dialog;
//	private List<ClientEstimate> selectedEstimates = new ArrayList<ClientEstimate>();
//	protected ClientSalesPerson salesPerson;
//
//	private ArrayList<DynamicForm> listforms;
//	private TextItem customerOrderText;
//	private Label lab1;
//	private final String OPEN = messages.open();
//	private final String COMPLETED = messages.completed();
//	private final String CANCELLED = messages.cancelled();
//	private TextAreaItem billToTextArea;
//	private ShipToForm shipToAddress;
//
//	private final boolean locationTrackingEnabled;
//
//	private SalesOrderTable customerTransactionTable;
//	private AddNewButton itemTableButton;
//	private List<ClientPaymentTerms> paymentTermsList;
//	protected ClientPaymentTerms paymentTerm;
//	private AmountLabel netAmountLabel, vatTotalNonEditableText,
//			balanceDueNonEditableText, paymentsNonEditableText,
//			salesTaxTextNonEditable;
//
//	private Double salesTax;
//
//	public SalesOrderView() {
//		super(ClientTransaction.TYPE_ESTIMATE);
//		locationTrackingEnabled = getCompany().getPreferences()
//				.isLocationTrackingEnabled();
//
//	}
//
//	@Override
//	protected void createControls() {
//		LinkItem emptylabel = new LinkItem();
//		emptylabel.setLinkTitle("");
//		emptylabel.setShowTitle(false);
//
//		lab1 = new Label(messages.salesOrder());
//		lab1.setStyleName("label-title");
//		statusSelect = new SelectCombo(messages.status());
//
//		ArrayList<String> selectComboList = new ArrayList<String>();
//		selectComboList.add(OPEN);
//		selectComboList.add(COMPLETED);
//		selectComboList.add(CANCELLED);
//		statusSelect.initCombo(selectComboList);
//		statusSelect.setComboItem(OPEN);
//		statusSelect
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {
//
//					@Override
//					public void selectedComboBoxItem(String selectItem) {
//						if (statusSelect.getSelectedValue() != null)
//							statusSelect.setComboItem(selectItem);
//
//					}
//				});
//		statusSelect.setRequired(true);
//		statusSelect.setDisabled(isInViewMode());
//
//		transactionDateItem = createTransactionDateItem();
//
//		transactionNumber = createTransactionNumberItem();
//		transactionNumber.setTitle(messages.orderNo());
//		transactionNumber.setWidth(50);
//
//		locationCombo = createLocationCombo();
//		listforms = new ArrayList<DynamicForm>();
//
//		DynamicForm dateNoForm = new DynamicForm();
//		dateNoForm.setNumCols(8);
//		dateNoForm.addStyleName("date-number");
//		dateNoForm.setFields(statusSelect, transactionDateItem);
//
//		StyledPanel datepanel = new StyledPanel();
//		datepanel.setWidth("98%");
//		datepanel.add(dateNoForm);
//		datepanel.setCellHorizontalAlignment(dateNoForm,
//				HasHorizontalAlignment.ALIGN_RIGHT);
//
//		StyledPanel labeldateNoLayout = new StyledPanel();
//		labeldateNoLayout.setWidth("100%");
//		// labeldateNoLayout.add(lab1);
//		labeldateNoLayout.add(datepanel);
//
//		customerCombo = new CustomerCombo(Global.get().customer(), true);
//		customerCombo
//				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {
//
//					@Override
//					public void selectedComboBoxItem(ClientCustomer selectItem) {
//						customerSelected(selectItem);
//
//					}
//
//				});
//
//		customerCombo.setRequired(true);
//		customerCombo.setHelpInformation(true);
//		customerCombo.setDisabled(isInViewMode());
//		// formItems.add(customerCombo);
//
//		customerCombo.setWidth(100);
//		quoteLabel = new LabelItem();
//		quoteLabel.setValue(messages.quotes());
//		quoteLabel.setWidth("100%");
//		quoteLabel.addStyleName("falseHyperlink");
//		quoteLabel.setShowTitle(false);
//		quoteLabel.setDisabled(isInViewMode());
//		quoteLabelListener();
//		contactCombo = createContactComboItem();
//		contactCombo.setWidth(100);
//
//		billToTextArea = new TextAreaItem();
//		billToTextArea.setWidth(100);
//		billToTextArea.setTitle(messages.billTo());
//		billToTextArea.setDisabled(true);
//
//		shipToCombo = createShipToComboItem();
//		shipToAddress = new ShipToForm(null);
//		shipToAddress.addrArea.setDisabled(true);
//		shipToAddress.getCellFormatter().getElement(0, 0).getStyle()
//				.setVerticalAlign(VerticalAlign.TOP);
//		// shipToAddress.getCellFormatter().setWidth(0, 0, "40px");
//		shipToAddress.getCellFormatter().addStyleName(0, 1, "memoFormAlign");
//		shipToAddress.businessSelect.addChangeHandler(new ChangeHandler() {
//
//			@Override
//			public void onChange(ChangeEvent event) {
//				shippingAddress = shipToAddress.getAddress();
//				if (shippingAddress != null)
//					shipToAddress.setAddres(shippingAddress);
//				else
//					shipToAddress.addrArea.setValue("");
//			}
//		});
//		if (isInViewMode())
//			shipToAddress.businessSelect.setDisabled(true);
//
//		phoneSelect = new TextItem(messages.phone());
//		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
//				.getCatagory()));
//		phoneSelect.setWidth(100);
//		phoneSelect.setDisabled(isInViewMode());
//
//		custForm = UIUtils.form(messages.billingAddress());
//		custForm.setNumCols(3);
//		// custForm.setWidth("50%");
//		custForm.setFields(customerCombo, quoteLabel, contactCombo, emptylabel,
//				phoneSelect, emptylabel, billToTextArea, emptylabel);
//		custForm.getCellFormatter().addStyleName(3, 0, "memoFormAlign");
//		// custForm.getCellFormatter().setWidth(0, 1, "180px");
//		// custForm.getCellFormatter().setWidth(0, 0, "225px");
//
//		customerOrderText = new TextItem(messages.payeeOrderNo(Global.get()
//				.customer()));
//		customerOrderText.setWidth(50);
//		customerOrderText.setColSpan(1);
//		customerOrderText.setDisabled(isInViewMode());
//
//		salesPersonCombo = createSalesPersonComboItem();
//
//		// salesPersonCombo = new SalesPersonCombo(FinanceApplication
//		// .constants().salesPerson(), false);
//		// salesPersonCombo.setDisabled(isEdit);
//		// salesPersonCombo
//		// .addSelectionChangeHandler(new
//		// IAccounterComboSelectionChangeHandler<ClientSalesPerson>() {
//		//
//		// public void selectedComboBoxItem(
//		// ClientSalesPerson selectItem) {
//		//
//		// salesPersonSelected(selectItem);
//		//
//		// }
//		//
//		// });
//
//		payTermsSelect = createPaymentTermsSelectItem();
//
//		// payTermsSelect = new PaymentTermsCombo(FinanceApplication
//		// .constants().paymentTerms(), false);
//		// payTermsSelect.setDisabled(isEdit);
//		// payTermsSelect
//		// .addSelectionChangeHandler(new
//		// IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {
//		//
//		// public void selectedComboBoxItem(
//		// ClientPaymentTerms selectItem) {
//		//
//		// paymentTermsSelected(selectItem);
//		//
//		// }
//		//
//		// });
//
//		shippingTermsCombo = createShippingTermsCombo();
//
//		// shippingTermsCombo = new ShippingTermsCombo("Shipping Terms ",
//		// false);
//		// shippingTermsCombo.setDisabled(isEdit);
//		// shippingTermsCombo
//		// .addSelectionChangeHandler(new
//		// IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {
//		//
//		// public void selectedComboBoxItem(
//		// ClientShippingTerms selectItem) {
//		//
//		// shippingTermSelected(selectItem);
//		//
//		// }
//		//
//		// });
//
//		shippingMethodsCombo = createShippingMethodCombo();
//
//		// shippingMethodsCombo = new ShippingMethodsCombo(
//		// FinanceApplication.constants().shippingMethod(),false);
//		// shippingMethodsCombo.setDisabled(isEdit);
//		// shippingMethodsCombo
//		// .addSelectionChangeHandler(new
//		// IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {
//		//
//		// public void selectedComboBoxItem(
//		// ClientShippingMethod selectItem) {
//		// shippingMethodSelected(selectItem);
//		// }
//		//
//		// });
//
//		dueDateItem = createDueDateItem();
//
//		DynamicForm termsForm = new DynamicForm();
//		termsForm.setWidth("100%");
//		termsForm.setIsGroup(true);
//		termsForm.setGroupTitle(messages.terms());
//		termsForm.setNumCols(2);
//		if (locationTrackingEnabled)
//			termsForm.setFields(locationCombo);
//		if (getPreferences().isSalesPersonEnabled()) {
//			termsForm.setFields(transactionNumber, customerOrderText,
//					salesPersonCombo, payTermsSelect, dueDateItem);
//		} else {
//			termsForm.setFields(transactionNumber, customerOrderText,
//					payTermsSelect, dueDateItem);
//		}
//		if (getPreferences().isClassTrackingEnabled()
//				&& getPreferences().isClassOnePerTransaction()) {
//			classListCombo = createAccounterClassListCombo();
//			termsForm.setFields(classListCombo);
//		}
//		if (getPreferences().isDoProductShipMents()) {
//			termsForm.setFields(shippingTermsCombo, shippingMethodsCombo);
//		}
//		// termsForm.getCellFormatter().setWidth(0, 0, "230px");
//
//		Label lab2 = new Label(messages.productAndService());
//
//		memoTextAreaItem = createMemoTextAreaItem();
//
//		// refText = createRefereceText();
//		// refText.setWidth(100);
//
//		DynamicForm prodAndServiceForm1 = new DynamicForm();
//		prodAndServiceForm1.setWidth("100%");
//		prodAndServiceForm1.setNumCols(2);
//		prodAndServiceForm1.setFields(memoTextAreaItem);
//		prodAndServiceForm1.getCellFormatter().addStyleName(0, 0,
//				"memoFormAlign");
//
//		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getCompany()
//				.getPrimaryCurrency());
//		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
//				.getPrimaryCurrency());
//
//		// priceLevelSelect = createPriceLevelSelectItem();
//		taxCodeSelect = createTaxCodeSelectItem();
//
//		paymentsNonEditableText = new AmountLabel(messages.payments());
//		paymentsNonEditableText.setDisabled(true);
//		paymentsNonEditableText.setDefaultValue(""
//				+ UIUtils.getCurrencySymbol() + " 0.00");
//
//		balanceDueNonEditableText = new AmountLabel(messages.balanceDue());
//		balanceDueNonEditableText.setDisabled(true);
//		balanceDueNonEditableText.setDefaultValue(""
//				+ UIUtils.getCurrencySymbol() + " 0.00");
//		salesTaxTextNonEditable = createSalesTaxNonEditableLabel();
//
//		netAmountLabel = createNetAmountLabel();
//		vatinclusiveCheck = getVATInclusiveCheckBox();
//
//		vatTotalNonEditableText = createVATTotalNonEditableLabel();
//
//		customerTransactionTable = new SalesOrderTable(isTrackTax(),
//				isTaxPerDetailLine(), isTrackDiscounts(),
//				isDiscountPerDetailLine(), this) {
//
//			@Override
//			public void updateNonEditableItems() {
//				if (currencyWidget != null) {
//					setCurrencyFactor(currencyWidget.getCurrencyFactor());
//				}
//				SalesOrderView.this.updateNonEditableItems();
//			}
//
//			@Override
//			public boolean isShowPriceWithVat() {
//				return SalesOrderView.this.isShowPriceWithVat();
//			}
//
//			@Override
//			protected boolean isInViewMode() {
//				return SalesOrderView.this.isInViewMode();
//			}
//
//			@Override
//			protected void updateDiscountValues(ClientTransactionItem row) {
//				if (discountField.getAmount() != null
//						&& discountField.getAmount() != 0) {
//					row.setDiscount(discountField.getAmount());
//				}
//				SalesOrderView.this.updateNonEditableItems();
//			}
//
//			@Override
//			public void delete(ClientTransactionItem row) {
//				super.delete(row);
//				List<ClientEstimate> newList = new ArrayList<ClientEstimate>();
//				if (row.getReferringTransactionItem() != 0) {
//					for (ClientEstimate estimate : selectedEstimates) {
//						boolean isExists = false;
//						for (ClientTransactionItem trItem : estimate
//								.getTransactionItems()) {
//							if (trItem.getID() == row
//									.getReferringTransactionItem()) {
//								isExists = true;
//								break;
//							}
//						}
//						if (!isExists) {
//							newList.add(estimate);
//						}
//					}
//					selectedEstimates = newList;
//				}
//			}
//		};
//		customerTransactionTable.setDisabled(isInViewMode());
//		customerTransactionTable.setWidth("99.5%");
//		// customerTransactionTable.setHeight("250px");
//		itemTableButton = new AddNewButton();
//		itemTableButton.setEnabled(!isInViewMode());
//		itemTableButton.addClickHandler(new ClickHandler() {
//
//			@Override
//			public void onClick(ClickEvent event) {
//				addItem();
//			}
//		});
//
//		DynamicForm prodAndServiceForm2 = new DynamicForm();
//		prodAndServiceForm2.setWidth("100%");
//		prodAndServiceForm2.setNumCols(2);
//
//		// TextItem = new TextItem("");
//		// .setVisible(false);
//
//		discountField = getDiscountField();
//
//		DynamicForm taxForm = new DynamicForm();
//		prodAndServiceForm2.setStyleName("boldtext");
//
//		if (isTrackTax()) {
//			prodAndServiceForm2.setFields(netAmountLabel);
//			if (isTaxPerDetailLine()) {
//				prodAndServiceForm2.setFields(vatTotalNonEditableText);
//			} else {
//				taxForm.setFields(taxCodeSelect);
//				prodAndServiceForm2.setFields(salesTaxTextNonEditable);
//			}
//			taxForm.setFields(vatinclusiveCheck);
//		}
//		if (isTrackDiscounts()) {
//			if (!isDiscountPerDetailLine()) {
//				taxForm.setFields(discountField);
//			}
//		}
//		prodAndServiceForm2.setFields(transactionTotalBaseCurrencyText);
//		if (isMultiCurrencyEnabled()) {
//			prodAndServiceForm2.setFields(foreignCurrencyamountLabel);
//		}
//
//		currencyWidget = createCurrencyFactorWidget();
//		StyledPanel prodAndServiceHLay = new StyledPanel();
//		prodAndServiceHLay.setWidth("100%");
//		prodAndServiceHLay.add(prodAndServiceForm1);
//		prodAndServiceHLay.add(taxForm);
//		prodAndServiceHLay.add(prodAndServiceForm2);
//		if (isTaxPerDetailLine()) {
//			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "30%");
//		} else
//			prodAndServiceHLay.setCellWidth(prodAndServiceForm2, "");
//		prodAndServiceHLay.setCellHorizontalAlignment(prodAndServiceForm2,
//				ALIGN_RIGHT);
//
//		StyledPanel vpanel = new StyledPanel();
//		vpanel.setWidth("100%");
//		vpanel.setHorizontalAlignment(ALIGN_RIGHT);
//
//		vpanel.add(prodAndServiceHLay);
//
//		StyledPanel leftVLay = new StyledPanel();
//		leftVLay.setHorizontalAlignment(ALIGN_LEFT);
//		// leftVLay.setWidth("100%");
//		leftVLay.add(custForm);
//		if (getPreferences().isDoProductShipMents())
//			leftVLay.add(shipToAddress);
//
//		StyledPanel rightVLay = new StyledPanel();
//		rightVLay.setHorizontalAlignment(ALIGN_LEFT);
//		// rightVLay.setWidth("100%");
//		rightVLay.add(termsForm);
//		if (isMultiCurrencyEnabled()) {
//			rightVLay.add(currencyWidget);
//			rightVLay.setCellHorizontalAlignment(currencyWidget,
//					HasHorizontalAlignment.ALIGN_RIGHT);
//			currencyWidget.setDisabled(isInViewMode());
//		}
//
//		StyledPanel topHLay = new StyledPanel();
//		topHLay.addStyleName("fields-panel");
//		topHLay.setWidth("100%");
//		topHLay.setSpacing(10);
//		topHLay.add(leftVLay);
//		topHLay.add(rightVLay);
//		topHLay.setCellWidth(leftVLay, "50%");
//		topHLay.setCellWidth(rightVLay, "50%");
//		topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);
//
//		StyledPanel mainVLay = new StyledPanel();
//		mainVLay.setSize("100%", "100%");
//		mainVLay.add(lab1);
//		mainVLay.add(voidedPanel);
//		mainVLay.add(labeldateNoLayout);
//		mainVLay.add(topHLay);
//		// mainVLay.add(lab2);
//
//		mainVLay.add(customerTransactionTable);
//		mainVLay.add(itemTableButton);
//		// mainVLay.add(createAddNewButton());
//		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);
//		mainVLay.add(vpanel);
//		mainVLay.getElement().getStyle().setPaddingTop(20, Unit.PX);
//		mainVLay.getElement().getStyle().setPaddingBottom(20, Unit.PX);
//
//		// if (UIUtils.isMSIEBrowser()) {
//		// resetFormView();
//		// termsForm.getCellFormatter().setWidth(0, 1, "230px");
//		// termsForm.setWidth("90%");
//		// }
//		this.add(mainVLay);
//
//		/* Adding dynamic forms in list */
//		listforms.add(dateNoForm);
//		listforms.add(termsForm);
//		listforms.add(prodAndServiceForm1);
//		listforms.add(prodAndServiceForm2);
//
//		settabIndexes();
//		if (isMultiCurrencyEnabled()) {
//			foreignCurrencyamountLabel.hide();
//		}
//
//	}
//
//	private void quoteLabelListener() {
//		if (!isInViewMode()) {
//			quoteLabel.addClickHandler(new ClickHandler() {
//
//				@Override
//				public void onClick(ClickEvent event) {
//					getEstimates();
//				}
//			});
//		}
//	}
//
//	public void resetFormView() {
//		// custForm.getCellFormatter().setWidth(0, 1, "200px");
//		// custForm.setWidth("94%");
//		// shipToAddress.getCellFormatter().setWidth(0, 1, "100");
//		// shipToAddress.getCellFormatter().setWidth(0, 2, "200");
//		// statusSelect.setWidth("150px");
//		// refText.setWidth("200px");
//	}
//
//	private void initDueDate() {
//
//		// if (transactionObject != null) {
//		// ClientInvoice invoice = (ClientInvoice) transactionObject;
//		// if (invoice.getDueDate() != 0) {
//		// dueDateItem.setEnteredDate(new Date(invoice.getDueDate()));
//		// } else if (invoice.getPaymentTerm() != null) {
//		// ClientPaymentTerms terms = FinanceApplication.getCompany()
//		// .getPaymentTerms(invoice.getPaymentTerm());
//		// Date transactionDate = this.transactionDateItem
//		// .getEnteredDate();
//		// Date dueDate = new Date(invoice.getDueDate());
//		// dueDate = Utility.getCalculatedDueDate(transactionDate, terms);
//		// if (dueDate != null) {
//		// dueDateItem.setEnteredDate(dueDate);
//		// }
//		//
//		// }
//		//
//		// } else
//		dueDateItem.setEnteredDate(new ClientFinanceDate());
//
//	}
//
//	private void initCustomers() {
//		List<ClientCustomer> result = getCompany().getActiveCustomers();
//		customerCombo.initCombo(result);
//		customerCombo.setDisabled(isInViewMode());
//
//	}
//
//	@Override
//	protected void initTransactionViewData() {
//		if (transaction == null || transaction.getID() == 0) {
//			setData(new ClientEstimate());
//			initCustomers();
//			ArrayList<ClientPriceLevel> priceLevels = getCompany()
//					.getPriceLevels();
//
//			// priceLevelSelect.initCombo(priceLevels);
//
//			initSalesTaxNonEditableItem();
//			initTransactionTotalNonEditableItem();
//			initMemoAndReference();
//			paymentTermsList = getCompany().getPaymentsTerms();
//			payTermsSelect.initCombo(paymentTermsList);
//			ArrayList<ClientShippingTerms> shippingTerms = getCompany()
//					.getShippingTerms();
//
//			shippingTermsCombo.initCombo(shippingTerms);
//
//			initShippingMethod();
//			initDueDate();
//		} else {
//			if (currencyWidget != null) {
//				if (transaction.getCurrency() > 1) {
//					this.currency = getCompany().getCurrency(
//							transaction.getCurrency());
//				} else {
//					this.currency = getCompany().getPrimaryCurrency();
//				}
//				this.currencyFactor = transaction.getCurrencyFactor();
//				currencyWidget.setSelectedCurrency(this.currency);
//				// currencyWidget.currencyChanged(this.currency);
//				currencyWidget.setCurrencyFactor(transaction
//						.getCurrencyFactor());
//				currencyWidget.setDisabled(isInViewMode());
//			}
//			ClientCompany company = getCompany();
//			this.setCustomer(company.getCustomer(transaction.getCustomer()));
//			// this.billingAddress = transaction.getBillingAddress();
//
//			this.contact = transaction.getContact();
//			this.addressListOfCustomer = getCustomer().getAddress();
//
//			if (billingAddress != null) {
//				billToTextArea.setValue(billingAddress.getAddress1() + "\n"
//						+ billingAddress.getStreet() + "\n"
//						+ billingAddress.getCity() + "\n"
//						+ billingAddress.getStateOrProvinence() + "\n"
//						+ billingAddress.getZipOrPostalCode() + "\n"
//						+ billingAddress.getCountryOrRegion());
//
//			}
//			this.shippingAddress = transaction.getShippingAdress();
//			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
//			if (getCustomer() != null)
//				addresses.addAll(getCustomer().getAddress());
//			shipToAddress.setListOfCustomerAdress(addresses);
//			if (shippingAddress != null) {
//				shipToAddress.businessSelect.setValue(shippingAddress
//						.getAddressTypes().get(shippingAddress.getType()));
//				shipToAddress.setAddres(shippingAddress);
//				ClientAddress add = new ClientAddress();
//				shipToAddress.businessSelect.setValue(add.getAddressTypes()
//						.get(1));
//			}
//
//			// this.priceLevel = company.getPriceLevel(salesOrderToBeEdited
//			// .getPriceLevel());
//			// FIXME in estimate
//			// shippingMethodSelected(company.getShippingMethod(transaction
//			// .getShippingMethod()));
//			this.paymentTerm = company.getPaymentTerms(transaction
//					.getPaymentTerm());
//			// FIXME in estimate
//			// this.shippingTerm = company.getShippingTerms(transaction
//			// .getShippingTerm());
//			if (shippingTerm != null && shippingTermsCombo != null) {
//				shippingTermsCombo.setComboItem(getCompany().getShippingTerms(
//						shippingTerm.getID()));
//				shippingTermsCombo.setDisabled(isInViewMode());
//			}
//			this.transactionItems = transaction.getTransactionItems();
//			// this.taxCode =
//			// getTaxItemGroupForTransactionItems(this.transactionItems);
//
//			customerCombo.setComboItem(this.getCustomer());
//
//			// customerSelected(this.getCustomer());
//			int status = transaction.getStatus();
//			switch (status) {
//			case ClientTransaction.STATUS_OPEN:
//				statusSelect.setComboItem(OPEN);
//				break;
//			case ClientTransaction.STATUS_COMPLETED:
//				statusSelect.setComboItem(COMPLETED);
//				break;
//			case ClientTransaction.STATUS_CANCELLED:
//				statusSelect.setComboItem(CANCELLED);
//			default:
//				break;
//			}
//
//			if (transaction.getPhone() != null)
//				phoneNo = transaction.getPhone();
//			String customerPhone = getCustomer().getPhoneNo();
//
//			if (customerPhone != null && customerPhone.isEmpty())
//				phoneSelect.setValue(phoneNo);
//
//			contactSelected(this.contact);
//
//			phoneSelect.setDisabled(isInViewMode());
//			// billToaddressSelected(this.billingAddress);
//			// shipToAddressSelected(shippingAddress);
//
//			vatTotalNonEditableText.setAmount(transaction.getTotal()
//					- transaction.getNetAmount());
//			// FIXME in estimate
//			// customerOrderText.setValue(transaction.getCustomerOrderNumber());
//			paymentTermsSelected(this.paymentTerm);
//			// priceLevelSelected(this.priceLevel);
//			salesPersonSelected(company.getSalesPerson(transaction
//					.getSalesPerson()));
//			shippingMethodSelected(this.shippingMethod);
//			if (shippingTerm != null && shippingTermsCombo != null) {
//				shippingTermsCombo.setComboItem(getCompany().getShippingTerms(
//						shippingTerm.getID()));
//				shippingTermsCombo.setDisabled(isInViewMode());
//			}
//			taxCodeSelected(this.taxCode);
//			// FIXME in estimate
//			// dueDateItem.setEnteredDate(new ClientFinanceDate(transaction
//			// .getDueDate()));
//
//			memoTextAreaItem.setValue(transaction.getMemo());
//			memoTextAreaItem.setDisabled(isInViewMode());
//			// refText.setValue(salesOrderToBeEdited.getReference());
//
//			if (isTrackTax()) {
//				if (isTaxPerDetailLine()) {
//					netAmountLabel.setAmount(transaction.getNetAmount());
//					vatTotalNonEditableText.setAmount(transaction.getTotal()
//							- transaction.getNetAmount());
//				} else {
//					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
//					if (taxCode != null) {
//						this.taxCodeSelect
//								.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
//					}
//					this.salesTaxTextNonEditable.setAmount(transaction
//							.getTaxTotal());
//					this.transactionTotalBaseCurrencyText
//							.setAmount(getAmountInBaseCurrency(transaction
//									.getTotal()));
//					this.foreignCurrencyamountLabel.setAmount(transaction
//							.getTotal());
//				}
//
//			}
//
//			if (vatinclusiveCheck != null) {
//				vatinclusiveCheck.setValue(isAmountIncludeTAX());
//			}
//			initAccounterClass();
//			// customerTransactionGrid.setRecords(transaction
//			// .getTransactionItems());
//		}
//
//		if (transaction.getTransactionItems() != null) {
//			if (isTrackDiscounts()) {
//				if (!isDiscountPerDetailLine()) {
//					this.discountField.setAmount(getdiscount(transaction
//							.getTransactionItems()));
//				}
//			}
//		}
//
//		superinitTransactionViewData();
//		// vatTotalNonEditableText.setAmount(customerTransactionTable.getTotal());
//		initTransactionNumber();
//		if (locationTrackingEnabled)
//			locationSelected(getCompany()
//					.getLocation(transaction.getLocation()));
//		if (isMultiCurrencyEnabled()) {
//			updateAmountsFromGUI();
//		}
//	}
//
//	private void superinitTransactionViewData() {
//
//		initTransactionNumber();
//
//		initCustomers();
//
//		ArrayList<ClientPriceLevel> priceLevels = getCompany().getPriceLevels();
//
//		// priceLevelSelect.initCombo(priceLevels);
//
//		ArrayList<ClientSalesPerson> salesPersons = getCompany()
//				.getActiveSalesPersons();
//
//		salesPersonCombo.initCombo(salesPersons);
//
//		initSalesTaxNonEditableItem();
//
//		initTransactionTotalNonEditableItem();
//
//		initMemoAndReference();
//
//		initTransactionsItems();
//
//	}
//
//	@Override
//	protected void initSalesTaxNonEditableItem() {
//		if (isInViewMode()) {
//			Double salesTaxAmout = transaction.getTaxTotal();
//			setSalesTax(salesTaxAmout);
//
//		}
//
//	}
//
//	public void setSalesTax(Double salesTax) {
//		if (salesTax == null)
//			salesTax = 0.0D;
//		this.salesTax = salesTax;
//
//		if (salesTaxTextNonEditable != null)
//			salesTaxTextNonEditable.setAmount(salesTax);
//
//	}
//
//	@Override
//	protected void initTransactionTotalNonEditableItem() {
//		if (isInViewMode()) {
//			Double transactionTotal = transaction.getTotal();
//			setTransactionTotal(transactionTotal);
//
//		}
//
//	}
//
//	public void setTransactionTotal(Double transactionTotal) {
//		if (transactionTotal == null)
//			transactionTotal = 0.0D;
//		transactionTotalBaseCurrencyText
//				.setAmount(getAmountInBaseCurrency(transactionTotal));
//		foreignCurrencyamountLabel.setAmount(transactionTotal);
//
//	}
//
//	@Override
//	public void saveAndUpdateView() {
//		updateTransaction();
//
//		super.saveAndUpdateView();
//		transactionTotalBaseCurrencyText
//				.setAmount(getAmountInBaseCurrency(customerTransactionTable
//						.getGrandTotal()));
//		foreignCurrencyamountLabel.setAmount(customerTransactionTable
//				.getGrandTotal());
//
//		saveOrUpdate(transaction);
//	}
//
//	@Override
//	protected void updateTransaction() {
//		super.updateTransaction();
//		if (taxCode != null && transactionItems != null) {
//			for (ClientTransactionItem item : transactionItems) {
//				item.setTaxCode(taxCode.getID());
//			}
//		}
//		if (statusSelect.getSelectedValue().equals(OPEN))
//			transaction.setStatus(ClientTransaction.STATUS_OPEN);
//		else if (statusSelect.getSelectedValue().equals(COMPLETED))
//			transaction.setStatus(ClientTransaction.STATUS_COMPLETED);
//		else if (statusSelect.getSelectedValue().equals(CANCELLED))
//			transaction.setStatus(ClientTransaction.STATUS_CANCELLED);
//		if (getCustomer() != null)
//			transaction.setCustomer(getCustomer().getID());
//		if (contact != null)
//			transaction.setContact(contact);
//		if (phoneSelect.getValue() != null)
//			transaction.setPhone(phoneSelect.getValue().toString());
//		if (billingAddress != null)
//			// transaction.setBillingAddress(billingAddress);
//			if (shippingAddress != null)
//				transaction.setShippingAdress(shippingAddress);
//
//		if (customerOrderText.getValue() != null)
//			// transaction.setCustomerOrderNumber(customerOrderText.getValue()
//			// .toString());
//			if (salesPerson != null)
//				transaction.setSalesPerson(salesPerson.getID());
//		if (paymentTerm != null)
//			transaction.setPaymentTerm(paymentTerm.getID());
//		// if (shippingTerm != null)
//		// transaction.setShippingTerm(shippingTerm.getID());
//		// if (shippingMethod != null)
//		// transaction.setShippingMethod(shippingMethod.getID());
//		// if (dueDateItem.getEnteredDate() != null)
//		// transaction.setDueDate(dueDateItem.getEnteredDate().getDate());
//
//		if (isTrackTax()) {
//			if (isTaxPerDetailLine()) {
//				transaction.setNetAmount(netAmountLabel.getAmount());
//			} else {
//				// if (taxCode != null) {
//				// for (ClientTransactionItem record : customerTransactionTable
//				// .getRecords()) {
//				// record.setTaxItemGroup(taxCode.getID());
//				//
//				// }
//				// }
//				transaction.setTaxTotal(this.salesTax);
//			}
//			setAmountIncludeTAX();
//		}
//
//		if (isTrackDiscounts()) {
//			if (discountField.getAmount() != 0.0 && transactionItems != null) {
//				for (ClientTransactionItem item : transactionItems) {
//					item.setDiscount(discountField.getAmount());
//				}
//			}
//		}
//		transaction.setTotal(foreignCurrencyamountLabel.getAmount());
//
//		transaction.setMemo(getMemoTextAreaItem());
//		// transaction.setReference(getRefText());
//		if (selectedEstimates != null)
//			// transaction.setEstimates(selectedEstimates);
//			if (currency != null)
//				transaction.setCurrency(currency.getID());
//		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
//	}
//
//	@Override
//	protected void customerSelected(final ClientCustomer customer) {
//
//		ClientCurrency currency = getCurrency(customer.getCurrency());
//
//		if (customer != null) {
//			if (this.getCustomer() != null
//					&& this.getCustomer().getID() != customer.getID())
//				customerTransactionTable.resetRecords();
//			selectedEstimates = new ArrayList<ClientEstimate>();
//			this.setCustomer(customer);
//			super.customerSelected(customer);
//			shippingTermSelected(shippingTerm);
//
//			if (this.paymentTerm != null && payTermsSelect != null)
//				payTermsSelect.setComboItem(this.paymentTerm);
//
//			if (this.salesPerson != null && salesPersonCombo != null)
//				salesPersonCombo.setComboItem(this.salesPerson);
//
//			if (this.taxCode != null
//					&& taxCodeSelect != null
//					&& taxCodeSelect.getValue() != ""
//					&& !taxCodeSelect.getName().equalsIgnoreCase(
//							messages.none()))
//				taxCodeSelect.setComboItem(this.taxCode);
//			customerCombo.setComboItem(customer);
//			// if (transactionObject == null)
//			// getEstimates();
//			if (customer.getPhoneNo() != null)
//				phoneSelect.setValue(customer.getPhoneNo());
//			else
//				phoneSelect.setValue("");
//			this.addressListOfCustomer = customer.getAddress();
//			billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
//			if (billingAddress != null) {
//				billToTextArea.setValue(billingAddress.getAddress1() + "\n"
//						+ billingAddress.getStreet() + "\n"
//						+ billingAddress.getCity() + "\n"
//						+ billingAddress.getStateOrProvinence() + "\n"
//						+ billingAddress.getZipOrPostalCode() + "\n"
//						+ billingAddress.getCountryOrRegion());
//
//			} else
//				billToTextArea.setValue("");
//			shippingAddress = getAddress(ClientAddress.TYPE_SHIP_TO);
//			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
//			addresses.addAll(customer.getAddress());
//			shipToAddress.setAddress(addresses);
//		}
//
//		if (currency.getID() != 0) {
//			currencyWidget.setSelectedCurrencyFactorInWidget(currency,
//					transactionDateItem.getDate().getDate());
//		} else {
//			currencyWidget.setSelectedCurrency(getBaseCurrency());
//		}
//
//		if (isMultiCurrencyEnabled()) {
//			super.setCurrency(currency);
//			setCurrencyFactor(currencyWidget.getCurrencyFactor());
//			updateAmountsFromGUI();
//		}
//	}
//
//	private void shippingTermSelected(ClientShippingTerms shippingTerm2) {
//		this.shippingTerm = shippingTerm2;
//		if (shippingTerm != null && shippingTermsCombo != null) {
//			shippingTermsCombo.setComboItem(getCompany().getShippingTerms(
//					shippingTerm.getID()));
//			shippingTermsCombo.setDisabled(isInViewMode());
//		}
//	}
//
//	@Override
//	protected void salesPersonSelected(ClientSalesPerson person) {
//		salesPerson = person;
//		if (salesPerson != null) {
//
//			salesPersonCombo.setComboItem(getCompany().getSalesPerson(
//					salesPerson.getID()));
//
//		}
//		salesPersonCombo.setDisabled(isInViewMode());
//
//	}
//
//	@Override
//	protected void paymentTermsSelected(ClientPaymentTerms paymentTerm) {
//		this.paymentTerm = paymentTerm;
//		if (this.paymentTerm != null && payTermsSelect != null) {
//
//			payTermsSelect.setComboItem(getCompany().getPaymentTerms(
//					paymentTerm.getID()));
//		}
//		ClientFinanceDate transDate = this.transactionDateItem.getEnteredDate();
//
//		if (transDate != null && paymentTerm != null) {
//			ClientFinanceDate dueDate = Utility.getCalculatedDueDate(transDate,
//					paymentTerm);
//			if (dueDate != null) {
//				dueDateItem.setValue(dueDate);
//			}
//		}
//
//	}
//
//	@Override
//	protected void priceLevelSelected(ClientPriceLevel priceLevel) {
//		// this.priceLevel = priceLevel;
//		// if (priceLevel != null && priceLevelSelect != null) {
//		//
//		// priceLevelSelect.setComboItem(FinanceApplication.getCompany()
//		// .getPriceLevel(priceLevel.getID()));
//		//
//		// }
//		// if (transactionObject == null && customerTransactionGrid != null) {
//		// customerTransactionGrid.priceLevelSelected(priceLevel);
//		// customerTransactionGrid.updatePriceLevel();
//		// }
//		// updateNonEditableItems();
//
//	}
//
//	protected DateField createDueDateItem() {
//
//		DateField dateItem = new DateField(messages.dueDate());
//		dateItem.setToolTip(messages.selectDateUntilDue(this.getAction()
//				.getViewName()));
//		dateItem.setTitle(messages.dueDate());
//		dateItem.setColSpan(1);
//
//		dateItem.setDisabled(isInViewMode());
//
//		// formItems.add(dateItem);
//
//		return dateItem;
//
//	}
//
//	@Override
//	public void setTransactionDate(ClientFinanceDate transactionDate) {
//		super.setTransactionDate(transactionDate);
//		if (this.transactionDateItem != null
//				&& this.transactionDateItem.getValue() != null)
//			;
//		updateNonEditableItems();
//	}
//
//	@Override
//	public void updateNonEditableItems() {
//		if (customerTransactionTable == null)
//			return;
//
//		if (isTrackTax()) {
//			netAmountLabel.setAmount(customerTransactionTable.getLineTotal());
//			setSalesTax(customerTransactionTable.getTotalTax());
//			vatTotalNonEditableText.setAmount(customerTransactionTable
//					.getTotalTax());
//		}
//		setTransactionTotal(customerTransactionTable.getGrandTotal());
//		transactionTotalBaseCurrencyText
//				.setAmount(getAmountInBaseCurrency(customerTransactionTable
//						.getGrandTotal()));
//
//		foreignCurrencyamountLabel.setAmount(customerTransactionTable
//				.getGrandTotal());
//		// Double payments = this.paymentsNonEditableText.getAmount();
//		// setBalanceDue((this.transactionTotal - payments));
//	}
//
//	@Override
//	public ValidationResult validate() {
//		ValidationResult result = super.validate();
//		// Validations
//		// 1. formItem validation
//		result.add(FormItem.validate(statusSelect));
//		result.add(super.validate());
//
//		if (!AccounterValidator.isValidDueOrDelivaryDates(
//				this.dueDateItem.getDate(), getTransactionDate())) {
//			result.addError(this.dueDateItem,
//					messages.the() + " " + messages.dueDate() + " " + " "
//							+ messages.cannotbeearlierthantransactiondate());
//		}
//
//		result.add(customerTransactionTable.validateGrid());
//
//		if (isTrackTax()) {
//			if (!isTaxPerDetailLine()) {
//				if (taxCodeSelect != null
//						&& taxCodeSelect.getSelectedValue() == null) {
//					result.addError(taxCodeSelect,
//							messages.pleaseSelect(messages.taxCode()));
//				}
//
//			}
//		}
//		return result;
//	}
//
//	private void getEstimates() {
//		if (this.rpcUtilService == null)
//			return;
//		if (getCustomer() == null) {
//			Accounter.showError(messages.pleaseSelect(Global.get().customer()));
//		} else {
//			this.rpcUtilService.getEstimates(getCustomer().getID(),
//					new AccounterAsyncCallback<ArrayList<ClientEstimate>>() {
//
//						@Override
//						public void onException(AccounterException caught) {
//							// Accounter.showError(FinanceApplication
//							// .constants()
//							// .noQuotesForCustomer()
//							// + " " + customer.getName());
//							return;
//
//						}
//
//						@Override
//						public void onResultSuccess(
//								ArrayList<ClientEstimate> result) {
//							if (result == null) {
//								onFailure(new Exception());
//							} else {
//								showQuotesDialog(result);
//							}
//						}
//
//					});
//
//		}
//	}
//
//	protected void showQuotesDialog(List<ClientEstimate> result) {
//		List<ClientEstimate> filteredList = new ArrayList<ClientEstimate>();
//		filteredList.addAll(result);
//		for (ClientEstimate record : result) {
//			for (ClientEstimate estimate : selectedEstimates) {
//				if (estimate.getID() == record.getID()) {
//					filteredList.remove(record);
//				}
//			}
//		}
//		// TODO need to add when removed item from table which contains refering
//		// transaction item.if sales order is in update mode
//		if (dialog == null) {
//			dialog = new SalesQuoteListDialog(this, filteredList);
//		}
//
//		dialog.setQuoteList(filteredList);
//		dialog.show();
//	}
//
//	public void selectedQuote(ClientEstimate selectedEstimate) {
//		if (selectedEstimate == null)
//			return;
//		for (ClientTransactionItem record : this.customerTransactionTable
//				.getRecords()) {
//			for (ClientTransactionItem salesRecord : selectedEstimate
//					.getTransactionItems())
//				if (record.getReferringTransactionItem() == salesRecord.getID())
//					customerTransactionTable.delete(record);
//		}
//
//		if (selectedEstimates != null)
//			selectedEstimates.add(selectedEstimate);
//
//		List<ClientTransactionItem> itemsList = new ArrayList<ClientTransactionItem>();
//		for (ClientTransactionItem item : selectedEstimate
//				.getTransactionItems()) {
//			if (item.getLineTotal() - item.getInvoiced() <= 0) {
//				continue;
//			}
//			ClientTransactionItem clientItem = new ClientTransactionItem();
//			if (item.getLineTotal() != 0.0) {
//				clientItem.setDescription(item.getDescription());
//				clientItem.setType(item.getType());
//				clientItem.setAccount(item.getAccount());
//				clientItem.setItem(item.getItem());
//				clientItem.setVatItem(item.getVatItem());
//				clientItem.setVATfraction(item.getVATfraction());
//				// clientItem.setVatCode(item.getVatCode());
//				clientItem.setTaxCode(item.getTaxCode());
//				clientItem.setDescription(item.getDescription());
//				clientItem.setQuantity(item.getQuantity());
//				clientItem.setUnitPrice(item.getUnitPrice());
//				clientItem.setDiscount(item.getDiscount());
//				clientItem.setLineTotal(item.getLineTotal()
//						- item.getInvoiced());
//				clientItem.setTaxable(item.isTaxable());
//				clientItem.setReferringTransactionItem(item.getID());
//				itemsList.add(clientItem);
//			}
//		}
//		selectedEstimates.add(selectedEstimate);
//		orderNum = selectedEstimate.getNumber();
//		customerTransactionTable.addRows(itemsList);
//		customerTransactionTable.updateTotals();
//		updateNonEditableItems();
//	}
//
//	@Override
//	public List<DynamicForm> getForms() {
//		return listforms;
//	}
//
//	/**
//	 * call this method to set focus in View
//	 */
//	@Override
//	public void setFocus() {
//		this.customerCombo.setFocus();
//	}
//
//	@Override
//	public void deleteFailed(AccounterException caught) {
//
//	}
//
//	@Override
//	public void deleteSuccess(IAccounterCore result) {
//
//	}
//
//	@Override
//	public void fitToSize(int height, int width) {
//		super.fitToSize(height, width);
//
//	}
//
//	@Override
//	public void onEdit() {
//		if (transaction.getStatus() == ClientTransaction.STATUS_COMPLETED)
//			Accounter.showError(messages.completedSalesOrdercantbeedited());
//		else {
//			AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {
//
//				@Override
//				public void onException(AccounterException caught) {
//					Accounter.showError(caught.getMessage());
//				}
//
//				@Override
//				public void onResultSuccess(Boolean result) {
//					// if (statusSelect.getValue().equals(COMPLETED))
//					// Accounter
//					// .showError("Completed sales order can't be edited.");
//					if (result)
//						enableFormItems();
//				}
//
//			};
//
//			AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
//					.getType());
//			this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
//		}
//	}
//
//	protected void enableFormItems() {
//		setMode(EditMode.EDIT);
//		statusSelect.setDisabled(isInViewMode());
//		transactionDateItem.setDisabled(isInViewMode());
//		transactionNumber.setDisabled(isInViewMode());
//		ClientTransactionItem item = new ClientTransactionItem();
//		if (item.getInvoiced() != null
//				&& !DecimalUtil.isEquals(item.getInvoiced(), 0)) {
//			customerCombo.setDisabled(isInViewMode());
//		} else {
//			customerCombo.setDisabled(true);
//			// FIXME in estimate
//			// if (this.transaction.getBillingAddress() == null) {
//			// this.addressListOfCustomer = customer.getAddress();
//			// billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
//			// if (billingAddress != null) {
//			// billToTextArea.setValue(billingAddress.getAddress1() + "\n"
//			// + billingAddress.getStreet() + "\n"
//			// + billingAddress.getCity() + "\n"
//			// + billingAddress.getStateOrProvinence() + "\n"
//			// + billingAddress.getZipOrPostalCode() + "\n"
//			// + billingAddress.getCountryOrRegion());
//			//
//			// } else
//			// billToTextArea.setValue("");
//			// }
//
//			if (this.transaction.getPhone() == null
//					|| this.transaction.getPhone().isEmpty()) {
//				initPhones(customer);
//			}
//
//			if (this.transaction.getContact() == null) {
//				initContacts(customer);
//			}
//		}
//		taxCodeSelect.setDisabled(isInViewMode());
//		customerOrderText.setDisabled(isInViewMode());
//		customerTransactionTable.setDisabled(false);
//		itemTableButton.setEnabled(!isInViewMode());
//		quoteLabel.setDisabled(isInViewMode());
//
//		quoteLabelListener();
//		if (getPreferences().isSalesPersonEnabled())
//			salesPersonCombo.setDisabled(isInViewMode());
//		shippingTermsCombo.setDisabled(isInViewMode());
//		payTermsSelect.setDisabled(isInViewMode());
//		shippingMethodsCombo.setDisabled(isInViewMode());
//		dueDateItem.setDisabled(isInViewMode());
//		shipToAddress.businessSelect.setDisabled(isInViewMode());
//		memoTextAreaItem.setDisabled(isInViewMode());
//		super.onEdit();
//		if (locationTrackingEnabled)
//			locationCombo.setDisabled(isInViewMode());
//		if (currencyWidget != null) {
//			currencyWidget.setDisabled(isInViewMode());
//		}
//	}
//
//	@Override
//	public void print() {
//
//	}
//
//	@Override
//	public void printPreview() {
//
//	}
//
//	@Override
//	protected void taxCodeSelected(ClientTAXCode taxCode) {
//		this.taxCode = taxCode;
//		if (taxCode != null) {
//
//			taxCodeSelect
//					.setComboItem(getCompany().getTAXCode(taxCode.getID()));
//			customerTransactionTable.setTaxCode(taxCode.getID(), true);
//		} else
//			taxCodeSelect.setValue("");
//		// updateNonEditableItems();
//
//	}
//
//	@Override
//	protected String getViewTitle() {
//		return messages.salesOrder();
//	}
//
//	@Override
//	protected void initMemoAndReference() {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void initTransactionsItems() {
//		if (transaction.getTransactionItems() != null
//				&& !transaction.getTransactionItems().isEmpty())
//			customerTransactionTable.setRecords(transaction
//					.getTransactionItems());
//	}
//
//	@Override
//	protected boolean isBlankTransactionGrid() {
//		return customerTransactionTable.getRecords().isEmpty();
//	}
//
//	@Override
//	protected void addNewData(ClientTransactionItem transactionItem) {
//		customerTransactionTable.add(transactionItem);
//	}
//
//	@Override
//	protected void refreshTransactionGrid() {
//		customerTransactionTable.updateTotals();
//	}
//
//	@Override
//	public List<ClientTransactionItem> getAllTransactionItems() {
//		return customerTransactionTable.getRecords();
//	}
//
//	private void settabIndexes() {
//
//		customerCombo.setTabIndex(1);
//		contactCombo.setTabIndex(2);
//		phoneSelect.setTabIndex(3);
//		billToTextArea.setTabIndex(4);
//		statusSelect.setTabIndex(5);
//		transactionDateItem.setTabIndex(6);
//		transactionNumber.setTabIndex(7);
//		customerOrderText.setTabIndex(8);
//		payTermsSelect.setTabIndex(9);
//		shippingTermsCombo.setTabIndex(10);
//		shippingMethodsCombo.setTabIndex(11);
//		dueDateItem.setTabIndex(12);
//		memoTextAreaItem.setTabIndex(13);
//		// menuButton.setTabIndex(14);
//		if (saveAndCloseButton != null)
//			saveAndCloseButton.setTabIndex(15);
//		if (saveAndNewButton != null)
//			saveAndNewButton.setTabIndex(16);
//		cancelButton.setTabIndex(17);
//	}
//
//	@Override
//	protected void addAccountTransactionItem(ClientTransactionItem item) {
//		// TODO Auto-generated method stub
//
//	}
//
//	@Override
//	protected void addItemTransactionItem(ClientTransactionItem item) {
//		customerTransactionTable.add(item);
//	}
//
//	@Override
//	public void updateAmountsFromGUI() {
//		modifyForeignCurrencyTotalWidget();
//		customerTransactionTable.updateAmountsFromGUI();
//	}
//
//	public void modifyForeignCurrencyTotalWidget() {
//		String formalName = currencyWidget.getSelectedCurrency()
//				.getFormalName();
//		if (currencyWidget.isShowFactorField()) {
//			foreignCurrencyamountLabel.hide();
//		} else {
//			foreignCurrencyamountLabel.show();
//			foreignCurrencyamountLabel.setTitle(messages
//					.currencyTotal(formalName));
//		}
//		netAmountLabel.setTitle(messages.currencyNetAmount(formalName));
//	}
//
//	@Override
//	protected boolean canDelete() {
//		return false;
//	}
//
//	@Override
//	protected boolean canVoid() {
//		return false;
//	}
//
//	@Override
//	protected boolean canRecur() {
//		return false;
//	}
//
//	protected void updateDiscountValues() {
//		if (discountField.getAmount() != null) {
//			customerTransactionTable.setDiscount(discountField.getAmount());
//		} else {
//			discountField.setAmount(0d);
//		}
//	}
// }
