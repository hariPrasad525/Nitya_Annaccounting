package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Set;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.event.dom.client.FocusEvent;
import com.google.gwt.event.dom.client.FocusHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.AddNewButton;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientBrandingTheme;
import com.nitya.accounter.web.client.core.ClientCompany;
import com.nitya.accounter.web.client.core.ClientCompanyPreferences;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientEmployeeTimeSheet;
import com.nitya.accounter.web.client.core.ClientEstimate;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientInvoice;
import com.nitya.accounter.web.client.core.ClientPaymentTerms;
import com.nitya.accounter.web.client.core.ClientPriceLevel;
import com.nitya.accounter.web.client.core.ClientSalesPerson;
import com.nitya.accounter.web.client.core.ClientShippingTerms;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.AddressDialog;
import com.nitya.accounter.web.client.ui.ShipToForm;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.BrandingThemeCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SalesPersonCombo;
import com.nitya.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.nitya.accounter.web.client.ui.combo.TAXCodeCombo;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.core.BrandingThemeComboAction;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.EmailThemeComboAction;
import com.nitya.accounter.web.client.ui.core.EmailViewAction;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TaxItemsForm;
import com.nitya.accounter.web.client.ui.edittable.TransactionsTree;
import com.nitya.accounter.web.client.ui.edittable.tables.CustomerItemTransactionTable;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.LabelItem;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author Fernandez
 * @modified by B.Srinivasa Rao
 * 
 * 
 */
