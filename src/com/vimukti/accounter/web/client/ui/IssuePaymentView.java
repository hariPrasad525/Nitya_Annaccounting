package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ClickEvent;
import com.google.gwt.event.dom.client.ClickHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.ui.Label;
import com.vimukti.accounter.web.client.AccounterAsyncCallback;
import com.vimukti.accounter.web.client.core.ClientAccount;
import com.vimukti.accounter.web.client.core.ClientChequeLayout;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientIssuePayment;
import com.vimukti.accounter.web.client.core.ClientTransaction;
import com.vimukti.accounter.web.client.core.ClientTransactionIssuePayment;
import com.vimukti.accounter.web.client.core.IAccounterCore;
import com.vimukti.accounter.web.client.core.PrintCheque;
import com.vimukti.accounter.web.client.core.ValidationResult;
import com.vimukti.accounter.web.client.core.ValidationResult.Error;
import com.vimukti.accounter.web.client.core.Lists.IssuePaymentTransactionsList;
import com.vimukti.accounter.web.client.exception.AccounterException;
import com.vimukti.accounter.web.client.exception.AccounterExceptions;
import com.vimukti.accounter.web.client.ui.combo.AccountCombo;
import com.vimukti.accounter.web.client.ui.combo.BankAccountCombo;
import com.vimukti.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.vimukti.accounter.web.client.ui.core.BaseView;
import com.vimukti.accounter.web.client.ui.core.CancelButton;
import com.vimukti.accounter.web.client.ui.core.SaveAndCloseButton;
import com.vimukti.accounter.web.client.ui.forms.DynamicForm;
import com.vimukti.accounter.web.client.ui.forms.FormItem;
import com.vimukti.accounter.web.client.ui.forms.TextItem;
import com.vimukti.accounter.web.client.ui.grids.TransactionIssuePaymentGrid;

/**
 * @author Ravi Kiran.G
 * 
 */
public class IssuePaymentView extends BaseView<ClientIssuePayment> {

	private BankAccountCombo accountCombo;
	private List<String> payMethodItemList;

	private TransactionIssuePaymentGrid grid;
	private StyledPanel gridLayout;
	private Label totalLabel;
	private Label amountLabel;
	protected String selectedpaymentMethod;
	public Double totalAmount = 0D;
	private List<ClientAccount> payFromAccounts;
	private ClientAccount selectedPayFromAccount;
	private TextItem checkNoText;
	private StyledPanel mainVLay;
	private DynamicForm payForm;
	StyledPanel bottomLabelLayOut;
	private String checkNo;
	private String transactionNumber;
	public int validationCount;

	public IssuePaymentView(String text, String description) {
	}

	@Override
	public void init() {
		super.init();
		this.getElement().setId("IssuePaymentView");
		createControls();
		getPayFromAccounts();
		setTransactionNumber();
	}

	@Override
	public void initData() {
		super.initData();
	}

	private void setTransactionNumber() {

		rpcUtilService.getNextTransactionNumber(
				ClientTransaction.TYPE_ISSUE_PAYMENT,
				new AccounterAsyncCallback<String>() {

					public void onException(AccounterException caught) {
						// UIUtils.logError(
						// "Failed to get the Transaction Number..",
						// caught);
						// //UIUtils.logError(
						// "Failed to get the Transaction Number..",
						// caught);
						return;
					}

					public void onResultSuccess(String result) {
						if (result == null)
							onFailure(null);
						IssuePaymentView.this.transactionNumber = result;
					}

				});

	}

