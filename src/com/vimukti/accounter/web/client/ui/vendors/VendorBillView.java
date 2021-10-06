package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientAddress;
import com.vimukti.accounter.web.client.core.ClientContact;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientEnterBill;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientPaymentTerms;
import com.vimukti.accounter.web.client.core.ClientPurchaseOrder;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.Utility;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.Lists.PurchaseOrdersAndItemReceiptsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.GwtDisclosurePanel;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.PaymentTermsCombo;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.DateField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.TransactionsTree;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.LinkItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * modified by Ravi Kiran.G, Murali.A
 * 
 */
public class VendorBillView extends
		AbstractVendorTransactionView<ClientEnterBill> implements
		IPrintableView {
	private PaymentTermsCombo paymentTermsCombo;
	private ClientPaymentTerms selectedPaymentTerm;
	private DateField dueDateItem;

	private AmountLabel netAmount;

	private DynamicForm vendorForm;
	private Double balanceDue = 0.0;

	private ArrayList<DynamicForm> listforms;
	protected VendorAccountTransactionTable vendorAccountTransactionTable;
	protected VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private final StyledPanel totalForm = new StyledPanel("totalForm");
	private GwtDisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;

	// private WarehouseAllocationTable inventoryTransactionTable;
	// private DisclosurePanel inventoryDisclosurePanel;

	TransactionsTree<PurchaseOrdersAndItemReceiptsList> transactionsTree;
	private StyledPanel accountFlowPanel;
	private StyledPanel itemsFlowPanel;

	public VendorBillView() {
		super(ClientTransaction.TYPE_ENTER_BILL);
		this.getElement().setId("VendorBillView");
	}

	private void resetGlobalVariables() {

		this.setVendor(null);
		this.billingAddress = null;
		this.contact = null;
		this.phoneNo = null;
		this.addressListOfVendor = null;
		this.contacts = null;
		List<ClientContact> list = new ArrayList<ClientContact>();
		list.addAll(contacts);
		contactCombo.initCombo(list);
		List<ClientAddress> adrsList = new ArrayList<ClientAddress>();
		adrsList.addAll(addressListOfVendor);
		billToCombo.initCombo(adrsList);
		contactCombo.setEnabled(!isInViewMode());
		billToCombo.setEnabled(!isInViewMode());
		// phoneSelect.setValueMap();
		setMemoTextAreaItem("");
		// setRefText("");
	}

	@Override
	protected void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientEnterBill());
		} else {
			if (currencyWidget != null) {
				if (transaction.getCurrency() > 1) {
					this.currency = getCompany().getCurrency(
							transaction.getCurrency());
				} else {
					this.currency = getCompany().getPrimaryCurrency();
				}
				this.currencyFactor = transaction.getCurrencyFactor();
				if (this.currency != null) {
					currencyWidget.setSelectedCurrency(this.currency);
				}
				// currencyWidget.currencyChanged(this.currency);
				currencyWidget.setCurrencyFactor(transaction
						.getCurrencyFactor());
				currencyWidget.setEnabled(!isInViewMode());
			}
			if (!getAccountTransactionItems(transaction.getTransactionItems())
					.isEmpty()) {
				this.vendorAccountTransactionTable
						.setAllRows(getAccountTransactionItems(transaction
								.getTransactionItems()));
			}
			this.vendorItemTransactionTable
					.setAllRows(getItemTransactionItems(transaction
							.getTransactionItems()));

			paymentTermsCombo.setValue(transaction.getPaymentTerm());
			dueDateItem
					.setValue(new ClientFinanceDate(transaction.getDueDate()));
			deliveryDateItem.setValue(new ClientFinanceDate(transaction
					.getDeliveryDate()));

			ClientVendor vendor = getCompany().getVendor(
					transaction.getVendor());
			vendorCombo.setValue(vendor);
			this.vendor = vendor;
			vendorItemTransactionTable.setPayee(vendor);
			billToaddressSelected(transaction.getVendorAddress());
			selectedVendor(vendor);
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			phoneSelect.setEnabled(!isInViewMode());
			transactionNumber.setValue(transaction.getNumber());
			// if (isTrackTax()) {
			// netAmount.setAmount(transaction.getNetAmount());
			// vatTotalNonEditableText.setAmount(transaction.getTotal()
			// - transaction.getNetAmount());
			// }
			netAmount.setAmount(transaction.getNetAmount());
			if (getPreferences().isTrackPaidTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					vatTotalNonEditableText.setTransaction(transaction);
				} else {
					selectTAXCode();
				}
				if (vatinclusiveCheck != null) {
					setAmountIncludeChkValue(isAmountIncludeTAX());
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
			transactionTotalNonEditableText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			balanceDueNonEditableText.setAmount(transaction.getBalanceDue());
			// balanceDueNonEditableText.setCurrency(getTransactionCurrency());

			this.dueDateItem
					.setValue(transaction.getDueDate() != 0 ? new ClientFinanceDate(
							transaction.getDueDate()) : getTransactionDate());
			initMemoAndReference();
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));

		super.initTransactionViewData();
		initPaymentTerms();

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

	private void initBalanceDue() {

		if (isInViewMode()) {

			setBalanceDue(transaction.getBalanceDue());

		}

	}

	public void setBalanceDue(Double balanceDue) {
		if (balanceDue == null)
			balanceDue = 0.0D;
		this.balanceDue = balanceDue;
		balanceDueNonEditableText.setAmount(balanceDue);
	}

	public Double getBalanceDue() {
		return balanceDue;
	}

	private void initPaymentTerms() {
		paymentTermsList = getCompany().getPaymentsTerms();

		paymentTermsCombo.initCombo(paymentTermsList);
		paymentTermsCombo.setEnabled(!isInViewMode());

		if (isInViewMode() && transaction.getPaymentTerm() != 0) {
			ClientPaymentTerms paymentTerm = getCompany().getPaymentTerms(
					transaction.getPaymentTerm());
			paymentTermsCombo.setComboItem(paymentTerm);
			selectedPaymentTerm = paymentTerm;

		} else {
			for (ClientPaymentTerms paymentTerm : paymentTermsList) {
				if (paymentTerm.getName().equals("Due on Receipt")) {
					paymentTermsCombo.addItemThenfireEvent(paymentTerm);
					break;
				}
			}
			this.selectedPaymentTerm = paymentTermsCombo.getSelectedValue();
		}

	}

	public void selectedVendor(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		updatePurchaseOrderOrItemReceipt(vendor);
		if (!transaction.isTemplate()) {
			getPurchaseOrdersAndItemReceipt();
		}
		super.vendorSelected(vendor);
		if (isInViewMode() && this.selectedPaymentTerm != null)
			paymentTermSelected(selectedPaymentTerm);
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		vendorItemTransactionTable.setPayee(vendor);
		transactionsTree.clear();
		updatePurchaseOrderOrItemReceipt(vendor);

		super.vendorSelected(vendor);
		if (transaction == null)
			vendorAccountTransactionTable.resetRecords();

		if (!(isInViewMode() && vendor.getID() == transaction.getVendor()))
			setPaymentTermsCombo(vendor);

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
		if (vendor.getPhoneNo() != null) {
			phoneSelect.setValue(vendor.getPhoneNo());
		} else {
			phoneSelect.setValue("");
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(getCompany().getCurrency(vendor.getCurrency()));
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
		getPurchaseOrdersAndItemReceipt();
	}

	private void updatePurchaseOrderOrItemReceipt(ClientVendor vendor) {
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientEnterBill ent = this.transaction;
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

	private void setPaymentTermsCombo(ClientVendor vendor) {
		ClientPaymentTerms vendorPaymentTerm = getCompany().getPaymentTerms(
				vendor.getPaymentTermsId());
		// if (transactionObject != null && this.selectedPaymentTerm != null)
		// paymentTermSelected(selectedPaymentTerm);

		// else if (transactionObject == null) {
		if (vendorPaymentTerm != null) {
			paymentTermsCombo.setComboItem(vendorPaymentTerm);
			paymentTermSelected(vendorPaymentTerm);

		} else {
			paymentTermsList = getCompany().getPaymentsTerms();
			for (ClientPaymentTerms paymentTerm : paymentTermsList) {
				if (paymentTerm.getName().equals("Due on Receipt")) {
					paymentTermsCombo.addItemThenfireEvent(paymentTerm);
					break;
				}
			}
			this.selectedPaymentTerm = paymentTermsCombo.getSelectedValue();
			paymentTermSelected(this.selectedPaymentTerm);
		}
		// }

	}

	private void setDueDate(long date) {
		dueDateItem.setEnteredDate(new ClientFinanceDate(date));
	}

	private ClientFinanceDate getDueDate() {
		return dueDateItem.getEnteredDate();
	}

	@Override
	protected void createControls() {
		Label lab1;
		lab1 = new Label(messages.enterBill());

		lab1.setStyleName("label-title");
		transactionDateItem = createTransactionDateItem();
		transactionDateItem.setTitle(messages.billDate());
		transactionDateItem
				.addDateValueChangeHandler(new DateValueChangeHandler() {

					@Override
					public void onDateValueChange(ClientFinanceDate date) {
						if (date != null) {
							deliveryDateItem.setEnteredDate(date);
							dueDateItem.setEnteredDate(date);
							setTransactionDate(date);
						}
					}
				});
		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(messages.invNo());
		listforms = new ArrayList<DynamicForm>();

		locationCombo = createLocationCombo();

		DynamicForm dateNoForm = new DynamicForm("dateNoForm");
		dateNoForm.setStyleName("datenumber-panel");
		if (isTemplate) {
			dateNoForm.add(transactionNumber);
		} else {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}

		StyledPanel datepanel = new StyledPanel("datepanel");
		// datepanel.setWidth("100%");
		datepanel.add(dateNoForm);

		vatinclusiveCheck = getVATInclusiveCheckBox();

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		vendorCombo = createVendorComboItem(messages.payeeName(Global.get()
				.Vendor()));
		// vendorCombo.setWidth(100);
		// purchaseLabel = new LinkItem();
		// purchaseLabel.setLinkTitle(FinanceApplication.constants()
		// .purchaseAndItemReceipt());
		// purchaseLabel.setShowTitle(false);
		// purchaseLabel.setDisabled(isEdit);
		// purchaseLabel.addClickHandler(new ClickHandler() {
		//
		// @Override
		// public void onClick(ClickEvent event) {
		// getPurchaseOrdersAndItemReceipt();
		// }
		// });
		LinkItem emptylabel = new LinkItem();
		emptylabel.setLinkTitle("");
		emptylabel.setShowTitle(false);

		contactCombo = createContactComboItem();
		// contactCombo.setWidth(100);
		billToCombo = createBillToComboItem();
		// billToCombo.setWidth(100);
		if (this.isInViewMode())
			billToCombo.setEnabled(false);

		vendorForm = UIUtils.form(Global.get().Vendor());
		// vendorForm.setWidth("100%");
		vendorForm.add(vendorCombo, emptylabel, contactCombo, emptylabel);

		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			vendorForm.add(classListCombo);
		}

		// formItems.add(vendorCombo);
		// formItems.add(contactCombo);
		// formItems.add(billToCombo);

		phoneSelect = new TextItem(messages.phone(), "phoneSelect");
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		// phoneSelect.setWidth(80);
		phoneSelect.setEnabled(true);
		// formItems.add(phoneSelect);

		dueDateItem = new DateField(messages.dueDate(), "dueDateItem");
		dueDateItem.setToolTip(messages.selectDateUntilDue(this.getAction()
				.getViewName()));
		dueDateItem.setEnteredDate(getTransactionDate());
		dueDateItem.setTitle(messages.dueDate());
		dueDateItem.setEnabled(!isInViewMode());

		paymentTermsCombo = new PaymentTermsCombo(messages.paymentTerms());
		// paymentTermsCombo.setWidth(80);
		paymentTermsCombo.setEnabled(!isInViewMode());
		paymentTermsCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientPaymentTerms>() {

					@Override
					public void selectedComboBoxItem(
							ClientPaymentTerms selectItem) {

						paymentTermSelected(selectItem);

					}

				});

		deliveryDateItem = createTransactionDeliveryDateItem();
		// deliveryDateItem.setWidth(100);
		taxCodeSelect = createTaxCodeSelectItem();

		DynamicForm termsForm = UIUtils.form(messages.terms());
		termsForm.setStyleName("vender-form");
		// termsForm.setWidth("75%");
		// termsForm.setFields(phoneSelect, paymentTermsCombo);

		DynamicForm dateform = new DynamicForm("dateform");
		// dateform.setWidth("100%");
		if (locationTrackingEnabled)
			dateform.add(locationCombo);
		if (isTemplate) {
			dateform.add(phoneSelect, paymentTermsCombo);
		} else {
			dateform.add(phoneSelect, paymentTermsCombo, dueDateItem,
					deliveryDateItem);
		}
		// dateform.getCellFormatter().setWidth(0, 0, "200px");
		netAmount = new AmountLabel(messages.currencyNetAmount(getCompany()
				.getPrimaryCurrency().getFormalName()), getCompany()
				.getPrimaryCurrency());
		netAmount.setDefaultValue("£0.00");
		netAmount.setEnabled(false);

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem(getCompany()
				.getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		vatTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();
		// balanceDueNonEditableText = new AmountField(messages
		// .balanceDue(), this, getBaseCurrency());
		balanceDueNonEditableText = new AmountLabel(messages.nameWithCurrency(
				messages.balanceDue(), getBaseCurrency().getFormalName()),
				getBaseCurrency());
		balanceDueNonEditableText.setEnabled(false);
		balanceDueNonEditableText.setDefaultValue(""
				+ UIUtils.getCurrencySymbol() + " 0.00");

		transactionsTree = new TransactionsTree<PurchaseOrdersAndItemReceiptsList>(
				this) {
			private HashMap<ClientTransactionItem, ClientTransactionItem> clonesObjs = new HashMap<ClientTransactionItem, ClientTransactionItem>();

			@Override
			public void updateTransactionTotal() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			public void setTransactionDate(ClientFinanceDate transactionDate) {
				VendorBillView.this.setTransactionDate(transactionDate);
			}

			@Override
			public boolean isinViewMode() {
				return !(VendorBillView.this.isInViewMode());
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
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorBillView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return VendorBillView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			protected boolean isTrackJob() {
				return VendorBillView.this.isTrackJob();
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
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorBillView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return VendorBillView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				VendorBillView.this.updateNonEditableItems();
			}

			@Override
			protected int getTransactionType() {
				return ClientTransaction.TYPE_ENTER_BILL;
			}

			@Override
			protected boolean isTrackJob() {
				return VendorBillView.this.isTrackJob();
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

		// addLinksButton = new Button(FinanceApplication.constants()
		// /addLinks());
		// //FIXME--need to disable basing on the mode of the view being opened
		// addLinksButton.setEnabled(isEdit);
		// addLinksButton.setEnabled(true);
		// linksText = new TextItem();
		// linksText.setWidth(100);
		// linksText.setShowTitle(false);
		// linksText.setDisabled(isEdit);
		// formItems.add(linksText);
		currencyWidget = createCurrencyFactorWidget();
		DynamicForm tdsForm = new DynamicForm("tdsForm");
		// tdsForm.setWidth("100%");
		tdsForm.add();

		DynamicForm memoForm = new DynamicForm("memoForm");
		// memoForm.setWidth("100%");
		memoForm.add(memoTextAreaItem);
		// memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");

		// memoForm.setWidget(3, 0, addLinksButton);
		// memoForm.setWidget(3, 1, linksText.getMainWidget());

		DynamicForm vatCheckform = new DynamicForm("vatCheckform");
		// vatCheckform.setFields(vatinclusiveCheck);

		// totalForm.setWidth("100%");
		totalForm.setStyleName("boldtext");
		// netAmount.setWidth((netAmount.getMainWidget().getOffsetWidth() +
		// "102")
		// + "px");
		StyledPanel taxPanel = new StyledPanel("taxPanel");
		discountField = getDiscountField();

		DynamicForm form = new DynamicForm("form");

		if (isTrackTax() && isTrackPaidTax()) {
			if (!isTaxPerDetailLine()) {
				form.add(taxCodeSelect);
			}
			form.add(vatinclusiveCheck);
			DynamicForm netAmountForm = new DynamicForm("netAmountForm");
			netAmountForm.add(netAmount);
			totalForm.add(netAmountForm);
			totalForm.add(vatTotalNonEditableText);
		}
		if (isTrackDiscounts()) {
			if (!isDiscountPerDetailLine()) {
				form.add(discountField);
			}
		}
		taxPanel.add(form);
		DynamicForm transactionTotalForm = new DynamicForm(
				"transactionTotalForm");

		transactionTotalForm.add(transactionTotalNonEditableText);
		if (isMultiCurrencyEnabled()) {
			transactionTotalForm.add(foreignCurrencyamountLabel);
		}

		totalForm.add(transactionTotalForm);
		taxPanel.add(totalForm);

		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		horizontalPanel.add(tdsForm);

		if (this.isInViewMode()) {
			DynamicForm balanceDueForm = new DynamicForm("balanceDueForm");
			balanceDueForm.add(balanceDueNonEditableText);
			totalForm.add(balanceDueForm);
		}
		StyledPanel leftVLay = new StyledPanel("leftVLay");
		// leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);

		StyledPanel rightVLay = new StyledPanel("rightVLay");
		// rightVLay.setWidth("100%");
		rightVLay.add(termsForm);
		rightVLay.add(dateform);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}

		StyledPanel bottomLayout = new StyledPanel("bottomLayout");

		StyledPanel bottompanel = new StyledPanel("bottompanel");
		//

		bottomLayout.add(memoForm);
		bottomLayout.add(taxPanel);

		bottompanel.add(bottomLayout);

		// StyledPanel vPanel = new StyledPanel();
		// vPanel.add(menuButton);
		// vPanel.add(memoForm);
		// vPanel.setWidth("100%");
		//
		// bottomLayout.add(vPanel);
		// bottomLayout.add(vatCheckform);
		// // bottomLayout.setHorizontalAlignment(align)
		// bottomLayout.setCellHorizontalAlignment(vatCheckform,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// bottomLayout.add(totalForm);
		// bottomLayout.setCellHorizontalAlignment(totalForm,
		// HasHorizontalAlignment.ALIGN_RIGHT);
		// } else if (getCompany().getAccountingType() ==
		// ClientCompany.ACCOUNTING_TYPE_INDIA) {
		// bottomLayout.add(horizontalPanel);
		// bottomLayout.add(totalForm);
		// bottomLayout.setCellWidth(totalForm, "30%");
		//
		// memoForm.setStyleName("align-form");
		// StyledPanel vPanel = new StyledPanel();
		// vPanel.add(hpanel);
		// vPanel.setWidth("100%");
		//
		// vPanel.setCellHorizontalAlignment(hpanel, ALIGN_RIGHT);
		// vPanel.add(horizontalPanel);
		// vPanel.add(memoForm);
		//
		// bottompanel.add(vPanel);
		// bottompanel.add(bottomLayout);

		// } else {
		// memoForm.setStyleName("align-form");
		// StyledPanel vPanel = new StyledPanel();
		// vPanel.setWidth("100%");
		//
		// vPanel.add(memoForm);
		//
		// bottompanel.add(vPanel);
		// }

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		// mainVLay.setSize("100%", "100%");
		mainVLay.add(labeldateNoLayout);
		// mainVLay.setCellHorizontalAlignment(topHLay, ALIGN_RIGHT);
		StyledPanel topHLay = getTopLayout();
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
		mainVLay.add(bottompanel);

		// if (UIUtils.isMSIEBrowser()) {
		// resetFormView();
		// vendorForm.setWidth("68%");
		// termsForm.setWidth("100%");
		// dateform.setWidth("100%");
		// }

		this.add(mainVLay);
		// setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(termsForm);

		listforms.add(dateform);

		listforms.add(memoForm);
		listforms.add(vatCheckform);
		listforms.add(transactionTotalForm);
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

	private void paymentTermSelected(ClientPaymentTerms selectItem) {
		if (selectItem == null) {
			return;
		}
		selectedPaymentTerm = selectItem;

		// paymentTermsCombo.setComboItem(selectedPaymentTerm);
		// if (isInViewMode()) {
		// // setDueDate(((ClientEnterBill) transactionObject).getDueDate());
		// setDueDate(Utility.getCalculatedDueDate(getTransactionDate(),
		// selectedPaymentTerm).getDate());
		// } else {
		// setDueDate(Utility.getCalculatedDueDate(getTransactionDate(),
		// selectedPaymentTerm).getDate());
		// }
		ClientFinanceDate transDate = this.transactionDateItem.getEnteredDate();
		setDueDate(Utility.getCalculatedDueDate(transDate, selectedPaymentTerm)
				.getDate());
	}

	@Override
	public ClientEnterBill saveView() {
		ClientEnterBill saveView = super.saveView();
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
	public void saveAndUpdateView() {
		updateTransaction();
		super.saveAndUpdateView();
		saveOrUpdate(transaction);
	}

	@Override
	protected void updateTransaction() {
		if (transaction == null)
			return;
		super.updateTransaction();
		// Setting Vendor
		if (getVendor() != null)
			transaction.setVendor(getVendor());

		// Setting Contact
		if (contact != null)
			transaction.setContact(contact);

		// Setting Address
		if (billingAddress != null)
			transaction.setVendorAddress(billingAddress);

		// Setting Phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());
		// else
		// transaction.setPhone(phoneNo);

		// Setting Payment Terms
		if (selectedPaymentTerm != null)
			transaction.setPaymentTerm(selectedPaymentTerm);

		// Setting Due date
		if (dueDateItem.getEnteredDate() != null)
			transaction.setDueDate((dueDateItem.getEnteredDate()).getDate());

		// Setting Delivery date
		if (deliveryDateItem.getEnteredDate() != null)
			transaction.setDeliveryDate(deliveryDateItem.getEnteredDate());

		// Setting Total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// transaction.setReference(getRefText());

		ClientFinanceDate discountDate = Utility.getCalculatedDiscountDate(
				transactionDateItem.getEnteredDate(), selectedPaymentTerm);
		transaction.setDiscountDate(discountDate.getDate());

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
		setAmountIncludeTAX();

		if (isTrackDiscounts()) {
			if (discountField.getPercentage() != 0.0
					&& transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getPercentage());
				}
			}
		}

		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

		// if (getCompany().getPreferences().isInventoryEnabled()
		// && getCompany().getPreferences().iswareHouseEnabled())
		// transaction.setWareHouseAllocations(inventoryTransactionTable
		// .getAllRows());
		// enterBill.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());
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

		transactionTotalNonEditableText
				.setAmount(getAmountInBaseCurrency(grandTotal));

		foreignCurrencyamountLabel.setAmount(grandTotal);

		netAmount.setAmount(lineTotal);
		if (isTrackTax()) {
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
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(isInViewMode());
		setMemoTextAreaItem(transaction.getMemo());
		// setRefText(((ClientEnterBill) transactionObject).getReference());

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. is Valid transaction date?
		// 2. is in prevent posting before date?
		// 3. vendorForm valid?
		// 4. is valid due date?
		// 5. isBlank transaction?
		// 6. is vendor transaction grid valid?
		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// messages.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}
		result.add(vendorForm.validate());

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				dueDateItem.getEnteredDate(), this.transactionDate)) {
			result.addError(dueDateItem,
					messages.the() + " " + messages.dueDate() + " " + " "
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

		String creditLimitWarning = isExceedCreditLimit(vendor,
				transaction.getTotal());
		if (creditLimitWarning != null) {
			result.addWarning(vendorCombo, creditLimitWarning);
		}

		// if (getAllTransactionItems().isEmpty()) {
		// result.addError(vendorAccountTransactionTable,
		// messages.blankTransaction());
		// } else {
		// result.add(vendorAccountTransactionTable.validateGrid());
		// result.add(vendorItemTransactionTable.validateGrid());
		// }
		return result;
	}

	// @Override
	// public void setViewConfiguration(ViewConfiguration viewConfiguration)
	// throws Exception {
	// super.setViewConfiguration(viewConfiguration);
	//
	// if (isEdit && (!transactionObject.isEnterBill()))
	// throw new Exception("Unable to load the Required EnterBill....");
	//
	// if (viewConfiguration.isInitWithPayee()) {
	// ClientPayee payee = viewConfiguration.getPayeeObject();
	//
	// if (payee == null || (!payee.isVendor()))
	// throw new Exception("Required Vendor Could Not be Loaded...");
	// }
	//
	// }

	public static VendorBillView getInstance() {
		return new VendorBillView();

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
	public void onEdit() {

		balanceDueNonEditableText.setVisible(false);
		AccounterAsyncCallback<Boolean> editCallBack = new AccounterAsyncCallback<Boolean>() {

			@Override
			public void onException(AccounterException caught) {
				int errorCode = caught.getErrorCode();
				String errorString = null;
				if (errorCode == AccounterException.ERROR_CANT_EDIT) {
					errorString = messages.billPaidSoYouCantEdit();
				} else if (errorCode == AccounterException.USED_IN_INVOICE) {
					errorString = messages.usedinInvoiceSoYoucantEdit();
				} else {
					errorString = AccounterExceptions.getErrorString(caught);
				}
				Accounter.showError(errorString);
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
		phoneSelect.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		// purchaseLabel.setDisabled(isEdit);
		paymentTermsCombo.setEnabled(!isInViewMode());
		dueDateItem.setEnabled(!isInViewMode());
		deliveryDateItem.setEnabled(!isInViewMode());
		vendorAccountTransactionTable.setEnabled(!isInViewMode());
		vendorItemTransactionTable.setEnabled(!isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		balanceDueNonEditableText.setEnabled(true);
		memoTextAreaItem.setDisabled(isInViewMode());
		discountField.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		taxCodeSelect.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		transactionsTree.setEnabled(!isInViewMode());
		classListCombo.setEnabled(!isInViewMode());
		super.onEdit();
	}

	@Override
	public void print() {
		updateTransaction();
		UIUtils.downloadAttachment(transaction.getID(),
				ClientTransaction.TYPE_ENTER_BILL);

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected Double getTransactionTotal() {
		return null;
		// return this.total.getAmount();
	}

	private void resetFormView() {
		// vendorForm.getCellFormatter().setWidth(0, 1, "200px");
		// refText.setWidth("200px");
		// phoneSelect.setWidth("210px");
		// paymentTermsCombo.setWidth("210px");
	}

	@Override
	protected String getViewTitle() {
		return messages.enterBills();
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
	protected void refreshTransactionGrid() {
		vendorAccountTransactionTable.updateTotals();
		vendorItemTransactionTable.updateTotals();
		transactionsTree.updateTransactionTreeItemTotals();
	}

	@Override
	public List<ClientTransactionItem> getAllTransactionItems() {
		List<ClientTransactionItem> list = new ArrayList<ClientTransactionItem>();
		list.addAll(vendorAccountTransactionTable.getRecords());
		list.addAll(vendorItemTransactionTable.getRecords());
		return list;
	}

	private void settabIndexes() {
		vendorCombo.setTabIndex(1);
		contactCombo.setTabIndex(2);
		transactionDateItem.setTabIndex(3);
		transactionNumber.setTabIndex(4);
		phoneSelect.setTabIndex(5);
		paymentTermsCombo.setTabIndex(6);
		dueDateItem.setTabIndex(7);
		deliveryDateItem.setTabIndex(8);
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
		vendorAccountTransactionTable.add(item);
		if (taxCodeSelect.getSelectedValue() != null) {
			vendorAccountTransactionTable.setTaxCode(taxCodeSelect
					.getSelectedValue().getID(), true);
		}
	}

	@Override
	protected void addItemTransactionItem(ClientTransactionItem item) {
		vendorItemTransactionTable.add(item);
		if (taxCodeSelect != null && taxCodeSelect.getSelectedValue() != null) {
			vendorItemTransactionTable.setTaxCode(taxCodeSelect
					.getSelectedValue().getID(), true);
		}
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
		balanceDueNonEditableText.setTitle(messages.nameWithCurrency(
				messages.balanceDue(), formalName));
		balanceDueNonEditableText.setCurrency(currencyWidget
				.getSelectedCurrency());
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
		return true;
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
		// TODO Auto-generated method stub
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
