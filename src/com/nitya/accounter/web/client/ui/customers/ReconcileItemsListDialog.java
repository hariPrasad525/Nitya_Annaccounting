package com.nitya.accounter.web.client.ui.customers;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.BlurEvent;
import com.google.gwt.event.dom.client.BlurHandler;
import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.Window;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Button;
import com.google.gwt.user.client.ui.Label;
import com.google.gwt.user.client.ui.ScrollPanel;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientCustomer;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientJournalEntry;
import com.nitya.accounter.web.client.core.ClientStatementRecord;
import com.nitya.accounter.web.client.core.ClientTAXCode;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientVendor;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Lists.TransactionsList;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.combo.BankAccountCombo;
import com.nitya.accounter.web.client.ui.combo.CustomerCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.SelectCombo;
import com.nitya.accounter.web.client.ui.combo.TAXCodeCombo;
import com.nitya.accounter.web.client.ui.combo.VendorCombo;
import com.nitya.accounter.web.client.ui.core.BaseDialog;
import com.nitya.accounter.web.client.ui.core.ViewManager;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.grids.MatchedReconcileItemsListGrid;

/**
 * to show the reconcile dialog based on the selected Received or Spent
 * Transaction
 */
public class ReconcileItemsListDialog extends BaseDialog implements
		AsyncCallback<PaginationList<TransactionsList>> {

	private TextItem search_name, search_amount, adjustment_amt;
	private Label search_nameLabel, search_amountLabel, amt, total_amt_label,
			money_spent_label, error_label, total_label;
	private Button goBtn;
	private final AccounterMessages messages;
	private MatchedReconcileItemsListGrid total_grid, selected_tr_grid;
	private List<TransactionsList> transactionsList;
	private List<TransactionsList> selected_transactionsList;
	private final String title;
	private final ClientStatementRecord record;
	private double matchBalance, sel_rec_balance, error_balance;
	private DynamicForm form_moneyspent, form_totalAmt;
	private StyledPanel vPanel, adjustment_panel, adjustmentBtnBar,
			v_trans_panel;
	StyledPanel v_amt_panel;
	private StyledPanel h_btn_Panel, h_subtotal_panel, hpanel;
	private TextItem to, description, bankfees_amt;
	private SelectCombo regionCombo;
	private DynamicForm bankfees_form;
	private TAXCodeCombo taxRateCombo;
	private BankAccountCombo accountCombo;
	private ClientAccount selected_account;
	private ClientTAXCode selected_TAXCode;
	private ScrollPanel scrollPanel;
	private ClientCustomer customer;
	private ClientVendor vendor;
	private boolean isSpent;
	private final ClientAccount bankAccount;
	private String nextJournalNumber;
	private CustomerCombo customerComboTo;
	private VendorCombo vendorComboTo;
	private ClientJournalEntry obj;

	public ReconcileItemsListDialog(String title, ClientStatementRecord obj,
			ClientAccount account) {
		super(title);
		this.getElement().setId("ReconcileItemsListDialog");
		this.title = title;
		this.record = obj;
		this.bankAccount = account;

		if (obj.getSpentAmount() > 0) {
			isSpent = true;
		}
		messages = Global.get().messages();
		setText(title);
		// setWidth("800px");
		createControls();
		ViewManager.getInstance().showDialog(this);
	}

	@Override
	protected boolean onOK() {
		return false;
	}

	@Override
	public void setFocus() {
	}

	private void createControls() {

		// we need to focus Reconcile button only when amounts are matched
		okbtn.setEnabled(false);
		okbtn.setText("Reconcile");

		// to initialize the grid
		initTotalTransactionsDataGrid();

		StyledPanel main_Vpanel = new StyledPanel("main_Vpanel");
		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		horizontalPanel.add(total_grid);
		// scrollPanel = new ScrollPanel();
		// scrollPanel.addStyleName("pre_scroll_table");
		// scrollPanel.add(total_grid);
		// horizontalPanel.add(scrollPanel);
		Label label_1 = new Label("1. Find & select matching transaction");
		Label label_2 = new Label(
				"2. View your selected transactions. Add new transactions, as needed.");
		Label label_3 = new Label(
				"3. The sum of your selected transactions must match the money spent. Make adjustments, as needed.");
		StyledPanel panel = new StyledPanel("panel");
		// for displaying the search box
		search_nameLabel = new Label("Search by name");
		panel.add(search_nameLabel);

		search_name = new TextItem("", "search_name");
		DynamicForm dynamicForm = new DynamicForm("dynamicForm");
		dynamicForm.add(search_name);
		panel.add(dynamicForm);

		search_amountLabel = new Label("Search by amount");
		panel.add(search_amountLabel);

		search_amount = new TextItem("", "search_amount");
		DynamicForm dynamicForm1 = new DynamicForm("dynamicForm1");
		dynamicForm1.add(search_amount);
		panel.add(dynamicForm1);

		goBtn = new Button(messages.search());
		goBtn.getElement().setAttribute("data-icon", "search");
		addButton(panel, goBtn);
		goBtn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				setSearchDataToGrid();
			}
		});
		horizontalPanel.add(panel);
		main_Vpanel.add(label_1);
		main_Vpanel.add(horizontalPanel);
		main_Vpanel.add(label_2);
		main_Vpanel.add(selected_tr_grid);

		h_btn_Panel = new StyledPanel("h_btn_Panel");
		v_amt_panel = new StyledPanel("v_amt_panel");

		v_amt_panel.add(label_3);

		// for displaying the balance label fields
		v_trans_panel = new StyledPanel("v_trans_panel");
		StyledPanel hh = new StyledPanel("hh");
		h_subtotal_panel = new StyledPanel("h_subtotal_panel");
		total_amt_label = new Label("No transactions selected");
		money_spent_label = new Label("Must match : Money Spent  "
				+ matchBalance);
		error_label = new Label();
		total_label = new Label();

		h_subtotal_panel.add(total_amt_label);
		v_trans_panel.add(h_subtotal_panel);
		hh.add(money_spent_label);
		hh.add(error_label);
		// hh.add(total_label);
		hpanel = new StyledPanel("hpanel");
		v_trans_panel.add(hpanel);
		v_trans_panel.add(hh);
		v_amt_panel.add(v_trans_panel);
		vPanel = new StyledPanel("vPanel");

		form_totalAmt = new DynamicForm("form_totalAmt");
		form_moneyspent = new DynamicForm("form_moneyspent");
		adjustment_amt = new TextItem("Adjustment", "adjustment_amt");
		bankfees_amt = new TextItem(messages.amount(), "bankfees_amt");

		adjustment_panel = new StyledPanel("vPanel");

		v_amt_panel.add(form_totalAmt);
		v_amt_panel.add(adjustment_panel);
		h_btn_Panel.add(v_amt_panel);
		main_Vpanel.add(vPanel);

		// for displaying the Adjustments buttons
		adjustmentBtnBar = new StyledPanel("adjustmentBtnBar");
		final Button bankfees_btn = new Button("Bank fees");
		final Button minor_adj_btn = new Button("Minor Adjustment");
		minor_adj_btn.getElement().setAttribute("data-icon", "highlight");
		addButton(adjustmentBtnBar, bankfees_btn);
		addButton(adjustmentBtnBar, minor_adj_btn);

		bankfees_btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				bankfees_btn.setEnabled(false);
				displayBankFeesDetails();
			}
		});

		minor_adj_btn.addClickHandler(new ClickHandler() {
			@Override
			public void onClick(ClickEvent event) {
				minor_adj_btn.setEnabled(false);
				adjustment_panel.setVisible(true);
				showAdjustmentAmount();
			}
		});
		h_btn_Panel.add(adjustmentBtnBar);
		main_Vpanel.add(h_btn_Panel);
		main_Vpanel.add(form_moneyspent);

		setBodyLayout(main_Vpanel);
	}

	/**
	 * for changing the ok_btn to Reconcile Button, when must match amount
	 * matched with the selected records total or with Adjustment amount
	 */
	private void changeButtonName() {
		double amt = 0;
		String value;
		if (adjustment_amt != null) {
			value = adjustment_amt.getValue();
			if (value.trim().length() > 0) {
				amt = Double.parseDouble(value);
			}
		}
		if (bankfees_amt != null) {
			value = bankfees_amt.getValue();
			if (value.trim().length() > 0) {
				amt = Double.parseDouble(value);
			}
		}
		if (matchBalance == sel_rec_balance || error_balance == amt
				|| amt == matchBalance - sel_rec_balance) {
			okbtn.setEnabled(true);

		}
	}

	/**
	 * Search the transactions By Amount and name
	 */
	private void setSearchDataToGrid() {
		total_grid.removeAllRecords();
		String text = search_name.getValue();
		String amountVal = search_amount.getValue();
		List<TransactionsList> matched_List = new ArrayList<TransactionsList>();
		// Search By Amount
		if (amountVal != null && !transactionsList.isEmpty()
				&& amountVal.trim().length() > 0) {
			double amount = Double.parseDouble(search_amount.getValue());
			for (TransactionsList transaction : transactionsList) {
				if (isSpent) {
					if (transaction.getSpentAmount() == amount) {
						matched_List.add(transaction);
					}
				} else {
					if (transaction.getReceivedAmount() == amount) {
						matched_List.add(transaction);
					}
				}
			}
			// Search by name
		} else if (text != null && !transactionsList.isEmpty()
				&& text.trim().length() > 0) {
			for (TransactionsList transaction : transactionsList) {
				if (transaction.getCustomerName().equalsIgnoreCase(text)) {
					matched_List.add(transaction);
				}
			}
		}
		if (!matched_List.isEmpty()) {
			total_grid.setRecords(matched_List);
		} else {
			total_grid.setRecords(transactionsList);
		}

		search_name.setValue("");
		search_amount.setValue("");
	}

	/**
	 * used to display the Adjustment text column , to enter the adjustment
	 * amount
	 */
	private void showAdjustmentAmount() {
		adjustment_panel.clear();

		DynamicForm f = new DynamicForm("f");
		f.add(adjustment_amt);
		adjustment_panel.add(f);

		adjustment_amt.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				changeButtonName();
			}
		});

	}

	/**
	 * to update the balance amounts, based on the selected records from the
	 * grid
	 */
	private void updateBalanceValues() {
		sel_rec_balance = 0;
		if (selected_tr_grid.getRecords().size() > 0) {

			List<TransactionsList> records = selected_tr_grid.getRecords();
			for (TransactionsList obj : records) {
				if (this.title.contains("Spent")) {// for spent transactions
					sel_rec_balance = sel_rec_balance + obj.getSpentAmount();
				} else {
					// for received transactions
					sel_rec_balance = sel_rec_balance + obj.getReceivedAmount();
				}
			}

			h_subtotal_panel.clear();
			amt = new Label();
			total_amt_label.setText("Subtotal ");

			amt.setText(" " + sel_rec_balance);
			h_subtotal_panel.add(total_amt_label);
			h_subtotal_panel.add(amt);

		}
		// for updating the must match total balance
		total_label.setText("" + sel_rec_balance);
		// for updating the remaining balance
		error_balance = matchBalance - sel_rec_balance;
		error_label.setText(" Total is out by : "
				+ String.valueOf(error_balance));

		if (error_balance == 0) {
			// if the matched balance is zero, then enable the reconcile button
			okbtn.setEnabled(true);
		}
	}

	/**
	 * to initialize the grid with records, based on the spent or received
	 * transactions
	 */
	private void initTotalTransactionsDataGrid() {
		total_grid = new MatchedReconcileItemsListGrid(this, isSpent);
		total_grid.init();

		selected_tr_grid = new MatchedReconcileItemsListGrid(this, isSpent);
		selected_tr_grid.init();

		if (title.equals("Spent Transactions")) {
			// to get all the spent transactions
			matchBalance = record.getSpentAmount();
			Accounter.createHomeService().getSpentTransactionsList(
					getCompany().getPreferences().getEndOfFiscalYear(),
					new ClientFinanceDate().getDate(), 0, 10, this);
		} else {
			// to get all the received transactions
			matchBalance = record.getReceivedAmount();
			Accounter.createHomeService().getReceivedTransactionsList(
					getCompany().getPreferences().getEndOfFiscalYear(),
					new ClientFinanceDate().getDate(), 0, 10, this);
		}

		// for getting the next journalEntry Number
		setTransactionNumber();

	}

	@Override
	protected void processOK() {
		setSelectedTransactionList();
		this.removeFromParent();
	}

	/**
	 * 
	 * for setting the selected records to the statementRecord
	 */
	private void setSelectedTransactionList() {

		for (int i = 0; i < selected_transactionsList.size(); i++) {
			final int index = i;
			TransactionsList item = selected_transactionsList.get(i);

			AccounterCoreType coreType = UIUtils.getAccounterCoreType(item
					.getType());
			Accounter.createGETService().getObjectById(coreType,
					item.getTransactionId(),
					new AsyncCallback<ClientTransaction>() {
						private String bankFessAdjustmentAmt;

						@Override
						public void onFailure(Throwable caught) {
						}

						@Override
						public void onSuccess(ClientTransaction result) {
							List<ClientTransaction> clientTransactions = new ArrayList<ClientTransaction>();

							if (record.getTransactionsLists().isEmpty()) {
								clientTransactions.add(result);
							} else {
								for (ClientTransaction clientTransaction : record
										.getTransactionsLists()) {
									if (result.getID() != clientTransaction
											.getID()) {
										clientTransactions.add(result);
									}
								}
							}
							if (index == selected_transactionsList.size() - 1) {
								record.getTransactionsLists().addAll(
										clientTransactions);
								record.setMatched(true);
							}
							String value = adjustment_amt.getValue();
							if (!value.isEmpty() && value.trim() != null) {
								record.setAdjustmentAmount(Double
										.parseDouble(value));
							}
							String bankFessAdjustmentAmt = bankfees_amt
									.getValue();
							if (!bankFessAdjustmentAmt.isEmpty()
									&& bankFessAdjustmentAmt.trim() != null) {
								setValuesToBankFeesAdjRec(bankFessAdjustmentAmt);
							}

							saveOrUpdate(record);
						};
					});
		}

	}

	private void setValuesToBankFeesAdjRec(String bankFessAdjustmentAmt) {
		if (customerComboTo != null
				&& customerComboTo.getSelectedValue() != null) {
			record.setPayeeName(customerComboTo.getSelectedValue().getName());
		} else if (vendorComboTo != null
				&& vendorComboTo.getSelectedValue() != null) {
			record.setPayeeName(vendorComboTo.getSelectedValue().getName());
		}
		record.setDescription(description.getValue());
		record.setBankFeesAdjustmentAcc(accountCombo.getSelectedValue().getID());
		if (getPreferences().isTrackTax()) {

			record.setTaxCode(taxRateCombo.getSelectedValue().getID());
		}
		record.setBankFeesAdjustmentAmt(Double.parseDouble(bankfees_amt
				.getValue()));
	}

	@Override
	protected boolean onCancel() {
		return true;
	}

	@Override
	public void onFailure(Throwable caught) {
	}

	@Override
	public void onSuccess(PaginationList<TransactionsList> result) {
		transactionsList = new ArrayList<TransactionsList>();
		total_grid.removeAllRecords();
		total_grid.removeLoadingImage();
		if (result == null) {
			total_grid.addEmptyMessage(messages.noRecordsToShow());
			return;
		}
		if (result != null && result.size() > 0) {
			transactionsList = result;
			total_grid.addLoadingImagePanel();
			total_grid.setRecords(result);
			total_grid.sort(10, false);
			Window.scrollTo(0, 0);

		}
	}

	/**
	 * to initialize the selected records grid , based on the records selected
	 * from total transactions list
	 */
	public void checkSelectedRecords() {
		selected_transactionsList = new ArrayList<TransactionsList>(
				transactionsList.size());
		if (transactionsList != null || transactionsList.size() > 0) {
			for (TransactionsList tr : transactionsList) {
				if (tr.isSelected()) {
					selected_transactionsList.add(tr);
				}
			}
		}
		selected_tr_grid.setRecords(selected_transactionsList);
		// for updating the balance label fields
		updateBalanceValues();
	}

	/**
	 * used to display bank fees details
	 */
	private void displayBankFeesDetails() {

		new DynamicForm("");
		DynamicForm leftDynamicForm = new DynamicForm("leftDynamicForm");

		if (record.getReceivedAmount() != 0) {
			customerComboTo = new CustomerCombo(messages.to());
			customerComboTo
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientCustomer>() {

						@Override
						public void selectedComboBoxItem(
								ClientCustomer selectItem) {
							customer = selectItem;
						}
					});
			leftDynamicForm.add(customerComboTo);
		} else {
			vendorComboTo = new VendorCombo(messages.to());
			vendorComboTo
					.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientVendor>() {

						@Override
						public void selectedComboBoxItem(ClientVendor selectItem) {
							vendor = selectItem;
						}
					});
			leftDynamicForm.add(vendorComboTo);
		}

		description = new TextItem(messages.description(), "description");
		accountCombo = new BankAccountCombo(messages.Account());
		List<ClientAccount> accounts = Accounter.getCompany().getAccounts();
		accountCombo.initCombo(accounts);

		accountCombo.initCombo(accounts);
		taxRateCombo = new TAXCodeCombo(messages.taxRate(), true);
		regionCombo = new SelectCombo("Region");

		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {
					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						accountCombo.setComboItem(selectItem);
						selected_account = selectItem;
					}
				});

		taxRateCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientTAXCode>() {
					@Override
					public void selectedComboBoxItem(ClientTAXCode selectItem) {
						selected_TAXCode = selectItem;
						taxRateCombo.setComboItem(selectItem);
					}
				});

		bankfees_amt.addBlurHandler(new BlurHandler() {
			@Override
			public void onBlur(BlurEvent event) {
				changeButtonName();

			}
		});

		StyledPanel horizontalPanel = new StyledPanel("horizontalPanel");
		leftDynamicForm.add(description, accountCombo);
		DynamicForm rightDynamicForm = new DynamicForm("rightDynamicForm");
		if (getPreferences().isTrackTax()) {
			rightDynamicForm.add(taxRateCombo, regionCombo, bankfees_amt);
		} else {
			rightDynamicForm.add(regionCombo, bankfees_amt);
		}

		horizontalPanel.add(leftDynamicForm);
		horizontalPanel.add(rightDynamicForm);

		hpanel.add(horizontalPanel);

	}

	private void setTransactionNumber() {

		rpcUtilService.getNextTransactionNumber(
				ClientTransaction.TYPE_JOURNAL_ENTRY,
				new AccounterAsyncCallback<String>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages
								.failedToGetTransactionNumber());
					}

					@Override
					public void onResultSuccess(String result) {
						if (result == null) {
							onFailure(new Exception());
						}
						nextJournalNumber = result;

					}
				});

	}

}