public class InvoiceView extends AbstractCustomerTransactionView<ClientInvoice>
		implements IPrintableView {
	
	protected ShippingTermsCombo shippingTermsCombo;
	protected TAXCodeCombo taxCodeSelect;
	protected SalesPersonCombo salesPersonCombo;
	private Double salesTax = 0.0D;
	// private final boolean locationTrackingEnabled;
	protected DateField deliveryDate;
	protected ClientSalesPerson salesPerson;
	protected TaxItemsForm vatTotalNonEditableText;
	protected TaxItemsForm salesTaxTextNonEditable;
	protected AmountLabel netAmountLabel, balanceDueNonEditableText;
	protected AmountLabel paymentsNonEditableText;
	List<ClientEstimate> previousEstimates = new ArrayList<ClientEstimate>();
	protected ClientEmployeeTimeSheet employeeTimeSheet;
	// private WarehouseAllocationTable table;
	// private DisclosurePanel inventoryDisclosurePanel;

	// private Double currencyfactor;
	// private ClientCurrency currencyCode;
	TransactionsTree<EstimatesAndSalesOrdersList> transactionsTree;
	protected TextItem phone, email, vatNo;

	public InvoiceView() {
		super(ClientTransaction.TYPE_INVOICE);
		this.getElement().setId("InvoiceView");
	}

	protected BrandingThemeCombo brandingThemeTypeCombo;
	DateField dueDateItem;
	protected Double payments = 0.0;
	protected Double balanceDue = 0.0;
	protected ArrayList<DynamicForm> listforms;
	protected TextAreaItem billToTextArea;
	protected ShipToForm shipToAddress;
	protected TextItem orderNumText;
	StyledPanel hpanel;
	protected LinkedHashMap<Integer, ClientAddress> allAddresses;
	protected Button emailButton;
	protected CustomerItemTransactionTable customerTransactionTable;
	protected ClientPriceLevel priceLevel;
	protected List<ClientPaymentTerms> paymentTermsList;
	protected AddNewButton itemTableButton;
	private StyledPanel mainVLay;

	private void initBalanceDue() {

		if (transaction != null) {

			setBalanceDue(transaction.getBalanceDue());

		}

	}

	@Override
	protected boolean canAddAttachmentPanel() {
		return true;
	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	private void initPayments() {

		if (transaction != null) {

			ClientInvoice invoice = transaction;

			setPayments(invoice.getPayments());
		}

	}

	protected void initPaymentTerms() {
		paymentTermsList = Accounter.getCompany().getPaymentsTerms();

		payTermsSelect.initCombo(paymentTermsList);
		for (ClientPaymentTerms paymentTerm : paymentTermsList) {
			if (paymentTerm.getName().equals("Due on Receipt")) {
				payTermsSelect.addItemThenfireEvent(paymentTerm);
				break;
			}
		}
		this.paymentTerm = payTermsSelect.getSelectedValue();
	}

	private void initDueDate() {
		if (isInViewMode()) {
			ClientInvoice invoice = transaction;
			if (invoice.getDueDate() != 0) {
				dueDateItem.setEnteredDate(new ClientFinanceDate(invoice
						.getDueDate()));
			} else if (invoice.getPaymentTerm() != 0) {
				ClientPaymentTerms terms = getCompany().getPaymentTerms(
						invoice.getPaymentTerm());
				ClientFinanceDate transactionDate = this.transactionDateItem
						.getEnteredDate();
				ClientFinanceDate dueDate = new ClientFinanceDate(
						invoice.getDueDate());
				dueDate = Utility.getCalculatedDueDate(transactionDate, terms);
				if (dueDate != null) {
					dueDateItem.setEnteredDate(dueDate);
				}

			}

		} else
			dueDateItem.setEnteredDate(new ClientFinanceDate());

	}

	@Override
	protected void createControls() {
		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						setDateValues(date);
					}
				});
		transactionNumber = createTransactionNumberItem();

		transactionNumber.setTitle(messages.invoiceNo());
		listforms = new ArrayList<DynamicForm>();
		brandingThemeTypeCombo = new BrandingThemeCombo(
				messages.brandingTheme());

		locationCombo = createLocationCombo();

		allAddresses = new LinkedHashMap<Integer, ClientAddress>();
		customerCombo = createCustomerComboItem(messages.payeeName(Global.get()
				.Customer()));

		contactCombo = createContactComboItem();

		billToTextArea = new TextAreaItem(messages.billTo(), "billToTextArea");

		billToTextArea.setDisabled(isInViewMode());

		billToTextArea.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				new AddressDialog("", "", billToTextArea, messages.billTo(),
						allAddresses);
			}
		});

		billToTextArea.addFocusHandler(new FocusHandler() {

			@Override
			public void onFocus(FocusEvent event) {
				new AddressDialog("", "", billToTextArea, messages.billTo(),
						allAddresses);

			}
		});

		shipToCombo = createShipToComboItem();

		shipToAddress = new ShipToForm(null);

		shipToAddress.businessSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						shippingAddress = shipToAddress.getAddress();
						if (shippingAddress != null)
							shipToAddress.setAddres(shippingAddress);
						else
							shipToAddress.addrArea.setValue("");
					}
				});

		if (transaction != null)
			shipToAddress.setEnabled(false);

		currencyWidget = createCurrencyFactorWidget();

		salesPersonCombo = createSalesPersonComboItem();

		payTermsSelect = createPaymentTermsSelectItem();

		shippingTermsCombo = createShippingTermsCombo();

		shippingMethodsCombo = createShippingMethodCombo();

		dueDateItem = new DateField(messages.dueDate(), "dueDateItem");
		dueDateItem.setToolTip(messages.selectDateUntilDue(this.getAction()
				.getViewName()));
		dueDateItem.setEnteredDate(getTransactionDate());
		dueDateItem.setTitle(messages.dueDate());
		dueDateItem.setEnabled(!isInViewMode());
		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());

		phone = new TextItem(messages.phoneNumber(), "phone");
		email = new TextItem(messages.email(), "email");
		vatNo = new TextItem(messages.taxRegNo(), "vatNo");
		if (getCountryPreferences().isServiceTaxAvailable()) {
			vatNo.setTitle(messages.taxRegNo());
		} else {
			vatNo.setTitle(messages.vatRegistrationNumber());
		}
		phone.setEnabled(false);
		email.setEnabled(false);
		vatNo.setEnabled(false);

		orderNumText = new TextItem(messages.orderNumber(), "orderNumText");
		orderNumText.setWidth(38);
		
		if (transaction != null) {
			orderNumText.setEnabled(false);
		}

		jobListCombo = createJobListCombo();
		classListCombo = createAccounterClassListCombo();
		// termsForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "200px");
		// multi
		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth("400px");

		Button printButton = new Button();

		printButton.setText(messages.print());
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				print();
				// InvoicePrintLayout printLt = new InvoicePrintLayout(
				// (ClientInvoice) getInvoiceObject());
				// printLt.setView(InvoiceView.this);
				// printLt.createTemplate();
				// printLt.print();
			}
		});

		taxCodeSelect = createTaxCodeSelectItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();

		netAmountLabel = createNetAmountLabel();

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getCompany()
				.getPreferences().getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPreferences().getPrimaryCurrency());

		roundAmountinBaseCurrenctText = createTransactionRoundingAmountNonEditableLabel(getCompany()
				.getPreferences().getPrimaryCurrency());

		roundAmountinforeignCurrencyLabel = createRoundingAmountForeignCurrencyAmountLable(getCompany()
				.getPreferences().getPrimaryCurrency());

		vatTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableLabel();

		paymentsNonEditableText = new AmountLabel(messages.nameWithCurrency(
				messages.payments(), getBaseCurrency().getFormalName()),
				getBaseCurrency());
		paymentsNonEditableText.setEnabled(false);
		paymentsNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");
		balanceDueNonEditableText = new AmountLabel(messages.nameWithCurrency(
				messages.balanceDue(), getBaseCurrency().getFormalName()),
				getBaseCurrency());
		balanceDueNonEditableText.setEnabled(false);
		balanceDueNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");

		salesTaxTextNonEditable = new TaxItemsForm();// createSalesTaxNonEditableLabel();

		transactionsTree = new TransactionsTree<EstimatesAndSalesOrdersList>(
				this) {
			HashMap<ClientTransactionItem, ClientTransactionItem> clonesObjs = new HashMap<ClientTransactionItem, ClientTransactionItem>();

			@Override
			public void updateTransactionTotal() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				InvoiceView.this.updateNonEditableItems();
				
			}

			@Override
			public void setTransactionDate(ClientFinanceDate transactionDate) {
				InvoiceView.this.setTransactionDate(transactionDate);
			}

			@Override
			public boolean isinViewMode() {
				return !(InvoiceView.this.isInViewMode());
			}

			@Override
			public void addToMap(ClientTransaction estimate) {
				List<ClientTransactionItem> transactionItems = transaction
						.getTransactionItems();
				List<ClientTransactionItem> estTItems = estimate
						.getTransactionItems();
				for (ClientTransactionItem clientTransactionItem : transactionItems) {
					for (ClientTransactionItem estTItem : estTItems) {
						if (clientTransactionItem.getReferringTransactionItem() == estTItem
								.getID()) {
							clonesObjs.put(estTItem, clientTransactionItem);
							break;
						}
					}
				}
			}

			@Override
			public void onSelectionChange(ClientTransaction result,
					boolean isSelected) {
				List<ClientTransactionItem> transactionItems = result
						.getTransactionItems();
				boolean updateCredit = false;
				if (result instanceof ClientEstimate) {
					ClientEstimate est = (ClientEstimate) result;
					updateCredit = est.getEstimateType() == ClientEstimate.CREDITS
							|| est.getEstimateType() == ClientEstimate.DEPOSIT_EXAPENSES;
				}
				for (ClientTransactionItem transactionItem : transactionItems) {
					if (!isSelected && getPreferences().getSelectedConsultant() != transactionItem.getDisplayName()) {
						// De SELECTED
						ClientTransactionItem cloned = clonesObjs
								.get(transactionItem);
						customerTransactionTable.delete(cloned);
						transaction.getTransactionItems().remove(cloned);
						continue;
					}
					// SELECTED
					ClientTransactionItem tItem = transactionItem.clone();
					tItem.setID(0l);
					if (vatinclusiveCheck != null) {
						tItem.setAmountIncludeTAX(vatinclusiveCheck.getValue());
					}
					tItem.setLineTotal(tItem.getLineTotal()
							/ getCurrencyFactor());
					tItem.setDiscount(tItem.getDiscount() / getCurrencyFactor());
					tItem.setUnitPrice(tItem.getUnitPrice()
							/ getCurrencyFactor());
					tItem.setVATfraction(tItem.getVATfraction()
							/ getCurrencyFactor());
					tItem.setReferringTransactionItem(transactionItem.getID());
					tItem.setTransaction(transaction);
					if (updateCredit) {
						tItem.updateAsCredit();
					}

					if (tItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
						tItem.setType(ClientTransactionItem.TYPE_ITEM);
						tItem.setAccount(0L);
					}

					int emptyRows = 0;
					for (ClientTransactionItem cItem : customerTransactionTable
							.getAllRows()) {
						if (cItem.isEmpty()) {
							customerTransactionTable.delete(cItem);
							emptyRows++;
						}
					}
					customerTransactionTable.add(tItem);
					while (emptyRows > 1) {
						customerTransactionTable.add(customerTransactionTable
								.getEmptyRow());
						emptyRows--;
					}

					clonesObjs.put(transactionItem, tItem);
					ClientTAXCode selectedValue = taxCodeSelect
							.getSelectedValue();
					if (selectedValue == null
							&& !getPreferences().isTaxPerDetailLine()) {
						taxCodeSelected(getCompany().getTAXCode(
								tItem.getTaxCode()));
					}
				}
				customerTransactionTable.updateTotals();
			}
		};
		customerTransactionTable = new CustomerItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), isTrackClass(),
				isClassPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				InvoiceView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return InvoiceView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return InvoiceView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				InvoiceView.this.updateNonEditableItems();
			}
		};
		customerTransactionTable.setEnabled(!isInViewMode());
		brandingThemeTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientBrandingTheme>() {

					@Override
					public void selectedComboBoxItem(
							ClientBrandingTheme selectItem) {
					}
				});

		discountField = getDiscountField();

		/*
		 * if (getCompany().getPreferences().isRegisteredForVAT()) {
		 * 
		 * DynamicForm priceLevelForm = new DynamicForm(); //
		 * priceLevelForm.setCellSpacing(4); priceLevelForm.setWidth("70%"); //
		 * priceLevelForm.setFields(priceLevelSelect);
		 * amountsForm.setFields(netAmountLabel, vatTotalNonEditableText,
		 * transactionTotalNonEditableText, paymentsNonEditableText,
		 * balanceDueNonEditableText);
		 * amountsForm.setStyleName("invoice-total"); //
		 * forms.add(priceLevelForm); // prodAndServiceHLay.add(priceLevelForm);
		 * // prodAndServiceHLay.setCellHorizontalAlignment(priceLevelForm, //
		 * ALIGN_RIGHT); // prodAndServiceHLay.add(amountsForm); //
		 * prodAndServiceHLay.setCellHorizontalAlignment(amountsForm, //
		 * ALIGN_RIGHT); // listforms.add(priceLevelForm);
		 * 
		 * } else {
		 * 
		 * // prodAndServiceForm2.setFields(salesTaxTextNonEditable, //
		 * transactionTotalNonEditableText, paymentsNonEditableText, //
		 * balanceDueNonEditableText, taxCodeSelect, priceLevelSelect);
		 * amountsForm.setNumCols(4); amountsForm.addStyleName("boldtext");
		 * 
		 * if (getCompany().getPreferences().isChargeSalesTax()) {
		 * amountsForm.setFields(taxCodeSelect, salesTaxTextNonEditable,
		 * disabletextbox, transactionTotalNonEditableText, disabletextbox,
		 * paymentsNonEditableText, disabletextbox, balanceDueNonEditableText);
		 * } else { amountsForm.setFields(transactionTotalNonEditableText,
		 * disabletextbox, paymentsNonEditableText, disabletextbox,
		 * balanceDueNonEditableText); }
		 * 
		 * prodAndServiceHLay.add(amountsForm);
		 * prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
		 * ALIGN_RIGHT); }
		 */

		// table = new WarehouseAllocationTable();
		// table.setDesable(isInViewMode());
		//
		// FlowPanel inventoryFlowPanel = new FlowPanel();
		// inventoryDisclosurePanel = new
		// DisclosurePanel("Warehouse Allocation");
		// inventoryFlowPanel.add(table);
		// inventoryDisclosurePanel.setContent(inventoryFlowPanel);
		// inventoryDisclosurePanel.setWidth("100%");
		// if (getCompany().getPreferences().isInventoryEnabled()
		// && getCompany().getPreferences().iswareHouseEnabled())
		// mainVLay.add(inventoryDisclosurePanel);
		// ---Inverntory table-----

		if (isMultiCurrencyEnabled()) {
			if (!isInViewMode()) {
				if (getPreferences().isEnabledRoundingOptions()) {
					roundAmountinforeignCurrencyLabel.hide();
				}
				foreignCurrencyamountLabel.hide();
			}
		}
		// settabIndexes();
		addControls();
	}

	private void addControls() {
		Label lab1 = new Label(messages.invoice());
		lab1.setStyleName("label-title");
		DynamicForm dateNoForm = new DynamicForm("dateNoForm");
		DynamicForm termsForm = new DynamicForm("termsForm");
		DynamicForm prodAndServiceForm1 = new DynamicForm("prodAndServiceForm1");
		DynamicForm prodAndServiceForm2 = new DynamicForm("prodAndServiceForm2");
		DynamicForm vatForm = new DynamicForm("vatForm");
		DynamicForm amountsForm = new DynamicForm("amountsForm");
		custForm = UIUtils.form(Global.get().customer());

		dateNoForm.setStyleName("datenumber-panel");
		if (!isTemplate) {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}

		// ---date--
		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateNoForm);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
		labeldateNoLayout.add(datepanel);

		LabelItem emptylabel = new LabelItem("", "emptylabel");
		emptylabel.setValue("");
		emptylabel.setShowTitle(false);

		custForm.add(customerCombo, emptylabel, contactCombo, emptylabel,
				billToTextArea, emptylabel);

		custForm.setStyleName("align-form");

		if (locationTrackingEnabled) {
			termsForm.add(locationCombo);
		}

		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setEnabled(false);
			termsForm.add(jobListCombo);
		}
		if (getPreferences().isSalesPersonEnabled()) {
			if (isTemplate) {
				termsForm.add(salesPersonCombo, payTermsSelect, orderNumText,
						phone, email);
			} else {
				termsForm.add(salesPersonCombo, payTermsSelect, dueDateItem,
						orderNumText, phone, email);
			}

			if (getPreferences().isDoProductShipMents())
				termsForm.add(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
		} else {
			if (isTemplate) {
				termsForm.add(payTermsSelect, orderNumText, phone, email);
			} else {
				termsForm.add(payTermsSelect, dueDateItem, orderNumText, phone,
						email);
			}
			if (getPreferences().isDoProductShipMents())
				termsForm.add(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);

		}
		if (getPreferences().isTrackTax()) {
			if (getCountryPreferences().isServiceTaxAvailable()
					|| getCountryPreferences().isVatAvailable()) {
				termsForm.add(vatNo);
			}
		}
		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			termsForm.add(classListCombo);
		}

		termsForm.setStyleName("align-form");

		prodAndServiceForm1.add(memoTextAreaItem);

		StyledPanel prodAndServiceHLay = new StyledPanel("prodAndServiceHLay");

		listforms.add(dateNoForm);
		listforms.add(termsForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(prodAndServiceForm2);

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(prodAndServiceForm2);

		StyledPanel nonEditablePanel = new StyledPanel("prodAndServiceHLay");

		if (isTrackTax()) {
			amountsForm.add(netAmountLabel);
			nonEditablePanel.add(amountsForm);
			if (!isTaxPerDetailLine()) {
				vatForm.add(taxCodeSelect);
				nonEditablePanel.add(salesTaxTextNonEditable);

			} else {
				nonEditablePanel.add(vatTotalNonEditableText);
			}
			vatForm.add(vatinclusiveCheck);
		}
		prodAndServiceHLay.add(vatForm);
		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				vatForm.add(discountField);
				prodAndServiceHLay.add(vatForm);
			}
		}

		DynamicForm totalForm = new DynamicForm("totalForm");

		DynamicForm roundingForm = new DynamicForm("roundingForm");
		// Check Enable Round Options Or Not
		if (getPreferences().isEnabledRoundingOptions()) {
			roundingForm.add(roundAmountinBaseCurrenctText);
		}
		totalForm.add(transactionTotalBaseCurrencyText);
		if (isMultiCurrencyEnabled()) {
			if (getPreferences().isEnabledRoundingOptions()) {
				roundingForm.add(roundAmountinforeignCurrencyLabel);
			}
			totalForm.add(foreignCurrencyamountLabel);
		}
		if (isInViewMode()) {
			totalForm.add(paymentsNonEditableText, balanceDueNonEditableText);
		}
		nonEditablePanel.add(roundingForm);
		nonEditablePanel.add(totalForm);
		nonEditablePanel.setStyleName("boldtext");
		prodAndServiceHLay.add(nonEditablePanel);

		StyledPanel panel = new StyledPanel("panel");

		StyledPanel panel11 = new StyledPanel("panel11");
		panel11.add(panel);
		panel11.add(prodAndServiceHLay);

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		leftVLay.add(custForm);
		if (getCompany().getPreferences().isDoProductShipMents())
			leftVLay.add(shipToAddress);

		StyledPanel rightVLay = new StyledPanel("rightVLay");
		rightVLay.add(termsForm);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}

		this.mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		mainVLay.add(labeldateNoLayout);
		StyledPanel topHLay = getTopLay();
		if (topHLay != null) {
			mainVLay.add(topHLay);
			topHLay.add(leftVLay);
			topHLay.add(rightVLay);
		} else {
			mainVLay.add(leftVLay);
			mainVLay.add(rightVLay);
		}
		mainVLay.add(transactionsTree);
		StyledPanel tableContainer = new StyledPanel("tableContainer");
		Label itemTableTitle =  new Label(messages.ItemizebyProductService());
		if(Global.get().preferences().getIndustryType() == 33)
		{
			 itemTableTitle.setText(messages2.consultantName());
		}
		itemTableTitle.setStyleName("editTableTitle");
		tableContainer.add(itemTableTitle);
		tableContainer.add(customerTransactionTable);
		itemTableButton = getItemAddNewButton();
		addButton(tableContainer, itemTableButton);
		mainVLay.add(tableContainer);

		mainVLay.add(panel11);

		if (UIUtils.isMSIEBrowser()) {
			resetFromView();
		}

		getButtonBar().addTo(mainVLay);
		if (transactionAttachmentPanel != null) {
			mainVLay.add(transactionAttachmentPanel);
		}
		super.insert(mainVLay, 1);

	}

	protected StyledPanel getTopLay() {
		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("fields-panel");
		return topHLay;
	}

	@Override
	protected void createButtons() {
		//if (!(getCompany().isPaid())) {
			setEmailButtonAllowed(true);
		//}
		super.createButtons();

		if (getCompany().isPaid()
				&& isInViewMode()
				&& (data != null && !data.isTemplate() && data.getSaveStatus() != ClientTransaction.STATUS_DRAFT)) {
			emailButton = new Button(messages.email());
			emailButton.getElement().setAttribute("data-icon", "mail");
			addButton(emailButton);
			emailButton.addClickHandler(new ClickHandler() {

				@Override
				public void onClick(ClickEvent event) {
					// ActionFactory.getEmailViewAction().run(transaction,
					// false);
					ArrayList<ClientBrandingTheme> themesList = Accounter
							.getCompany().getBrandingTheme();

					if (themesList.size() > 1) {
						// if there are more than one branding themes, then
						// show
						// branding
						// theme dialog box
						new EmailThemeComboAction().run(transaction, false);
					} else {
						new EmailViewAction().run(transaction, themesList
								.get(0).getID(), false);
					}
				}
			});
		}

	}

	@Override
	protected void clearButtons() {
		super.clearButtons();
		removeButton(mainVLay, itemTableButton);
	}

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button, messages.productOrServiceItem());

	}

	protected void setDateValues(ClientFinanceDate date) {
		if (date != null) {
			deliveryDate.setEnteredDate(date);
			dueDateItem.setValue(date);
			setTransactionDate(date);
			calculateDatesforPayterm(date);
			updateNonEditableItems();
		}

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
		if (this.transaction == null || customerTransactionTable != null) {
			customerTransactionTable.setPricingLevel(priceLevel);
			// customerTransactionTable.updatePriceLevel();
		}
		updateNonEditableItems();

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
		if (customerTransactionTable == null)
			return;			
		ClientTAXCode tax = taxCodeSelect.getSelectedValue();
		if (tax != null) {
			for (ClientTransactionItem item : customerTransactionTable
					.getRecords()) {
				item.setTaxCode(tax.getID());
			}
		}

		if (isTrackTax()) {
			setSalesTax(customerTransactionTable.getTotalTax());
			List<ClientTransaction> selectedRecords = transactionsTree
					.getSelectedRecords();
			if (!isInViewMode()) {
				List<ClientEstimate> estimates = new ArrayList<ClientEstimate>();
				for (ClientTransaction clientTransaction : selectedRecords) {
					estimates.add((ClientEstimate) clientTransaction);
				}
				transaction.setEstimates(estimates);
				transaction.setTransactionItems(customerTransactionTable
						.getTransactionItems());
				
			}
			if (currency != null) {
				transaction.setCurrency(currency.getID());
			}
			vatTotalNonEditableText.setTransaction(transaction);
			salesTaxTextNonEditable.setTransaction(transaction);
		}
		netAmountLabel.setAmount(customerTransactionTable.getLineTotal());
		double total = customerTransactionTable.getGrandTotal();

		if (getPreferences().isEnabledRoundingOptions()) {

			double round = round(getPreferences().getRoundingMethod(), total,
					getPreferences().getRoundingLimit());
			if (round == 0.0) {
				setTransactionTotal(customerTransactionTable.getGrandTotal());
				roundAmountinBaseCurrenctText.setAmount(0.0);
				roundAmountinforeignCurrencyLabel.setAmount(0.0);
			} else {
				if (roundAmountinBaseCurrenctText != null) {
					roundAmountinBaseCurrenctText
							.setAmount(getAmountInBaseCurrency(round - total));
					roundAmountinforeignCurrencyLabel.setAmount(round - total);
				}

				setTransactionTotal(round);
			}
		} else {
			setTransactionTotal(customerTransactionTable.getGrandTotal());
		}

		// Double payments =
		// getAmountInBaseCurrency(this.paymentsNonEditableText
		// .getAmount());

		// if (transaction != null) {
		// payments = this.transactionTotal < payments ? this.transactionTotal
		// : payments;
		// setPayments(payments);
		// }
		// setBalanceDue((this.transactionTotal - payments));
	}

	@Override
	protected void customerSelected(final ClientCustomer customer) {
		ClientCurrency currency = getCurrency(customer.getCurrency());
		customerTransactionTable.setPayee(customer);
		if (this.getCustomer() != null && !this.getCustomer().equals(customer)
				&& transaction.getID() == 0) {
			customerTransactionTable.resetRecords();
			transaction.setTransactionItems(customerTransactionTable
					.getRecords());
			if (taxCodeSelect.getSelectedValue() != null) {
				customerTransactionTable.setTaxCode(taxCodeSelect
						.getSelectedValue().getID(), true);
			}
			vatTotalNonEditableText.setTransaction(transaction);
			salesTaxTextNonEditable.setTransaction(transaction);
		}
		// Job Tracking
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setValue("");
			jobListCombo.setCustomer(customer);
			jobListCombo.setEnabled(true);
		}
		phone.setValue(customer.getPhoneNo());
		email.setValue(customer.getEmail());
		if (getCountryPreferences().isVatAvailable()) {
			vatNo.setValue(customer.getVATRegistrationNumber());
		} else {
			vatNo.setValue(customer.getServiceTaxRegistrationNumber());
		}

		this.setCustomer(customer);
		super.customerSelected(customer);
		shippingTermSelected(shippingTerm);

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		if (customer != null && customerCombo != null) {
			customerCombo.setComboItem(customer);
		}
		this.addressListOfCustomer = customer.getAddress();
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {

			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		addresses.addAll(customer.getAddress());
		shipToAddress.setAddress(addresses);

		allAddresses = new LinkedHashMap<Integer, ClientAddress>();
		if (addressListOfCustomer != null) {
			Iterator it = addressListOfCustomer.iterator();
			while (it.hasNext()) {
				ClientAddress add = (ClientAddress) it.next();

				allAddresses.put(add.getType(), add);
			}
		}

		currencyWidget.setSelectedCurrencyFactorInWidget(currency,
				transactionDateItem.getDate().getDate());
		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
		transaction.setEstimates(new ArrayList<ClientEstimate>());
		getEstimatesAndSalesOrder();
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
	protected void salesPersonSelected(ClientSalesPerson person) {
		this.salesPerson = person;
		if (salesPerson != null) {
			salesPersonCombo.setComboItem(getCompany().getSalesPerson(
					salesPerson.getID()));

		}
		salesPersonCombo.setEnabled(!isInViewMode());
	}

	@Override
	protected void initTransactionViewData() {
		ClientCompany company = Accounter.getCompany();
		initPaymentTerms();
		if (transaction == null) {
			setData(new ClientInvoice());
		} else {
			if (isMultiCurrencyEnabled()) {
				if (transaction.getCurrency() > 0) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPreferences()
							.getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				if (this.currency != null) {
					currencyWidget.setSelectedCurrency(this.currency);
				}
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setEnabled(!isInViewMode());
			}
			this.setCustomer(company.getCustomer(transaction.getCustomer()));
			if (customer != null) {
				phone.setValue(customer.getPhoneNo());
				email.setValue(customer.getEmail());
				if (getCountryPreferences().isVatAvailable()) {
					vatNo.setValue(customer.getVATRegistrationNumber());
				} else {
					vatNo.setValue(customer.getServiceTaxRegistrationNumber());
				}
				customerTransactionTable.setPayee(customer);
			}
			this.contact = transaction.getContact();

			if (transaction.getPhone() != null)
				this.phoneNo = transaction.getPhone();
			this.billingAddress = transaction.getBillingAddress();
			this.shippingAddress = transaction.getShippingAdress();
			this.transactionItems = transaction.getTransactionItems();
			this.priceLevel = company
					.getPriceLevel(transaction.getPriceLevel());
			this.payments = transaction.getPayments();
			this.salesPerson = company.getSalesPerson(transaction
					.getSalesPerson());
			this.shippingMethod = company.getShippingMethod(transaction
					.getShippingMethod());
			this.paymentTerm = company.getPaymentTerms(transaction
					.getPaymentTerm());
			this.shippingTerm = company.getShippingTerms(transaction
					.getShippingTerm());
			initTransactionsItems();
			previousEstimates = transaction.getEstimates();
			this.orderNumText
					.setValue(transaction.getOrderNum() != null ? transaction
							.getOrderNum() : "");
			if (getCustomer() != null && customerCombo != null) {
				customerCombo.setComboItem(getCustomer());
				if (!data.isTemplate()) {
					getEstimatesAndSalesOrder();
				}
			}

			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			if (getCustomer() != null)
				addresses.addAll(getCustomer().getAddress());

			Iterator<ClientAddress> it = addresses.iterator();
			while (it.hasNext()) {
				ClientAddress add = it.next();

				allAddresses.put(add.getType(), add);
			}
			shipToAddress.setListOfCustomerAdress(addresses);
			if (shippingAddress != null) {
				shipToAddress.businessSelect.setValue(UIUtils
						.getAddressesTypes(shippingAddress.getType()));
				shipToAddress.setAddres(shippingAddress);
			}
			if (getCustomer() != null) {
				this.addressListOfCustomer = getCustomer().getAddress();
			}

			if (billingAddress != null) {

				billToTextArea.setValue(getValidAddress(billingAddress));

			} else
				billToTextArea.setValue("");
			contactSelected(this.contact);
			paymentTermsSelected(this.paymentTerm);
			if (priceLevel != null) {
				priceLevelSelected(this.priceLevel);
			}
			salesPersonSelected(this.salesPerson);
			shippingMethodSelected(this.shippingMethod);
			if (shippingTerm != null && shippingTermsCombo != null) {
				shippingTermsCombo.setComboItem(getCompany().getShippingTerms(
						shippingTerm.getID()));

				shippingTermsCombo.setEnabled(!isInViewMode());
			}
			if (transaction.getMemo() != null)
				memoTextAreaItem.setValue(transaction.getMemo());
			memoTextAreaItem.setDisabled(isInViewMode());

			if (transaction.getDeliverydate() != 0)
				this.deliveryDate.setValue(new ClientFinanceDate(transaction
						.getDeliverydate()));
			this.dueDateItem
					.setValue(transaction.getDueDate() != 0 ? new ClientFinanceDate(
							transaction.getDueDate()) : getTransactionDate());
			netAmountLabel.setAmount(transaction.getNetAmount());
			if (isTrackTax()) {
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(isAmountIncludeTAX());
				}
				if (isTaxPerDetailLine()) {
					vatTotalNonEditableText.setTransaction(transaction);
				} else {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						this.taxCodeSelect.setComboItem(taxCode);
						taxCodeSelected(taxCode);
					} else {
						if (getCustomer() != null) {
							this.taxCode = getCompany().getTAXCode(
									getCustomer().getTAXCode());
							if (taxCode != null) {
								this.taxCodeSelect.setComboItem(taxCode);
								taxCodeSelected(taxCode);
							}
						}
					}
					this.salesTaxTextNonEditable.setTransaction(transaction);

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

			if (locationTrackingEnabled)
				locationSelected(company.getLocation(transaction.getLocation()));

			if (getPreferences().isJobTrackingEnabled()) {
				if (customer != null) {
					jobListCombo.setCustomer(customer);
				}
				jobSelected(company.getjob(transaction.getJob()));
			}
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());
			// Init Rounding Amount
			if (getPreferences().isEnabledRoundingOptions()) {
				roundAmountinBaseCurrenctText
						.setAmount(getAmountInBaseCurrency(transaction
								.getRoundingTotal()));
				roundAmountinforeignCurrencyLabel.setAmount(transaction
						.getRoundingTotal());
			}
			paymentsNonEditableText.setAmount(transaction.getPayments());
			balanceDueNonEditableText.setAmount(transaction.getBalanceDue());
			// memoTextAreaItem.setDisabled(true);

		}

		superinitTransactionViewData();

		ArrayList<ClientShippingTerms> shippingTerms = getCompany()
				.getShippingTerms();

		shippingTermsCombo.initCombo(shippingTerms);

		initShippingMethod();
		initDueDate();
		initPayments();
		initBalanceDue();

		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
		if (customerCombo != null && customerCombo.getSelectedValue() != null
				&& !isInViewMode()) {
			if (contactCombo != null) {
				contactCombo.setEnabled(true);
			}
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

		initTransactionsItems();

		initSalesTaxNonEditableItem();

		initTransactionTotalNonEditableItem();

		initMemoAndReference();

	}

	@Override
	public void initTransactionsItems() {
		if (transaction.getTransactionItems() != null
				&& !transaction.getTransactionItems().isEmpty()) {
			// ArrayList<ClientTransactionItem> list = new
			// ArrayList<ClientTransactionItem>();
			// for (ClientTransactionItem item :
			// transaction.getTransactionItems()) {
			// // We should exclude those which come from quote/charge/credit
			// if (item.getReferringTransactionItem() == 0) {
			// list.add(item);
			// }
			// }
			customerTransactionTable.setAllRows(transaction
					.getTransactionItems());
		}
	}

	@Override
	protected void shipToAddressSelected(ClientAddress selectItem) {
		this.shippingAddress = selectItem;
		if (this.shippingAddress != null && shipToAddress != null)
			shipToCombo.setComboItem(this.shippingAddress);
	}

	@Override
	protected void initSalesTaxNonEditableItem() {

		if (transaction != null) {
			Double salesTaxAmout = transaction.getTaxTotal();
			setSalesTax(salesTaxAmout);

		}

	}

	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;

		if ((transaction.getTransactionItems() != null && transaction
				.getTransactionItems().isEmpty()) && !isInViewMode())
			transaction.setTransactionItems(customerTransactionTable
					.getAllRows());
		if (currency != null) {
			transaction.setCurrency(currency.getID());
		}
		if (salesTaxTextNonEditable != null) {
			salesTaxTextNonEditable.setTransaction(transaction);
		}
		if (vatTotalNonEditableText != null) {
			vatTotalNonEditableText.setTransaction(transaction);
		}

	}

	@Override
	protected void initTransactionTotalNonEditableItem() {
		if (transaction != null) {
			Double transactionTotal = transaction.getTotal();
			setTransactionTotal(transactionTotal);
			this.roundAmountinBaseCurrenctText
					.setAmount(getAmountInBaseCurrency(transaction
							.getRoundingTotal()));
			this.roundAmountinforeignCurrencyLabel.setAmount(transaction
					.getRoundingTotal());
		}
	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		if (transactionTotalBaseCurrencyText != null) {
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transactionTotal));
			foreignCurrencyamountLabel.setAmount(transactionTotal);
		}

	}

	@Override
	protected void initMemoAndReference() {
		if (this.transaction != null) {

			ClientInvoice invoice = transaction;

			if (invoice.getMemo() != null) {
				memoTextAreaItem.setValue(invoice.getMemo());
			}

		}

	}

	@Override
	protected void paymentTermsSelected(ClientPaymentTerms paymentTerm) {
		if (paymentTerm != null) {
			this.paymentTerm = paymentTerm;
		}
		if (this.paymentTerm != null && payTermsSelect != null) {

			payTermsSelect.setComboItem(getCompany().getPaymentTerms(
					this.paymentTerm.getID()));
		}
		ClientFinanceDate transDate = this.transactionDateItem.getEnteredDate();
		calculateDatesforPayterm(transDate);
	}

	private void calculateDatesforPayterm(ClientFinanceDate transDate) {
		if (transDate != null && this.paymentTerm != null) {
			ClientFinanceDate dueDate = Utility.getCalculatedDueDate(transDate,
					this.paymentTerm);
			if (dueDate != null) {
				dueDateItem.setValue(dueDate);
			}
		}
	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();
		// No Need to update Customer Object separately It will be automatically
		// updated.
		// saveOrUpdate(getCustomer());
		previousEstimates.clear();
//		employeeTimeSheet.setBilled(true);
//		saveOrUpdate(employeeTimeSheet);
		employeeTimeSheet = new ClientEmployeeTimeSheet();
		employeeTimeSheet.setId(Global.get().preferences().getEmpTimeSheetId());
		employeeTimeSheet.setBilled(true);
//		Window.alert(employeeTimeSheet.getId()+"");	
//		if(employeeTimeSheet.getId() > 0 )
//		{
//			Accounter.createGETService().updateTimeSheetBilling(employeeTimeSheet.getId(), new AsyncCallback<Boolean>() {
//
//				@Override
//				public void onFailure(Throwable caught) {
//					// TODO Auto-generated method stub
//					
//				}
//
//				@Override
//				public void onSuccess(Boolean result) {
//					Window.alert("success");
//					
//				}
//			});
//		}
//		saveOrUpdate(employeeTimeSheet);
		saveOrUpdate(transaction);
		
	}
	
	@Override
	public void saveAndEmailView() {
	
	
		updateTransaction();
//		saveOrUpdate(transaction);
//		if(employeeTimeSheet.getId() > 0 )
//		{
//			employeeTimeSheet.setBilled(true);
//		}
//		
//		saveOrUpdate(employeeTimeSheet);
		
		saveOrUpdate(transaction);
		ArrayList<ClientBrandingTheme> themesList = Accounter
				.getCompany().getBrandingTheme();

		if (themesList.size() > 1) {
			// if there are more than one branding themes, then
			// show
			// branding
			// theme dialog box
			new EmailThemeComboAction().run(transaction, false);
		} else {
			new EmailViewAction().run(transaction, themesList
					.get(0).getID(), false);
		}	
		new EmailViewAction().run(transaction, themesList
				.get(0).getID(), false);
	}
	

	@Override
	public ClientInvoice saveView() {
		ClientInvoice saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
			transaction.setEstimates(new ArrayList<ClientEstimate>());
			List<ClientTransactionItem> tItems = transaction
					.getTransactionItems();
			Iterator<ClientTransactionItem> iterator = tItems.iterator();
			while (iterator.hasNext()) {
				ClientTransactionItem next = iterator.next();
				if (next.getReferringTransactionItem() != 0) {
					iterator.remove();
				}
			}
		}
		return saveView;
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		List<ClientTransaction> selectedRecords = transactionsTree
				.getSelectedRecords();
		List<ClientEstimate> estimates = new ArrayList<ClientEstimate>();
		for (ClientTransaction clientTransaction : selectedRecords) {
			ClientEstimate estimate = (ClientEstimate) clientTransaction;
			if (estimate.getEstimateType() == ClientEstimate.BILLABLEEXAPENSES
					&& estimate.getCurrency() != getCurrencycode().getID()) {
				estimate.setCurrency(getCurrencycode().getID());
				estimate.setTotal(estimate.getTotal() / getCurrencyFactor());
				estimate.setNetAmount(estimate.getNetAmount()
						/ getCurrencyFactor());
				estimate.setTaxTotal(estimate.getTaxTotal()
						/ getCurrencyFactor());
				for (ClientTransactionItem item : estimate
						.getTransactionItems()) {
					item.setLineTotal(item.getLineTotal() / getCurrencyFactor());
					item.setDiscount(item.getDiscount() / getCurrencyFactor());
					item.setUnitPrice(item.getUnitPrice() / getCurrencyFactor());
					item.setVATfraction(item.getVATfraction()
							/ getCurrencyFactor());
				}

			}
			estimates.add(estimate);
		}
		transaction.setEstimates(estimates);
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
		if (getCustomer() != null) {
			Set<ClientAddress> addr = shipToAddress.getAddresss();
			billingAddress = allAddresses.get(ClientAddress.TYPE_BILL_TO);
			if (billingAddress != null) {
				for (ClientAddress clientAddress : addr) {
					if (clientAddress.getType() == ClientAddress.TYPE_BILL_TO) {
						addr.remove(clientAddress);
					}
				}
				addr.add(billingAddress);
			}
			if (!addr.isEmpty()) {
				getCustomer().setAddress(addr);
				// Accounter.createOrUpdate(this, getCustomer());

				for (ClientAddress clientAddress : addr) {
					if (clientAddress.getType() == ClientAddress.TYPE_SHIP_TO)
						shippingAddress = clientAddress;
				}
			}

			transaction.setCustomer(getCustomer().getID());
		}

		if (dueDateItem.getEnteredDate() != null)
			transaction.setDueDate((dueDateItem.getEnteredDate()).getDate());
		if (deliveryDate.getEnteredDate() != null)
			transaction
					.setDeliverydate(deliveryDate.getEnteredDate().getDate());
		if (getCountryPreferences().isSalesTaxAvailable()) {

			transaction.setTaxTotal(salesTaxTextNonEditable.getTotalTax());
		}
		if (contactCombo.getSelectedValue() != null) {
			contact = contactCombo.getSelectedValue();
			transaction.setContact(contact);
		}
		transaction.setContact(contact);
		if (phoneNo != null)
			transaction.setPhone(phoneNo);
		if (billingAddress != null)
			transaction.setBillingAddress(billingAddress);
		if (shippingAddress != null)
			transaction.setShippingAdress(shippingAddress);
		if (salesPerson != null)
			transaction.setSalesPerson(salesPerson.getID());
		if (paymentTerm != null)
			transaction.setPaymentTerm(paymentTerm.getID());
		if (shippingTerm != null)
			transaction.setShippingTerm(shippingTerm.getID());
		if (shippingMethod != null)
			transaction.setShippingMethod(shippingMethod.getID());
		if (priceLevel != null)
			transaction.setPriceLevel(priceLevel.getID());

		if (orderNumText.getValue() != null
				&& !orderNumText.getValue().equals(""))
			orderNum = orderNumText.getValue().toString();
		
		if (orderNum != null)
			transaction.setOrderNum(orderNum);
		
		// if (taxItemGroup != null)
		// transaction.setTaxItemGroup(taxItemGroup);
		if (isTrackDiscounts()) {
			if (discountField.getPercentage() != 0.0
					&& transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getPercentage());
				}
			}
		}
		if (getPreferences().isJobTrackingEnabled()) {
			if (jobListCombo.getSelectedValue() != null) {
				transaction.setJob(jobListCombo.getSelectedValue().getID());
			}
		}
		transaction.setNetAmount(netAmountLabel.getAmount());
		if (isTrackTax()) {
			// if (isTaxPerDetailLine()) {
			setAmountIncludeTAX();
			// } else {
			// if (taxCode != null) {
			// for (ClientTransactionItem record : customerTransactionTable
			// .getAllRows()) {
			// record.setTaxItemGroup(taxCode.getID());
			// }
			transaction.setTaxTotal(this.salesTax);

			// if (getCompany().getPreferences().isInventoryEnabled()
			// && getCompany().getPreferences().iswareHouseEnabled())
			// transaction.setWareHouseAllocations(table.getAllRows());
			// }

		}
		transaction.setTotal(foreignCurrencyamountLabel.getAmount());

		// transaction.setBalanceDue(getBalanceDue());
		transaction.setPayments(getPayments());
		transaction.setMemo(getMemoTextAreaItem());
		// transaction.setReference(getRefText());

		ClientFinanceDate discountDate = Utility.getCalculatedDiscountDate(
				transactionDateItem.getEnteredDate(), paymentTerm);
		transaction.setDiscountDate(discountDate.getDate());
		if (currency != null)
			transaction.setCurrency(currency.getID());

		transaction.setRoundingTotal(roundAmountinforeignCurrencyLabel
				.getAmount());

		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
		transaction.setUploadFile(getAttachments());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		ClientCustomer previousCustomer = getCustomer();

		if (getCustomer() != null && getCustomer() != previousCustomer) {
			transaction.setEstimates(new ArrayList<ClientEstimate>());
			getEstimatesAndSalesOrder();
		}
		// result.add(super.validate());

		// Validations
		// 1. IF(!isValidDueOrDeliveryDates(dueDate, transactionDate)) ERROR

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				this.dueDateItem.getDate(), getTransactionDate())) {
			result.addError(this.dueDateItem,
					messages.the() + " " + messages.dueDate() + " " + " "
							+ messages.cannotbeearlierthantransactiondate());
		}

		boolean isSelected = transactionsTree.validateTree();
		if (!isSelected) {
			if (transaction.getTotal() <= 0
					&& customerTransactionTable.isEmpty()) {
				result.addError(this,
						messages.transactiontotalcannotbe0orlessthan0());
			}
			result.add(customerTransactionTable.validateGrid());
		} else {
			boolean hasTransactionItems = false;
			for (ClientTransactionItem clientTransactionItem : getAllTransactionItems()) {
				if (clientTransactionItem.getAccount() != 0
						|| clientTransactionItem.getItem() != 0) {
					hasTransactionItems = true;
					continue;
				}
			}
			if (hasTransactionItems) {
				result.add(customerTransactionTable.validateGrid());
			} else {
				transaction
						.setTransactionItems(new ArrayList<ClientTransactionItem>());
			}
		}
		if (!isSelected && isTrackTax() && !isTaxPerDetailLine()) {
			if (taxCodeSelect != null
					&& taxCodeSelect.getSelectedValue() == null) {
				result.addError(taxCodeSelect,
						messages.pleaseSelect(messages.taxCode()));
			}
		} else if (isSelected && isTrackTax() && !isTaxPerDetailLine()
				&& !transaction.getTransactionItems().isEmpty()) {
			if (taxCodeSelect != null
					&& taxCodeSelect.getSelectedValue() == null) {
				result.addError(taxCodeSelect,
						messages.pleaseSelect(messages.taxCode()));
			}
		}

		String creditLimitWarning = isExceedCreditLimit(customer,
				transaction.getTotal());
		if (creditLimitWarning != null) {
			result.addWarning(customerCombo, creditLimitWarning);
		}

		return result;
	}

	public void setPayments(Double payments) {
		if (payments == null)
			payments = 0.0D;
		this.payments = payments;
		paymentsNonEditableText.setAmount(payments);
	}

	public Double getPayments() {
		return payments;
	}

	public void setBalanceDue(Double balanceDue) {
		if (balanceDue == null)
			balanceDue = 0.0D;
		this.balanceDue = balanceDue;
		balanceDueNonEditableText.setAmount(balanceDue);
	}

	public static InvoiceView getInstance() {

		return new InvoiceView();
	}

	private void getEstimatesAndSalesOrder() {
		ClientCompanyPreferences preferences = getCompany().getPreferences();

		if ((preferences.isDoyouwantEstimates() && !preferences
				.isDontIncludeEstimates())
				|| (preferences.isBillableExpsesEnbldForProductandServices() && preferences
						.isProductandSerivesTrackingByCustomerEnabled())
				|| preferences.isDelayedchargesEnabled()
				|| preferences.isSalesOrderEnabled()) {
			if (this.rpcUtilService == null)
				return;
			if (getCustomer() == null) {
				Accounter.showError(messages.pleaseSelect(Global.get()
						.customer()));
			} else {

				AsyncCallback<ArrayList<EstimatesAndSalesOrdersList>> callback = new AsyncCallback<ArrayList<EstimatesAndSalesOrdersList>>() {

					@Override
					public void onFailure(Throwable caught) {
						return;
					}

					@Override
					public void onSuccess(
							ArrayList<EstimatesAndSalesOrdersList> result) {
						if (result == null)
							onFailure(new Exception());
						filterEstimates(result);
					}

				};

				this.rpcUtilService.getEstimatesAndSalesOrdersList(
						getCustomer().getID(), callback);
			}
		}
	}

	protected void filterEstimates(ArrayList<EstimatesAndSalesOrdersList> result) {
		List<ClientEstimate> salesAndEstimates = new ArrayList<ClientEstimate>();
		if (transaction.getCustomer() == getCustomer().getID()) {
			salesAndEstimates = previousEstimates;
		}
		if (transaction.getID() != 0 && !result.isEmpty()) {
			ArrayList<EstimatesAndSalesOrdersList> estimatesList = new ArrayList<EstimatesAndSalesOrdersList>();
			ArrayList<ClientTransaction> notAvailableEstimates = new ArrayList<ClientTransaction>();

			for (ClientTransaction clientTransaction : salesAndEstimates) {
				boolean isThere = false;
				for (EstimatesAndSalesOrdersList estimatesalesorderlist : result) {
					int estimateType = estimatesalesorderlist.getEstimateType();
					int status = estimatesalesorderlist.getStatus();
					if (estimatesalesorderlist.getTransactionId() == clientTransaction
							.getID()) {
						estimatesList.add(estimatesalesorderlist);
						isThere = true;
					} else if (estimateType == ClientEstimate.QUOTES) {
						if (getPreferences().isDontIncludeEstimates()) {
							estimatesList.add(estimatesalesorderlist);
						} else if (getPreferences()
								.isIncludeAcceptedEstimates()
								&& status != ClientEstimate.STATUS_ACCECPTED) {
							estimatesList.add(estimatesalesorderlist);
						}
					} else if ((estimateType == ClientEstimate.CHARGES || estimateType == ClientEstimate.CREDITS)
							&& !getPreferences().isDelayedchargesEnabled()) {
						estimatesList.add(estimatesalesorderlist);
					} else if (estimateType == ClientEstimate.SALES_ORDER
							&& !getPreferences().isSalesOrderEnabled()) {
						estimatesList.add(estimatesalesorderlist);
					} else if ((estimateType == ClientEstimate.BILLABLEEXAPENSES || estimateType == ClientEstimate.DEPOSIT_EXAPENSES)
							&& !(getPreferences()
									.isBillableExpsesEnbldForProductandServices() && getPreferences()
									.isProductandSerivesTrackingByCustomerEnabled())) {
						estimatesList.add(estimatesalesorderlist);
					}
				}
				if (!isThere) {
					notAvailableEstimates.add(clientTransaction);
				}
			}

			if (transaction.isDraft()) {
				for (ClientTransaction clientTransaction : notAvailableEstimates) {
					salesAndEstimates.remove(clientTransaction);
				}
			}

			for (EstimatesAndSalesOrdersList estimatesAndSalesOrdersList : estimatesList) {
				result.remove(estimatesAndSalesOrdersList);
			}
		}
		transactionsTree.setAllrows(result, transaction.getID() == 0 ? true
				: salesAndEstimates.isEmpty());

		if (!previousEstimates.isEmpty()
				&& getCustomer().getID() == transaction.getCustomer()) {
			transactionsTree.setRecords(new ArrayList<ClientTransaction>(
					previousEstimates));
		}

		transactionsTree.setEnabled(!isInViewMode());
		refreshTransactionGrid();
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

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void onEdit() {

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

	}

	protected void enableFormItems() {

		// if (UIUtils.isMSIEBrowser())
		// custForm.setWidth("100%");

		setMode(EditMode.EDIT);

		if (emailButton != null) {
			getButtonBar().remove(emailButton);
		}

		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		customerCombo.setEnabled(!isInViewMode());
		shipToAddress.businessSelect.setEnabled(!isInViewMode());
		if (getPreferences().isSalesPersonEnabled())
			salesPersonCombo.setEnabled(!isInViewMode());
		payTermsSelect.setEnabled(!isInViewMode());
		dueDateItem.setEnabled(!isInViewMode());
		deliveryDate.setEnabled(!isInViewMode());
		taxCodeSelect.setEnabled(!isInViewMode());
		orderNumText.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		customerTransactionTable.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		currencyWidget.setEnabled(!isInViewMode());
		discountField.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (shippingTermsCombo != null)
			shippingTermsCombo.setEnabled(!isInViewMode());
		super.onEdit();
		if (isInViewMode()) {
			balanceDueNonEditableText.setVisible(isInViewMode());
			paymentsNonEditableText.setVisible(isInViewMode());
		} else {
			balanceDueNonEditableText.setVisible(isInViewMode());
			paymentsNonEditableText.setVisible(isInViewMode());
		}
		classListCombo.setEnabled(!isInViewMode());
		transactionsTree.setEnabled(!isInViewMode());
		jobListCombo.setEnabled(!isInViewMode());

		enableAttachmentPanel(!isInViewMode());
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
			ClientBrandingTheme clientBrandingTheme = themesList.get(0);
			UIUtils.downloadAttachment(transaction.getID(),
					ClientTransaction.TYPE_INVOICE, clientBrandingTheme.getID());
		}
	}

	@Override
	public void fitToSize(int height, int width) {
		super.fitToSize(height, width);
	}

	private void resetFromView() {
		// custForm.getCellFormatter().setWidth(0, 1, "200");
		//
		// shipToAddress.getCellFormatter().setWidth(0, 1, "100");
		// shipToAddress.getCellFormatter().setWidth(0, 2, "200");

		// priceLevelSelect.setWidth("150px");
		// refText.setWidth("200px");
	}

	@Override
	public void printPreview() {

	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {

			taxCodeSelect.setComboItem(taxCode);
			customerTransactionTable.setTaxCode(taxCode.getID(), true);
		} else
			taxCodeSelect.setValue("");
	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {
		this.accounterClass = clientAccounterClass;
		if (accounterClass != null) {
			classListCombo.setComboItem(accounterClass);
			customerTransactionTable.setClass(accounterClass.getID(), true);
		} else {
			classListCombo.setValue("");
		}
	}

	@Override
	protected String getViewTitle() {
		return messages.invoice();
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
	public List<ClientTransactionItem> getAllTransactionItems() {
		return customerTransactionTable.getAllRows();
	}

	@Override
	protected boolean isBlankTransactionGrid() {
		return customerTransactionTable.getAllRows().isEmpty();
	}

	@Override
	public void addNewData(ClientTransactionItem transactionItem) {
		customerTransactionTable.add(transactionItem);
	}

	@Override
	protected void refreshTransactionGrid() {
		customerTransactionTable.updateTotals();
		transactionsTree.updateTransactionTreeItemTotals();
	}

	private void settabIndexes() {
		customerCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		billToTextArea.setTabIndex(3);
		shipToAddress.setTabIndexforShiptocombo(4);
		shipToAddress.setTabIndex(5);
		transactionDateItem.setTabIndex(6);
		transactionNumber.setTabIndex(7);
		payTermsSelect.setTabIndex(8);
		dueDateItem.setTabIndex(9);
		orderNumText.setTabIndex(10);
		memoTextAreaItem.setTabIndex(11);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(12);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(13);
		if (saveAndEmailButton != null)
			saveAndEmailButton.setTabIndex(14);
		cancelButton.setTabIndex(15);
	}

	@Override
	protected void addAccountTransactionItem(ClientTransactionItem item) {
		// TODO Auto-generated method stub

	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		customerTransactionTable.add(item);
	}

	@Override
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		this.customerTransactionTable.updateAmountsFromGUI();
		transactionsTree.refreshBillableTransactionTree();
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
		if (netAmountLabel != null) {
			netAmountLabel.setTitle(messages.currencyNetAmount(formalName));
			netAmountLabel.setCurrency(currencyWidget.getSelectedCurrency());
		}
		if (paymentsNonEditableText != null) {
			paymentsNonEditableText.setTitle(messages.nameWithCurrency(
					messages.payments(), formalName));
			paymentsNonEditableText.setCurrency(currencyWidget
					.getSelectedCurrency());
		}
		if (balanceDueNonEditableText != null) {
			balanceDueNonEditableText.setTitle(messages.nameWithCurrency(
					messages.balanceDue(), formalName));
			balanceDueNonEditableText.setCurrency(currencyWidget
					.getSelectedCurrency());
		}
	}

	@Override
	protected ValidationResult validateBaseRequirement() {
		ValidationResult result = new ValidationResult();
		result.add(custForm.validate());
		result.add(super.validateBaseRequirement());
		return result;
	}

	@Override
	protected void updateDiscountValues() {
		if (discountField.getPercentage() != null) {
			customerTransactionTable.setDiscount(discountField.getPercentage());
		} else {
			discountField.setPercentage(0d);
		}
	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return true;
	}
	
	public void selectedConsultantValue(ClientTransactionItem selectedItem)
	{
		billToTextArea.setValue("");
		List<ClientCustomer> customer = new ArrayList<ClientCustomer>();
		customer.addAll(customerCombo.getComboItems());
		for(ClientCustomer customers : customer)
		{
			if(customers.getName() == selectedItem.getCustomerName())
			{
				customerSelected(customers);
				contactCombo.setEnabled(!isInViewMode());
				break;
			}
		}
		InvoiceView.this.updateNonEditableItems();
		selectedItem.setType(1);
		selectedItem = selectedItem.clone();
		customerTransactionTable.removeAllRecords();
		customerTransactionTable.getSelectedRecords(1);
		customerTransactionTable.updateColumnHeaders();
		customerTransactionTable.add(selectedItem);	;
		customerTransactionTable.consultantFocus();	
//		Global.get().preferences().setEmpTimeSheetId(selectedItem.getEmployeeTimeSheetId());
//		Window.alert(Global.get().preferences().getEmpTimeSheetId()+"answer");
	}
}