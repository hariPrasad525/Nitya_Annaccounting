package com.nitya.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.AddNewButton;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientAddress;
import com.nitya.accounter.web.client.core.ClientCashPurchase;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientPurchaseOrder;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionItem;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.GwtDisclosurePanel;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.IPrintableView;
import com.nitya.accounter.web.client.ui.core.TaxItemsForm;
import com.nitya.accounter.web.client.ui.edittable.TransactionsTree;
import com.nitya.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.nitya.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextAreaItem;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * @author vimukti1
 * 
 */
public class CashPurchaseView extends
		AbstractVendorTransactionView<ClientCashPurchase> implements
		IPrintableView {

	protected DynamicForm vendorForm;
	protected DynamicForm termsForm;
	public List<String> selectedComboList;
	private ArrayList<DynamicForm> listforms;
	protected Label titlelabel;
	private TextAreaItem billToAreaItem;
	protected VendorAccountTransactionTable vendorAccountTransactionTable;
	protected VendorItemTransactionTable vendorItemTransactionTable;
	protected AddNewButton accountTableButton, itemTableButton;
	protected GwtDisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;
	private TextItem checkNoText;
	private CheckboxItem printCheck;
	private boolean isChecked = false;
	TransactionsTree<PurchaseOrdersAndItemReceiptsList> transactionsTree;
	private StyledPanel accountFlowPanel;
	private StyledPanel itemsFlowPanel;

	// private WarehouseAllocationTable inventoryTransactionTable;
	// private DisclosurePanel inventoryDisclosurePanel;

	public CashPurchaseView() {
		super(ClientTransaction.TYPE_CASH_PURCHASE);
		this.getElement().setId("CashPurchaseView");
	}

	protected CashPurchaseView(int type) {
		super(type);
	}

	@Override
	protected void createControls() {

		titlelabel = new Label(messages.cashPurchase());
		titlelabel.setStyleName("label-title");
		// titlelabel.setHeight("50px");
		listforms = new ArrayList<DynamicForm>();

		transactionDateItem = createTransactionDateItem();
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							deliveryDateItem.setEnteredDate(date);
							setTransactionDate(date);
						}
					}
				});
		// transactionDateItem.setWidth(100);

		transactionNumber = createTransactionNumberItem();
		locationCombo = createLocationCombo();
		DynamicForm dateNoForm = new DynamicForm("dateNoForm");
		dateNoForm.setStyleName("datenumber-panel");
		if (!isTemplate) {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}
		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateNoForm);
		datepanel.getElement().getStyle().setPaddingRight(25, Unit.PX);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");

		// labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.add(datepanel);

		if (this.isInViewMode())
			// --the form need to be disabled here
			dateNoForm.setEnabled(false);

		// formItems.add(transactionDateItem);
		// formItems.add(transactionNumber);

		vendorCombo = createVendorComboItem(messages.payeeName(Global.get()
				.Vendor()));
		vendorCombo.setRequired(false);
		// vendorCombo.setWidth(100);
		contactCombo = createContactComboItem();
		// contactCombo.setWidth(100);
		billToAreaItem = new TextAreaItem(messages.billTo(), "billToAreaItem");
		// billToAreaItem.setWidth(100);
		billToAreaItem.setDisabled(true);
		phoneSelect = new TextItem(messages.phone(), "phoneSelect");
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		// phoneSelect.setWidth(100);
		if (isInViewMode())
			phoneSelect.setEnabled(false);

		vendorForm = UIUtils.form(Global.get().Vendor());

		// vendorForm.setWidth("100%");
		vendorForm.add(vendorCombo, contactCombo, phoneSelect, billToAreaItem);
		// vendorForm.getCellFormatter().setWidth(0, 0, "160px");
		// formItems.add(contactCombo);
		// formItems.add(billToCombo);

		payFromCombo = createPayFromCombo(messages.payFrom());
		// payFromCombo.setWidth(100);
		// payFromCombo.setPopupWidth("500px");
		// checkNo = createCheckNumberItem(messages.chequeNo());
		// checkNo.setDisabled(true);
		// checkNo.setWidth(100);
		deliveryDateItem = createTransactionDeliveryDateItem();
		// deliveryDateItem.setWidth(100);

		paymentMethodCombo = createPaymentMethodSelectItem();
		// paymentMethodCombo.setWidth(100);
		paymentMethodCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentMethodSelected(paymentMethodCombo
								.getSelectedValue());
					}
				});

		printCheck = new CheckboxItem(messages.toBePrinted(), "printCheck");
		printCheck.setValue(true);
		// printCheck.setWidth(100);
		printCheck.setEnabled(false);
		printCheck.addChangeHandler(new ValueChangeHandler<Boolean>() {

			@Override
			public void onValueChange(ValueChangeEvent<Boolean> event) {
				isChecked = event.getValue();
				if (isChecked) {
					if (printCheck.getValue().toString()
							.equalsIgnoreCase("true")) {
						checkNoText.setValue(messages.toBePrinted());
						checkNoText.setEnabled(false);
					} else {
						if (payFromCombo.getValue() == null)
							checkNoText.setValue(messages.toBePrinted());
						else if (transaction != null) {
							checkNoText.setValue(transaction.getCheckNumber());
						}
					}
				} else
					checkNoText.setValue("");
				checkNoText.setEnabled(true);

			}
		});

		checkNoText = new TextItem(messages.chequeNo(), "checkNoText");
		checkNoText.setValue(messages.toBePrinted());
		// checkNoText.setWidth(100);
		if (paymentMethodCombo.getSelectedValue() != null
				&& !paymentMethodCombo.getSelectedValue().equals(
						UIUtils.getpaymentMethodCheckBy_CompanyType(messages
								.check())))
			checkNoText.setEnabled(false);
		checkNoText.addChangeHandler(new ChangeHandler() {

			@Override
			public void onChange(ChangeEvent event) {
				checkNumber = checkNoText.getValue().toString();
			}
		});

		termsForm = new DynamicForm("termsForm");
		if (locationTrackingEnabled)
			termsForm.add(locationCombo);
		// termsForm.setWidth("100%");
		if (isTemplate) {
			termsForm.add(paymentMethodCombo, printCheck, checkNoText,
					payFromCombo);
		} else {
			termsForm.add(paymentMethodCombo, printCheck, checkNoText,
					payFromCombo, deliveryDateItem);
		}

		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			termsForm.add(classListCombo);
		}
		// termsForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "203px");

		// formItems.add(checkNo);
		// formItems.add(deliveryDateItem);

		Label lab2 = new Label(messages.itemsAndExpenses());
		// menuButton = createAddNewButton();

		netAmount = new AmountLabel(
				messages.currencyNetAmount(getBaseCurrency().getFormalName()),
				getBaseCurrency());
		netAmount.setDefaultValue("£0.00");
		netAmount.setEnabled(false);

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem(getCompany()
				.getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		vatTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();
		taxCodeSelect = createTaxCodeSelectItem();
		transactionsTree = new TransactionsTree<PurchaseOrdersAndItemReceiptsList>(
				this) {
			HashMap<ClientTransactionItem, ClientTransactionItem> clonesObjs = new HashMap<ClientTransactionItem, ClientTransactionItem>();

			@Override
			public void updateTransactionTotal() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashPurchaseView.this.updateNonEditableItems();
			}

			@Override
			public void setTransactionDate(ClientFinanceDate transactionDate) {
				CashPurchaseView.this.setTransactionDate(transactionDate);
			}

			@Override
			public boolean isinViewMode() {
				return !(CashPurchaseView.this.isInViewMode());
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
						vendorItemTransactionTable.delete(cloned);
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
					for (ClientTransactionItem cItem : vendorItemTransactionTable
							.getAllRows()) {
						if (cItem.isEmpty()) {
							vendorItemTransactionTable.delete(cItem);
							emptyRows++;
						}
					}
					vendorItemTransactionTable.add(tItem);
					while (emptyRows > 1) {
						vendorItemTransactionTable
								.add(vendorItemTransactionTable.getEmptyRow());
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
				vendorItemTransactionTable.updateTotals();

			}
		};
		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax(), isTaxPerDetailLine(), isTrackDiscounts(),
				isDiscountPerDetailLine(), this, isCustomerAllowedToAdd(),
				isTrackClass(), isClassPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashPurchaseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashPurchaseView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CashPurchaseView.this.isInViewMode();
			}

			@Override
			protected boolean isTrackJob() {
				return CashPurchaseView.this.isTrackJob();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CashPurchaseView.this.updateNonEditableItems();
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
				isDiscountPerDetailLine(), this, isCustomerAllowedToAdd(),
				isTrackClass(), isClassPerDetailLine()) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashPurchaseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashPurchaseView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CashPurchaseView.this.isInViewMode();
			}

			@Override
			protected boolean isTrackJob() {
				return CashPurchaseView.this.isTrackJob();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CashPurchaseView.this.updateNonEditableItems();
			}

			@Override
			protected int getTransactionType() {
				return ClientTransaction.TYPE_CASH_PURCHASE;
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
		// itemsDisclosurePanel.setWidth("100%");

		// Inventory table..
		// inventoryTransactionTable = new WarehouseAllocationTable();
		// inventoryTransactionTable.setDesable(isInViewMode());
		//
		// FlowPanel inventoryFlowPanel = new FlowPanel();
		// inventoryDisclosurePanel = new
		// DisclosurePanel("Warehouse Allocation");
		// inventoryFlowPanel.add(inventoryTransactionTable);
		// inventoryDisclosurePanel.setContent(inventoryFlowPanel);
		// inventoryDisclosurePanel.setWidth("100%");
		// ---Inverntory table-----

		memoTextAreaItem = createMemoTextAreaItem();
		// memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);

		DynamicForm memoForm = new DynamicForm("memoForm");
		// memoForm.setWidth("100%");
		memoForm.add(memoTextAreaItem);

		discountField = getDiscountField();

		StyledPanel totalForm = new StyledPanel("totalForm");
		totalForm.setStyleName("boldtext");

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		leftVLay.add(vendorForm);

		StyledPanel rightVLay = new StyledPanel("rightVLay");
		// rightVLay.setWidth("100%");
		rightVLay.add(termsForm);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}

		DynamicForm form = new DynamicForm("form");

		StyledPanel bottomLayout = new StyledPanel("bottomLayout");

		StyledPanel bottompanel = new StyledPanel("bottompanel");

		DynamicForm transactionTotalForm = new DynamicForm(
				"transactionTotalForm");

		if (isTrackTax() && isTrackPaidTax()) {
			DynamicForm netAmountForm = new DynamicForm("netAmountForm");
			netAmountForm.add(netAmount);
			totalForm.add(netAmountForm);
			totalForm.add(vatTotalNonEditableText);
			if (isMultiCurrencyEnabled()) {
				transactionTotalForm.add(transactionTotalNonEditableText,
						foreignCurrencyamountLabel);
			} else {
				transactionTotalForm.add(transactionTotalNonEditableText);
			}
			StyledPanel vpanel = new StyledPanel("vpanel");
			vpanel.add(totalForm);

			bottomLayout.add(memoForm);
			if (!isTaxPerDetailLine()) {
				// taxCodeSelect.setVisible(isInViewMode());
				form.add(taxCodeSelect);
			}
			form.add(vatinclusiveCheck);
			bottomLayout.add(form);
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					form.add(discountField);
					bottomLayout.add(form);
				}

			}
			bottomLayout.add(totalForm);

			bottompanel.add(vpanel);
			bottompanel.add(bottomLayout);

			// StyledPanel vPanel = new StyledPanel();
			// vPanel.add(menuButton);
			// vPanel.add(memoForm);
			// vPanel.setWidth("100%");
			//
			// bottomLayout.add(vPanel);
			// bottomLayout.add(vatCheckform);
			// bottomLayout.setCellHorizontalAlignment(vatCheckform,
			// HasHorizontalAlignment.ALIGN_RIGHT);
			// bottomLayout.add(totalForm);
			// bottomLayout.setCellHorizontalAlignment(totalForm,
			// HasHorizontalAlignment.ALIGN_RIGHT);
		} else {
			memoForm.setStyleName("align-form");
			bottomLayout.add(memoForm);
			if (isMultiCurrencyEnabled()) {
				transactionTotalForm.add(transactionTotalNonEditableText,
						foreignCurrencyamountLabel);
			} else {
				transactionTotalForm.add(transactionTotalNonEditableText);
			}

			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					form.add(discountField);
					bottomLayout.add(form);
				}

			}

			bottomLayout.add(totalForm);
			bottompanel.add(bottomLayout);
		}
		totalForm.add(transactionTotalForm);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(titlelabel);
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
		mainVLay.add(transactionsTree);
		mainVLay.add(accountsDisclosurePanel.getPanel());
		mainVLay.add(itemsDisclosurePanel.getPanel());
		mainVLay.add(bottompanel);

		// setOverflow(Overflow.SCROLL);
		this.add(mainVLay);
		// addChild(mainVLay);

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);
		listforms.add(memoForm);
		listforms.add(transactionTotalForm);

		// if (UIUtils.isMSIEBrowser())
		// resetFormView();
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}
		initViewType();

		// settabIndexes();

	}

	protected StyledPanel getTopLayout() {
		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("fields-panel");
		return topHLay;
	}

	@Override
	protected void accountSelected(ClientAccount account) {

		if (account == null) {
			payFromCombo.setValue("");
			return;
		}
		this.payFromAccount = account;
		payFromCombo.setComboItem(payFromAccount);
		if (account != null
				&& paymentMethod.equalsIgnoreCase(messages.cheque())
				&& isInViewMode()) {
			ClientCashPurchase cashPurchase = transaction;
			// setCheckNumber();
		}
	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientCashPurchase());
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
			ClientVendor vendor = getCompany().getVendor(
					transaction.getVendor());
			vendorCombo.setValue(vendor);
			this.vendor = vendor;
			vendorItemTransactionTable.setPayee(vendor);
			selectedVendor(vendor);
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			this.billingAddress = transaction.getVendorAddress();
			if (billingAddress != null) {

				billToAreaItem.setValue(getValidAddress(billingAddress));

			} else
				billToAreaItem.setValue("");
			// paymentMethodSelected(cashPurchaseToBeEdited.getPaymentMethod()
			// !=
			// null ? cashPurchaseToBeEdited
			// .getPaymentMethod()
			// : "");
			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			if (transaction.getPaymentMethod().equals(messages.check())) {
				printCheck.setEnabled(!isInViewMode());
				checkNoText.setEnabled(!isInViewMode());
			} else {
				printCheck.setEnabled(false);
				checkNoText.setEnabled(false);
			}

			if (transaction.getCheckNumber() != null) {
				if (transaction.getCheckNumber().equals(messages.toBePrinted())) {
					checkNoText.setValue(messages.toBePrinted());
					printCheck.setValue(true);
				} else {
					checkNoText.setValue(transaction.getCheckNumber());
					printCheck.setValue(false);
				}
			}

			accountSelected(getCompany().getAccount(transaction.getPayFrom()));
			// transactionDateItem.setEnteredDate(cashPurchaseToBeEdited.get)
			initMemoAndReference();
			netAmount.setAmount(transaction.getNetAmount());
			if (getPreferences().isTrackPaidTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					vatTotalNonEditableText.setTransaction(transaction);
				} else {
					selectTAXCode();
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

			// if (isTrackTax()) {
			// if (isTaxPerDetailLine()) {
			// netAmountLabel.setAmount(transaction.getNetAmount());
			// taxTotalNonEditableText.setAmount(transaction.getTotal()
			// - transaction.getNetAmount());
			// } else {
			// this.taxCode =
			// getTaxCodeForTransactionItems(this.transactionItems);
			// if (taxCode != null) {
			// this.taxCodeSelect
			// .setComboItem(getTaxCodeForTransactionItems(this.transactionItems));
			// }
			// this.taxTotalNonEditableText.setValue(String
			// .valueOf(transaction.getTaxTotla()));
			// }
			// }
			//
			transactionTotalNonEditableText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(isAmountIncludeTAX());
			}
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		super.initTransactionViewData();
		initPayFromAccounts();
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

	private void selectedVendor(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		updatePurchaseOrderOrItemReceipt(vendor);
		if (!transaction.isTemplate()) {
			getPurchaseOrdersAndItemReceipt();
		}
		super.vendorSelected(vendor);
	}

	private void updatePurchaseOrderOrItemReceipt(ClientVendor vendor) {
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientCashPurchase ent = this.transaction;
			if (ent != null && ent.getVendor() == vendor.getID()) {
				this.vendorAccountTransactionTable
						.setRecords(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.vendorItemTransactionTable
						.setRecords(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getVendor() != vendor.getID()) {
				this.vendorAccountTransactionTable.resetRecords();
				this.vendorAccountTransactionTable.updateTotals();
				this.vendorItemTransactionTable.updateTotals();
			}
		}
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		vendorItemTransactionTable.setPayee(vendor);
		transactionsTree.clear();
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientCashPurchase ent = this.transaction;

			if (ent != null && ent.getVendor() == vendor.getID()) {
				this.vendorAccountTransactionTable
						.setRecords(getAccountTransactionItems(ent
								.getTransactionItems()));
				this.vendorItemTransactionTable
						.setRecords(getItemTransactionItems(ent
								.getTransactionItems()));
			} else if (ent != null && ent.getVendor() != vendor.getID()) {
				this.vendorAccountTransactionTable.resetRecords();
				this.vendorAccountTransactionTable.updateTotals();
				this.vendorItemTransactionTable.updateTotals();
			}
		}
		super.vendorSelected(vendor);
		if (vendor == null) {
			return;
		}
		if (vendor.getPhoneNo() != null)
			phoneSelect.setValue(vendor.getPhoneNo());
		else
			phoneSelect.setValue("");
		billingAddress = getAddress(ClientAddress.TYPE_BILL_TO);
		if (billingAddress != null) {
			billToAreaItem.setValue(getValidAddress(billingAddress));
		} else
			billToAreaItem.setValue("");
		long code = vendor.getTAXCode();
		if (code == 0 && taxCodeSelect != null) {
			code = Accounter.getCompany().getDefaultTaxCode();
			taxCodeSelect.setComboItem(getCompany().getTAXCode(code));
		}

		long currency = vendor.getCurrency();
		if (currency != 0) {
			ClientCurrency clientCurrency = getCompany().getCurrency(currency);
			currencyWidget.setSelectedCurrencyFactorInWidget(clientCurrency,
					transactionDateItem.getDate().getDate());
		} else {
			ClientCurrency clientCurrency = getCompany().getPrimaryCurrency();
			if (clientCurrency != null) {
				currencyWidget.setSelectedCurrency(clientCurrency);
			}
		}

		vendorAccountTransactionTable.setTaxCode(code, false);
		vendorItemTransactionTable.setTaxCode(code, false);

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(getCompany().getCurrency(vendor.getCurrency()));
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
		getPurchaseOrdersAndItemReceipt();
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;

		if (paymentMethod != null) {
			this.paymentMethod = paymentMethod;
			if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
				printCheck.setEnabled(true);
				checkNoText.setEnabled(true);
			} else {
				// paymentMethodCombo.setComboItem(paymentMethod);
				printCheck.setEnabled(false);
				checkNoText.setEnabled(false);
			}
		}

	}

	// private void setDisableStateForCheckNo(String paymentMethod) {
	//
	// if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
	// checkNo.setDisabled(false);
	// } else {
	// checkNo.setValue("");
	// checkNo.setDisabled(true);
	//
	// }
	// if (isInViewMode()) {
	// if (paymentMethod.equalsIgnoreCase(messages.cheque())) {
	// checkNo.setDisabled(false);
	// } else {
	// checkNo.setDisabled(true);
	// }
	// }
	// }

	@Override
	public void saveAndUpdateView() {
		updateTransaction();

		super.saveAndUpdateView();

		createAlterObject();
	}

	@Override
	public ClientCashPurchase saveView() {
		ClientCashPurchase saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
			transaction.setPurchaseOrders(new ArrayList<ClientPurchaseOrder>());
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
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CASH_PURCHASE);

		if (this.getVendor() != null) {
			// Setting Vendor
			transaction.setVendor(this.getVendor().getID());
		}

		// Setting Contact
		if (contact != null)
			transaction.setContact(this.contact);

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress((billingAddress));

		// Setting Phone
		// if (phoneNo != null)
		transaction.setPhone(phoneSelect.getValue().toString());

		// Setting Payment Methods
		transaction.setPaymentMethod(paymentMethodCombo.getValue().toString());
		if (checkNoText.getValue() != null
				&& !checkNoText.getValue().equals("")) {
			transaction.setCheckNumber(getCheckValue());
		} else
			transaction.setCheckNumber("");

		// transaction.setIsToBePrinted(isChecked);

		// Setting Pay From Account
		transaction
				.setPayFrom(payFromCombo.getSelectedValue() != null ? payFromCombo
						.getSelectedValue().getID() : 0);

		// transaction
		// .setCheckNumber(getCheckNoValue() ==
		// ClientWriteCheck.IS_TO_BE_PRINTED ? "0"
		// : getCheckNoValue() + "");

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate(deliveryDateItem.getEnteredDate()
					.getDate());

		// Setting Total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());
		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// cashPurchase.setReference(getRefText());
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
		// if (getCompany().getPreferences().isInventoryEnabled()
		// && getCompany().getPreferences().iswareHouseEnabled())
		// transaction.setWareHouseAllocations(inventoryTransactionTable
		// .getAllRows());
		if (isTrackDiscounts()) {
			if (discountField.getPercentage() != 0.0
					&& transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getPercentage());
				}
			}
		}

		List<ClientTransaction> selectedRecords = transactionsTree
				.getSelectedRecords();
		List<ClientPurchaseOrder> orders = new ArrayList<ClientPurchaseOrder>();
		for (ClientTransaction clientTransaction : selectedRecords) {
			if (clientTransaction instanceof ClientPurchaseOrder) {
				orders.add((ClientPurchaseOrder) clientTransaction);
			}
		}
		transaction.setPurchaseOrders(orders);

		transaction.setNetAmount(netAmount.getAmount());
		if (isTrackPaidTax()) {
			setAmountIncludeTAX();
		}
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

	public void createAlterObject() {

		saveOrUpdate(transaction);

	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(true);
		setMemoTextAreaItem(transaction.getMemo());
		// setRefText(((ClientCashPurchase) transactionObject).getReference());
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

		netAmount.setAmount(lineTotal);
		if (getCompany().getPreferences().isTrackPaidTax()) {
			List<ClientTransaction> selectedRecords = transactionsTree
					.getSelectedRecords();
			if (!isInViewMode()) {
				List<ClientPurchaseOrder> orders = new ArrayList<ClientPurchaseOrder>();
				for (ClientTransaction clientTransaction : selectedRecords) {
					if (clientTransaction instanceof ClientPurchaseOrder) {
						orders.add((ClientPurchaseOrder) clientTransaction);
					}
				}
				transaction.setPurchaseOrders(orders);
			}
			if (transaction.getTransactionItems() != null && !isInViewMode()) {
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
		transactionTotalNonEditableText
				.setAmount(getAmountInBaseCurrency(grandTotal));
		foreignCurrencyamountLabel.setAmount(grandTotal);
	}

	@Override
	public ValidationResult validate() {

		ValidationResult result = super.validate();
		// Validations
		// 1. isValidTransactionDate?
		// 2. isInPreventPostingBeforeDate?
		// 3. form validations
		// 4. isValidDueOrDeliveryDate?
		// 5. isBlankTransaction?
		// 6. validateGrid?

		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// messages.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}

		result.add(vendorForm.validate());
		result.add(termsForm.validate());

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				deliveryDateItem.getEnteredDate(), this.transactionDate)) {
			result.addError(deliveryDateItem,
					messages.the() + " " + messages.deliveryDate() + " " + " "
							+ messages.cannotbeearlierthantransactiondate());
		}

		boolean isSelected = transactionsTree.validateTree();
		if (!isSelected) {
			if (transaction.getTotal() <= 0
					&& vendorAccountTransactionTable.isEmpty()
					&& vendorItemTransactionTable.isEmpty()) {
				result.addError(this,
						messages.transactiontotalcannotbe0orlessthan0());
			}
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
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
				result.add(vendorAccountTransactionTable.validateGrid());
				result.add(vendorItemTransactionTable.validateGrid());
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

		return result;

	}

	protected void getPurchaseOrdersAndItemReceipt() {
		if (this.rpcUtilService == null
				|| !getPreferences().isPurchaseOrderEnabled())
			return;
		if (getVendor() == null) {
			Accounter.showError(messages.pleaseSelectThePayee(Global.get()
					.Vendor()));
		} else {
			AccounterAsyncCallback<ArrayList<PurchaseOrdersAndItemReceiptsList>> callback = new AccounterAsyncCallback<ArrayList<PurchaseOrdersAndItemReceiptsList>>() {

				@Override
				public void onException(AccounterException caught) {
					// Accounter.showError(FinanceApplication.constants()
					// .noPurchaseOrderAndItemReceiptForVendor()
					// + vendor.getName());
					return;

				}

				@Override
				public void onResultSuccess(
						ArrayList<PurchaseOrdersAndItemReceiptsList> result) {
					if (result == null) {
						onFailure(new Exception());
					} else {
						List<ClientPurchaseOrder> salesAndEstimates = transaction
								.getPurchaseOrders();
						if (transaction.getID() != 0 && !result.isEmpty()) {
							ArrayList<PurchaseOrdersAndItemReceiptsList> estimatesList = new ArrayList<PurchaseOrdersAndItemReceiptsList>();
							ArrayList<ClientTransaction> notAvailableEstimates = new ArrayList<ClientTransaction>();

							for (ClientTransaction clientTransaction : salesAndEstimates) {
								boolean isThere = false;
								for (PurchaseOrdersAndItemReceiptsList estimatesalesorderlist : result) {
									if (estimatesalesorderlist
											.getTransactionId() == clientTransaction
											.getID()) {
										estimatesList
												.add(estimatesalesorderlist);
										isThere = true;
									}
								}
								if (!isThere) {
									notAvailableEstimates
											.add(clientTransaction);
								}
							}

							if (transaction.isDraft()) {
								for (ClientTransaction clientTransaction : notAvailableEstimates) {
									salesAndEstimates.remove(clientTransaction);
								}
							}

							for (PurchaseOrdersAndItemReceiptsList estimatesAndSalesOrdersList : estimatesList) {
								result.remove(estimatesAndSalesOrdersList);
							}
						}
						transactionsTree.setAllrows(result,
								transaction.getID() == 0 ? true
										: salesAndEstimates.isEmpty());
						if (transaction.getPurchaseOrders() != null) {
							transactionsTree
									.setRecords(new ArrayList<ClientTransaction>(
											transaction.getPurchaseOrders()));
						}
						transactionsTree.setEnabled(!isInViewMode());
						refreshTransactionGrid();
					}
				}
			};

			this.rpcUtilService.getPurchasesAndItemReceiptsList(getVendor()
					.getID(), callback);
		}

		// if (vendor == null)
		// Accounter.showError("Please Select the Vendor");
		// else
		// new VendorBillListDialog(this).show();

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
		super.fitToSize(height, width);

	}

	@Override
	protected void onLoad() {
	}

	@Override
	public void onEdit() {
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				Accounter.showError(AccounterExceptions.getErrorString(caught));
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
		vendorCombo.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		paymentMethodCombo.setEnabled(!isInViewMode());
		if (printCheck.getValue().toString().equalsIgnoreCase("true")) {
			checkNoText.setValue(messages.toBePrinted());
		}
		deliveryDateItem.setEnabled(!isInViewMode());
		vendorAccountTransactionTable.setEnabled(!isInViewMode());
		vendorItemTransactionTable.setEnabled(!isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		discountField.setEnabled(!isInViewMode());
		if (locationTrackingEnabled) {
			locationCombo.setEnabled(!isInViewMode());
		}
		if (taxCodeSelect != null) {
			taxCodeSelect.setEnabled(!isInViewMode());
		}
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		if (classListCombo != null) {
			classListCombo.setEnabled(!isInViewMode());
		}
		transactionsTree.setEnabled(!isInViewMode());
		super.onEdit();

	}

	protected void initViewType() {

	}

	@Override
	public void print() {
		updateTransaction();
		UIUtils.downloadAttachment(transaction.getID(),
				ClientTransaction.TYPE_CASH_PURCHASE);
	}

	@Override
	public void printPreview() {

	}

	@Override
	protected Double getTransactionTotal() {
		return this.transactionTotalNonEditableText.getAmount();
	}

	private void resetFormView() {
		// vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// vendorForm.setWidth("75%");
		// refText.setWidth("200px");

	}

	@Override
	protected String getViewTitle() {
		return messages.cashPurchases();
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
	protected void addAllRecordToGrid(
			List<ClientTransactionItem> transactionItems) {
		vendorAccountTransactionTable
				.setAllRows(getAccountTransactionItems(transactionItems));
		vendorItemTransactionTable
				.setAllRows(getItemTransactionItems(transactionItems));
		refreshTransactionGrid();
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
		transactionsTree.updateTransactionTreeItemTotals();
	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		phoneSelect.setTabIndex(3);
		billToAreaItem.setTabIndex(4);
		transactionDateItem.setTabIndex(5);
		transactionNumber.setTabIndex(6);
		paymentMethodCombo.setTabIndex(7);
		payFromCombo.setTabIndex(8);
		checkNoText.setTabIndex(9);
		deliveryDateItem.setTabIndex(10);
		memoTextAreaItem.setTabIndex(11);
		// menuButton.setTabIndex(12);
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
		vendorAccountTransactionTable.updateAmountsFromGUI();
		vendorItemTransactionTable.updateAmountsFromGUI();
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
		return true;
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
