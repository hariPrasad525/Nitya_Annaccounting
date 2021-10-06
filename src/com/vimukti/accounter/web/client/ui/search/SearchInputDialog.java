package com.vimukti.accounter.web.client.ui.search;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.google.gwt.core.client.GWT;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.cellview.client.SimplePager;
import com.google.gwt.user.cellview.client.SimplePager.TextLocation;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientEstimate;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.SearchInput;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.StyledPanel;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.CustomerCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.combo.ItemCombo;
import com.vimukti.accounter.web.client.ui.combo.PayeeCombo;
import com.vimukti.accounter.web.client.ui.combo.SelectCombo;
import com.vimukti.accounter.web.client.ui.combo.VendorCombo;
import com.vimukti.accounter.web.client.ui.core.AmountField;
import com.vimukti.accounter.web.client.ui.core.BaseDialog;
import com.vimukti.accounter.web.client.ui.core.TransactionsListView;
import com.vimukti.accounter.web.client.ui.forms.DateItem;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;

public class SearchInputDialog extends BaseDialog {

	private SelectCombo transactionTypeCombo, searchByTypeCombo, findByCombo,
			matchIfCombo;
	private DynamicForm mainForm, searchTypeForm, findbyForm, matchIfForm;
	private StyledPanel buttonPanel, mainPanel, allFormPanel, resultPanel;
	private StyledPanel panel, typePanel;
	private Button findButton, closeButton;
	private DateItem dateField;
	private CustomerCombo customerCombo;
	private VendorCombo vendorCombo;
	private AccountCombo accountCombo;
	private PayeeCombo payeeCombo;
	private ItemCombo itemCombo;
	private SearchInput input;
	private SearchTable grid;
	private Label labelItem;
	List<ClientAccount> accounts;
	private AmountField amountField;
	private SimplePager pager;

	private TextItem findByItem;
	private boolean isViewInitialized;

	public SearchInputDialog(String string) {
		super(string);
		this.getElement().setId("searchInputDialog");
		if (!isViewInitialized) {
			createControls();
		}
	}

