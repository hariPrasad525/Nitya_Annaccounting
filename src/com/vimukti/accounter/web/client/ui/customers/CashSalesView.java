package com.vimukti.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientCashSales;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCompanyPreferences;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientCustomer;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPriceLevel;
import com.vimukti.accounter.web.client.core.ClientSalesPerson;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.EstimatesAndSalesOrdersList;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.GwtDisclosurePanel;
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.SalesPersonCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.TAXCodeCombo;
import com.vimukti.accounter.web.client.ui.core.BrandingThemeComboAction;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.EmailThemeComboAction;
import com.vimukti.accounter.web.client.ui.core.EmailViewAction;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.TransactionsTree;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.CustomerItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * gg
 * 
 * @author Fernandez
 * 
 */
public class CashSalesView extends
		AbstractCustomerTransactionView<ClientCashSales> implements
		IPrintableView {
	private ShippingTermsCombo shippingTermsCombo;
	private TAXCodeCombo taxCodeSelect;
	private SalesPersonCombo salesPersonCombo;
	private Double salesTax = 0.0D;
	protected DateField deliveryDate;
	private ArrayList<DynamicForm> listforms;
	private ShipToForm shipToAddress;
	private CustomerAccountTransactionTable customerAccountTransactionTable;
	private CustomerItemTransactionTable customerItemTransactionTable;
	private ClientPriceLevel priceLevel;
	private ClientSalesPerson salesPerson;
	private AmountLabel netAmountLabel;
	private TaxItemsForm taxTotalNonEditableText;
	private Double transactionTotal = 0.0D;
	private AddNewButton accountTableButton, itemTableButton;
	private GwtDisclosurePanel accountsDisclosurePanel;
	private GwtDisclosurePanel itemsDisclosurePanel;
	private TextItem checkNoText;
	// private CheckboxItem printCheck;
	private boolean isChecked = false;
	List<ClientEstimate> previousEstimates = new ArrayList<ClientEstimate>();
	private Button emailButton;

	TransactionsTree<PurchaseOrdersAndItemReceiptsList> transactionsTree;
	private StyledPanel accountFlowPanel;
	private StyledPanel itemsFlowPanel;

	public CashSalesView() {
		super(ClientTransaction.TYPE_CASH_SALES);
		this.getElement().setId("CashSalesView");
	}

	private void initCashSalesView() {

		ArrayList<ClientShippingTerms> shippingTerms = getCompany()
				.getShippingTerms();

		shippingTermsCombo.initCombo(shippingTerms);

		initDepositInAccounts();
		initShippingMethod();

	}

	@Override
	protected void createControls() {

		Label lab1 = new Label(messages.cashSale());
		lab1.setStyleName("label-title");
		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						deliveryDate.setEnteredDate(date);
						setTransactionDate(date);
					}
				});

		transactionNumber = createTransactionNumberItem();

		listforms = new ArrayList<DynamicForm>();

		DynamicForm dateNoForm = new DynamicForm("dateNoForm");
		// dateNoForm.setNumCols(6);
		dateNoForm.setStyleName("datenumber-panel");
		locationCombo = createLocationCombo();
		if (!isTemplate) {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}

		StyledPanel datepanel = new StyledPanel("StyledPanel");
		datepanel.add(dateNoForm);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
		labeldateNoLayout.add(datepanel);

		customerCombo = createCustomerComboItem(messages.payeeName(Global.get()
				.Customer()));
		customerCombo.setRequired(false);
		contactCombo = createContactComboItem();

		phoneSelect = new TextItem(messages.phone(), "phoneSelect");
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		phoneSelect.setEnabled(!isInViewMode());

		billToTextArea = new TextAreaItem(messages.billTo(), "billToTextArea");
		billToTextArea.setDisabled(true);

		shipToAddress = new ShipToForm(null);
		shipToAddress.addrArea.setDisabled(true);
		shipToAddress.businessSelect.setEnabled(false);
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

		custForm = UIUtils.form(Global.get().customer());
		custForm.add(customerCombo, contactCombo, phoneSelect, billToTextArea);
		custForm.setStyleName("align-form");
		// custForm.setWidth("100%");
		salesPersonCombo = createSalesPersonComboItem();
		paymentMethodCombo = createPaymentMethodSelectItem();

		paymentMethodCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentMethodSelected(paymentMethodCombo
								.getSelectedValue());
					}
				});

		// printCheck = new CheckboxItem(messages.toBePrinted(), "printCheck");
		// printCheck.setValue(true);
		// printCheck.setEnabled(!true);
		// printCheck.setVisible(false);
		// printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {
		//
		// @Override
		// public void onValueChange(ValueChangeEvent<Boolean> event) {
		// isChecked = event.getValue();
		// if (isChecked) {
		// if (printCheck.getValue().toString()
		// .equalsIgnoreCase("true")) {
		// checkNoText.setValue(messages.toBePrinted());
		// checkNoText.setEnabled(!true);
		// } else {
		// if (payFromCombo.getValue() == null)
		// checkNoText.setValue(messages.toBePrinted());
		// else if (transaction != null) {
		// checkNoText.setValue(transaction.getCheckNumber());
		// }
		// }
		// } else
		// checkNoText.setValue("");
		// checkNoText.setEnabled(!false);
		//
		// }
		// });

		checkNoText = new TextItem(messages.chequeNo(), "checkNoText");
		checkNoText.setValue(messages.toBePrinted());
		checkNoText.setVisible(false);
		// checkNoText.setWidth(100);
		if (paymentMethodCombo.getSelectedValue() != null
				&& !paymentMethodCombo.getSelectedValue().equals(
						UIUtils.getpaymentMethodCheckBy_CompanyType(messages
								.check())))
			checkNoText.setEnabled(!true);
		checkNoText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNoText.getValue().toString();
			}
		});
		depositInCombo = createDepositInComboItem(null);
		depositInCombo.setPopupWidth("500px");
		shippingTermsCombo = createShippingTermsCombo();
		shippingMethodsCombo = createShippingMethodCombo();
		deliveryDate = createTransactionDeliveryDateItem();
		deliveryDate.setEnteredDate(getTransactionDate());

		DynamicForm termsForm = new DynamicForm("termsForm");
		if (locationTrackingEnabled)
			termsForm.add(locationCombo);
		if (getPreferences().isSalesPersonEnabled()) {
			// printCheck filed removed here
			termsForm.add(salesPersonCombo, paymentMethodCombo, checkNoText,
					depositInCombo);
			if (getPreferences().isDoProductShipMents()) {
				termsForm.add(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
			}
		} else {
			// printCheck filed removed here
			termsForm.add(paymentMethodCombo, checkNoText, depositInCombo);
			if (getPreferences().isDoProductShipMents()) {
				termsForm.add(shippingTermsCombo, shippingMethodsCombo,
						deliveryDate);
			}
		}

		termsForm.setStyleName("align-form");
		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			termsForm.add(classListCombo);
		}
		jobListCombo = createJobListCombo();
		if (isTrackJob()) {
			jobListCombo.setEnabled(false);
			termsForm.add(jobListCombo);
		}

		memoTextAreaItem = createMemoTextAreaItem();
		// memoTextAreaItem.setWidth(160);

		taxCodeSelect = createTaxCodeSelectItem();

		// priceLevelSelect = createPriceLevelSelectItem();

		// refText = createRefereceText();
		// refText.setWidth(160);

		DynamicForm prodAndServiceForm1 = new DynamicForm("prodAndServiceForm1");
		prodAndServiceForm1.add(memoTextAreaItem);

		taxTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();
		netAmountLabel = createNetAmountLabel();

		transactionTotalBaseCurrencyText = createTransactionTotalNonEditableLabel(getCompany()
				.getPrimaryCurrency());
		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		roundAmountinBaseCurrenctText = createTransactionRoundingAmountNonEditableLabel(getCompany()
				.getPreferences().getPrimaryCurrency());
		roundAmountinforeignCurrencyLabel = createRoundingAmountForeignCurrencyAmountLable(getCompany()
				.getPreferences().getPrimaryCurrency());

		transactionsTree = new TransactionsTree<PurchaseOrdersAndItemReceiptsList>(
				this) {
			HashMap<ClientTransactionItem, ClientTransactionItem> clonesObjs = new HashMap<ClientTransactionItem, ClientTransactionItem>();

			@Override
			public void updateTransactionTotal() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashSalesView.this.updateNonEditableItems();
			}

			@Override
			public void setTransactionDate(ClientFinanceDate transactionDate) {
				CashSalesView.this.setTransactionDate(transactionDate);
			}

			@Override
			public boolean isinViewMode() {
				return !(CashSalesView.this.isInViewMode());
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
				for (ClientTransactionItem transactionItem : transactionItems) {
					if (!isSelected) {
						// De SELECTED
						ClientTransactionItem cloned = clonesObjs
								.get(transactionItem);
						customerItemTransactionTable.delete(cloned);
						transaction.getTransactionItems().remove(cloned);
						continue;

					}
					// SELECTED
					ClientTransactionItem tItem = transactionItem.clone();
					tItem.setID(0l);
					tItem.setLineTotal(tItem.getLineTotal()
							/ getCurrencyFactor());
					tItem.setDiscount(tItem.getDiscount() / getCurrencyFactor());
					tItem.setUnitPrice(tItem.getUnitPrice()
							/ getCurrencyFactor());
					tItem.setVATfraction(tItem.getVATfraction()
							/ getCurrencyFactor());
					tItem.setReferringTransactionItem(transactionItem.getID());
					tItem.setTransaction(transaction);
					if (vatinclusiveCheck != null) {
						tItem.setAmountIncludeTAX(vatinclusiveCheck.getValue());
					}
					if (tItem.getType() == ClientTransactionItem.TYPE_ACCOUNT) {
						tItem.setType(ClientTransactionItem.TYPE_ITEM);
						tItem.setAccount(0L);
					}

					int emptyRows = 0;
					for (ClientTransactionItem cItem : customerItemTransactionTable
							.getAllRows()) {
						if (cItem.isEmpty()) {
							customerItemTransactionTable.delete(cItem);
							emptyRows++;
						}
					}
					customerItemTransactionTable.add(tItem);
					while (emptyRows > 1) {
						customerItemTransactionTable
								.add(customerItemTransactionTable.getEmptyRow());
						emptyRows--;
					}
					transaction.getTransactionItems().add(tItem);
					clonesObjs.put(transactionItem, tItem);
					ClientTAXCode selectedValue = taxCodeSelect
							.getSelectedValue();
					if (selectedValue == null
							&& !getPreferences().isTaxPerDetailLine()) {
						taxCodeSelected(getCompany().getTAXCode(
								tItem.getTaxCode()));
					}
				}
				customerItemTransactionTable.updateTotals();

			}
		};

		customerAccountTransactionTable = new CustomerAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), isTrackClass(),
				isClassPerDetailLine(), this) {

			@Override
			public void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashSalesView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashSalesView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CashSalesView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CashSalesView.this.updateNonEditableItems();
			}
		};

		customerAccountTransactionTable.setEnabled(!isInViewMode());

		this.accountFlowPanel = new StyledPanel("accountFlowPanel");

		accountsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		accountsDisclosurePanel.setTitle(messages.ItemizebyAccount());
		accountFlowPanel.add(customerAccountTransactionTable);

		accountsDisclosurePanel.setContent(accountFlowPanel);
		customerItemTransactionTable = new CustomerItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), isTrackClass(),
				isClassPerDetailLine(), this) {

			@Override
			public void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashSalesView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashSalesView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CashSalesView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CashSalesView.this.updateNonEditableItems();
			}
		};
		customerItemTransactionTable.setEnabled(!isInViewMode());

		this.itemsFlowPanel = new StyledPanel("itemsFlowPanel");
		itemsFlowPanel.add(customerItemTransactionTable);

		itemsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		itemsDisclosurePanel.setTitle(messages2.consultantName());
		itemsDisclosurePanel.setContent(itemsFlowPanel);

		// Inventory table..
		// inventoryTransactionTable = new WarehouseAllocationTable();
		// inventoryTransactionTable.setDesable(isInViewMode());
		// FlowPanel inventoryFlowPanel = new FlowPanel();
		// inventoryDisclosurePanel = new
		// DisclosurePanel("Warehouse Allocation");
		// inventoryFlowPanel.add(inventoryTransactionTable);
		// inventoryDisclosurePanel.setContent(inventoryFlowPanel);
		// inventoryDisclosurePanel.setWidth("100%");
		// ---Inverntory table-----

		// final TextItem disabletextbox = new TextItem();
		// disabletextbox.setVisible(false);

		DynamicForm taxForm = new DynamicForm("taxForm");
		StyledPanel nonEditablePanel = new StyledPanel("nonEditablePanel");
		nonEditablePanel.addStyleName("boldtext");

		DynamicForm netAmountForm = new DynamicForm("netAmountForm");

		discountField = getDiscountField();
		DynamicForm totalForm = new DynamicForm("totalForm");
		DynamicForm roundingForm = new DynamicForm("roundingForm");
		// totalForm.setNumCols(2);
		if (isTrackTax()) {
			netAmountForm.add(netAmountLabel);
			nonEditablePanel.add(netAmountForm);
			nonEditablePanel.add(taxTotalNonEditableText);
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
		nonEditablePanel.add(roundingForm);
		nonEditablePanel.add(totalForm);
		nonEditablePanel.addStyleName("boldtext");

		currencyWidget = createCurrencyFactorWidget();

		StyledPanel prodAndServiceHLay = new StyledPanel("prodAndServiceHLay");

		prodAndServiceHLay.add(prodAndServiceForm1);
		prodAndServiceHLay.add(taxForm);
		prodAndServiceHLay.add(nonEditablePanel);

		StyledPanel vPanel = new StyledPanel("vPanel");
		vPanel.add(prodAndServiceHLay);

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		leftVLay.add(custForm);
		if (getPreferences().isDoProductShipMents())
			leftVLay.add(shipToAddress);
		StyledPanel rightVLay = new StyledPanel("rightVLay");
		rightVLay.add(termsForm);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}

		// topHLay.setSpacing(20);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
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

		mainVLay.add(transactionsTree);
		mainVLay.add(accountsDisclosurePanel.getPanel());
		mainVLay.add(itemsDisclosurePanel.getPanel());

		mainVLay.add(vPanel);

		if (UIUtils.isMSIEBrowser())
			resetFormView();

		this.add(mainVLay);

		// setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(termsForm);
		listforms.add(prodAndServiceForm1);
		listforms.add(netAmountForm);
		listforms.add(totalForm);

		// settabIndexes();
		if (isMultiCurrencyEnabled()) {
			if (getPreferences().isEnabledRoundingOptions()) {
				roundAmountinforeignCurrencyLabel.hide();
			}
			foreignCurrencyamountLabel.hide();
		}

	}

	protected StyledPanel getTopLayOut() {
		StyledPanel styledPanel = new StyledPanel("topHLay");
		styledPanel.addStyleName("fields-panel");
		return styledPanel;
	}

	protected ShippingTermsCombo createShippingTermsCombo() {

		final ShippingTermsCombo shippingTermsCombo = new ShippingTermsCombo(
				messages.shippingTerms());
		shippingTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {

					@Override
					public void selectedComboBoxItem(
							ClientShippingTerms selectItem) {
						shippingTerm = selectItem;
						if (shippingTerm != null && shippingTermsCombo != null) {
							shippingTermsCombo.setComboItem(getCompany()
									.getShippingTerms(shippingTerm.getID()));
							shippingTermsCombo.setEnabled(!isInViewMode());
						}
					}

				});

		shippingTermsCombo.setEnabled(!isInViewMode());
		return shippingTermsCombo;
	}

	@Override
	protected void customerSelected(ClientCustomer customer) {
		if (customer == null) {
			return;
		}
		customerItemTransactionTable.setPayee(customer);
		transactionsTree.clear();
		ClientCurrency currency = getCurrency(customer.getCurrency());
		// Job Tracking
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setEnabled(!isInViewMode());
			jobListCombo.setValue("");
			jobListCombo.setCustomer(customer);
		}
		if (this.getCustomer() != null && this.getCustomer() != customer) {
			ClientCashSales ent = this.transaction;

			if (ent != null && ent.getCustomer() == customer.getID()) {
				this.customerAccountTransactionTable
						.setAllRows(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.customerItemTransactionTable
						.setAllRows(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getCustomer() != customer.getID()) {
				this.customerAccountTransactionTable.updateTotals();
				this.customerItemTransactionTable.updateTotals();
			} else if (ent == null)
				this.customerAccountTransactionTable.resetRecords();
		}

		super.customerSelected(customer);
		if (this.shippingTerm != null && shippingTermsCombo != null)
			shippingTermsCombo.setComboItem(this.shippingTerm);

		if (this.salesPerson != null && salesPersonCombo != null)
			salesPersonCombo.setComboItem(this.salesPerson);

		if (this.taxCode != null && taxCodeSelect != null
				&& taxCodeSelect.getValue() != ""
				&& !taxCodeSelect.getTitle().equalsIgnoreCase(messages.none()))
			taxCodeSelect.setComboItem(this.taxCode);

		if (customer.getPhoneNo() != null)
			phoneSelect.setValue(customer.getPhoneNo());
		else
			phoneSelect.setValue("");
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {

			billToTextArea.setValue(getValidAddress(billingAddress));

		} else
			billToTextArea.setValue("");

		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		addresses.addAll(customer.getAddress());
		shipToAddress.setAddress(addresses);
		this.setCustomer(customer);
		if (customer != null) {
			customerCombo.setComboItem(customer);
		}

		if (currency.getID() != 0) {
			if (currency != null) {
				currencyWidget.setSelectedCurrencyFactorInWidget(currency,
						transactionDateItem.getDate().getDate());
			}
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
		getEstimatesAndSalesOrder();
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

				this.rpcUtilService.getSalesOrdersList(getCustomer().getID(),
						callback);
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
					} else if (estimateType == ClientEstimate.SALES_ORDER
							&& !getPreferences().isSalesOrderEnabled()) {
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
		if (salesPerson != null && salesPersonCombo != null) {

			salesPersonCombo.setComboItem(getCompany().getSalesPerson(
					salesPerson.getID()));

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
		if (this.transaction == null || customerItemTransactionTable != null) {
			customerItemTransactionTable.setPricingLevel(priceLevel);
			// customerTransactionTable.updatePriceLevel();
		}
		updateNonEditableItems();

	}

	@Override
	public void saveAndUpdateView() {

		updateTransaction();

		saveOrUpdate(transaction);

	}

	@Override
	public ClientCashSales saveView() {
		ClientCashSales saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
			transaction.setSalesOrders(new ArrayList<ClientEstimate>());
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

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		if (taxCode != null && transactionItems != null) {

			for (ClientTransactionItem item : transactionItems) {
				// if (taxCode instanceof ClientTAXItem)
				// item.setTaxItem((ClientTAXItem) taxCode);
				// if (taxCode instanceof ClientTAXGroup)
				// item.setTaxGroup((ClientTAXGroup) taxCode);
				item.setTaxCode(taxCode.getID());

			}
		}
		if (!getPreferences().isClassPerDetailLine() && accounterClass != null
				&& transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setAccounterClass(accounterClass.getID());
			}
		}
		if (discountField.getPercentage() != 0) {
			transaction.setDiscountTotal(discountField.getPercentage());
		}
		transaction.setTransactionDate(transactionDate.getDate());
		if (getCustomer() != null)
			transaction.setCustomer(getCustomer().getID());
		transaction.setPaymentMethod(paymentMethodCombo.getSelectedValue());
		if (checkNoText.getValue() != null
				&& !checkNoText.getValue().equals("")) {
			transaction.setCheckNumber(getCheckValue());
		} else
			transaction.setCheckNumber("");

		if (depositInAccount != null)
			transaction.setDepositIn(depositInAccount.getID());
		if (contact != null)
			transaction.setContact(contact);
		transaction.setPhone(phoneSelect.getValue().toString());
		if (salesPerson != null) {
			transaction.setSalesPerson(salesPerson.getID());
		}
		if (billingAddress != null)
			transaction.setBillingAddress(billingAddress);
		if (shipToAddress != null)
			transaction.setShippingAdress(shipToAddress.getAddress());
		if (shippingTerm != null)
			transaction.setShippingTerm(shippingTerm.getID());
		if (shippingMethod != null)
			transaction.setShippingMethod(shippingMethod.getID());
		if (deliveryDate != null && deliveryDate.getEnteredDate() != null)
			transaction
					.setDeliverydate(deliveryDate.getEnteredDate().getDate());

		if (priceLevel != null)
			transaction.setPriceLevel(priceLevel.getID());
		transaction.setMemo(getMemoTextAreaItem());
		transaction.setNetAmount(netAmountLabel.getAmount());
		if (isTrackTax()) {
			setAmountIncludeTAX();
			transaction.setTaxTotal(salesTax);
		}
		List<ClientTransaction> selectedRecords = transactionsTree
				.getSelectedRecords();
		List<ClientEstimate> orders = new ArrayList<ClientEstimate>();
		for (ClientTransaction clientTransaction : selectedRecords) {
			if (clientTransaction instanceof ClientEstimate) {
				orders.add((ClientEstimate) clientTransaction);
			}
		}
		transaction.setSalesOrders(orders);

		transaction.setTotal(foreignCurrencyamountLabel.getAmount());
		transaction.setRoundingTotal(roundAmountinforeignCurrencyLabel
				.getAmount());
		if (getPreferences().isJobTrackingEnabled()) {
			if (jobListCombo.getSelectedValue() != null)
				transaction.setJob(jobListCombo.getSelectedValue().getID());
		}
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

		// if (getCompany().getPreferences().isInventoryEnabled()
		// && getCompany().getPreferences().iswareHouseEnabled())
		// transaction.setWareHouseAllocations(inventoryTransactionTable
		// .getAllRows());
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
		double lineTotal = customerAccountTransactionTable.getLineTotal()
				+ customerItemTransactionTable.getLineTotal();
		double totalTax = customerAccountTransactionTable.getTotalTax()
				+ customerItemTransactionTable.getTotalTax();
		double total = customerAccountTransactionTable.getGrandTotal()
				+ customerItemTransactionTable.getGrandTotal();
		if (getCompany().getPreferences().isTrackTax()) {
			netAmountLabel.setAmount(lineTotal);
			setSalesTax(totalTax);
		}
		if (getPreferences().isEnabledRoundingOptions()) {
			double round = round(getPreferences().getRoundingMethod(), total,
					getPreferences().getRoundingLimit());
			if (round == 0.0) {
				setTransactionTotal(total);
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
			setTransactionTotal(total);
		}

		// } else {
		// double lineTotal = customerAccountTransactionTable.getLineTotal()
		// + customerItemTransactionTable.getLineTotal();
		// double grandTotal = customerAccountTransactionTable.getGrandTotal()
		// + customerItemTransactionTable.getGrandTotal();
		// double totalTax = customerAccountTransactionTable.getTotalTax()
		// + customerItemTransactionTable.getTotalTax();
		// netAmountLabel.setAmount(lineTotal);
		// taxTotalNonEditableText.setAmount(grandTotal - lineTotal);
		// if (getCompany().getPreferences().isTrackTax()) {
		// netAmountLabel.setAmount(lineTotal);
		// setSalesTax(totalTax);
		// }
		// setTransactionTotal(grandTotal);
		// }

	}

	public void setTransactionTotal(Double transactionTotal) {
		if (transactionTotal == null)
			transactionTotal = 0.0D;
		this.transactionTotal = transactionTotal;
		if (transactionTotalBaseCurrencyText != null) {
			transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(transactionTotal));
			foreignCurrencyamountLabel.setAmount(transactionTotal);
		}

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientCashSales());
		} else {
			if (isMultiCurrencyEnabled()) {
				if (transaction.getCurrency() > 0) {
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
			ClientCompany company = getCompany();
			initTransactionsItems();
			previousEstimates = transaction.getSalesOrders();
			this.setCustomer(company.getCustomer(transaction.getCustomer()));
			customerItemTransactionTable.setPayee(customer);
			customerSelected(this.customer);
			if (this.getCustomer() != null) {

				this.contacts = getCustomer().getContacts();
			}
			this.contact = transaction.getContact();
			this.phoneNo = transaction.getPhone();
			phoneSelect.setValue(this.phoneNo);

			this.billingAddress = transaction.getBillingAddress();
			this.shippingAddress = transaction.getShippingAdress();

			this.priceLevel = company
					.getPriceLevel(transaction.getPriceLevel());

			this.salesPerson = company.getSalesPerson(transaction
					.getSalesPerson());
			this.shippingTerm = company.getShippingTerms(transaction
					.getShippingTerm());
			this.shippingMethod = company.getShippingMethod(transaction
					.getShippingMethod());
			this.depositInAccount = company.getAccount(transaction
					.getDepositIn());

			if (getCustomer() != null) {
				customerCombo.setComboItem(getCustomer());
			}
			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			if (getCustomer() != null)
				addresses.addAll(getCustomer().getAddress());
			shipToAddress.setListOfCustomerAdress(addresses);
			if (shippingAddress != null) {
				shipToAddress.businessSelect.setValue(shippingAddress
						.getAddressTypes().get(shippingAddress.getType()));
				shipToAddress.setAddres(shippingAddress);
			}
			if (billingAddress != null) {

				billToTextArea.setValue(getValidAddress(billingAddress));

			} else
				billToTextArea.setValue("");
			contactSelected(this.contact);
			priceLevelSelected(this.priceLevel);
			salesPersonSelected(this.salesPerson);
			shippingMethodSelected(this.shippingMethod);
			shippingTermSelected(shippingTerm);
			depositInAccountSelected(this.depositInAccount);

			this.transactionItems = transaction.getTransactionItems();
			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			paymentMethodSelected(transaction.getPaymentMethod());

			if (transaction.getDeliverydate() != 0)
				this.deliveryDate.setEnteredDate(new ClientFinanceDate(
						transaction.getDeliverydate()));

			if (transaction.getID() != 0) {
				setMode(EditMode.VIEW);
			}
			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(cashSale.getReference());
			netAmountLabel.setAmount(transaction.getNetAmount());
			if (isTrackTax()) {
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(isAmountIncludeTAX());
				}

				if (isTaxPerDetailLine()) {
					taxTotalNonEditableText.setTransaction(transaction);
				} else {
					this.taxCode = getTaxCodeForTransactionItems(this.transactionItems);
					if (taxCode != null) {
						this.taxCodeSelect
								.setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
						taxCodeSelected(taxCode);
					}
					taxTotalNonEditableText.setTransaction(transaction);
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

			memoTextAreaItem.setEnabled(!true);

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

		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		if (getPreferences().isJobTrackingEnabled()) {
			if (customer != null) {
				jobListCombo.setCustomer(customer);
			}
			jobSelected(Accounter.getCompany().getjob(transaction.getJob()));
		}
		superinitTransactionViewData();
		initCashSalesView();

		if (isMultiCurrencyEnabled()) {
			updateAmountsFromGUI();
		}
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

	private void initCustomers() {
		List<ClientCustomer> result = getCompany().getActiveCustomers();
		customerCombo.initCombo(result);
		customerCombo.setEnabled(!isInViewMode());

	}

	@Override
	public void setTransactionDate(ClientFinanceDate transactionDate) {
		super.setTransactionDate(transactionDate);
		if (this.transactionDateItem != null
				&& this.transactionDateItem.getValue() != null)
			updateNonEditableItems();
	}

	@Override
	protected void initMemoAndReference() {

		if (this.transaction != null) {

			ClientCashSales cashSales = transaction;

			if (cashSales != null) {

				memoTextAreaItem
						.setValue(cashSales.getMemo() != null ? cashSales
								.getMemo() : "");
				memoTextAreaItem.setEnabled(!isInViewMode());
				// refText.setValue(cashSales.getReference() != null ? cashSales
				// .getReference() : "");

			}

		}

	}

	@Override
	protected void initSalesTaxNonEditableItem() {

		if (transaction != null) {
			Double salesTaxAmout = transaction.getTaxTotla();
			setSalesTax(salesTaxAmout);

		}

	}

	public void setSalesTax(Double salesTax) {
		if (salesTax == null)
			salesTax = 0.0D;
		this.salesTax = salesTax;

		if (taxTotalNonEditableText != null) {
			List<ClientTransaction> selectedRecords = transactionsTree
					.getSelectedRecords();
			if (!isInViewMode()) {
				List<ClientEstimate> orders = new ArrayList<ClientEstimate>();
				for (ClientTransaction clientTransaction : selectedRecords) {
					if (clientTransaction instanceof ClientEstimate) {
						orders.add((ClientEstimate) clientTransaction);
					}
				}
				transaction.setSalesOrders(orders);
			}

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
	protected void initTransactionTotalNonEditableItem() {

		if (transaction != null) {
			this.transactionTotal = transaction.getTotal();
			this.transactionTotalBaseCurrencyText
					.setAmount(getAmountInBaseCurrency(this.transactionTotal));
			this.foreignCurrencyamountLabel.setAmount(this.transactionTotal);
			this.roundAmountinBaseCurrenctText
					.setAmount(getAmountInBaseCurrency(transaction
							.getRoundingTotal()));
			this.roundAmountinforeignCurrencyLabel.setAmount(transaction
					.getRoundingTotal());

		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		result.add(FormItem.validate(this.paymentMethodCombo,
				this.depositInCombo));
		// Validations
		// 1. paymentMethodCombo validation
		// 2. depositInCombo validation i.e form items
		boolean isSelected = transactionsTree.validateTree();
		if (!isSelected) {
			if (transaction.getTotal() <= 0
					&& customerAccountTransactionTable.isEmpty()
					&& customerItemTransactionTable.isEmpty()) {
				result.addError(this,
						messages.transactiontotalcannotbe0orlessthan0());
			}
			result.add(customerAccountTransactionTable.validateGrid());
			result.add(customerItemTransactionTable.validateGrid());
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
				result.add(customerAccountTransactionTable.validateGrid());
				result.add(customerItemTransactionTable.validateGrid());
			} else {
				transaction
						.setTransactionItems(new ArrayList<ClientTransactionItem>());
			}
		}
		if (!isSelected && isTrackTax() && isTrackPaidTax()
				&& !isTaxPerDetailLine()) {
			if (taxCodeSelect != null
					&& taxCodeSelect.getSelectedValue() == null) {
				result.addError(taxCodeSelect,
						messages.pleaseSelect(messages.taxCode()));
			}
		} else if (isSelected && isTrackTax() && isTrackPaidTax()
				&& !isTaxPerDetailLine()
				&& !transaction.getTransactionItems().isEmpty()) {
			if (taxCodeSelect != null
					&& taxCodeSelect.getSelectedValue() == null) {
				result.addError(taxCodeSelect,
						messages.pleaseSelect(messages.taxCode()));
			}
		}

		ClientAccount bankAccount = depositInCombo.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());

			if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
				result.addError(depositInCombo,
						messages.selectProperBankAccount());
			}
		}
		return result;

	}

	@Override
	protected ValidationResult validateBaseRequirement() {
		ValidationResult result = new ValidationResult();
		result.add(FormItem.validate(this.paymentMethodCombo,
				this.depositInCombo));
		result.add(super.validateBaseRequirement());
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
		this.customerCombo.setFocus();
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
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(Global.get().messages()
							.sessionExpired());
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
		setMode(EditMode.EDIT);

		if (emailButton != null) {
			getButtonBar().remove(emailButton);
		}

		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		customerCombo.setEnabled(!isInViewMode());
		if (getPreferences().isSalesPersonEnabled())
			salesPersonCombo.setEnabled(!isInViewMode());
		paymentMethodCombo.setEnabled(!isInViewMode());
		// if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
		// checkNoText.setValue(messages.toBePrinted());
		// }
		depositInCombo.setEnabled(!isInViewMode());
		taxCodeSelect.setEnabled(!isInViewMode());

		shippingTermsCombo.setEnabled(!isInViewMode());
		shippingMethodsCombo.setEnabled(!isInViewMode());

		shipToAddress.businessSelect.setEnabled(!isInViewMode());
		classListCombo.setEnabled(!isInViewMode());
		deliveryDate.setEnabled(!isInViewMode());
		memoTextAreaItem.setEnabled(!isInViewMode());
		customerAccountTransactionTable.setEnabled(!isInViewMode());
		customerItemTransactionTable.setEnabled(!isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		discountField.setEnabled(!isInViewMode());
		transactionsTree.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (shippingTermsCombo != null)
			shippingTermsCombo.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		if (getPreferences().isJobTrackingEnabled()) {
			jobListCombo.setEnabled(!isInViewMode());
		}
		paymentMethodSelected(paymentMethodCombo.getSelectedValue());
		classListCombo.setEnabled(!isInViewMode());
		super.onEdit();
	}

	@Override
	protected void paymentMethodSelected(String paymentmethod) {
		this.paymentMethod = paymentmethod;
		if (paymentMethod == null) {
			return;
		}

		if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
			// printCheck.setEnabled(!isInViewMode());
			checkNoText.setEnabled(!isInViewMode());
			// printCheck.setVisible(true);
			checkNoText.setVisible(true);
		} else {
			// paymentMethodCombo.setComboItem(paymentMethod);
			// printCheck.setEnabled(!true);
			// printCheck.setVisible(false);
			checkNoText.setEnabled(!true);
			checkNoText.setVisible(false);
		}

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
					ClientTransaction.TYPE_CASH_SALES,
					clientBrandingTheme.getID());
		}
	}

	@Override
	public void printPreview() {

	}

	private void resetFormView() {
		// custForm.getCellFormatter().setWidth(0, 1, "200px");
		// custForm.setWidth("75%");
		// priceLevelSelect.setWidth("150px");
		// refText.setWidth("200px");
		// memoTextAreaItem.setWidth("200px");
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
		return messages.cashSales();
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
		transactionsTree.updateTransactionTreeItemTotals();
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(customerAccountTransactionTable.getRecords());
		list.addAll(customerItemTransactionTable.getRecords());
		return list;
	}

	private void settabIndexes() {
		customerCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		billToTextArea.setTabIndex(4);
		transactionDateItem.setTabIndex(5);
		transactionNumber.setTabIndex(6);
		paymentMethodCombo.setTabIndex(7);
		depositInCombo.setTabIndex(8);
		memoTextAreaItem.setTabIndex(9);
		// menuButton.setTabIndex(10);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(11);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(12);

		cancelButton.setTabIndex(13);

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
		customerItemTransactionTable.updateAmountsFromGUI();
		customerAccountTransactionTable.updateAmountsFromGUI();
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
		return true;
	}

	@Override
	protected void createButtons() {
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
						// if there are more than one branding themes, then show
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
