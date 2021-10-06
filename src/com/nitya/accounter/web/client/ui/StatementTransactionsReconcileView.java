package com.nitya.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

import com.google.gwt.user.client.ui.DecoratedTabPanel;
import com.google.gwt.user.client.ui.HTML;
import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientReconciliation;
import com.nitya.accounter.web.client.core.ClientReconciliationItem;
import com.nitya.accounter.web.client.core.ClientStatement;
import com.nitya.accounter.web.client.core.ClientStatementRecord;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.PaginationList;
import com.nitya.accounter.web.client.core.Utility;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.core.reports.AccountRegister;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.externalization.AccounterMessages;
import com.nitya.accounter.web.client.ui.core.BaseView;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.forms.AmountLabel;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.grids.AccountRegisterOtherListGrid;
import com.nitya.accounter.web.client.ui.grids.ReconcilListGrid;

public class StatementTransactionsReconcileView extends
		BaseView<ClientReconciliation> {

	private AccountRegisterOtherListGrid grid;
	protected List<AccountRegister> accountRegister;
	// private BankStatementGrid bankStatementGrid;
	private DecoratedTabPanel tabSet;
	private AmountLabel closebalanceLable, clearedbalance, difference;
	private ClientAccount account;
	private AccounterMessages messages;
	private StyledPanel Vpanel, main_Vpanel;
	// private double totalAmount = 0.0;
	private ReconcilListGrid reconcilListGrid;
	protected ClientStatement statement;
	// private PaginationList<TransactionsList> allRecords;
	double spentTotal = 0.0;
	double receivedTotal = 0.0;

	public StatementTransactionsReconcileView(long accountid,
			ClientStatement statement) {
		super();
		this.getElement().setId("StatementTransactionsReconcileView");
		this.account = getCompany().getAccount(accountid);
		this.statement = statement;

	}

	@Override
	public void init() {
		super.init();
		this.messages = Global.get().messages();
		setMode(EditMode.CREATE);
		saveAndCloseButton.setText("Publish");
		saveAndCloseButton.setVisible(false);
		saveAndNewButton.setVisible(false);
		createControls();
//		setSize("100%", "100%");
	}

	/**
 * 
 */
	private void createControls() {
		Vpanel = new StyledPanel("Vpanel");
		showBankDetails();
		tabSet = new DecoratedTabPanel();
		new ArrayList<DynamicForm>();
		tabSet.add(getReconcileTab(), "Reconcile");
		tabSet.add(getBankStatementsTab(), "Bank transactions");
		tabSet.selectTab(0);
//		tabSet.setSize("100%", "100%");
		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(tabSet);
		Vpanel.add(mainVLay);
		this.add(Vpanel);
		// setSize("100%", "100%");
	}

	/**
	 * this method is used to show the bank details and statementDetails
	 */
	private void showBankDetails() {
		StyledPanel hPanel = new StyledPanel("hPanel");

		ClientAccount clientAccount = Accounter.getCompany().getAccount(
				statement.getAccount());
		// for bank name
		StyledPanel invoicePanel = new StyledPanel("invoicePanel");
		HTML bankAccountlabel = new HTML("<p>" + messages.bankAccount()
				+ "<br/><br/></p>" + clientAccount.getDisplayName() + "</p>");
		invoicePanel.add(bankAccountlabel);
		invoicePanel.setStyleName("rightBorder");

		// for matched records total balance
		StyledPanel accBalancePanel = new StyledPanel("accBalancePanel");
		HTML accBalancelabel = new HTML("<p>" + "Balance " + "<br/><br/></p>"
				+ getBalanceAmount(true) + "</p>");
		accBalancePanel.add(accBalancelabel);
		accBalancePanel.setStyleName("rightBorder");

		// for no of reconcile records
		StyledPanel reconcilePanel = new StyledPanel("reconcilePanel");
		HTML reconcile = new HTML("<p>" + messages.Reconcile()
				+ "<br/><br/></p>" + statement.getStatementList().size()
				+ messages.itemsToBeReconciled() + "</p>");
		reconcilePanel.add(reconcile);
		reconcilePanel.setStyleName("rightBorder");

		// for statement balance
		StyledPanel balancePanel = new StyledPanel("balancePanel");
		HTML balance = new HTML("<p>" + messages.statementBalance()
				+ "<br/><br/></p>" + getBalanceAmount(false) + "</p>");
		balancePanel.add(balance);
		balancePanel.setStyleName("rightBorder");

		// for statement imported date
		StyledPanel importedDatePanel = new StyledPanel("importedDatePanel");
		HTML importedDate = new HTML("<p>" + messages.statementsImportedDate()
				+ "<br/><br/></p>" + statement.getImporttedDate() + "</p>");
		importedDatePanel.add(importedDate);
		importedDatePanel.setStyleName("rightBorder");

		// for starting balance
		StyledPanel startingBalancePanel = new StyledPanel(
				"startingBalancePanel");
		HTML startBalance = new HTML("<p>" + messages.startingBalance()
				+ "<br/><br/></p>" + getStartingBalance() + "</p>");
		startingBalancePanel.add(startBalance);
		startingBalancePanel.setStyleName("rightBorder");

		// for closing balance
		StyledPanel closingBalancePanel = new StyledPanel("closingBalancePanel");
		HTML closeBalance = new HTML("<p>" + messages.ClosingBalance()
				+ "<br/><br/></p>" + statement.getClosingBalance() + "</p>");
		closingBalancePanel.add(closeBalance);

		hPanel.add(invoicePanel);
		hPanel.add(accBalancePanel);
		hPanel.add(reconcilePanel);
		hPanel.add(balancePanel);
		hPanel.add(importedDatePanel);
		hPanel.add(startingBalancePanel);
		hPanel.add(closingBalancePanel);

		Vpanel.add(hPanel);

	}

	private double getBalanceAmount(boolean forMatched) {
		// for displaying balance
		double balanceAmt = 0;
		List<ClientStatementRecord> statementList = statement
				.getStatementList();
		for (ClientStatementRecord stRecord : statementList) {
			if (forMatched) {
				if (stRecord.isMatched()) {
					if (stRecord.getSpentAmount() > 0) {
						balanceAmt += stRecord.getSpentAmount();
					} else {
						balanceAmt += stRecord.getReceivedAmount();
					}
				}
			} else {
				if (stRecord.getSpentAmount() > 0) {
					balanceAmt += stRecord.getSpentAmount();
				} else {
					balanceAmt += stRecord.getReceivedAmount();
				}
			}

		}

		return balanceAmt;
	}

	/**
	 * used to get the starting balance
	 * 
	 * @return
	 */
	private double getStartingBalance() {
		ClientStatementRecord stRec = statement.getStatementList().get(0);
		double startBalance = 0;
		if (stRec.getSpentAmount() > 0) {
			startBalance = stRec.getClosingBalance() + stRec.getSpentAmount();
		} else {
			startBalance = stRec.getClosingBalance()
					- stRec.getReceivedAmount();
		}
		startBalance = Utility.roundTo2Digits(startBalance);
		return startBalance;
	}

	/**
	 * 
	 * @return
	 */
	private StyledPanel getBankStatementsTab() {

		StyledPanel mainPanel = new StyledPanel("mainPanel");

		// transactions grid
		// this.bankStatementGrid = new BankStatementGrid();
		// bankStatementGrid.init();
		// bankStatementGrid.setWidth("100%");
		// bankStatementGrid.addLoadingImagePanel();
		// bankStatementGrid.setRecords(statement.getStatementList());

		grid = new AccountRegisterOtherListGrid(false);
		// grid.addStyleName("listgrid-tl");
		grid.init();
		// grid.setView(this);
		accountSelected(account);
		mainPanel.add(grid);

		// DynamicForm amountsForm = new DynamicForm();
		// // AmountLables
		// closebalanceLable = new AmountLabel(messages.ClosingBalance());
		// closebalanceLable.setHelpInformation(true);
		// closebalanceLable.setDisabled(true);
		//
		// clearedbalance = new AmountLabel(messages.ClearedAmount());
		// clearedbalance.setHelpInformation(true);
		// clearedbalance.setDisabled(true);
		//
		// difference = new AmountLabel(messages.Difference());
		// difference.setHelpInformation(true);
		// difference.setDisabled(true);
		// setdataToAmountLables();
		// amountsForm.setItems(closebalanceLable, clearedbalance, difference);
		// StyledPanel amountsPanel = new StyledPanel();
		// amountsPanel.setWidth("100%");
		// amountsPanel.add(amountsForm);
		//
		// amountsPanel.setStyleName("bottom_total_view");
		// amountsPanel.setCellHorizontalAlignment(amountsForm,
		// HasHorizontalAlignment.ALIGN_RIGHT);

		// mainPanel.add(bankStatementGrid);
		// mainPanel.add(amountsPanel);

		return mainPanel;
	}

	/**
	 * Set amount to non edibles labels
	 */
	private void setdataToAmountLables() {
		double clearedBalance = 0.0;
		double differenceBalance = 0.0;
		differenceBalance = statement.getClosingBalance()
				- statement.getStartBalance();
		for (ClientStatementRecord statementRecord : statement
				.getStatementList()) {
			if (statementRecord.isMatched()) {
				if (statementRecord.getSpentAmount() > 0) {
					clearedBalance += statementRecord.getSpentAmount();
				} else {
					clearedBalance += statementRecord.getReceivedAmount();
				}
			}
		}
		closebalanceLable.setAmount(statement.getClosingBalance());
		clearedbalance.setAmount(clearedBalance);
		difference.setAmount(differenceBalance - clearedBalance);
	}

	/**
	 * 
	 * @return
	 */
	private StyledPanel getReconcileTab() {

		StyledPanel panel = new StyledPanel("panel");
		main_Vpanel = new StyledPanel("main_Vpanel");
		reconcilListGrid = new ReconcilListGrid(account);
		reconcilListGrid.init();
		reconcilListGrid.setCanEdit(true);
		reconcilListGrid.addLoadingImagePanel();
		panel.add(reconcilListGrid);

		int count = 0;
		PaginationList<ClientStatementRecord> list = new PaginationList<ClientStatementRecord>();
		List<ClientStatementRecord> stList = statement.getStatementList();
		Iterator iterator = stList.iterator();
		while ((iterator).hasNext()) {
			ClientStatementRecord obj = (ClientStatementRecord) iterator.next();
			list.add(obj);
			if (obj.isMatched()) {
				count++;
			}
		}
		if (stList.size() == count) {
			saveAndCloseButton.setVisible(true);
		}
		reconcilListGrid.setRecords(list);

		// for displaying the closingBalance, balanceDifference,clearedAmount
		DynamicForm amountsForm = new DynamicForm("amountsForm");
		// AmountLables
		closebalanceLable = new AmountLabel(messages.ClosingBalance());
		closebalanceLable.setEnabled(false);

		clearedbalance = new AmountLabel(messages.ClearedAmount());
		// clearedbalance.setHelpInformation(true);
		clearedbalance.setEnabled(false);

		difference = new AmountLabel(messages.Difference());
		// difference.setHelpInformation(true);
		difference.setEnabled(false);

		setdataToAmountLables();
		amountsForm.add(closebalanceLable, clearedbalance, difference);
		StyledPanel amountsPanel = new StyledPanel("amountsPanel");
		amountsPanel.add(amountsForm);

		amountsPanel.addStyleName("bottom_total_view");

		main_Vpanel.add(amountsPanel);

		panel.add(main_Vpanel);
		return panel;
	}

	/**
	 * Disable the delete button
	 */
	@Override
	protected boolean canDelete() {
		return false;
	}

	/**
	 * Disable the edit button
	 */
	@Override
	public boolean canEdit() {
		return false;
	}

	@Override
	public void initData() {
		if (data == null) {
			setData(new ClientReconciliation());
		}
	}

	@Override
	public void saveFailed(AccounterException exception) {
		super.saveFailed(exception);
		AccounterException accounterException = exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	@Override
	public void saveSuccess(IAccounterCore result) {
		if (result != null) {
			super.saveSuccess(result);
		} else {
			saveFailed(new AccounterException());
		}
	}

	@Override
	public ClientReconciliation saveView() {
		ClientReconciliation saveview = super.saveView();
		if (saveview != null) {
			updateObject();
		}
		return super.saveView();
	}

	@Override
	public void saveAndUpdateView() {
		updateObject();
		saveOrUpdate(getData());
	}

	/**
 * 
 */
	private void updateObject() {

		data.setAccount(account);
		List<ClientStatementRecord> statementList = statement
				.getStatementList();
		Set<ClientReconciliationItem> reconciliationItems = new HashSet<ClientReconciliationItem>();
		for (ClientStatementRecord stRecord : statementList) {
			if (stRecord.isMatched()) {
				List<ClientTransaction> transactionsLists = stRecord
						.getTransactionsLists();
				if (!transactionsLists.isEmpty()) {
					for (ClientTransaction transaction : transactionsLists) {
						ClientReconciliationItem reconciliationItem = new ClientReconciliationItem();
						reconciliationItem.setTransaction(transaction.getID());
						reconciliationItem
								.setTransactionDate(new ClientFinanceDate(
										transaction.getTransactionDate()));
						reconciliationItem.setTransactionNo(transaction
								.getNumber());
						reconciliationItem.setAmount(transaction.getTotal());
						reconciliationItem.setTransationType(transaction
								.getType());
						reconciliationItems.add(reconciliationItem);
					}
				}
			}
		}
		data.setItems(reconciliationItems);
		data.setReconcilationDate(new ClientFinanceDate());
		data.setStartDate(statement.getStartDate());
		data.setEndDate(statement.getEndDate());
		data.setClosingBalance(statement.getClosingBalance());
		data.setOpeningBalance(statement.getStartBalance());
		data.setStatement(statement.getID());

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = super.validate();
		// if statementrecords are not reconciled, show error message
		if (!validateStaementRecords()) {
			result.addError(this.data, "Statement Records are not matched");
		}
		return result;
	}

	@Override
	public void deleteFailed(AccounterException caught) {
	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	protected String getViewTitle() {
		return null;
	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void setFocus() {

	}

	protected void accountSelected(final ClientAccount takenaccount) {

		// if (takenaccount == null) {
		// accountRegister = null;
		// return;
		// }

		this.account = takenaccount;
		grid.setAccount(takenaccount);
		// Accounter.createReportService().getAccountRegister(null, null,0,-1,
		// takenaccount.getID(),
		// new AccounterAsyncCallback<ArrayList<AccountRegister>>() {
		//
		// @Override
		// public void onException(AccounterException caught) {
		// Accounter.showError(messages
		// .failedtoGetListofAccounts(takenaccount
		// .getName()));
		//
		// }
		//
		// @Override
		// public void onResultSuccess(
		// ArrayList<AccountRegister> result) {
		// accountRegister = result;
		// getAccountRegisterGrid(result);
		//
		// }
		//
		// });
		grid.removeAllRecords();
		grid.addLoadingImagePanel();

	}

	public void getAccountRegisterGrid(List<AccountRegister> result) {

		grid.removeAllRecords();
		grid.removeLoadingImage();
		grid.balance = 0.0;
		grid.totalBalance = 0.0;
		if (accountRegister != null) {
			for (int i = 0; i < accountRegister.size(); i++) {
				AccountRegister accRegister = this.accountRegister.get(i);
				grid.addData(accRegister);
				// this.total += accRegister.getBalance();
			}
			if (accountRegister.size() == 0) {
				grid.addEmptyMessage(messages.noRecordsToShow());
			}
		} else {
			grid.addEmptyMessage(messages.noRecordsToShow());
		}
		// grid.updateFooterValues(FinanceApplication.constants()
		// .endingbalance(), 6);
		// grid.addFooterValue(DataUtils.getAmountAsString(takenaccount
		// .getCurrentBalance()), 7);
		// this.total = 0;
	}

	private boolean validateStaementRecords() {
		boolean isReconcilable = false;
		List<ClientStatementRecord> statementList = statement
				.getStatementList();
		for (ClientStatementRecord record : statementList) {
			// if statementRecord is matched, then set true
			if (record.isMatched()) {
				isReconcilable = true;
			} else {
				// if statementRecord is not matched, return false
				return false;
			}
		}
		return isReconcilable;
	}
}