	// /**
	// * This method fills the grid with records while initialising the dialog,
	// * See FinanceTool.getChecks() for the record types displaying in this
	// * dialog
	// *
	// */
	// private void fillGrid() {
	//
	// rpcUtilService
	// .getChecks(new
	// AccounterAsyncCallback<ArrayList<IssuePaymentTransactionsList>>() {
	//
	// public void onException(AccounterException caught) {
	// // UIUtils
	// // .logError(
	// // "Failed to get the IssuePaymetTransactionsList",
	// // caught);
	//
	// }
	//
	// public void onSuccess(
	// List<IssuePaymentTransactionsList> result) {
	//
	// for (IssuePaymentTransactionsList entry : result)
	// addRecord(entry);
	//
	// }
	//
	// });
	//
	// }
	@Override
	protected void createButtons() {
		ImageButton printButton = new ImageButton(messages.print(), Accounter
				.getFinanceImages().Print1Icon(), "savelocal");
		printButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				printAndCreateIssuePayment();
			}
		});
		saveAndCloseButton = new SaveAndCloseButton(messages.issuePayment());
		saveAndCloseButton.addClickHandler(new ClickHandler() {

			@Override
			public void onClick(ClickEvent event) {
				cretaeOnlyIssuePayment();
			}
		});
		saveAndCloseButton.setView(this);
		if (!isInViewMode()) {
			addButton(saveAndCloseButton);
		}
		cancelButton = new CancelButton(this);
		addButton(printButton);
		addButton(cancelButton);
	}

	protected void cretaeOnlyIssuePayment() {
		for (Object errorSource : lastErrorSourcesFromValidation) {
			clearError(errorSource);
		}
		ValidationResult validationResult = validateView();
		lastErrorSourcesFromValidation.clear();
		if (validationResult.haveErrors()) {
			for (Error error : validationResult.getErrors()) {
				addError(error.getSource(), error.getMessage());
				lastErrorSourcesFromValidation.add(error.getSource());
			}
			return;
		}
		List<ClientTransactionIssuePayment> selectedRecords = grid
				.getSelectedRecords(0);
		List<PrintCheque> printCheques = new ArrayList<PrintCheque>();
		for (ClientTransactionIssuePayment payment : selectedRecords) {
			PrintCheque cheque = new PrintCheque();
			cheque.setPayeeName(payment.getDisplayName());
			cheque.setAmount(payment.getAmount());
			cheque.setDate(payment.getDate());
			cheque.setCurrency(getCompany().getCurrency(payment.getCurrency())
					.getSymbol());
			printCheques.add(cheque);
		}

		createIssuePayment();

	}

	protected void printAndCreateIssuePayment() {
		for (Object errorSource : lastErrorSourcesFromValidation) {
			clearError(errorSource);
		}
		ValidationResult validationResult = validateView();
		lastErrorSourcesFromValidation.clear();
		if (validationResult.haveErrors()) {
			for (Error error : validationResult.getErrors()) {
				addError(error.getSource(), error.getMessage());
				lastErrorSourcesFromValidation.add(error.getSource());
			}
			return;
		}
		List<ClientTransactionIssuePayment> selectedRecords = grid
				.getSelectedRecords(0);
		ArrayList<PrintCheque> printCheques = new ArrayList<PrintCheque>();
		for (ClientTransactionIssuePayment payment : selectedRecords) {
			PrintCheque cheque = new PrintCheque();
			cheque.setPayeeName(payment.getDisplayName());
			cheque.setAmount(payment.getAmount());
			cheque.setDate(payment.getDate());
			cheque.setCurrency(getCompany().getCurrency(payment.getCurrency())
					.getSymbol());
			printCheques.add(cheque);
		}
		ClientChequeLayout checkLayout = getCompany().getCheckLayout(
				selectedPayFromAccount.getID());
		if (checkLayout == null) {
			checkLayout = getCompany().getCheckLayout(0);
		}
		rpcUtilService.printCheques(checkLayout.getID(), printCheques,
				new AsyncCallback<String>() {

					@Override
					public void onSuccess(String result) {
						UIUtils.downloadFileFromTemp("printcheque", result);
						createIssuePayment();
					}

					@Override
					public void onFailure(Throwable caught) {
						int i = 0;
					}
				});
	}

	protected ValidationResult validateView() {
		ValidationResult result = FormItem.validate(accountCombo);
		if (grid.getAllRows().isEmpty()) {
			result.addError(grid,
					messages.noTransactionIsAvailableToIssuePayments());
		} else {
			if (grid.getSelectedRecords(0).size() == 0)
				result.addError(grid,
						messages.pleaseSelectAnyOneOfTheTransactions());
		}
		return result;
	}

	protected void addRecord(IssuePaymentTransactionsList entry) {

		ClientTransactionIssuePayment record = new ClientTransactionIssuePayment();

		setValuesToRecord(record, entry);
		// grid.addData(record);

	}

	private void setValuesToRecord(ClientTransactionIssuePayment record,
			IssuePaymentTransactionsList entry) {

		if (entry.getDate() != null)
			record.setDate(entry.getDate().getDate());
		if (entry.getNumber() != null)
			record.setNumber(entry.getNumber());
		record.setName(entry.getName() != null ? entry.getName() : "");
		record.setMemo(entry.getMemo() != null ? entry.getMemo() : "");
		if (entry.getAmount() != null)
			record.setAmount(entry.getAmount());
		if (entry.getPaymentMethod() != null)
			record.setPaymentMethod(entry.getPaymentMethod());
		record.setRecordType(entry.getType());
		record.setCurrency(entry.getCurrency());
		switch (entry.getType()) {
		case ClientTransaction.TYPE_WRITE_CHECK:
			record.setWriteCheck(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_WRITE_CHECK);
			break;
		case ClientTransaction.TYPE_CASH_PURCHASE:
		case ClientTransaction.TYPE_CASH_EXPENSE:
		case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
			record.setCashPurchase(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_CASH_PURCHASE);
			break;
		case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
			record.setCustomerRefund(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
			break;
		case ClientTransaction.TYPE_PAY_TAX:
			record.setPaySalesTax(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_PAY_TAX);
			break;
		case ClientTransaction.TYPE_PAY_BILL:
			record.setPayBill(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_PAY_BILL);
			break;
		case ClientTransaction.TYPE_VENDOR_PAYMENT:
			record.setPayBill(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_VENDOR_PAYMENT);
			break;
		case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
		case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
			record.setCreditCardCharge(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
			break;
		case ClientTransaction.TYPE_RECEIVE_TAX:
			record.setReceiveVAT(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_RECEIVE_TAX);
			break;
		case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
			record.setCustomerPrepayment(entry.getTransactionId());
			record.setRecordType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
			break;

		}

		// record.setID(entry.getTransactionId());

	}

	private void getPayFromAccounts() {
		payFromAccounts = new ArrayList<ClientAccount>();
		payFromAccounts = accountCombo.getAccounts();
		accountCombo.initCombo(payFromAccounts);
		accountCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.PAY_FROM_COMBO));
		selectedPayFromAccount = accountCombo.getSelectedValue();
	}

	private void createControls() {
		// setWidth("100%");
		Label titleLabel = new Label(messages.printCheque());
		titleLabel.setStyleName("label-title");

		payMethodItemList = new ArrayList<String>();
		payMethodItemList.add(UIUtils
				.getpaymentMethodCheckBy_CompanyType(messages.check()));

		checkNoText = new TextItem(messages.startingChequeNo(), "checkNoText");
		checkNoText.setWidth(100);

		accountCombo = new BankAccountCombo(messages.Account());
		accountCombo.setRequired(true);
		accountCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedPayFromAccount = selectItem;
						changeGridData(selectedPayFromAccount);
						if (selectedPayFromAccount != null)
							setStartingCheckNumber(selectedPayFromAccount);

					}

				});

		payForm = new DynamicForm("payForm");
		payForm.add(accountCombo, checkNoText);

		Label label = new Label();
		label.setText(messages.paymentsToBeIssued());
		initListGrid();

		mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(titleLabel);
		mainVLay.add(payForm);
		mainVLay.add(label);
		mainVLay.add(gridLayout);

		this.add(mainVLay);

	}

	protected void setStartingCheckNumber(ClientAccount account) {

		if (checkNoText != null) {
			rpcUtilService.getNextIssuepaymentCheckNumber(account.getID(),
					new AccounterAsyncCallback<String>() {

						public void onException(AccounterException caught) {
							// UIUtils.logError(
							// "Failed to get the Check number..", caught);
							// //UIUtils.logError(
							// "Failed to get the Check number..", caught);

						}

						public void onResultSuccess(String result) {

							if (result == null) {
								onFailure(null);
								return;
							}
							setCheckNo(result);
							checkNoText.setValue(result);
						}

					});

		}

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = payForm.validate();
		if (grid.getAllRows().isEmpty()) {
			result.addError(grid,
					messages.noTransactionIsAvailableToIssuePayments());
		} else {
			if (grid.getSelectedRecords(0).size() == 0)
				result.addError(grid,
						messages.pleaseSelectAnyOneOfTheTransactions());
		}
		// result.add(grid.validateGrid());
		return result;
	}

	private boolean validateCheckNo() {
		boolean valid = true;
		try {
			String number = checkNoText.getValue().toString();
			setCheckNo(number);
		} catch (NumberFormatException e) {
			valid = false;
			Accounter.showError(messages.invalidChequeNumber());
		}

		return valid;
	}

	protected void createIssuePayment() {

		ClientIssuePayment issuePayment = getIssuePaymentObject();
		Accounter.doCreateIssuePaymentEffect(this, issuePayment);
		// Accounter.showError(AccounterErrorType.FAILEDREQUEST);

		// public void onSuccess(String result) {
		// if (result == null) {p
		// onFailure(null);
		// return;
		// }
		// Accounter.showInformation(FinanceApplication
		// .constants().issuePaymentWith()
		// + transactionNumber
		// + FinanceApplication.constants()
		// .createdSuccessfully());
		// IssuePaymentDialog.this.removeFromParent();
		// }
		//
		// });

	}

	@Override
	public void saveSuccess(IAccounterCore object) {
		// Accounter.showInformation(FinanceApplication.constants()
		// .issuePaymentWith()
		// + transactionNumber
		// + FinanceApplication.constants()
		// .createdSuccessfully());
		IssuePaymentView.this.removeFromParent();
		super.saveSuccess(object);
		// ActionFactory.getExpensesAction(null).run(null, true);
	}

	public void saveFailed(AccounterException exception) {
		// Accounter.showError(AccounterErrorType.FAILEDREQUEST);
		super.saveFailed(exception);
		AccounterException accounterException = (AccounterException) exception;
		int errorCode = accounterException.getErrorCode();
		String errorString = AccounterExceptions.getErrorString(errorCode);
		Accounter.showError(errorString);
	}

	private ClientIssuePayment getIssuePaymentObject() {

		ClientIssuePayment issuePayment = new ClientIssuePayment();

		issuePayment.setType(ClientTransaction.TYPE_ISSUE_PAYMENT);

		issuePayment.setNumber(transactionNumber);

		issuePayment.setDate(new ClientFinanceDate().getDate());

		issuePayment.setPaymentMethod(selectedpaymentMethod);

		issuePayment.setAccount(selectedPayFromAccount.getID());

		issuePayment.setTotal(totalAmount);
		String chkNo;
		if (checkNoText != null) {
			chkNo = (checkNoText.getValue().toString().isEmpty()) ? "0"
					: (checkNoText.getValue().toString());
			issuePayment.setCheckNumber(chkNo);
		}
		issuePayment.setTransactionIssuePayment(grid.getSelectedRecords(0));

		return issuePayment;
	}

	/*
	 * This method fills the grid with the records which has this account
	 * selected while creating them.
	 */
	protected void changeGridData(ClientAccount selectedPayFromAccount2) {
		if (selectedPayFromAccount2 != null) {
			// grid.removeAllRecords();
			// grid.addLoadingImagePanel();
			rpcUtilService
					.getChecks(
							selectedPayFromAccount2.getID(),
							new AccounterAsyncCallback<ArrayList<IssuePaymentTransactionsList>>() {

								public void onException(AccounterException t) {

									// UIUtils
									// .logError(
									// "Failed to get the IssuePaymentTransactionsList..",
									// t);
									grid.addEmptyMessage(messages
											.noRecordsToShow());

								}

								public void onResultSuccess(
										ArrayList<IssuePaymentTransactionsList> result) {

									if (result == null) {
										onFailure(null);
										return;
									}
									if (result.size() > 0) {
										grid.clear();
										addTransactiontoGrid(result);
									} else
										grid.addEmptyMessage(messages
												.noRecordsToShow());

								}

							});
		}

	}

	protected void removeGridData() {
		grid.clear();
		// totalAmount = 0D;
	}

	private void addTransactiontoGrid(
			ArrayList<IssuePaymentTransactionsList> result) {
		List<ClientTransactionIssuePayment> records = new ArrayList<ClientTransactionIssuePayment>();
		for (IssuePaymentTransactionsList entry : result) {
			ClientTransactionIssuePayment record = new ClientTransactionIssuePayment();

			if (entry.getDate() != null)
				record.setDate(entry.getDate().getDate());
			if (entry.getNumber() != null)
				record.setNumber(entry.getNumber());
			record.setName(entry.getName() != null ? entry.getName() : "");
			record.setMemo(entry.getMemo() != null ? entry.getMemo() : "");
			if (entry.getAmount() != null)
				record.setAmount(entry.getAmount());
			if (entry.getPaymentMethod() != null)
				record.setPaymentMethod(entry.getPaymentMethod());
			record.setRecordType(entry.getType());
			record.setCurrency(entry.getCurrency());
			switch (entry.getType()) {
			case ClientTransaction.TYPE_WRITE_CHECK:
				record.setWriteCheck(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_WRITE_CHECK);
				break;
			case ClientTransaction.TYPE_CASH_PURCHASE:
			case ClientTransaction.TYPE_CASH_EXPENSE:
			case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
				record.setCashPurchase(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CASH_PURCHASE);
				break;
			case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
				record.setCustomerRefund(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
				break;
			case ClientTransaction.TYPE_PAY_TAX:
				record.setPaySalesTax(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_PAY_TAX);
				break;
			case ClientTransaction.TYPE_PAY_BILL:
				record.setPayBill(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_PAY_BILL);
				break;
			case ClientTransaction.TYPE_VENDOR_PAYMENT:
				record.setPayBill(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_VENDOR_PAYMENT);
				break;
			case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
				record.setCreditCardCharge(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
				break;
			case ClientTransaction.TYPE_RECEIVE_TAX:
				record.setReceiveVAT(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_RECEIVE_TAX);
				break;
			case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
				record.setCustomerPrepayment(entry.getTransactionId());
				record.setRecordType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
				break;
			}
			// record.setID(entry.getTransactionId());
			records.add(record);
		}
		grid.setAllRows(records);
	}

	private void initListGrid() {
		gridLayout = new StyledPanel("gridLayout");
		grid = new TransactionIssuePaymentGrid(null);

		// grid.isEnable = false;
		// grid.init();
		// grid.setIssuePaymentView(this);
		// grid.addFooterValues("", "", "", "", FinanceApplication
		// .constants().total(), DataUtils
		// .getAmountAsString(0.00));

		// bottomLabelLayOut = new StyledPanel();
		// bottomLabelLayOut.setWidth("100%");
		// bottomLabelLayOut.setHeight("100px");
		totalLabel = new Label();
		totalLabel.setText(messages.totalAmount());

		amountLabel = new Label();
		amountLabel.setText("" + UIUtils.getCurrencySymbol() + "0");
		Label tableTitle = new Label(messages2.table(messages.issuePayment()));
		tableTitle.addStyleName("editTableTitle");
		gridLayout.add(tableTitle);
		gridLayout.add(grid);
		// gridLayout.add(bottomLabelLayOut);

	}

	private List<ClientTransactionIssuePayment> getTransactionIssuePayments(
			ClientIssuePayment issuePayment) {
		List<ClientTransactionIssuePayment> transactionIssuePaymentsList = new ArrayList<ClientTransactionIssuePayment>();

		ClientTransactionIssuePayment entry;

		for (ClientTransactionIssuePayment record : grid.getSelectedRecords(0)) {
			entry = new ClientTransactionIssuePayment();
			if (record.getDate() != 0)
				entry.setDate(record.getDate());
			if (record.getNumber() != null)
				entry.setNumber(record.getNumber());

			if (record.getName() != null)
				entry.setName(record.getName());

			entry.setAmount(record.getAmount());
			entry.setMemo(record.getMemo());

			if (record.getPaymentMethod() != null) {
				entry.setPaymentMethod(record.getPaymentMethod());
			}
			entry.setCurrency(record.getCurrency());
			switch (record.getRecordType()) {
			case ClientTransaction.TYPE_WRITE_CHECK:
				entry.setWriteCheck(record.getWriteCheck());
				entry.setRecordType(ClientTransaction.TYPE_WRITE_CHECK);
				break;
			case ClientTransaction.TYPE_CASH_PURCHASE:
			case ClientTransaction.TYPE_CASH_EXPENSE:
			case ClientTransaction.TYPE_EMPLOYEE_EXPENSE:
				entry.setCashPurchase(record.getCashPurchase());
				entry.setRecordType(ClientTransaction.TYPE_CASH_PURCHASE);
				break;
			case ClientTransaction.TYPE_CUSTOMER_REFUNDS:
				entry.setCustomerRefund(record.getCustomerRefund());
				entry.setRecordType(ClientTransaction.TYPE_CUSTOMER_REFUNDS);
				break;
			case ClientTransaction.TYPE_PAY_TAX:
				entry.setPaySalesTax(record.getPaySalesTax());
				entry.setRecordType(ClientTransaction.TYPE_PAY_TAX);
				break;
			case ClientTransaction.TYPE_PAY_BILL:
				entry.setPayBill(record.getPayBill());
				entry.setRecordType(ClientTransaction.TYPE_PAY_BILL);
				break;
			case ClientTransaction.TYPE_VENDOR_PAYMENT:
				entry.setPayBill(record.getPayBill());
				entry.setRecordType(ClientTransaction.TYPE_VENDOR_PAYMENT);
				break;
			case ClientTransaction.TYPE_CREDIT_CARD_CHARGE:
			case ClientTransaction.TYPE_CREDIT_CARD_EXPENSE:
				entry.setCreditCardCharge(record.getCreditCardCharge());
				entry.setRecordType(ClientTransaction.TYPE_CREDIT_CARD_CHARGE);
				break;
			case ClientTransaction.TYPE_RECEIVE_TAX:
				entry.setReceiveVAT(record.getReceiveVAT());
				entry.setRecordType(ClientTransaction.TYPE_RECEIVE_TAX);
				break;
			case ClientTransaction.TYPE_CUSTOMER_PREPAYMENT:
				entry.setCustomerPrepayment(record.getCustomerPrepayment());
				entry.setRecordType(ClientTransaction.TYPE_CUSTOMER_PREPAYMENT);
				break;
			}
			entry.setTransaction(issuePayment);
			transactionIssuePaymentsList.add(entry);

		}
		return transactionIssuePaymentsList;

	}

	public void setCheckNo(String checkNo) {
		this.checkNo = checkNo;
	}

	public String getCheckNo() {
		return checkNo;
	}

	@Override
	public ClientIssuePayment saveView() {
		ClientIssuePayment saveView = super.saveView();
		if (saveView != null) {
			createIssuePayment();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		createIssuePayment();
	}

	@Override
	public void setFocus() {
		accountCombo.setFocus();

	}

	@Override
	public String getViewTitle() {
		return messages.printCheque();
	}

	@Override
	public void deleteFailed(AccounterException caught) {
		// TODO Auto-generated method stub

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {
		// TODO Auto-generated method stub

	}

	@Override
	public List<DynamicForm> getForms() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	protected boolean canDelete() {
		return false;
	}
}