	public void createControls() {

		mainForm = new DynamicForm("mainForm");
		typePanel = new StyledPanel("typePanel");
		transactionTypeCombo = new SelectCombo(messages.transactionType());
		List<String> transactionsList = getTransactionsList();
		transactionTypeCombo.initCombo(transactionsList);
		transactionTypeCombo.setComboItem(transactionsList.get(0));
		transactionTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						setSearchOptionsComboByType(selectItem);
					}
				});

		dateField = new DateItem(messages.findBy(), "dateField");
		dateField.setValue(new ClientFinanceDate());
		findByItem = new TextItem(messages.findBy(), "findByItem");
		dateField.addStyleName("search_combo_width");
		amountField = new AmountField(messages.findBy(), this);
		amountField.addStyleName("search_combo_width");
		searchTypeForm = new DynamicForm("searchTypeForm");
		searchByTypeCombo = new SelectCombo(messages.search());

		List<String> allList = allList();
		searchByTypeCombo.initCombo(allList);
		searchByTypeCombo.setComboItem(allList.get(0));
		searchByTypeCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<String>() {

					@Override
					public void selectedComboBoxItem(String selectItem) {
						setFindByComboOptions(selectItem);
					}
				});
		customerCombo = new CustomerCombo(messages.findBy(), false);
		vendorCombo = new VendorCombo(messages.findBy(), false);
		payeeCombo = new PayeeCombo(messages.findBy(), false);
		itemCombo = new ItemCombo(messages.findBy(), 1, false);
		itemCombo.initCombo(Accounter.getCompany().getAllItems());
		payeeCombo.initCombo(getCompany().getActivePayees());
		accountCombo = new AccountCombo(messages.findBy(), false) {

			@Override
			protected List<ClientAccount> getAccounts() {
				return getCompany().getActiveAccounts();
			}
		};
		findByCombo = new SelectCombo(messages.findBy());
		findByCombo.addStyleName("search_select_combo");
		matchIfCombo = new SelectCombo(messages.match(), false);

		List<String> matchByNumber = getMatchByNumber();
		matchIfCombo.initCombo(matchByNumber);
		matchIfCombo.setComboItem(matchByNumber.get(0));
		searchTypeForm.add(searchByTypeCombo);

		findbyForm = new DynamicForm("findbyForm");
		findbyForm.add(amountField);

		matchIfForm = new DynamicForm("matchIfForm");
		matchIfForm.add(matchIfCombo);

		allFormPanel = new StyledPanel("allFormPanel");
		allFormPanel.add(searchTypeForm);
		allFormPanel.add(findbyForm);
		allFormPanel.add(matchIfForm);
		allFormPanel.addStyleName("search_textbox_labels");

		buttonPanel = new StyledPanel("buttonPanel");

		buttonPanel.addStyleName("search_buttons");

		panel = new StyledPanel("panel");
		panel.setStyleName("search-all-forms");
		panel.add(allFormPanel);
		panel.add(buttonPanel);

		labelItem = new Label();
		labelItem.setStyleName("label-status");
		labelItem.setText(messages.status() + ": " + messages.selectCreteria());
		mainForm.add(transactionTypeCombo);
		transactionTypeCombo.getMainWidget().getParent().getElement()
				.addClassName("search_combo_width");
		typePanel.add(mainForm);
		typePanel.setStyleName("transaction_type_panel");

		mainPanel = new StyledPanel("mainPanel");
		mainPanel.add(typePanel);
		mainPanel.add(panel);
		mainPanel.add(labelItem);
		footerLayout.setVisible(false);

		setBodyLayout(mainPanel);
		mainPanel.getElement().getParentElement().addClassName("search-width");
		isViewInitialized = true;
	}

	@Override
	protected void createButtons(StyledPanel footer) {
		if (!isViewInitialized) {
			createControls();
		}

		findButton = new Button(messages.find());
		findButton.getElement().setAttribute("data-icon", "find");
		findButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				makeResult();
			}
		});

		closeButton = new Button(messages.close());
		closeButton.getElement().setAttribute("data-icon", "cancel");
		closeButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				onCancel();
			}
		});

		addButton(buttonPanel, findButton);
		addButton(buttonPanel, closeButton);
	}

	protected int getSearchByType(String value) {
		if (value.equals(messages.amount())) {
			return SearchInput.TYPE_AMOUNT;
		}
		if (value.equals(messages.descOrMemo())) {
			return SearchInput.TYPE_DESC_MEMO;
		}
		if (value.equals(messages.chequeNo())) {
			return SearchInput.TYPE_CHEQUE_NO;
		}
		if (value.equals(messages.receivedChequeNo())) {
			return SearchInput.TYPE_RECEIVED_NO;
		}
		if (value.equals(messages.creditNo())) {
			return SearchInput.TYPE_CREDIT_NO;
		}
		if (value.equals(messages.creditNoteNo())) {
			return SearchInput.TYPE_CREDIT_NOTE_NO;
		}
		if (value.equals(messages.chargeNo())) {
			return SearchInput.TYPE_CHARGE_NO;
		}
		if (value.equals(messages.invoiceNo())) {
			return SearchInput.TYPE_INOVICE_NO;
		}
		if (value.equals(messages.date())) {
			return SearchInput.TYPE_DATE;
		}
		if (value.equals(messages.dueDate())) {
			return SearchInput.TYPE_DUE_DATE;
		}
		if (value.equals(messages.invoiceDate())) {
			return SearchInput.TYPE_INVOICE_DATE;
		}
		if (value.equals(Global.get().Customer())) {
			return SearchInput.TYPE_CUSTOMER;
		}
		if (value.equals(Global.get().Vendor())) {
			return SearchInput.TYPE_VENDOR;
		}
		if (value.equals(messages.Account())) {
			return SearchInput.TYPE_ACCOUNT;
		}
		if (value.equals(messages.payee())) {
			return SearchInput.TYPE_PAYEE;
		}
		if (value.equals(messages.productOrService())) {
			return SearchInput.TYPE_PRODUCT_SERVICE;
		}
		if (value.equals(messages.estimateNo())) {
			return SearchInput.TYPE_ESTIMATE_NO;
		}
		if (value.equals(messages.saleNo())) {
			return SearchInput.TYPE_SALE_NO;
		}
		if (value.equals(messages.refundNo())) {
			return SearchInput.TYPE_REFUND_NO;
		}
		if (value.equals(messages.journalEntryNo())) {
			return SearchInput.TYPE_ENTRY_NO;
		}

		return 0;
	}

	protected void makeResult() {

		Object value = getFindByObjType().getValue();
		if (value == null || value.equals("")) {
			updateGrid();
			labelItem.setText(messages.status() + ": "
					+ messages.findByMustBeFilled());
			return;
		}

		input = new SearchInput();
		input.setTransactionType(getTransactionType(transactionTypeCombo
				.getSelectedValue()));
		input.setTransactionSubType(getTransactionSubType(transactionTypeCombo
				.getSelectedValue()));
		input.setSearchbyType(getSearchByType(searchByTypeCombo
				.getSelectedValue()));

		if (getFindByObjType() instanceof AmountField) {
			input.setAmount(amountField.getAmount());
		} else if (getFindByObjType() instanceof TextItem) {
			String string = getFindByObjType().getValue().toString();
			input.setFindBy(string);
		} else if (getFindByObjType() instanceof CustomerCombo) {
			input.setFindBy(((CustomerCombo) getFindByObjType())
					.getSelectedValue().getDisplayName());
		} else if (getFindByObjType() instanceof VendorCombo) {
			input.setFindBy(((VendorCombo) getFindByObjType())
					.getSelectedValue().getDisplayName());
		} else if (getFindByObjType() instanceof DateItem) {
			input.setValue(dateField.getDate().getDate());
		} else if (getFindByObjType() instanceof AccountCombo) {
			input.setFindBy(((AccountCombo) getFindByObjType())
					.getSelectedValue().getDisplayName());
		} else if (getFindByObjType() instanceof PayeeCombo) {
			input.setFindBy(((PayeeCombo) getFindByObjType())
					.getSelectedValue().getDisplayName());
		} else if (getFindByObjType() instanceof ItemCombo) {
			input.setFindBy(((ItemCombo) getFindByObjType()).getSelectedValue()
					.getDisplayName());
		}
		input.setMatchType(getMatachType(matchIfCombo.getSelectedValue()));
		if (resultPanel != null) {
			mainPanel.remove(resultPanel);
		}

		resultPanel = new StyledPanel("resultPanel");
		SimplePager.Resources pagerResources = GWT
				.create(SimplePager.Resources.class);
		pager = new SimplePager(TextLocation.CENTER, pagerResources, false, 0,
				true);

		grid = new SearchTable(input) {
			@Override
			public void disableGrid() {
				updateGrid();
				labelItem.setText(messages.status() + ": "
						+ messages.noRecordsToShow());
			}

			@Override
			public void enableGrid() {
				enableGridAndPager();
			}
		};

		pager.setDisplay(grid);
		pager.setStyleName("pager-alignment");

		resultPanel.add(grid);
		resultPanel.add(pager);
		resultPanel.setVisible(false);
	}

	private int getTransactionSubType(String selectedValue) {
		if (selectedValue.equals(messages.estimate())) {
			return ClientEstimate.QUOTES;
		} else if (selectedValue.equals(messages.credit())) {
			return ClientEstimate.CREDITS;
		} else if (selectedValue.equals(messages.charge())) {
			return ClientEstimate.CHARGES;
		} else if (selectedValue.equals(messages.salesOrder())) {
			return ClientEstimate.SALES_ORDER;
		}
		return 0;
	}

	protected void enableGridAndPager() {
		labelItem.setVisible(false);
		mainPanel.add(resultPanel);
		resultPanel.setVisible(true);
	}

	private void updateGrid() {
		labelItem.setVisible(true);

		if (resultPanel != null) {
			resultPanel.setVisible(false);
		}

	}

	private int getMatachType(String selectedValue) {
		if (selectedValue.equals(messages.contains())) {
			return 3;
		} else if (selectedValue.equals(messages.exact())) {
			return 0;
		} else if (selectedValue.equals(messages.greater())) {
			return 1;
		} else if (selectedValue.equals(messages.Less())) {
			return 2;
		}
		return 3;
	}

	protected int getTransactionType(String selectItem) {
		if (selectItem.equals(messages.all())) {
			return TransactionsListView.TYPE_ALL;
		} else if (selectItem.equals(messages.bills())) {
			return ClientTransaction.TYPE_ENTER_BILL;
		} else if (selectItem.equals(messages.billPayment())) {
			return ClientTransaction.TYPE_PAY_BILL;
		} else if (selectItem.equals(messages.cashExpense())) {
			return ClientTransaction.TYPE_CASH_EXPENSE;
		} else if (selectItem.equals(messages.charge())) {
			return ClientTransaction.TYPE_ESTIMATE;
		} else if (selectItem.equals(messages.credit())) {
			return ClientTransaction.TYPE_ESTIMATE;
		} else if (selectItem.equals(messages.salesOrder())) {
			return ClientTransaction.TYPE_ESTIMATE;
		} else if (selectItem.equals(messages.writeCheck())) {
			return ClientTransaction.TYPE_WRITE_CHECK;
		} else if (selectItem.equals(messages.creditCardExpense())) {
			return ClientTransaction.TYPE_CREDIT_CARD_EXPENSE;
		} else if (selectItem.equals(messages.creditCardCharge())) {
			return ClientTransaction.TYPE_CREDIT_CARD_CHARGE;
		} else if (selectItem.equals(messages.customerCreditNote(Global.get()
				.Customer()))) {
			return ClientTransaction.TYPE_CUSTOMER_CREDIT_MEMO;
		} else if (selectItem.equals(messages.invoice())) {
			return ClientTransaction.TYPE_INVOICE;
		} else if (selectItem.equals(messages.paymentFromCustomer())) {
			return ClientTransaction.TYPE_RECEIVE_PAYMENT;
		} else if (selectItem.equals(messages.estimate())) {
			return ClientTransaction.TYPE_ESTIMATE;
		} else if (selectItem.equals(messages.customerRefund(Global.get()
				.Customer()))) {
			return ClientTransaction.TYPE_CUSTOMER_REFUNDS;
		} else if (selectItem.equals(messages.cashSale())) {
			return ClientTransaction.TYPE_CASH_SALES;
		} else if (selectItem.equals(messages.deposit())) {
			return ClientTransaction.TYPE_MAKE_DEPOSIT;
		} else if (selectItem.equals(messages.journalEntry())) {
			return ClientTransaction.TYPE_JOURNAL_ENTRY;
		} else if (selectItem.equals(messages.transfer())) {
			return ClientTransaction.TYPE_TRANSFER_FUND;
		} else if (selectItem.equals(messages.vatPayment())) {
			return ClientTransaction.TYPE_PAY_TAX;
		} else if (selectItem.equals(messages.vatAdjustment())) {
			return ClientTransaction.TYPE_ADJUST_VAT_RETURN;
		} else if (selectItem.equals(messages
				.payeeCredit(Global.get().Vendor()))) {
			return ClientTransaction.TYPE_VENDOR_CREDIT_MEMO;
		} else if (selectItem.equals(messages.purchaseOrder())) {
			return ClientTransaction.TYPE_PURCHASE_ORDER;
		}
		return 0;
	}

	protected void close() {
		hide();
	}

	private List<String> getMatchIfComboList() {
		List<String> list = new ArrayList<String>();
		for (String string : getMatchIfoptions()) {
			list.add(string);
		}
		list.remove(2);
		list.remove(2);
		return list;
	}

	private List<String> getMatchByNumber() {
		List<String> list = new ArrayList<String>();
		for (String string : getMatchIfoptions()) {
			list.add(string);
		}
		list.remove(0);
		return list;
	}

	private List<String> getMatchByDate() {
		List<String> list = new ArrayList<String>();
		for (String string : getMatchIfoptions()) {
			list.add(string);
		}
		list.remove(0);
		list.remove(1);
		list.remove(1);
		return list;
	}

	protected void setFindByComboOptions(String selectItem) {
		labelItem.setText(messages.status() + ": " + messages.selectCreteria());
		findByItem.setValue("");
		amountField.setAmount(0.00);
		findbyForm.clear();
		matchIfForm.clear();
		if (selectItem.equals(messages.descOrMemo())
				|| selectItem.equals(messages.chequeNo())
				|| selectItem.equals(messages.receivedChequeNo())
				|| selectItem.equals(messages.creditNo())
				|| selectItem.equals(messages.creditNoteNo())
				|| selectItem.equals(messages.chargeNo())
				|| selectItem.equals(messages.invoiceNo())
				|| selectItem.equals(messages.journalEntryNo())) {
			findbyForm.add(findByItem);
			findByItem.getMainWidget().addStyleName("search_textbox");
			matchIfForm.add(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(messages.date())
				|| selectItem.equals(messages.dueDate())
				|| selectItem.equals(messages.invoiceDate())) {
			findbyForm.add(dateField);
			dateField.setValue(new ClientFinanceDate());
			dateField.getMainWidget().addStyleName("search_date");
			matchIfForm.add(matchIfCombo);
			matchIfCombo.initCombo(getMatchByDate());
		} else if (selectItem.equals(Global.get().Customer())) {
			findbyForm.add(customerCombo);
			matchIfForm.add(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(Global.get().Vendor())) {
			findbyForm.add(vendorCombo);
			matchIfForm.add(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(messages.Account())) {
			initAccountsCombo();
			findbyForm.add(accountCombo);
			accountCombo.getMainWidget().addStyleName("search_account_combo");
			matchIfForm.add(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(messages.payee())) {
			findbyForm.add(payeeCombo);
			payeeCombo.getMainWidget().addStyleName("search_account_combo");
			matchIfForm.add(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(messages.productOrService())) {
			findbyForm.add(itemCombo);
			itemCombo.getMainWidget().addStyleName("search_account_combo");
			matchIfForm.add(matchIfCombo);
			matchIfCombo.initCombo(getMatchIfComboList());
		} else if (selectItem.equals(messages.amount())) {
			findbyForm.add(amountField);
			amountField.getMainWidget().addStyleName("search_textbox");
			matchIfForm.add(matchIfCombo);
			matchIfCombo.initCombo(getMatchByNumber());
		} else if (selectItem.equals(messages.refundNo())
				|| selectItem.equals(messages.saleNo())
				|| selectItem.equals(messages.estimateNo())) {
			findbyForm.add(findByItem);
			findByItem.getMainWidget().addStyleName("search_textbox");
			matchIfForm.add(matchIfCombo);
			matchIfCombo.initCombo(getMatchByNumber());
		}
		matchIfCombo.setSelectedItem(0);

	}

	private void initAccountsCombo() {
		accounts = new ArrayList<ClientAccount>();
		accounts.clear();
		if (transactionTypeCombo.getSelectedValue().equals(messages.bills())
				|| transactionTypeCombo.getSelectedValue().equals(
						messages.creditCardCharge())
				|| transactionTypeCombo.getSelectedValue().equals(
						messages.payeeCredit(Global.get().Vendor()))) {
			accounts.addAll(getCompany()
					.getAccounts(ClientAccount.TYPE_EXPENSE));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_COST_OF_GOODS_SOLD));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_EXPENSE));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.writeCheck())) {
			accounts.addAll(getCompany()
					.getAccounts(ClientAccount.TYPE_EXPENSE));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_COST_OF_GOODS_SOLD));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.billPayment())
				|| transactionTypeCombo.getSelectedValue().equals(
						messages.cashExpense())
				|| transactionTypeCombo.getSelectedValue().equals(
						messages.vatPayment())) {
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_BANK));
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_CASH));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_CURRENT_ASSET));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_CREDIT_CARD));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_INVENTORY_ASSET));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.vatAdjustment())) {
			accounts.addAll(getCompany().getAccounts());
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.creditCardExpense())) {
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_CREDIT_CARD));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.customerCreditNote(Global.get().Customer()))) {
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_INCOME));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_CREDIT_CARD));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.deposit())) {
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_ASSET));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_CREDIT_CARD));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_CURRENT_LIABILITY));
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_BANK));
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_EQUITY));
			accountCombo.initCombo(accounts);
		} else if (transactionTypeCombo.getSelectedValue().equals(
				messages.paymentFromCustomer())) {
			accounts.addAll(getCompany().getAccounts(ClientAccount.TYPE_BANK));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_CREDIT_CARD));
			accounts.addAll(getCompany().getAccounts(
					ClientAccount.TYPE_OTHER_CURRENT_ASSET));
			accountCombo.initCombo(accounts);
		}
	}

	public FormItem<?> getFindByObjType() {
		for (int i = 0; i < findbyForm.getWidgetCount(); i++) {
			FormItem item = (FormItem) findbyForm.getWidget(i);
			return item;
		}
		return null;
	}

	protected void setSearchOptionsComboByType(String selectItem) {
		updateGrid();
		labelItem.setText(messages.status() + ": " + messages.selectCreteria());
		if (selectItem.equals(messages.all())) {
			setOptionsForPayeeList(selectItem);
		} else if (selectItem.equals(messages.bills())) {
			setOptionsForPayeeList(selectItem);
		} else if (selectItem.equals(messages.billPayment())) {
			setOptionsForSupplierTransactions(selectItem);
		} else if (selectItem.equals(messages.cashExpense())) {
			setOptionsForSupplierTransactions(selectItem);
		} else if (selectItem.equals(messages.charge())) {
			setOptionsByCustomer(selectItem);
		} else if (selectItem.equals(messages.credit())) {
			setOptionsByCustomer(selectItem);
		} else if (selectItem.equals(messages.salesOrder())) {
			setOptionsByCustomer(selectItem);
		} else if (selectItem.equals(messages.writeCheck())) {
			setOptionsForPayeeList(selectItem);
		} else if (selectItem.equals(messages.creditCardExpense())) {
			setOptionsForSupplierTransactions(selectItem);
		} else if (selectItem.equals(messages.creditCardCharge())) {
			setOptionsForPayeeList(selectItem);
		} else if (selectItem.equals(messages.customerCreditNote(Global.get()
				.Customer()))) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.invoice())) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.paymentFromCustomer())) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.estimate())) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.customerRefund(Global.get()
				.Customer()))) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.cashSale())) {
			setOptionsByCustomerTransactions(selectItem);
		} else if (selectItem.equals(messages.deposit())) {
			setOptionsForDepositList(selectItem);
		} else if (selectItem.equals(messages.journalEntry())) {
			setOptionsByJournal();
		} else if (selectItem.equals(messages.transfer())) {
			setOptionsByVatOrTransfer(selectItem);
		} else if (selectItem.equals(messages.vatPayment())) {
			setOptionsByVatOrTransfer(selectItem);
		} else if (selectItem.equals(messages.vatAdjustment())) {
			setOptionsByVatOrTransfer(selectItem);
		} else if (selectItem.equals(messages
				.payeeCredit(Global.get().Vendor()))) {
			setOptionsForSupplierTransactions(selectItem);
		} else if (selectItem.equals(messages.purchaseOrder())) {
			setOptionsForPayeeList(selectItem);
		}
	}

	private void setOptionsForDepositList(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : getSearchByDepositOptions()) {
			list.add(string);
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(0));
		searchTypeForm.add(searchByTypeCombo);
		findbyForm.add(amountField);
		setFindByComboOptions(list.get(0));
	}

	private void setOptionsForPayeeList(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : getSearchByPayee()) {
			list.add(string);
		}
		if (selectItem.equals(messages.purchaseOrder())) {
			list.remove(1);
		}
		if (selectItem.equals(messages.bills())) {
			list.add(messages.productOrService());
			list.add(messages.dueDate());
			list.remove(0);
			list.add(Global.get().Vendor());
		}
		if (selectItem.equals(messages.deposit())) {
			list.add(messages.receivedChequeNo());
		}
		if (selectItem.equals(messages.creditCardCharge())) {
			list.remove(0);
			list.add(Global.get().Vendor());
			list.add(messages.productOrService());
		}
		if (selectItem.equals(messages.writeCheck())) {
			list.remove(0);
			list.add(Global.get().Customer());
			list.add(Global.get().Vendor());
			list.add(messages.chequeNo());
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(1));
		searchTypeForm.add(searchByTypeCombo);
		findByItem.addStyleName("search_combo_width");
		findbyForm.add(findByItem);
		setFindByComboOptions(list.get(1));
	}

	private void setOptionsForAll(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> searchAll = new ArrayList<String>();
		for (String string : getSearchByAll()) {
			searchAll.add(string);
		}
		searchByTypeCombo.initCombo(searchAll);
		searchByTypeCombo.setComboItem(searchAll.get(0));
		searchTypeForm.add(searchByTypeCombo);
		findbyForm.add(findByItem);
		findByItem.addStyleName("search_combo_width");
		setFindByComboOptions(searchAll.get(0));
	}

	public void setOptionsForSupplierTransactions(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : getSearchBySupplierTransactionOptions()) {
			list.add(string);
		}
		if (selectItem.equals(messages.billPayment())) {
			list.remove(4);
			list.remove(4);
			list.add(messages.chequeNo());
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(1));
		searchTypeForm.add(searchByTypeCombo);
		findbyForm.add(findByItem);
		findByItem.addStyleName("search_combo_width");
		setFindByComboOptions(list.get(1));
	}

	public void setOptionsByCustomerTransactions(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : getSearchByCustomerTransactionOptions()) {
			list.add(string);
		}
		if (selectItem.equals(messages.customerRefund(Global.get().Customer()))) {
			list.remove(4);
			list.add(messages.refundNo());
		}
		if (selectItem.equals(messages.cashSale())) {
			list.add(messages.saleNo());
		}
		if (selectItem.equals(messages.customerCreditNote(Global.get()
				.Customer()))) {
			list.add(messages.creditNoteNo());
		}
		if (selectItem.equals(messages.invoice())) {
			list.add(messages.invoiceNo());
			list.add(messages.dueDate());
		}
		if (selectItem.equals(messages.estimate())) {
			list.add(messages.estimateNo());
			list.add(messages.invoiceDate());
		}
		if (selectItem.equals(messages.paymentFromCustomer())) {
			list.remove(4);
			list.add(messages.receivedChequeNo());
			list.add(messages.Account());
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(0));
		searchTypeForm.add(searchByTypeCombo);
		customerCombo.addStyleName("search_combo_width");
		findbyForm.add(customerCombo);
		setFindByComboOptions(list.get(0));
	}

	private void setOptionsByJournal() {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : getSearchByJournalOption()) {
			list.add(string);
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(1));
		searchTypeForm.add(searchByTypeCombo);
		findbyForm.add(dateField);
		setFindByComboOptions(list.get(1));
	}

	private void setOptionsByVatOrTransfer(String selectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : getSearchByVatOptions()) {
			list.add(string);
		}
		if (selectItem.equals(messages.transfer())) {
			list.add(messages.amount());

		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(0));
		searchTypeForm.add(searchByTypeCombo);
		findbyForm.add(accountCombo);
		setFindByComboOptions(list.get(0));
	}

	public void setOptionsByCustomer(String slectItem) {
		searchTypeForm.clear();
		findbyForm.clear();
		List<String> list = new ArrayList<String>();
		for (String string : getSearchByCustomerOptions()) {
			list.add(string);
		}
		if (slectItem.equals(messages.charge())
				|| slectItem.equals(messages.credit())) {
			list.remove(1);
			list.remove(3);
			list.add(messages.chargeNo());
		}
		searchByTypeCombo.initCombo(list);
		searchByTypeCombo.setComboItem(list.get(0));
		searchTypeForm.add(searchByTypeCombo);
		findbyForm.add(customerCombo);
		customerCombo.addStyleName("search_combo_width");
		setFindByComboOptions(list.get(0));
	}

	private List<String> allList() {
		List<String> searchAll = new ArrayList<String>();
		for (String string : getSearchByAll()) {
			searchAll.add(string);
		}
		return searchAll;
	}

	private List<String> getTransactionsList() {
		List<String> transactionTypes = new ArrayList<String>();
		for (String string : getTransactionNames()) {
			transactionTypes.add(string);
		}
		if (getCompany().getPreferences().isKeepTrackofBills()) {
			transactionTypes.add(messages.bills());
		}
		if (getCompany().getPreferences().isDoyouwantEstimates()) {
			transactionTypes.add(messages.estimate());
		}
		if (getCompany().getPreferences().isDelayedchargesEnabled()) {
			transactionTypes.add(messages.charge());
			transactionTypes.add(messages.credit());
		}
		if (getCompany().getPreferences().isSalesOrderEnabled()) {
			transactionTypes.add(messages.salesOrder());
		}

		if (getCompany().getPreferences().isPurchaseOrderEnabled()) {
			transactionTypes.add(messages.purchaseOrder());
		}
		if (getCompany().getPreferences().isTrackTax()) {
			transactionTypes.add(messages.vatPayment());
			transactionTypes.add(messages.vatAdjustment());
		}

		Collections.sort(transactionTypes);
		return transactionTypes;
	}

	@Override
	protected boolean onOK() {
		return false;
	}

	@Override
	public void setFocus() {
		transactionTypeCombo.setFocus();
	}

	@Override
	protected boolean onCancel() {
		String token = com.google.gwt.user.client.History.getToken();
		if (token.equalsIgnoreCase("search")) {
			com.google.gwt.user.client.History.back();
		}
		close();
		return true;
	}

	private String[] getTransactionNames() {
		return new String[] { messages.all(), messages.billPayment(),
				messages.cashExpense(), messages.creditCardExpense(),
				messages.customerCreditNote(Global.get().Customer()),
				messages.customerRefund(Global.get().Customer()),
				messages.cashSale(), messages.deposit(), messages.invoice(),
				messages.journalEntry(), messages.paymentFromCustomer(),
				messages.payeeCredit(Global.get().Vendor()),
				messages.transfer(), messages.writeCheck() };
	}

	private String[] getSearchByAll() {
		return new String[] { messages.amount(), messages.date(),
				messages.descOrMemo(), messages.payee() };
	}

	private String[] getSearchByPayee() {
		return new String[] { messages.payee(), messages.Account(),
				messages.amount(), messages.date(), messages.descOrMemo() };
	}

	private String[] getSearchByJournalOption() {
		return new String[] { messages.Account(), messages.date(),
				messages.descOrMemo(), messages.journalEntryNo() };
	}

	private String[] getSearchByCustomerTransactionOptions() {
		return new String[] { Global.get().Customer(), messages.amount(),
				messages.date(), messages.descOrMemo(),
				messages.productOrService() };
	}

	private String[] getSearchBySupplierTransactionOptions() {
		return new String[] { Global.get().Vendor(), messages.amount(),
				messages.Account(), messages.date(), messages.descOrMemo(),
				messages.productOrService() };
	}

	private String[] getSearchByCustomerOptions() {
		return new String[] { Global.get().Customer(), messages.Account(),
				messages.amount(), messages.date(), messages.dueDate(),
				messages.invoiceDate(), messages.descOrMemo(),
				messages.productOrService() };
	}

	private String[] getSearchByDepositOptions() {
		return new String[] { messages.amount(), messages.Account(),
				messages.date(), messages.descOrMemo() };
	}

	private String[] getMatchIfoptions() {
		return new String[] { messages.contains(), messages.exact(),
				messages.greater(), messages.Less() };
	}

	private String[] getSearchByVatOptions() {
		return new String[] { messages.Account(), messages.date(),
				messages.descOrMemo() };
	}
}
