package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.dom.client.Style.Unit;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.ClientVendorCreditMemo;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.GwtDisclosurePanel;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class VendorCreditMemoView extends
		AbstractVendorTransactionView<ClientVendorCreditMemo> implements
		IPrintableView {
	private DynamicForm vendorForm;
	private ArrayList<DynamicForm> listforms;
	private VendorAccountTransactionTable vendorAccountTransactionTable;
	private VendorItemTransactionTable vendorItemTransactionTable;
	private AddNewButton accountTableButton, itemTableButton;
	private GwtDisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;
	private StyledPanel accountFlowPanel;
	private StyledPanel itemsFlowPanel;

	public VendorCreditMemoView() {
		super(ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);
		this.getElement().setId("VendorCreditMemoView");
	}

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		vendorItemTransactionTable.setPayee(vendor);
		if (this.getVendor() != null && this.getVendor() != vendor) {
			ClientVendorCreditMemo ent = this.transaction;

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
		if (isMultiCurrencyEnabled()) {
			super.setCurrency(getCompany().getCurrency(vendor.getCurrency()));
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}

		if (vendor.getPhoneNo() != null)
			phoneSelect.setValue(vendor.getPhoneNo());
		else
			phoneSelect.setValue("");
		super.vendorSelected(vendor);

	}

	@Override
	public void initTransactionViewData() {

		if (transaction == null) {
			setData(new ClientVendorCreditMemo());
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
			super.vendorSelected(getCompany()
					.getVendor(transaction.getVendor()));
			vendorItemTransactionTable.setPayee(vendor);
			vendorItemTransactionTable
					.setAllRows(getItemTransactionItems(transaction
							.getTransactionItems()));
			vendorAccountTransactionTable
					.setAllRows(getAccountTransactionItems(transaction
							.getTransactionItems()));
			contactSelected(transaction.getContact());
			phoneSelect.setValue(transaction.getPhone());
			transactionNumber.setValue(transaction.getNumber());
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

			transactionTotalNonEditableText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());
		}

		if (transaction.getTransactionItems() != null) {
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					this.discountField.setPercentage(getdiscount(transaction
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
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));
		initMemoAndReference();
		super.initTransactionViewData();
		accountsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ACCOUNT, true));
		itemsDisclosurePanel.setOpen(checkOpen(
				transaction.getTransactionItems(),
				ClientTransactionItem.TYPE_ITEM, false));
		updateAmountsFromGUI();
	}

	public void resetElements() {
		this.setVendor(null);
		this.contact = null;
		this.phoneNo = null;
		setMemoTextAreaItem("");
		// setRefText("");

	}

	@Override
	public void createControls() {

		Label lab1 = new Label(messages.payeeCredit(Global.get().Vendor())
				+ "(" + getTransactionStatus() + ")");

		lab1.setStyleName("label-title");
		if (transaction == null
				|| transaction.getStatus() == ClientTransaction.STATUS_NOT_PAID_OR_UNAPPLIED_OR_NOT_ISSUED)
			lab1 = new Label(messages.payeeCredit(Global.get().Vendor()));

		else
			lab1 = new Label(messages.payeeCredit(Global.get().Vendor()) + "("
					+ getTransactionStatus() + ")");

		lab1.setStyleName("label-title");
		// lab1.setHeight("50px");
		transactionDateItem = createTransactionDateItem();

		transactionNumber = createTransactionNumberItem();
		transactionNumber.setTitle(messages.creditNoteNo());

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
		datepanel.add(dateNoForm);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");
		// labeldateNoLayout.add(lab1);
		labeldateNoLayout.add(datepanel);

		vendorCombo = createVendorComboItem(messages.payeeName(Global.get()
				.Vendor()));

		contactCombo = createContactComboItem();
		// if (this.isEdit)
		// FIXME--need to disable the form
		// vendorForm.setDisabled(true);

		phoneSelect = new TextItem(messages.phone(), "phoneSelect");
		phoneSelect.setToolTip(messages.phoneNumberOf(this.getAction()
				.getCatagory()));
		phoneSelect.setEnabled(!isInViewMode());
		// phoneSelect.setWidth(100);

		if (this.isInViewMode()) {
			// FiXME--The form need to be disabled
			// phoneForm.setDisabled(true);
		}

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
		taxCodeSelect = createTaxCodeSelectItem();
		// Label lab2 = new Label(messages.itemsAndExpenses());
		// menuButton = createAddNewButton();
		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackTax() && isTrackPaidTax(), isTaxPerDetailLine(),
				isTrackDiscounts(), isDiscountPerDetailLine(), isTrackClass(),
				isClassPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				VendorCreditMemoView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorCreditMemoView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return VendorCreditMemoView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				VendorCreditMemoView.this.updateNonEditableItems();
			}
		};

		vendorAccountTransactionTable.setEnabled(!isInViewMode());
		vendorAccountTransactionTable.getElement().getStyle()
				.setMarginTop(10, Unit.PX);

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
				VendorCreditMemoView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return VendorCreditMemoView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return VendorCreditMemoView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				VendorCreditMemoView.this.updateNonEditableItems();
			}
		};

		vendorItemTransactionTable.setEnabled(!isInViewMode());
		currencyWidget = createCurrencyFactorWidget();
		this.itemsFlowPanel = new StyledPanel("itemsFlowPanel");
		itemsDisclosurePanel = (GwtDisclosurePanel) GWT
				.create(GwtDisclosurePanel.class);
		itemsDisclosurePanel.setTitle(messages2.consultantName());
		itemsFlowPanel.add(vendorItemTransactionTable);

		itemsDisclosurePanel.setContent(itemsFlowPanel);

		StyledPanel leftVLay = new StyledPanel("leftVLay");

		vendorForm = UIUtils.form(Global.get().vendor());
		// vendorForm.setWidth("50%");
		vendorForm.add(vendorCombo, contactCombo, phoneSelect);
		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			vendorForm.add(classListCombo);
		}

		// vendorForm.getCellFormatter().getElement(0, 0).setAttribute(
		// messages.width(), "190px");

		leftVLay.add(vendorForm);

		StyledPanel rightVLay = new StyledPanel("rightVLay");
		DynamicForm locationForm = new DynamicForm("locationForm");
		if (locationTrackingEnabled) {
			locationForm.add(locationCombo);
			rightVLay.add(locationForm);
		}
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}

		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("fields-panel");
		topHLay.add(leftVLay);

		memoTextAreaItem = createMemoTextAreaItem();
		memoTextAreaItem.setWidth(100);
		// refText = createRefereceText();
		// refText.setWidth(100);
		vatinclusiveCheck = getVATInclusiveCheckBox();
		DynamicForm memoForm = new DynamicForm("memoForm");
		// memoForm.setWidth("100%");
		memoForm.add(memoTextAreaItem);
		// memoForm.getCellFormatter().addStyleName(0, 0, "memoFormAlign");
		DynamicForm vatCheckform = new DynamicForm("vatCheckform");
		// vatCheckform.setFields(vatinclusiveCheck);
		StyledPanel totalForm = new StyledPanel("totalForm");
		totalForm.setStyleName("boldtext");
		// netAmount.setWidth((netAmount.getMainWidget().getOffsetWidth() + 100)
		// + "px");

		StyledPanel bottomLayout = new StyledPanel("bottomLayout");
		leftVLay.add(vendorForm);
		StyledPanel rightVLay1 = new StyledPanel("rightVLay1");
		// rightVLay1.setWidth("100%");

		StyledPanel bottomLayout1 = new StyledPanel("bottomLayout1");

		StyledPanel bottomPanel = new StyledPanel("bottomPanel");

		DynamicForm transactionTotalForm = new DynamicForm(
				"transactionTotalForm");
		discountField = getDiscountField();

		DynamicForm taxForm = new DynamicForm("taxForm");
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

			StyledPanel vPanel = new StyledPanel("vPanel");
			vPanel.add(totalForm);

			bottomLayout1.add(memoForm);
			if (!isTaxPerDetailLine()) {
				taxForm.add(taxCodeSelect);

			}
			taxForm.add(vatinclusiveCheck);
			bottomLayout1.add(taxForm);
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					taxForm.add(discountField);
					bottomLayout1.add(taxForm);
				}
			}
			bottomLayout1.add(totalForm);

			bottomPanel.add(vPanel);
			bottomPanel.add(bottomLayout1);
		} else {
			memoForm.setStyleName("align-form");
			bottomLayout1.add(memoForm);
			if (isMultiCurrencyEnabled()) {
				transactionTotalForm.add(transactionTotalNonEditableText,
						foreignCurrencyamountLabel);
			} else {
				transactionTotalForm.add(transactionTotalNonEditableText);
			}
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					taxForm.add(discountField);
					bottomLayout1.add(taxForm);
				}
			}

			bottomLayout1.add(totalForm);

			bottomPanel.add(bottomLayout1);

		}

		totalForm.add(transactionTotalForm);

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		// mainVLay.setSize("100%", "100%");
		mainVLay.add(lab1);
		mainVLay.add(voidedPanel);
		mainVLay.add(labeldateNoLayout);
		StyledPanel topHLay1 = getTopLayout();
		if (topHLay1 != null) {
			topHLay1.add(leftVLay);
			topHLay1.add(rightVLay);
			mainVLay.add(topHLay1);
		} else {
			mainVLay.add(leftVLay);
			mainVLay.add(rightVLay);
		}

		mainVLay.add(accountsDisclosurePanel.getPanel());
		mainVLay.add(itemsDisclosurePanel.getPanel());
		mainVLay.add(bottomPanel);

		// if (UIUtils.isMSIEBrowser())
		// resetFormView();

		this.add(mainVLay);

		// setSize("100%", "100%");

		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);

		listforms.add(memoForm);

		listforms.add(vatCheckform);
		listforms.add(transactionTotalForm);
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}

		// settabIndexes();
	}

	protected StyledPanel getTopLayout() {
		StyledPanel styledPanel = new StyledPanel("topHLay1");
		styledPanel.addStyleName("fields-panel");
		return styledPanel;
	}

	@Override
	public void initMemoAndReference() {

		if (this.isInViewMode()) {

			ClientVendorCreditMemo vendorCreditMemo = transaction;

			if (vendorCreditMemo != null) {
				memoTextAreaItem.setDisabled(true);
				setMemoTextAreaItem(vendorCreditMemo.getMemo());
				// setRefText(vendorCreditMemo.getReference());

			}
		}
	}

	@Override
	public ClientVendorCreditMemo saveView() {
		ClientVendorCreditMemo saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
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
		super.updateTransaction();
		// Setting Vendor
		if (vendor != null)
			transaction.setVendor(getVendor().getID());

		// Setting Contact
		if (contact != null)
			transaction.setContact(contact);

		// Setting Phone
		if (phoneSelect.getValue() != null)
			transaction.setPhone(phoneSelect.getValue().toString());

		// Setting Total
		transaction.setTotal(vendorAccountTransactionTable.getGrandTotal()
				+ vendorItemTransactionTable.getGrandTotal());

		// Setting Memo
		transaction.setMemo(getMemoTextAreaItem());
		// Setting Reference
		// transaction.setReference(getRefText());

		transaction.setNetAmount(netAmount.getAmount());
		// itemReceipt.setAmountsIncludeVAT((Boolean) vatinclusiveCheck
		// .getValue());

		setAmountIncludeTAX();
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

		if (discountField.getPercentage() != 0.0 && transactionItems != null) {
			for (ClientTransactionItem item : transactionItems) {
				item.setDiscount(discountField.getPercentage());
			}

		}
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// Validations
		// 1. is Valid transaction date?
		// 2. is in prevent posting before date?
		// 3. vendorForm valid?
		// 4. isBlank transaction?
		// 5. is vendor transaction grid valid?
		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// messages.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}
		result.add(vendorForm.validate());
		if (getAllTransactionItems().isEmpty()) {
			result.addError(vendorAccountTransactionTable,
					messages.blankTransaction());
		} else {
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
		}
		return result;
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
		if (getPreferences().isTrackPaidTax()) {
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
	}

	// @Override
	// public void setViewConfiguration(ViewConfiguration viewConfiguration)
	// throws Exception {
	//
	// super.setViewConfiguration(viewConfiguration);
	//
	// if (isEdit && (!transactionObject.isVendorCreditMemo()))
	// throw new Exception("Unable to load the Required CashPurchase....");
	//
	// if (viewConfiguration.isInitWithPayee()) {
	// ClientPayee payee = viewConfiguration.getPayeeObject();
	//
	// if (payee == null || (!payee.isVendor()))
	// throw new Exception("Required Vendor Could Not be Loaded...");
	// }
	// }

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
		vendorCombo.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		vendorAccountTransactionTable.setEnabled(!isInViewMode());
		vendorItemTransactionTable.setEnabled(!isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		discountField.setEnabled(!isInViewMode());
		phoneSelect.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		classListCombo.setEnabled(!isInViewMode());
		super.onEdit();
	}

	@Override
	public void print() {
		updateTransaction();
		UIUtils.downloadAttachment(transaction.getID(),
				ClientTransaction.TYPE_VENDOR_CREDIT_MEMO);
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
		// vendorForm.setWidth("40%");
	}

	@Override
	protected String getViewTitle() {
		return messages.payeeCredit(Global.get().Vendor());
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
