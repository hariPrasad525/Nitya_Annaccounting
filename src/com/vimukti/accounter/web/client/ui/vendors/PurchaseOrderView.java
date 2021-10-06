package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.HTML;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientBrandingTheme;
import com.vimukti.accounter.web.client.core.ClientCompany;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientShippingMethod;
import com.vimukti.accounter.web.client.core.ClientShippingTerms;
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
import com.vimukti.accounter.web.client.ui.ShipToForm;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.AddressCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingMethodsCombo;
import com.vimukti.accounter.web.client.ui.combo.ShippingTermsCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.BrandingThemeComboAction;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.EmailThemeComboAction;
import com.vimukti.accounter.web.client.ui.core.EmailViewAction;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextAreaItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class PurchaseOrderView extends
		AbstractVendorTransactionView<ClientPurchaseOrder> implements
		IPrintableView {

	private PaymentTermsCombo payTermsSelect;
	private ShippingTermsCombo shippingTermsCombo;
	private ShippingMethodsCombo shippingMethodsCombo;
	private AddressCombo shipToCombo;
	private Button addLinksButton;
	private TextItem linksText;
	private DynamicForm vendorForm;
	private DynamicForm termsForm;
	private ClientAddress shippingAddress;
	protected ClientAddress vendorAddress;
	protected ClientPaymentTerms paymentTerms;
	protected ClientShippingTerms shippingTerms;
	protected ClientShippingMethod shippingMethod;
	private TextAreaItem billtoAreaItem;
	private ShipToForm shipToAddress;

	StyledPanel amountsForm;

	private ArrayList<DynamicForm> listforms;
	private TextItem purchaseOrderText;
	private HTML lab1;
	private List<String> listOfTypes;
	private final String OPEN = messages.open();
	private final String COMPLETED = messages.completed();
	private final String CANCELLED = messages.cancelled();
	private DateField despatchDateItem;
	private final boolean locationTrackingEnabled;
	private VendorAccountTransactionTable vendorAccountTransactionTable;
	private VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	GwtDisclosurePanel accountsDisclosurePanel;
	GwtDisclosurePanel itemsDisclosurePanel;
	private Button emailButton;
	private StyledPanel accountFlowPanel;
	private StyledPanel itemsFlowPanel;

	public PurchaseOrderView() {
		super(ClientTransaction.TYPE_PURCHASE_ORDER);
		locationTrackingEnabled = getCompany().getPreferences()
				.isLocationTrackingEnabled();
		this.getElement().setId("PurchaseOrderView");
	}

	@Override
	protected void createControls() {

		lab1 = new HTML(messages.purchaseOrder());
		lab1.setStyleName("label-title");

		statusSelect = new SelectCombo(messages.status());
		listOfTypes = new ArrayList<String>();
		listOfTypes.add(OPEN);
		listOfTypes.add(COMPLETED);
		listOfTypes.add(CANCELLED);
		statusSelect.initCombo(listOfTypes);
		statusSelect.setComboItem(OPEN);
		statusSelect
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						if (statusSelect.getSelectedValue() != null)
							statusSelect.setComboItem(selectItem);

					}
				});
		statusSelect.setRequired(true);
		statusSelect.setEnabled(!isInViewMode());
		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(messages.orderNo());
		transactionNumber.setWidth(50);

		listforms = new ArrayList<DynamicForm>();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm("dateNoForm");
		dateNoForm.addStyleName("date-number");
		dateNoForm.add(statusSelect, transactionDateItem);

		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateNoForm);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
		labeldateNoLayout.add(datepanel);
		amountsForm = new StyledPanel("amountsForm");

		netAmount = createNetAmountLabel();
		vatinclusiveCheck = getVATInclusiveCheckBox();
		transactionTotalNonEditableText = createTransactionTotalNonEditableItem(getCompany()
				.getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		vatTotalNonEditableText = new TaxItemsForm();

		discountField = getDiscountField();

		// vendorCombo =
		// createVendorComboItem(messages.vendorName());

		StyledPanel prodAndServiceHLay = new StyledPanel("prodAndServiceHLay");

		DynamicForm transactionTotalForm = new DynamicForm(
				"transactionTotalForm");

		DynamicForm form = new DynamicForm("form");

		taxCodeSelect = createTaxCodeSelectItem();
		if (isTrackTax() && isTrackPaidTax()) {

			DynamicForm priceLevelForm = new DynamicForm("priceLevelForm");
			// priceLevelForm.setCellSpacing(4);
			// priceLevelForm.setWidth("70%");
			// priceLevelForm.add(priceLevelSelect);
			if (!isTaxPerDetailLine()) {
				form.add(taxCodeSelect);
			}
			form.add(vatinclusiveCheck);
			prodAndServiceHLay.add(form);

			DynamicForm netAmountForm = new DynamicForm("netAmountForm");
			netAmountForm.add(netAmount);

			amountsForm.add(netAmountForm);
			amountsForm.add(vatTotalNonEditableText);
			if (isMultiCurrencyEnabled()) {

				transactionTotalForm.add(transactionTotalNonEditableText,
						foreignCurrencyamountLabel);
			} else {
				transactionTotalForm.add(transactionTotalNonEditableText);
			}
			amountsForm.setStyleName("boldtext");
			// forms.add(priceLevelForm);
			// prodAndServiceHLay.add(priceLevelForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(priceLevelForm,
			// ALIGN_RIGHT);
			// prodAndServiceHLay.add(amountsForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
			// ALIGN_RIGHT);
			// listforms.add(priceLevelForm);abstracttrans

		} else {

			salesTaxTextNonEditable = new TaxItemsForm();// createSalesTaxNonEditableLabel();
			transactionTotalNonEditableText = createTransactionTotalNonEditableItem(getCompany()
					.getPrimaryCurrency());

			paymentsNonEditableText = new AmountLabel(
					messages.nameWithCurrency(messages.payments(),
							getBaseCurrency().getFormalName()),
					getBaseCurrency());
			paymentsNonEditableText.setEnabled(false);
			paymentsNonEditableText.setDefaultValue(""
					+ UIUtils.getCurrencySymbol() + " 0.00");

			balanceDueNonEditableText = new AmountLabel(
					messages.nameWithCurrency(messages.balanceDue(),
							getBaseCurrency().getFormalName()),
					getBaseCurrency());
			// balanceDueNonEditableText = new
			// AmountField(messages.balanceDue(),
			// this, getBaseCurrency());
			balanceDueNonEditableText.setEnabled(false);
			balanceDueNonEditableText.setDefaultValue(""
					+ UIUtils.getCurrencySymbol() + " 0.00");
			// prodAndServiceForm2.add(salesTaxTextNonEditable,
			// transactionTotalNonEditableText, ,
			// balanceDueNonEditableText, taxCodeSelect, priceLevelSelect);

			amountsForm.addStyleName("boldtext");
			if (isMultiCurrencyEnabled()) {
				transactionTotalForm.add(transactionTotalNonEditableText,
						foreignCurrencyamountLabel);
			} else {
				transactionTotalForm.add(transactionTotalNonEditableText);
			}

			// prodAndServiceHLay.add(amountsForm);
			// prodAndServiceHLay.setCellHorizontalAlignment(amountsForm,
			// ALIGN_RIGHT);
		}

		if (!isDiscountPerDetailLine() && isTrackDiscounts()) {
			form.add(discountField);
		}
		amountsForm.add(transactionTotalForm);

		prodAndServiceHLay.add(amountsForm);

		vendorCombo = new VendorCombo(Global.get().Vendor(), true);
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
		// vendorCombo.setWidth(100);
		contactCombo = createContactComboItem();
		contactCombo.setTitle(messages.contact());

		// contactCombo.setWidth(100);
		// billToCombo = createVendorAddressComboItem();
		// billToCombo.setTitle(FinanceApplication.constants().billTo());
		billtoAreaItem = new TextAreaItem(messages.billTo(), "billtoAreaItem");
		billtoAreaItem.setEnabled(false);

		// shipToCombo = createShipToComboItem();
		shipToAddress = new ShipToForm(null);
		shipToAddress.addrArea.setDisabled(true);
		shipToAddress.businessSelect.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				shippingAddress = shipToAddress.getAddress();
				if (shippingAddress != null)
					shipToAddress.setAddres(shippingAddress);
				else
					shipToAddress.addrArea.setValue("");
			}
		});
		if (isInViewMode())
			shipToAddress.businessSelect.setEnabled(false);
		phoneSelect = new TextItem(messages.phone(), "phoneSelect");
		phoneSelect.setToolTip(messages.phoneNumber());
		phoneSelect.setEnabled(true);

		// formItems.add(phoneSelect);

		vendorForm = UIUtils.form(Global.get().Vendor());
		// vendorForm.setWidth("100%");
		vendorForm.add(vendorCombo, contactCombo, phoneSelect, billtoAreaItem);
		// vendorForm.getCellFormatter().setWidth(0, 0, "226px");

		// formItems.add(billToCombo);

		purchaseOrderText = new TextItem(messages.payeeOrderNo(Global.get()
				.Vendor()), "purchaseOrderText");
		purchaseOrderText.setEnabled(!isInViewMode());

		payTermsSelect = createPaymentTermsSelectItem();

		shippingTermsCombo = createShippingTermsCombo();

		shippingMethodsCombo = createShippingMethodCombo();

		despatchDateItem = new DateField(messages.dispatchDate(),
				"despatchDateItem");
		despatchDateItem.setEnabled(!isInViewMode());
		if (isInViewMode()) {
		} else
			setDespatchDate(new ClientFinanceDate().getDate());

		despatchDateItem.addChangedHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				try {
					ClientFinanceDate newDate = ((DateField) event.getSource())
							.getValue();
					setDespatchDate(newDate.getDate());
				} catch (Exception e) {
					Accounter.showError(messages.invalidDispatchDate());
				}

			}

		});

		deliveryDateItem = createTransactionDeliveryDateItem();

		DynamicForm dateform = new DynamicForm("dateform");
		if (locationTrackingEnabled)
			dateform.add(locationCombo);
		dateform.add( /* despatchDateItem, */deliveryDateItem);

		termsForm = new DynamicForm("termsForm");
		termsForm.add(transactionNumber, purchaseOrderText, payTermsSelect);
		if (getPreferences().isDoProductShipMents()) {
			termsForm.add(shippingTermsCombo, shippingMethodsCombo);
		}
		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			termsForm.add(classListCombo);
		}
		// termsForm.getCellFormatter().setWidth(0, 0, "208px");
		// dateform.getCellFormatter().setWidth(0, 0, "230px");

		// formItems.add(checkNo);
		// formItems.add(dueDateItem);
		// formItems.add(despatchDateItem);
		// formItems.add(deliveryDateItem);

		// Label lab2 = new Label(messages.itemsAndExpenses());
		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this, isCustomerAllowedToAdd(),
				isTrackClass(), isClassPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				PurchaseOrderView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return PurchaseOrderView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return PurchaseOrderView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				PurchaseOrderView.this.updateNonEditableItems();
			}
		};

		vendorAccountTransactionTable.setEnabled(!isInViewMode());

		this.accountFlowPanel = new StyledPanel("accountFlowPanel");
		accountsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		accountsDisclosurePanel.setTitle(messages.ItemizebyAccount());
		accountFlowPanel.add(vendorAccountTransactionTable);

		accountsDisclosurePanel.setContent(accountFlowPanel);
		vendorItemTransactionTable = new VendorItemTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this, isCustomerAllowedToAdd(),
				isTrackClass(), isClassPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				PurchaseOrderView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return PurchaseOrderView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return PurchaseOrderView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				PurchaseOrderView.this.updateNonEditableItems();
			}

			@Override
			protected int getTransactionType() {
				return ClientTransaction.TYPE_PURCHASE_ORDER;
			}

		};

		vendorItemTransactionTable.setEnabled(!isInViewMode());

		currencyWidget = createCurrencyFactorWidget();

		this.itemsFlowPanel = new StyledPanel("itemsFlowPanel");

		itemsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		itemsDisclosurePanel.setTitle(messages.ItemizebyProductService());
		itemsFlowPanel.add(vendorItemTransactionTable);

		itemsDisclosurePanel.setContent(itemsFlowPanel);
		memoTextAreaItem = createMemoTextAreaItem();

		// refText = createRefereceText();
		// refText.setWidth(100);
		addLinksButton = new Button(messages.addLinks());
		// FIXME--need to disable basing on the mode of the view being opened

		// addLinksButton.setEnabled(true);
		linksText = new TextItem("", "linksText");
		linksText.setShowTitle(false);
		linksText.setEnabled(!isInViewMode());
		// formItems.add(linksText);

		DynamicForm memoForm = new DynamicForm("memoForm");
		// memoForm.setWidth("80%");
		memoForm.add(memoTextAreaItem);
		// memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		DynamicForm linksform = new DynamicForm("linksform");
		linksform.add(linksText);
		StyledPanel linkspanel = new StyledPanel("linkspanel");
		linkspanel.add(addLinksButton);
		addLinksButton.setEnabled(isInViewMode());
		linkspanel.add(linksform);

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		leftVLay.add(vendorForm);
		if (getPreferences().isDoProductShipMents()) {
			leftVLay.add(shipToAddress);
		}
		StyledPanel rightVLay = new StyledPanel("rightVLay");
		// rightVLay.setWidth("93%");
		rightVLay.add(termsForm);
		rightVLay.add(dateform);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}
		// rightVLay.setCellHorizontalAlignment(termsForm, ALIGN_RIGHT);
		// rightVLay.setCellHorizontalAlignment(dateform, ALIGN_RIGHT);

		// topHLay.setCellHorizontalAlignment(rightVLay, ALIGN_RIGHT);

		StyledPanel panel = new StyledPanel("panel");
		panel.add(memoForm);
		panel.add(prodAndServiceHLay);

		// panel.setHorizontalAlignment(ALIGN_RIGHT);

		StyledPanel bottomLayout = new StyledPanel("bottomLayout");

		bottomLayout.add(panel);
		// bottomLayout.add(linkspanel);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		mainVLay.add(labeldateNoLayout);
		StyledPanel topHLay = getTopLayout();
		if (topHLay != null) {
			topHLay.add(leftVLay);
			topHLay.add(rightVLay);
			mainVLay.add(topHLay);
		} else {
			mainVLay.add(leftVLay);
			mainVLay.add(rightVLay);
		}
		// mainVLay.add(lab2);

		mainVLay.add(accountsDisclosurePanel.getPanel());
		mainVLay.add(itemsDisclosurePanel.getPanel());
		// mainVLay.add(createAddNewButton());
		// menuButton.getElement().getStyle().setMargin(5, Unit.PX);
		mainVLay.add(bottomLayout);

		if (UIUtils.isMSIEBrowser()) {
			// vendorForm.getCellFormatter().setWidth(0, 1, "200px");
			// vendorForm.setWidth("75%");
			// // termsForm.getCellFormatter().setWidth(0, 1, "68%");
			// memoForm.getCellFormatter().setWidth(0, 1, "300px");
			// memoForm.setWidth("40%");
			// statusSelect.setWidth("150px");
		}
		// setOverflow(Overflow.SCROLL);
		this.add(mainVLay);
		// addChild(mainVLay);

		setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(dateform);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(linksform);
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}
		// settabIndexes();
	}

	protected StyledPanel getTopLayout() {
		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("fields-panel");
		return topHLay;
	}

	private PaymentTermsCombo createPaymentTermsSelectItem() {

		PaymentTermsCombo comboItem = new PaymentTermsCombo(
				messages.paymentTerms());

		comboItem
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					@Override
					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {
						if (selectItem != null) {
							paymentTerms = selectItem;
							paymentTermsSelected(paymentTerms);
						}

					}

				});
		comboItem.setEnabled(!isInViewMode());
		// comboItem.setShowDisabled(false);
		//
		return comboItem;
	}

	private ShippingTermsCombo createShippingTermsCombo() {

		ShippingTermsCombo shippingTermsCombo = new ShippingTermsCombo(
				messages.shippingTerms());

		shippingTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingTerms>() {

					@Override
					public void selectedComboBoxItem(
							ClientShippingTerms selectItem) {
						if (selectItem != null)
							shippingTerms = selectItem;
					}

				});

		shippingTermsCombo.setEnabled(!isInViewMode());

		// formItems.add(shippingTermsCombo);

		return shippingTermsCombo;
	}

	protected ShippingMethodsCombo createShippingMethodCombo() {

		ShippingMethodsCombo shippingMethodsCombo = new ShippingMethodsCombo(
				messages.shippingMethod());

		shippingMethodsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientShippingMethod>() {

					@Override
					public void selectedComboBoxItem(
							ClientShippingMethod selectItem) {
						if (selectItem != null)
							shippingMethod = selectItem;
					}

				});

		shippingMethodsCombo.setEnabled(!isInViewMode());

		// formItems.add(shippingMethodsCombo);

		return shippingMethodsCombo;

	}

	protected AddressCombo createShipToComboItem() {

		AddressCombo shipToCombo = new AddressCombo(messages.shipTo());

		shipToCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					@Override
					public void selectedComboBoxItem(ClientAddress selectItem) {
						shipToAddressSelected(selectItem);
					}

				});

		shipToCombo.setEnabled(!isInViewMode());
		// shipToCombo.setShowDisabled(false);
		if (getPreferences().isDoProductShipMents()) {
			// formItems.add(shipToCombo);
		}
		return shipToCombo;

	}

	public AddressCombo createVendorAddressComboItem() {

		AddressCombo addressCombo = new AddressCombo(
				messages.payeeAddress(Global.get().Vendor()));

		addressCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAddress>() {

					@Override
					public void selectedComboBoxItem(ClientAddress selectItem) {

						if (selectItem != null)
							vendoraddressSelected(selectItem);
					}

				});

		addressCombo.setEnabled(!isInViewMode());
		// addressCombo.setShowDisabled(false);

		return addressCombo;

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientPurchaseOrder());
			super.initTransactionViewData();
			initVendorAddressCombo();
			initShipToCombo();
			initPaymentTerms();
			initShippingTerms();
			initShippingMethod();
		} else {
			if (currencyWidget != null) {
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
			// taxCodeSelected(this.taxCode);
			ClientCompany company = getCompany();

			vendorAccountTransactionTable
					.setAllRows(getAccountTransactionItems(transaction
							.getTransactionItems()));
			vendorItemTransactionTable
					.setAllRows(getItemTransactionItems(transaction
							.getTransactionItems()));

			// String status;
			// if (purchaseOrderToBeEdited.getStatus() ==
			// ClientPurchaseOrder.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			// status = "Un-Applied";
			// else if (purchaseOrderToBeEdited.getStatus() ==
			// ClientPurchaseOrder.STATUS_PARTIALLY_PAID_OR_PARTIALLY_APPLIED)
			// status = "Partially Applied";
			// else
			// status = "Applied";
			// lab1.setText(FinanceApplication.constants().purchaseOrder()
			// + " (" + status + ")");

			this.transactionItems = transaction.getTransactionItems();
			this.setVendor(company.getVendor(transaction.getVendor()));
			vendorCombo.setComboItem(vendor);
			vendorItemTransactionTable.setPayee(vendor);
			// vendorSelected(company.getVendor(transaction.getVendor()));
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			phoneSelect.setEnabled(!isInViewMode());
			// vendoraddressSelected(purchaseOrderToBeEdited.getVendorAddress());
			// shipToAddressSelected(purchaseOrderToBeEdited.getShippingAddress());

			List<ClientAddress> addresses = new ArrayList<ClientAddress>();
			this.billingAddress = transaction.getVendorAddress();
			this.shippingAddress = transaction.getShippingAddress();
			if (getVendor() != null)
				addresses.addAll(getVendor().getAddress());
			shipToAddress.setListOfCustomerAdress(addresses);

			if (shippingAddress != null) {
				shipToAddress.businessSelect.setValue(shippingAddress
						.getAddressTypes().get(shippingAddress.getType()));
				shipToAddress.setAddres(shippingAddress);
				ClientAddress add = new ClientAddress();
				shipToAddress.businessSelect.setValue(add.getAddressTypes()
						.get(1));
			}
			shipToAddress.businessSelect.setEnabled(false);
			if (getVendor() != null) {
				this.addressListOfVendor = getVendor().getAddress();
			}
			if (billingAddress != null) {
				billtoAreaItem.setValue(billingAddress.getAddress1() + "\n"
						+ billingAddress.getStreet() + "\n"
						+ billingAddress.getCity() + "\n"
						+ billingAddress.getStateOrProvinence() + "\n"
						+ billingAddress.getZipOrPostalCode() + "\n"
						+ billingAddress.getCountryOrRegion());

			}
			if (billingAddress != null) {

				billtoAreaItem.setValue(getValidAddress(billingAddress));

			} else
				billtoAreaItem.setValue("");
			if (isTrackTax() && isTrackPaidTax()) {
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(isAmountIncludeTAX());
				}
				if (!isTaxPerDetailLine()) {
					selectTAXCode();
				}
			}

			purchaseOrderText.setValue(transaction.getPurchaseOrderNumber());

			paymentTermsSelected(company.getPaymentTerms(transaction
					.getPaymentTerm()));
			shippingTermsSelected(company.getShippingTerms(transaction
					.getShippingTerms()));
			shippingMethodSelected(company.getShippingMethod(transaction
					.getShippingMethod()));
			despatchDateItem.setEnteredDate(new ClientFinanceDate(transaction
					.getDespatchDate()));
			deliveryDateItem.setEnteredDate(new ClientFinanceDate(transaction
					.getDeliveryDate()));
			memoTextAreaItem.setValue(transaction.getMemo());
			memoTextAreaItem.setDisabled(isInViewMode());
			// refText.setValue(purchaseOrderToBeEdited.getReference());
			int status = transaction.getStatus();
			switch (status) {
			case ClientTransaction.STATUS_OPEN:
				statusSelect.setComboItem(OPEN);
				break;
			case ClientTransaction.STATUS_COMPLETED:
				statusSelect.setComboItem(COMPLETED);
				break;
			case ClientTransaction.STATUS_CANCELLED:
				statusSelect.setComboItem(CANCELLED);
			default:
				break;
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
		}
		if (isTrackClass() && !isClassPerDetailLine()) {
			this.accounterClass = getClassForTransactionItem(this.transactionItems);
			if (accounterClass != null) {
				this.classListCombo.setComboItem(accounterClass);
				classSelected(accounterClass);
			}
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
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

	private void initDeliveryDate() {

		if (isInViewMode()) {
			ClientPurchaseOrder purchaseOrder = transaction;
			deliveryDateItem.setEnteredDate(new ClientFinanceDate(purchaseOrder
					.getDeliveryDate()));

		}

	}

	private void initVendorAddressCombo() {

		if (billToCombo == null || addressListOfVendor == null)
			return;

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;
		for (ClientAddress address : addressListOfVendor) {

			if (address.getType() == ClientAddress.TYPE_BILL_TO) {

				tempSet.add(address);
				clientAddress = address;
				break;
			}

		}
		List<ClientAddress> adressList = new ArrayList<ClientAddress>();
		adressList.addAll(tempSet);
		billToCombo.initCombo(adressList);
		billToCombo.setEnabled(!isInViewMode());
		// billToCombo.setShowDisabled(false);

		if (isInViewMode() && billingAddress != null) {
			billToCombo.setComboItem(billingAddress);
			return;
		}
		if (clientAddress != null) {
			billToCombo.setComboItem(clientAddress);
			billToaddressSelected(clientAddress);

		} else {
			billToCombo.setComboItem(null);
			billToaddressSelected(clientAddress);
		}
	}

	private void initShipToCombo() {

		if (shipToCombo == null || addressListOfVendor == null)
			return;
		// ClientCompany company = FinanceApplication.getCompany();
		//
		// List<ClientAddress> listOfAddress = company.getAddresses();

		Set<ClientAddress> tempSet = new HashSet<ClientAddress>();
		ClientAddress clientAddress = null;
		for (ClientAddress address : addressListOfVendor) {

			if (address.getType() == ClientAddress.TYPE_SHIP_TO) {

				tempSet.add(address);
				clientAddress = address;
				break;
			}

		}

		List<ClientAddress> adressList = new ArrayList<ClientAddress>();
		adressList.addAll(tempSet);
		shipToCombo.initCombo(adressList);
		shipToCombo.setEnabled(!isInViewMode());

		if (isInViewMode() && shippingAddress != null) {
			shipToCombo.setComboItem(shippingAddress);
			return;
		}
		if (clientAddress != null) {
			shipToCombo.setComboItem(clientAddress);
			shipToAddressSelected(clientAddress);

		} else {
			shipToCombo.setComboItem(null);
			shipToAddressSelected(clientAddress);
		}

	}

	private void initPaymentTerms() {

		payTermsSelect.initCombo(getCompany().getPaymentsTerms());

	}

	private void initShippingTerms() {

		shippingTermsCombo.initCombo(getCompany().getShippingTerms());

	}

	private void initShippingMethod() {

		List<ClientShippingMethod> result = getCompany().getShippingMethods();
		if (shippingMethodsCombo != null) {
			shippingMethodsCombo.initCombo(result);

		}

	}

	@Override
	protected void initMemoAndReference() {

		if (isInViewMode()) {

			memoTextAreaItem.setValue(transaction.getMemo());
			// refText.setValue(salesOrder.getReference());

		}

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
		transactionTotalNonEditableText
				.setAmount(getAmountInBaseCurrency(grandTotal));
		foreignCurrencyamountLabel.setAmount(grandTotal);

		netAmount.setAmount(lineTotal);
		// vatTotalNonEditableText.setValue(vendorTransactionGrid.getVatTotal());
		if (getPreferences().isTrackPaidTax()) {
			if ((transaction.getTransactionItems() != null) && !isInViewMode()) {
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
	public void saveAndUpdateView() {

		super.saveAndUpdateView();

		saveOrUpdate(transaction);

		// if (isTrackTax()) {
		// netAmount.setAmount(transaction.getNetAmount());
		// vatTotalNonEditableText.setAmount(transaction.getTotal()
		// - transaction.getNetAmount());
		// transactionTotalNonEditableText.setAmount(transaction.getTotal());
		// }

	}

	@Override
	public ClientPurchaseOrder saveView() {
		ClientPurchaseOrder saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		if (getVendor() != null) {
			transaction.setVendor(getVendor().getID());
		}

		if (statusSelect.getSelectedValue().equals(OPEN))
			transaction.setStatus(ClientTransaction.STATUS_OPEN);
		else if (statusSelect.getSelectedValue().equals(COMPLETED))
			transaction.setStatus(ClientTransaction.STATUS_COMPLETED);
		else if (statusSelect.getSelectedValue().equals(CANCELLED))
			transaction.setStatus(ClientTransaction.STATUS_CANCELLED);

		if (contact != null)
			transaction.setContact(contact);
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);
		if (shippingAddress != null)
			transaction.setShippingAddress(shippingAddress);

		if (purchaseOrderText.getValue() != null)
			transaction.setPurchaseOrderNumber(purchaseOrderText.getValue()
					.toString());
		if (paymentTerms != null)
			transaction.setPaymentTerm(paymentTerms.getID());
		if (shippingTerms != null)
			transaction.setShippingTerms(shippingTerms.getID());
		if (shippingMethod != null)
			transaction.setShippingMethod(shippingMethod.getID());
		if (despatchDateItem.getEnteredDate() != null) {
			transaction.setDespatchDate(despatchDateItem.getEnteredDate()
					.getDate());
		}
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate((deliveryDateItem.getEnteredDate()
					.getDate()));

		transaction.setMemo(getMemoTextAreaItem());
		transaction.setNetAmount(vendorAccountTransactionTable.getLineTotal()
				+ vendorItemTransactionTable.getLineTotal());
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());
		// transaction.setReference(getRefText());

		if (isTrackTax()) {
			setAmountIncludeTAX();
		}

		if (discountField.getPercentage() != 0.0 && transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setDiscount(discountField.getPercentage());
			}
		}
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		this.setVendor(vendor);
		if (vendor == null)
			return;
		vendorItemTransactionTable.setPayee(vendor);
		super.vendorSelected(vendor);
		if (vendor.getPhoneNo() != null)
			phoneSelect.setValue(vendor.getPhoneNo());
		else
			phoneSelect.setValue("");
		this.addressListOfVendor = vendor.getAddress();
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billtoAreaItem.setValue(billingAddress.getAddress1() + "\n"
					+ billingAddress.getStreet() + "\n"
					+ billingAddress.getCity() + "\n"
					+ billingAddress.getStateOrProvinence() + "\n"
					+ billingAddress.getZipOrPostalCode() + "\n"
					+ billingAddress.getCountryOrRegion());

		} else
			billtoAreaItem.setValue("");
		shippingAddress = getAddress(ClientAddress.TYPE_SHIP_TO);
		List<ClientAddress> addresses = new ArrayList<ClientAddress>();
		addresses.addAll(vendor.getAddress());
		shipToAddress.setAddress(addresses);

		initVendorAddressCombo();
		initShipToCombo();

		ClientCompany company = getCompany();
		paymentTerms = company.getPaymentTerms(vendor.getPaymentTerms());
		shippingMethod = company.getShippingMethod(vendor.getShippingMethod());
		if (paymentTerms != null) {
			payTermsSelect.setComboItem(paymentTerms);
			paymentTermsSelected(paymentTerms);
		}

		long currency = vendor.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			currencyWidget.setSelectedCurrencyFactorInWidget(clientCurrency,
					transactionDateItem.getValue().getDate());
		} else {
			ClientCurrency clientCurrency = getCompany().getPrimaryCurrency();
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}
		shippingMethodsCombo.setComboItem(shippingMethod);
		vendorCombo.setComboItem(vendor);

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(getCompany().getCurrency(vendor.getCurrency()));
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
			modifyForeignCurrencyTotalWidget();
		}
	}

	private void vendoraddressSelected(ClientAddress selectedAddress) {
		if (selectedAddress == null)
			return;
		this.vendorAddress = selectedAddress;
		billToCombo.setComboItem(selectedAddress);

	}

	protected void shipToAddressSelected(ClientAddress selectedAddress) {
		if (selectedAddress == null)
			return;
		this.shippingAddress = selectedAddress;
		shipToCombo.setComboItem(selectedAddress);

	}

	private void paymentTermsSelected(ClientPaymentTerms paymentTerms) {
		if (paymentTerms != null) {
			this.paymentTerms = paymentTerms;
			payTermsSelect.setComboItem(paymentTerms);
		}
	}

	private void shippingTermsSelected(ClientShippingTerms shippingTerms) {
		if (shippingTerms != null) {
			this.shippingTerms = shippingTerms;
			shippingTermsCombo.setComboItem(shippingTerms);
		}
	}

	private void shippingMethodSelected(ClientShippingMethod shippingMethod) {
		if (shippingMethod != null) {
			this.shippingMethod = shippingMethod;
			shippingMethodsCombo.setComboItem(shippingMethod);
		}
	}

	private void setDespatchDate(long date) {
		despatchDateItem.setEnteredDate(new ClientFinanceDate(date));
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

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void fitToSize(int height, int width) {

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// TODO:: is it required to validate transaction date?yes
		// TODO:: do we require validation for dispatchdate,
		// receiveddate?dispatchDate is not required
		// 1. isValid transaction date?
		// 2. is in prevent posting before date?
		// 3. statusSelect valid? is valid received date?
		// 4. is valid due date?
		// 5. vendon form valid?
		// 6. is blank transaction?
		// 7. vendor transaction grid valid?
		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// messages.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}

		// TODO::: isvalid received date
		/*
		 * if (!AccounterValidator.isValidPurchaseOrderRecievedDate(
		 * deliveryDateItem.getDate(), transactionDate)) {
		 * result.addError(deliveryDateItem,
		 * messages.receivedDateShouldNotBeAfterTransactionDate()); }
		 */

		if (!statusSelect.validate()) {
			result.addError(statusSelect, statusSelect.getTitle());
		}

		result.add(vendorForm.validate());

		if (getAllTransactionItems().isEmpty()) {
			result.addError(vendorAccountTransactionTable,
					messages.blankTransaction());
		} else {
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
		}

		// if (getCompany().getAccountingType() !=
		// ClientCompany.ACCOUNTING_TYPE_UK
		// && getCompany().getPreferences().getDoYouPaySalesTax()) {
		// if (taxCodeSelect != null && !taxCodeSelect.validate()) {
		// result.addError(taxCodeSelect,
		// messages.pleaseEnter(taxCodeSelect.getTitle()));
		// }
		// }

		return result;
	}

	@Override
	public void onEdit() {
		if (transaction.getStatus() == ClientTransaction.STATUS_COMPLETED)
			Accounter.showError("Completed purchase order can't be edited");
		else {
			AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

				@Override
				public void onException(AccounterException caught) {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
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

	}

	protected void enableFormItems() {
		setMode(EditMode.EDIT);

		if (emailButton != null) {
			getButtonBar().remove(emailButton);
		}

		statusSelect.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		shipToAddress.businessSelect.setEnabled(!isInViewMode());
		// shipToCombo.setEnabled(!isEdit);
		ClientTransactionItem item = new ClientTransactionItem();
		if (!DecimalUtil.isEquals(
				item.getInvoiced() == null ? 0 : item.getInvoiced(), 0)) {
			vendorCombo.setEnabled(!isInViewMode());
		} else {
			vendorCombo.setEnabled(false);
			if (vendor != null) {
				if (this.transaction.getVendorAddress() == null) {
					this.addressListOfVendor = vendor.getAddress();
					billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
					if (billingAddress != null) {
						billtoAreaItem.setValue(billingAddress.getAddress1()
								+ "\n" + billingAddress.getStreet() + "\n"
								+ billingAddress.getCity() + "\n"
								+ billingAddress.getStateOrProvinence() + "\n"
								+ billingAddress.getZipOrPostalCode() + "\n"
								+ billingAddress.getCountryOrRegion());

					} else {
						billtoAreaItem.setValue("");
					}
				}
				if (this.transaction.getPhone() == null
						|| this.transaction.getPhone().isEmpty()) {
					initPhones(vendor);
				}

				if (this.transaction.getContact() == null) {
					initContacts(vendor);
				}
			}

		}

		// billToCombo.setEnabled(!isEdit);
		purchaseOrderText.setEnabled(!isInViewMode());
		deliveryDateItem.setEnabled(!isInViewMode());
		payTermsSelect.setEnabled(!isInViewMode());

		shippingTermsCombo.setEnabled(!isInViewMode());
		shippingMethodsCombo.setEnabled(!isInViewMode());

		despatchDateItem.setEnabled(!isInViewMode());

		vendorAccountTransactionTable.setEnabled(!isInViewMode());
		vendorItemTransactionTable.setEnabled(!isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		memoTextAreaItem.setEnabled(!isInViewMode());
		vendorCombo.setEnabled(!isInViewMode());
		taxCodeSelect.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		discountField.setEnabled(!isInViewMode());
		classListCombo.setEnabled(!isInViewMode());
		super.onEdit();
	}

	@Override
	public void print() {
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
					ClientTransaction.TYPE_PURCHASE_ORDER,
					brandingTheme.getID());

		}
	}

	@Override
	public void printPreview() {

	}

	@Override
	protected Double getTransactionTotal() {
		return null;
	}

	@Override
	protected String getViewTitle() {
		return messages.purchaseOrder();
	}

	@Override
	protected void taxCodeSelected(ClientTAXCode taxCode) {
		this.taxCode = taxCode;
		if (taxCode != null) {
			taxCodeSelect.setComboItem(taxCode);
			vendorAccountTransactionTable.setTaxCode(taxCode.getID(), true);
			vendorItemTransactionTable.setTaxCode(taxCode.getID(), true);
		} else
			taxCodeSelect.setValue("");
		// updateNonEditableItems();
	}

	@Override
	protected void addAllRecordToGrid(
			List<ClientTransactionItem> transactionItems) {
		vendorAccountTransactionTable
				.setAllRows(getAccountTransactionItems(transactionItems));
		vendorItemTransactionTable
				.setAllRows(getItemTransactionItems(transactionItems));
	}

	@Override
	protected void removeAllRecordsFromGrid() {
		vendorAccountTransactionTable.removeAllRecords();
		vendorItemTransactionTable.removeAllRecords();
	}

	@Override
	protected void addNewData(ClientTransactionItem transactionItem) {
		vendorAccountTransactionTable.add(transactionItem);

	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	@Override
	protected void refreshTransactionGrid() {
		vendorAccountTransactionTable.updateTotals();
		vendorItemTransactionTable.updateTotals();
	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		billtoAreaItem.setTabIndex(4);
		statusSelect.setTabIndex(5);
		transactionDateItem.setTabIndex(6);
		transactionNumber.setTabIndex(7);
		purchaseOrderText.setTabIndex(8);
		payTermsSelect.setTabIndex(9);
		despatchDateItem.setTabIndex(10);
		deliveryDateItem.setTabIndex(11);
		memoTextAreaItem.setTabIndex(12);
		// menuButton.setTabIndex(14);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(13);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(14);
		cancelButton.setTabIndex(15);
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
	public void updateAmountsFromGUI() {
		modifyForeignCurrencyTotalWidget();
		vendorItemTransactionTable.updateAmountsFromGUI();
		vendorAccountTransactionTable.updateAmountsFromGUI();
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
	protected void classSelected(ClientAccounterClass clientAccounterClass) {

		this.accounterClass = clientAccounterClass;
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
