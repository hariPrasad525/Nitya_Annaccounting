package com.vimukti.accounter.web.client.ui.vendors;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.Widget;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.AccounterCoreType;
import com.vimukti.accounter.web.client.core.AddNewButton;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientAccounterClass;
import com.vimukti.accounter.web.client.core.ClientCashPurchase;
import com.vimukti.accounter.web.client.core.ClientCurrency;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTAXCode;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionItem;
import com.vimukti.accounter.web.client.core.ClientUserPermissions;
import com.vimukti.accounter.web.client.core.ClientVendor;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.GwtDisclosurePanel;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.UIUtils;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.AccounterValidator;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.EditMode;
import com.vimukti.accounter.web.client.ui.core.IPrintableView;
import com.vimukti.accounter.web.client.ui.core.TaxItemsForm;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorAccountTransactionTable;
import com.vimukti.accounter.web.client.ui.edittable.tables.VendorItemTransactionTable;
import com.vimukti.accounter.web.client.ui.forms.AmountLabel;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.settings.RolePermissions;
import com.vimukti.accounter.web.client.ui.widgets.DateValueChangeHandler;

public class CashExpenseView extends
		AbstractVendorTransactionView<ClientCashPurchase> implements
		IPrintableView {

	protected DynamicForm vendorForm;
	public List<String> selectedComboList;
	private ArrayList<DynamicForm> listforms;
	protected Label titlelabel;
	protected VendorAccountTransactionTable vendorAccountTransactionTable;
	protected VendorItemTransactionTable vendorItemTransactionTable;
	protected AddNewButton accountTableButton, itemTableButton;
	protected GwtDisclosurePanel accountsDisclosurePanel, itemsDisclosurePanel;

	private AmountField accountBalText, vendorBalText;
	private StyledPanel accountFlowPanel;
	private StyledPanel itemsFlowPanel;

	public CashExpenseView() {
		super(ClientTransaction.TYPE_CASH_EXPENSE);
		this.getElement().setId("CashExpenseView");
	}

	@Override
	public ClientCashPurchase saveView() {
		ClientCashPurchase saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
			transaction.setNetAmount(netAmount.getAmount());
			if (getPreferences().isTrackPaidTax()) {
				setAmountIncludeTAX();
			}

		}
		return saveView;
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		// Setting Type
		transaction.setType(ClientTransaction.TYPE_CASH_EXPENSE);

		// transaction.setCashExpenseAccount(petycash.getSelectedValue());

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
		if (phoneNo != null)
			transaction.setPhone(phoneNo);

		// Setting Payment Methods
		transaction.setPaymentMethod(paymentMethod);

		// Setting Pay From Account
		transaction
				.setPayFrom(payFromCombo.getSelectedValue() != null ? payFromCombo
						.getSelectedValue().getID() : 0);

		// Setting Check number
		transaction.setCheckNumber(checkNo.getValue().toString());
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

		if (isTrackDiscounts()) {
			if (discountField.getPercentage() != 0.0
					&& transactionItems != null) {
				for (ClientTransactionItem item : transactionItems) {
					item.setDiscount(discountField.getPercentage());
				}
			}
		}
		// Setting Reference
		// cashPurchase.setReference(getRefText());
		if (currency != null)
			transaction.setCurrency(currency.getID());
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());
	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();

		// if (!AccounterValidator.isValidTransactionDate(transactionDate)) {
		// result.addError(transactionDate,
		// messages.invalidateTransactionDate());
		// }

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}

		result.add(vendorForm.validate());

		if (!AccounterValidator.isValidDueOrDelivaryDates(
				deliveryDateItem.getEnteredDate(), this.transactionDate)) {
			result.addError(deliveryDateItem,
					messages.the() + " " + messages.deliveryDate() + " " + " "
							+ messages.cannotbeearlierthantransactiondate());
		}

		if (getAllTransactionItems().isEmpty()) {
			result.addError(vendorAccountTransactionTable,
					messages.blankTransaction());
		} else {
			result.add(vendorAccountTransactionTable.validateGrid());
			result.add(vendorItemTransactionTable.validateGrid());
		}
		// ClientAccount bankAccount = payFromCombo.getSelectedValue();
		// check if the currency of accounts is valid or not
		ClientVendor vendor = vendorCombo.getSelectedValue();
		if (vendor != null) {
			ClientCurrency vendorCurrency = getCompany().getCurrency(
					vendor.getCurrency());
			// ClientCurrency bankCurrency =
			// getCurrency(bankAccount.getCurrency());
			if (vendorCurrency != getBaseCurrency()
					&& vendorCurrency != currency) {
				result.addError(payFromCombo, messages.selectProperVendor());
			}
		}
		return result;
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

	@Override
	public void showMenu(Widget button) {
		setMenuItems(button, messages.Accounts(),
				messages.productOrServiceItem());
	}

	// @Override
	// protected void refreshTransactionGrid() {
	// customerTransactionTable.updateTotals();
	// transactionsTree.updateTransactionTreeItemTotals();
	// }

	@Override
	protected String getViewTitle() {
		return messages.cashExpense();
	}

	private void settabIndexes() {
		paymentMethodCombo.setTabIndex(1);
		payFromCombo.setTabIndex(2);
		checkNo.setTabIndex(3);
		transactionDateItem.setTabIndex(4);
		transactionNumber.setTabIndex(5);
		accountBalText.setTabIndex(6);
		vendorBalText.setTabIndex(7);
		memoTextAreaItem.setTabIndex(8);
		// menuButton.setTabIndex(7);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(9);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(10);
		cancelButton.setTabIndex(11);

	}

	@Override
	protected void createControls() {

		titlelabel = new Label(messages.cashExpense());
		titlelabel.setStyleName("label-title");
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
		if (isTemplate) {
			dateNoForm.add(transactionNumber);
		} else {
			dateNoForm.add(transactionDateItem, transactionNumber);
		}

		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateNoForm);

		StyledPanel labeldateNoLayout = new StyledPanel("labeldateNoLayout");

		// labeldateNoLayout.add(titlelabel);
		labeldateNoLayout.add(datepanel);

		// --the form need to be disabled here
		dateNoForm.setEnabled(!this.isInViewMode());

		// formItems.add(transactionDateItem);
		// formItems.add(transactionNumber);

		vendorForm = UIUtils.form(Global.get().Vendor());

		// vendorForm.setWidth("100%");

		vendorCombo = createVendorComboItem(messages.payeeName(Global.get()
				.Vendor()));
		vendorCombo.setRequired(false);

		// formItems.add(contactCombo);
		// formItems.add(billToCombo);

		payFromCombo = createPayFromCombo(messages.payFrom());
		payFromCombo.setPopupWidth("500px");
		checkNo = createCheckNumberItem(messages.chequeNo());
		checkNo.setEnabled(true);
		// checkNo.setWidth(100);
		deliveryDateItem = createTransactionDeliveryDateItem();

		paymentMethodCombo = createPaymentMethodSelectItem();

		paymentMethodCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						paymentMethod = selectItem;
						paymentMethodSelected(paymentMethodCombo
								.getSelectedValue());
						if (paymentMethodCombo.getSelectedValue().equals(
								messages.check())
								|| paymentMethodCombo.getSelectedValue()
										.equals(messages.cheque())) {
							checkNo.setEnabled(false);
						} else {
							checkNo.setEnabled(true);
						}

						if (paymentMethod.equals(messages.check())
								&& isInViewMode() && payFromAccount != null) {
							ClientCashPurchase cashPurchase = transaction;
							checkNo.setValue(cashPurchase.getCheckNumber());
						}
					}
				});
		String listString[] = new String[] { messages.cash(),
				messages.creditCard(), messages.directDebit(),
				messages.masterCard(), messages.onlineBanking(),
				messages.standingOrder(), messages.switchMaestro() };
		selectedComboList = new ArrayList<String>();
		for (int i = 0; i < listString.length; i++) {
			selectedComboList.add(listString[i]);
		}
		paymentMethodCombo.initCombo(selectedComboList);

		vendorForm.add(vendorCombo, paymentMethodCombo, payFromCombo);
		taxCodeSelect = createTaxCodeSelectItem();
		// Ending and Vendor Balance
		accountBalText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency(), "accountBalText");
		// accountBalText.setWidth(100);
		accountBalText.setEnabled(false);

		vendorBalText = new AmountField(messages.payeeBalance(Global.get()
				.Vendor()), this, getBaseCurrency(), "vendorBalText");
		vendorBalText.setEnabled(false);
		classListCombo = createAccounterClassListCombo();
		if (isTrackClass() && !isClassPerDetailLine()) {
			vendorForm.add(classListCombo);
		}

		netAmount = new AmountLabel(
				messages.currencyNetAmount(getBaseCurrency().getFormalName()),
				getBaseCurrency());
		netAmount.setDefaultValue("£0.00");
		netAmount.setEnabled(true);

		transactionTotalNonEditableText = createTransactionTotalNonEditableItem(getCompany()
				.getPrimaryCurrency());

		foreignCurrencyamountLabel = createForeignCurrencyAmountLable(getCompany()
				.getPrimaryCurrency());

		vatTotalNonEditableText = new TaxItemsForm();// createVATTotalNonEditableItem();

		vatinclusiveCheck = getVATInclusiveCheckBox();
		vendorAccountTransactionTable = new VendorAccountTransactionTable(
				isTrackDiscounts(), isTrackTax() && isTrackPaidTax(),
				isTaxPerDetailLine(), isTrackClass(), isClassPerDetailLine(),
				this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashExpenseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashExpenseView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CashExpenseView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CashExpenseView.this.updateNonEditableItems();
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
				isTrackDiscounts(), isTrackTax(), isTaxPerDetailLine(),
				isTrackClass(), isClassPerDetailLine(), this) {

			@Override
			protected void updateNonEditableItems() {
				if (currencyWidget != null) {
					setCurrencyFactor(currencyWidget.getCurrencyFactor());
				}
				CashExpenseView.this.updateNonEditableItems();
			}

			@Override
			public boolean isShowPriceWithVat() {
				return CashExpenseView.this.isShowPriceWithVat();
			}

			@Override
			protected boolean isInViewMode() {
				return CashExpenseView.this.isInViewMode();
			}

			@Override
			protected void updateDiscountValues(ClientTransactionItem row) {
				if (discountField.getPercentage() != null
						&& discountField.getPercentage() != 0) {
					row.setDiscount(discountField.getPercentage());
				}
				CashExpenseView.this.updateNonEditableItems();
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
		// refText = createRefereceText();
		// refText.setWidth(100);
		currencyWidget = createCurrencyFactorWidget();
		DynamicForm memoForm = new DynamicForm("memoForm");
		// memoForm.setWidth("100%");
		memoForm.add(memoTextAreaItem);

		StyledPanel totalForm = new StyledPanel("totalForm");
		totalForm.setStyleName("boldtext");

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		// leftVLay.setWidth("100%");
		leftVLay.add(vendorForm);

		StyledPanel rightVLay = new StyledPanel("rightVLay");
		// rightVLay.setWidth("100%");
		DynamicForm locationform = new DynamicForm("locationform");
		if (locationTrackingEnabled) {
			locationform.add(locationCombo);
		}
		locationform.add(accountBalText, vendorBalText);
		rightVLay.add(locationform);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
			currencyWidget.setEnabled(!isInViewMode());
		}

		StyledPanel bottomLayout = new StyledPanel("bottomLayout");

		StyledPanel bottompanel = new StyledPanel("bottompanel");

		DynamicForm transactionTotalForm = new DynamicForm(
				"transactionTotalForm");

		discountField = getDiscountField();

		DynamicForm form = new DynamicForm("form");
		if (isTrackTax() && isTrackPaidTax()) {
			DynamicForm netAmountForm = new DynamicForm("netAmountForm");
			netAmountForm.add(netAmount);

			totalForm.add(netAmountForm);
			totalForm.add(vatTotalNonEditableText);

			StyledPanel vpanel = new StyledPanel("vpanel");

			transactionTotalForm.add(transactionTotalNonEditableText);

			if (isMultiCurrencyEnabled())
				transactionTotalForm.add(foreignCurrencyamountLabel);

			vpanel.add(totalForm);

			bottomLayout.add(memoForm);
			if (!isTaxPerDetailLine()) {
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

			transactionTotalForm.add(transactionTotalNonEditableText);

			if (isMultiCurrencyEnabled())
				transactionTotalForm.add(foreignCurrencyamountLabel);

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

		mainVLay.add(accountsDisclosurePanel.getPanel());
		mainVLay.add(itemsDisclosurePanel.getPanel());
		mainVLay.add(bottompanel);

		this.add(mainVLay);
		/* Adding dynamic forms in list */
		listforms.add(dateNoForm);
		listforms.add(vendorForm);
		listforms.add(memoForm);
		listforms.add(transactionTotalForm);

		// settabIndexes();
		if (isMultiCurrencyEnabled()) {
			foreignCurrencyamountLabel.hide();
		}
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
			checkNo.setValue(cashPurchase.getCheckNumber());
		} else {
			checkNo.setValue("");
		}

		this.accountBalText.setAmount(account.getCurrentBalance());
		accountBalText.setCurrency(getCurrency(account.getCurrency()));
		ClientCurrency currency = getCurrency(account.getCurrency());
		if (currency.getID() != 0) {
			currencyWidget.setSelectedCurrencyFactorInWidget(currency,
					transactionDateItem.getDate().getDate());
		} else {
			currencyWidget.setSelectedCurrency(getBaseCurrency());
		}
		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(currencyWidget.getCurrencyFactor());
			updateAmountsFromGUI();
		}
	}

	protected void setCheckNumber() {

		rpcUtilService.getNextCheckNumber(payFromAccount.getID(),
				new AccounterAsyncCallback<Long>() {

					@Override
					public void onException(AccounterException t) {
						// //UIUtils.logError(
						// "Failed to get the next check number!!", t);
						checkNo.setValue(messages.toBePrinted());
						return;
					}

					@Override
					public void onResultSuccess(Long result) {
						if (result == null)
							onFailure(null);

						checkNumber = String.valueOf(result);
						checkNo.setValue(result.toString());
					}

				});

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
			setVendor(getCompany().getVendor(transaction.getVendor()));
			vendorItemTransactionTable.setPayee(vendor);
			if (transaction.getTransactionItems() != null
					&& !transaction.getTransactionItems().isEmpty()) {
				this.vendorAccountTransactionTable
						.setAllRows(getAccountTransactionItems(transaction
								.getTransactionItems()));
				this.vendorItemTransactionTable
						.setAllRows(getItemTransactionItems(transaction
								.getTransactionItems()));
			}
			paymentMethodCombo.setComboItem(transaction.getPaymentMethod());
			accountSelected(getCompany().getAccount(transaction.getPayFrom()));
			// transactionDateItem.setEnteredDate(cashPurchaseToBeEdited.get)
			initMemoAndReference();
			checkNo.setEnabled(false);
			netAmount.setAmount(transaction.getNetAmount());
			if (getPreferences().isTrackPaidTax()) {
				if (getPreferences().isTaxPerDetailLine()) {
					vatTotalNonEditableText.setTransaction(transaction);
				} else {
					selectTAXCode();
				}
			}
			transactionTotalNonEditableText
					.setAmount(getAmountInBaseCurrency(transaction.getTotal()));
			foreignCurrencyamountLabel.setAmount(transaction.getTotal());

			if (vatinclusiveCheck != null) {
				setAmountIncludeChkValue(isAmountIncludeTAX());
			}
			deliveryDateItem.setValue(new ClientFinanceDate(transaction
					.getDeliveryDate()));
			if (vendor != null) {
				vendorBalText.setAmount(vendor.getBalance());
			}
		}
		if (locationTrackingEnabled)
			locationSelected(getCompany()
					.getLocation(transaction.getLocation()));

		if (transaction.getTransactionItems() != null) {
			if (isTrackDiscounts()) {
				if (!isDiscountPerDetailLine()) {
					this.discountField.setPercentage(getdiscount(transaction
							.getTransactionItems()));
				}
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

	@Override
	protected void vendorSelected(ClientVendor vendor) {
		if (vendor == null) {
			return;
		}
		vendorItemTransactionTable.setPayee(vendor);
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
		if (vendor == null) {
			return;
		}
		this.setVendor(vendor);
		paymentMethodSelected(vendor.getPaymentMethod());
		long code = vendor.getTAXCode();
		if (taxCodeSelect != null) {
			if (code == 0)
				code = Accounter.getCompany().getDefaultTaxCode();
			taxCodeSelect.setComboItem(getCompany().getTAXCode(code));
		}

		long currency = vendor.getCurrency();
		ClientCurrency clientCurrency = getCompany().getCurrency(currency);
		// if (currency != 0) {
		// clientCurrency = getCompany().getCurrency(currency);
		// currencyWidget.setSelectedCurrencyFactorInWidget(clientCurrency,
		// transactionDateItem.getDate().getDate());
		// } else {
		// clientCurrency = getCompany().getPrimaryCurrency();
		// if (clientCurrency != null) {
		// currencyWidget.setSelectedCurrency(clientCurrency);
		// }
		// }
		vendorBalText.setAmount(vendor.getBalance());
		vendorBalText.setCurrency(clientCurrency);
		vendorAccountTransactionTable.setTaxCode(code, false);
		vendorItemTransactionTable.setTaxCode(code, false);

	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		super.paymentMethodSelected(paymentMethod);
		if (paymentMethodCombo.getComboItems().contains(paymentMethod)) {
			paymentMethodCombo.setComboItem(paymentMethod);
		}
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		transaction.setNetAmount(netAmount.getAmount());
		if (getPreferences().isTrackPaidTax()) {
			setAmountIncludeTAX();
		}
		createAlterObject();
	}

	public void createAlterObject() {

		saveOrUpdate(transaction);

	}

	@Override
	protected void initMemoAndReference() {
		memoTextAreaItem.setDisabled(isInViewMode());
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
		transactionTotalNonEditableText
				.setAmount(getAmountInBaseCurrency(grandTotal));
		foreignCurrencyamountLabel.setAmount(grandTotal);
	}

	public static CashPurchaseView getInstance() {
		return new CashPurchaseView();
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

	protected void enableFormItems() {
		setMode(EditMode.EDIT);
		vendorCombo.setEnabled(!isInViewMode());
		transactionDateItem.setEnabled(!isInViewMode());
		transactionNumber.setEnabled(!isInViewMode());
		paymentMethodCombo.setEnabled(!isInViewMode());
		if (paymentMethod.equals(messages.check())
				|| paymentMethod.equals(messages.cheque())) {
			checkNo.setEnabled(!isInViewMode());
		} else {
			checkNo.setEnabled(isInViewMode());
		}
		deliveryDateItem.setEnabled(!isInViewMode());
		vendorAccountTransactionTable.setEnabled(!isInViewMode());
		vendorItemTransactionTable.setEnabled(!isInViewMode());
		accountTableButton.setEnabled(!isInViewMode());
		itemTableButton.setEnabled(!isInViewMode());
		memoTextAreaItem.setDisabled(isInViewMode());
		discountField.setEnabled(!isInViewMode());
		if (locationTrackingEnabled)
			locationCombo.setEnabled(!isInViewMode());
		if (currencyWidget != null) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		if (isTrackClass()) {
			classListCombo.setEnabled(!isInViewMode());
		}
		super.onEdit();
	}

	@Override
	public void print() {
		updateTransaction();
		UIUtils.downloadAttachment(transaction.getID(),
				ClientTransaction.TYPE_CASH_EXPENSE);
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

	protected boolean isSaveButtonAllowed() {
		ClientUserPermissions permissions = Accounter.getUser()
				.getPermissions();
		// canDoBankReconcialiation || canDoManageAccounts
		if (Accounter.getUser().canDoInvoiceTransactions()) {
			return true;
		}
		if (permissions.getTypeOfBankReconcilation() == RolePermissions.TYPE_YES) {
			return true;
		}

		if (permissions.getTypeOfManageAccounts() == RolePermissions.TYPE_YES) {
			return true;
		}

		return false;
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
