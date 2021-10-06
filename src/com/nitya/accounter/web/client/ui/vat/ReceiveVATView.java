package com.nitya.accounter.web.client.ui.vat;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.event.dom.client.ChangeEvent;
import com.google.gwt.event.dom.client.ChangeHandler;
import com.google.gwt.event.logical.shared.ValueChangeEvent;
import com.google.gwt.event.logical.shared.ValueChangeHandler;
import com.google.gwt.user.client.rpc.AsyncCallback;
import com.google.gwt.user.client.rpc.InvocationException;
import com.google.gwt.user.client.ui.Label;
import com.nitya.accounter.web.client.AccounterAsyncCallback;
import com.nitya.accounter.web.client.core.AccounterCoreType;
import com.nitya.accounter.web.client.core.ClientAccount;
import com.nitya.accounter.web.client.core.ClientAccounterClass;
import com.nitya.accounter.web.client.core.ClientCurrency;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientReceiveVAT;
import com.nitya.accounter.web.client.core.ClientReceiveVATEntries;
import com.nitya.accounter.web.client.core.ClientTAXAgency;
import com.nitya.accounter.web.client.core.ClientTAXReturn;
import com.nitya.accounter.web.client.core.ClientTransaction;
import com.nitya.accounter.web.client.core.ClientTransactionReceiveVAT;
import com.nitya.accounter.web.client.core.IAccounterCore;
import com.nitya.accounter.web.client.core.ValidationResult;
import com.nitya.accounter.web.client.exception.AccounterException;
import com.nitya.accounter.web.client.exception.AccounterExceptions;
import com.nitya.accounter.web.client.ui.Accounter;
import com.nitya.accounter.web.client.ui.DataUtils;
import com.nitya.accounter.web.client.ui.StyledPanel;
import com.nitya.accounter.web.client.ui.UIUtils;
import com.nitya.accounter.web.client.ui.Accounter.AccounterType;
import com.nitya.accounter.web.client.ui.combo.AccountCombo;
import com.nitya.accounter.web.client.ui.combo.DepositInAccountCombo;
import com.nitya.accounter.web.client.ui.combo.IAccounterComboSelectionChangeHandler;
import com.nitya.accounter.web.client.ui.combo.TAXAgencyCombo;
import com.nitya.accounter.web.client.ui.core.AbstractTransactionBaseView;
import com.nitya.accounter.web.client.ui.core.AccounterValidator;
import com.nitya.accounter.web.client.ui.core.AmountField;
import com.nitya.accounter.web.client.ui.core.DateField;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;
import com.nitya.accounter.web.client.ui.core.EditMode;
import com.nitya.accounter.web.client.ui.core.ErrorDialogHandler;
import com.nitya.accounter.web.client.ui.forms.CheckboxItem;
import com.nitya.accounter.web.client.ui.forms.DynamicForm;
import com.nitya.accounter.web.client.ui.forms.TextItem;
import com.nitya.accounter.web.client.ui.grids.TransactionReceiveVATGrid;
import com.nitya.accounter.web.client.ui.widgets.DateValueChangeHandler;

/**
 * 
 * @author Tirupathi
 * 
 */
public class ReceiveVATView extends
		AbstractTransactionBaseView<ClientReceiveVAT> {

	private ArrayList<DynamicForm> listforms;
	private DepositInAccountCombo depositInAccCombo;
	private DateField billsDue;
	private TAXAgencyCombo vatAgencyCombo;
	private DynamicForm mainform;
	private AmountField amountText;
	private AmountField endingBalanceText;
	private DynamicForm balForm;
	protected ClientAccount selectedDepositInAccount;
	protected double initialEndingBalance;
	protected ClientTAXAgency selectedVATAgency;
	private StyledPanel gridLayout;
	private TransactionReceiveVATGrid grid;
	private Double totalAmount = 0.0D;
	private String transactionNumber;
	protected List<ClientReceiveVATEntries> entries;
	private ClientTAXAgency selectedTaxAgency;
	private double endingBalance;
	private ArrayList<ClientReceiveVATEntries> filterList;
	private ArrayList<ClientReceiveVATEntries> tempList;
	private ClientFinanceDate dueDateOnOrBefore;

	private TextItem transNumber;
	private TextItem checkNoText;
	private CheckboxItem printCheck;
	private boolean isChecked = false;

	public ReceiveVATView() {
		super(ClientTransaction.TYPE_PAY_TAX);
		this.getElement().setId("receivetaxview");
	}

	@Override
	protected void createControls() {
		listforms = new ArrayList<DynamicForm>();
		currencyWidget = createCurrencyFactorWidget();

		// setTitle(UIUtils.title(FinanceApplication.constants()
		// .payVAT()));

		Label lab = new Label(messages.tAXRefund());
		lab.setStyleName("label-title");
		// lab.setHeight("35px");
		// date = new DateField(companyConstants.date());
		// date.setHelpInformation(true);
		// date.setTitle(companyConstants.date());
		// date.setEnteredDate(new ClientFinanceDate());
		// date.setDisabled(isEdit);
		transactionDateItem = createTransactionDateItem();

		transNumber = createTransactionNumberItem();
		transNumber.setTitle(messages.no());
		transNumber.setToolTip(messages
				.giveNoTo(this.getAction().getViewName()));

		depositInAccCombo = new DepositInAccountCombo(messages.depositIn());
		depositInAccCombo.setAccountTypes(UIUtils
				.getOptionsByType(AccountCombo.DEPOSIT_IN_ACCOUNT));
		depositInAccCombo.setRequired(true);
		depositInAccCombo
				.addSelectionChangeHandler(new IAccounterComboSelectionChangeHandler<ClientAccount>() {

					@Override
					public void selectedComboBoxItem(ClientAccount selectItem) {
						selectedDepositInAccount = selectItem;
						selectedAccount(selectItem);
						ClientCurrency currency = getCompany().getCurrency(
								selectItem.getCurrency());
						amountText.setCurrency(currency);
						endingBalanceText.setCurrency(currency);
						// initialEndingBalance = selectedPayFromAccount
						// .getTotalBalance() != 0 ? selectedPayFromAccount
						// .getTotalBalance()
						// : 0D;

						initialEndingBalance = !DecimalUtil.isEquals(
								selectedDepositInAccount.getTotalBalance(), 0) ? selectedDepositInAccount
								.getTotalBalance() : 0D;

						calculateEndingBalance();
					}

				});

		depositInAccCombo.setEnabled(!isInViewMode());
		// depositInAccCombo.setPopupWidth("500px");
		paymentMethodCombo = createPaymentMethodSelectItem();
		paymentMethodCombo.setRequired(true);
		// paymentMethodCombo.setWidth(100);

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
						checkNoText.setEnabled(!isInViewMode());
					} else {
						if (payFromCombo.getValue() == null)
							checkNoText.setValue(messages.toBePrinted());
						else if (transaction != null) {
							checkNoText.setValue(transaction.getCheckNumber());
						}
					}
				} else
					checkNoText.setValue("");
				checkNoText.setEnabled(false);

			}
		});

		checkNoText = new TextItem(messages.chequeNo(), "checkNoText");
		checkNoText.setValue(messages.toBePrinted());
		// checkNoText.setHelpInformation(true);
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

		billsDue = new DateField(messages.returnsDueOnOrBefore(), "billsDue");
		// billsDue.setHelpInformation(true);
		billsDue.setTitle(messages.returnsDueOnOrBefore());
		billsDue.setEnteredDate(new ClientFinanceDate());
		billsDue.setEnabled(!isInViewMode());
		billsDue.addDateValueChangeHandler(new DateValueChangeHandler() {

			@Override
			public void onDateValueChange(ClientFinanceDate date) {
				if (transaction == null) {
					dueDateOnOrBefore = date;
					filterGrid();
				}
			}
		});

		// vatAgencyCombo = new VATAgencyCombo("Filter By "
		// + companyConstants.vatAgency());
		// vatAgencyCombo.setDisabled(isEdit);
		// vatAgencyCombo
		// .addSelectionChangeHandler(new
		// IAccounterComboSelectionChangeHandler<ClientVATAgency>() {
		//
		// public void selectedComboBoxItem(ClientVATAgency selectItem) {
		//
		// selectedVATAgency = selectItem;
		// if (selectedVATAgency != null)
		// filterlistbyVATAgency(selectedVATAgency);
		//
		// }
		//
		// });
		DynamicForm dateForm = new DynamicForm("datenumber-panel");
		dateForm.add(transactionDateItem, transNumber);
		StyledPanel datepanel = new StyledPanel("datepanel");
		datepanel.add(dateForm);

		mainform = new DynamicForm("mainform");
		mainform = UIUtils.form(messages.filter());
		mainform.add(depositInAccCombo, paymentMethodCombo, printCheck,
				checkNoText, billsDue);
		// mainform.setWidth("80%");

		// fileterForm = new DynamicForm();
		// fileterForm.setFields(billsDue);
		// fileterForm.setWidth("80%");

		amountText = new AmountField(messages.amount(), this,
				getBaseCurrency(), "amountText");
		// amountText.setHelpInformation(true);
		amountText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		amountText.setEnabled(false);

		endingBalanceText = new AmountField(messages.bankBalance(), this,
				getBaseCurrency(), "endingBalanceText");
		// endingBalanceText.setHelpInformation(true);
		endingBalanceText.setValue("" + UIUtils.getCurrencySymbol() + " 0.00");
		endingBalanceText.setEnabled(false);

		balForm = new DynamicForm("balForm");
		balForm = UIUtils.form(messages.balances());
		balForm.add(amountText, endingBalanceText);

		classListCombo = createAccounterClassListCombo();
		if (getPreferences().isClassTrackingEnabled()) {
			balForm.add(classListCombo);
		}

		// balForm.getCellFormatter().setWidth(0, 0, "197px");

		StyledPanel leftVLay = new StyledPanel("leftVLay");
		leftVLay.add(mainform);
		// leftVLay.add(fileterForm);

		StyledPanel rightVLay = new StyledPanel("rightVLay");
		rightVLay.add(balForm);
		if (isMultiCurrencyEnabled()) {
			rightVLay.add(currencyWidget);
		}

		Label lab1 = new Label("" + messages.billsToReceive() + "");

		initListGrid();

		StyledPanel mainVLay = new StyledPanel("mainVLay");
		mainVLay.add(lab);
		mainVLay.add(voidedPanel);
		mainVLay.add(datepanel);
		StyledPanel topHLay = getTopLayout();
		if (topHLay != null) {
			topHLay.add(leftVLay);
			topHLay.add(rightVLay);
			mainVLay.add(topHLay);
		} else {
			mainVLay.add(leftVLay);
			mainVLay.add(rightVLay);
		}
		mainVLay.add(lab1);
		mainVLay.add(gridLayout);
		this.add(mainVLay);
		// setSize("100%", "100%");
		listforms.add(mainform);
		listforms.add(balForm);

		selectedDepositInAccount = depositInAccCombo.getSelectedValue();
		initialEndingBalance = selectedDepositInAccount == null ? 0D
				: !DecimalUtil.isEquals(
						selectedDepositInAccount.getTotalBalance(), 0) ? selectedDepositInAccount
						.getTotalBalance() : 0D;
		calculateEndingBalance();
		// settabIndexes();

	}

	protected StyledPanel getTopLayout(){
		StyledPanel topHLay = new StyledPanel("topHLay");
		topHLay.addStyleName("fields-panel");
		return topHLay;
	}

	@Override
	protected void paymentMethodSelected(String paymentMethod) {
		if (paymentMethod == null)
			return;

		if (paymentMethod != null) {
			this.paymentMethod = paymentMethod;
			if (paymentMethod.equalsIgnoreCase(messages.cheque())
					|| paymentMethod.equalsIgnoreCase(messages.check())) {
				printCheck.setEnabled(!isInViewMode());
				checkNoText.setEnabled(!isInViewMode());
			} else {
				// paymentMethodCombo.setComboItem(paymentMethod);
				printCheck.setEnabled(false);
				checkNoText.setEnabled(false);
			}
		}

	}

	/**
	 * 
	 * @param selectItem
	 */
	private void selectedAccount(ClientAccount selectItem) {
		ClientCurrency accountAccount = getCurrency(selectItem.getCurrency());
		if (accountAccount != null && accountAccount.getID() != 0) {
			currencyWidget.setSelectedCurrency(accountAccount);
		} else {
			currencyWidget.setSelectedCurrency(getBaseCurrency());
		}

		if (isMultiCurrencyEnabled()) {
			super.setCurrency(currency);
			setCurrencyFactor(1.0);
		}
	}

	protected void filterGrid() {
		filterList = new ArrayList<ClientReceiveVATEntries>();
		tempList = new ArrayList<ClientReceiveVATEntries>();

		filterList.addAll(entries);

		if (dueDateOnOrBefore != null) {
			for (ClientReceiveVATEntries cont : filterList) {
				ClientTAXReturn clientVATReturn = Accounter.getCompany()
						.getVatReturn(cont.getTAXReturn());
				ClientFinanceDate date = new ClientFinanceDate(
						clientVATReturn.getPeriodEndDate());
				if (date.equals(dueDateOnOrBefore)
						|| date.before(dueDateOnOrBefore))
					tempList.add(cont);
			}
			filterList.clear();
			filterList.addAll(tempList);
			tempList.clear();
		}

		loadData(filterList);
		int size = grid.getRecords().size();
		if (size == 0)
			grid.addEmptyMessage(messages.noRecordsToShow());
	}

	private void calculateEndingBalance() {
		if (selectedDepositInAccount != null) {
			endingBalance = initialEndingBalance + totalAmount;
			endingBalanceText.setValue(DataUtils
					.getAmountAsStringInPrimaryCurrency(endingBalance));

		}
	}

	protected void filterlistbyVATAgency(ClientTAXAgency selectedVATAgency) {
		List<ClientTransactionReceiveVAT> filterRecords = new ArrayList<ClientTransactionReceiveVAT>();
		String selectedagency = selectedVATAgency.getName();
		for (ClientTransactionReceiveVAT receiveVAT : grid.getRecords()) {
			String taxAgencyname = getCompany().getTaxAgency(
					receiveVAT.getTaxAgency()).getName();
			if (taxAgencyname.equals(selectedagency))
				filterRecords.add(receiveVAT);
		}
		grid.setRecords(filterRecords);
	}

	// initializes the grid.
	private void initListGrid() {

		gridLayout = new StyledPanel("gridLayout");
		grid = new TransactionReceiveVATGrid(true, true);
		grid.setCanEdit(!isInViewMode());
		grid.isEnable = false;
		grid.init();
		grid.setRecieveVATView(this);
		grid.setEnabled(!isInViewMode());
		// grid.setHeight("200px");
		// if (!isEdit) {
		// // grid.addFooterValue("Total", 1);
		// grid
		// .updateFooterValues(DataUtils
		// .getAmountAsString(totalAmount), 2);
		// }
		gridLayout.add(grid);
		// gridLayout.setWidth("100%");

	}

	@Override
	protected void initTransactionViewData() {
		if (transaction == null) {
			setData(new ClientReceiveVAT());
			initTransactionNumber();
			fillGrid();
			initPayFromAccounts();
			return;
		}

		if (isMultiCurrencyEnabled()) {
			if (transaction.getCurrency() > 0) {
				this.currency = getCompany().getCurrency(
						transaction.getCurrency());
			} else {
				this.currency = getCompany().getPreferences()
						.getPrimaryCurrency();
			}
			this.currencyFactor = transaction.getCurrencyFactor();
			if (this.currency != null) {
				currencyWidget.setSelectedCurrency(this.currency);
			}
			currencyWidget.setCurrencyFactor(transaction.getCurrencyFactor());
			currencyWidget.setEnabled(!isInViewMode());
		}

		selectedDepositInAccount = getCompany().getAccount(
				transaction.getDepositIn());
		depositInAccCombo.setComboItem(selectedDepositInAccount);
		selectedVATAgency = getCompany().getTaxAgency(
				transaction.getVatAgency());
		if (selectedVATAgency != null)
			vatAgencyCombo.setComboItem(selectedVATAgency);

		billsDue.setEnteredDate(new ClientFinanceDate(transaction
				.getReturnsDueOnOrBefore()));
		transactionDateItem.setEnteredDate(transaction.getDate());
		transactionNumber = transaction.getNumber();
		transNumber.setValue(transactionNumber);

		endingBalanceText.setAmount(transaction.getEndingBalance());
		paymentMethodCombo.setComboItem(transaction.getPaymentMethod());

		paymentMethodSelected(transaction.getPaymentMethod());

		if (transaction.getCheckNumber() != null) {
			if (transaction.getCheckNumber().equals(messages.toBePrinted())) {
				checkNoText.setValue(messages.toBePrinted());
				printCheck.setValue(true);
			} else {
				checkNoText.setValue(transaction.getCheckNumber());
				printCheck.setValue(false);
			}
		}

		amountText.setValue(DataUtils
				.getAmountAsStringInPrimaryCurrency(transaction.getTotal()));
		List<ClientTransactionReceiveVAT> list = transaction
				.getClientTransactionReceiveVAT();
		int count = 0;
		grid.removeAllRecords();
		for (ClientTransactionReceiveVAT record : list) {
			if (record != null) {
				grid.addData(record);
				grid.selectRow(count);
				count++;
			}
		}
		if (isTrackClass())
			classListCombo.setComboItem(getCompany().getAccounterClass(
					transaction.getAccounterClass()));
		grid.setEnabled(!isInViewMode());
		// grid.updateFooterValues("Total"
		// + DataUtils.getAmountAsString(receiveVAT.getTotal()), 2);

	}

	@Override
	protected void initPayFromAccounts() {
		// getPayFromAccounts();
		// payFromCombo.initCombo(payFromAccounts);
		// payFromCombo.setAccountTypes(UIUtils
		// .getOptionsByType(AccountCombo.payFromCombo));
		depositInAccCombo.setAccounts();
		// payFromCombo.setDisabled(isEdit);
		ClientAccount DepositInAccount = depositInAccCombo.getSelectedValue();
		if (DepositInAccount != null)
			depositInAccCombo.setComboItem(DepositInAccount);
	}

	private void fillGrid() {
		grid.addLoadingImagePanel();
		rpcUtilService
				.getReceiveVATEntries(new AccounterAsyncCallback<ArrayList<ClientReceiveVATEntries>>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages
								.failedtogettheTransactionPayVATList());
						grid.addEmptyMessage(messages
								.noFiledTaxEntriesToReceive());

					}

					@Override
					public void onResultSuccess(
							ArrayList<ClientReceiveVATEntries> result) {
						if (result == null) {

							onFailure(null);
						}
						entries = result;
						if (result.size() == 0) {
							// Accounter.showInformation("No PayVAT list to show");
							grid.addEmptyMessage(messages
									.noFiledTaxEntriesToReceive());
						} else {

							// loadData(getfilterRecordsByDate(billsDue
							// .getEnteredDate(), entries));
							loadData(entries);

						}

					}
				});

	}

	// fills the list grid with data.

	protected void loadData(List<ClientReceiveVATEntries> result) {
		List<ClientTransactionReceiveVAT> records = new ArrayList<ClientTransactionReceiveVAT>();
		for (ClientReceiveVATEntries entry : result) {
			ClientTransactionReceiveVAT clientEntry = new ClientTransactionReceiveVAT();

			clientEntry.setTaxAgency(entry.getTAXAgency());
			clientEntry.setTAXReturn(entry.getTAXReturn());
			// clientEntry.setAmountToReceive(entry.getAmount())
			// double total = entry.getAmount();
			double balance = entry.getBalance();
			// clientEntry
			// .setTaxDue(total - balance > 0.0 ? total - balance : 0.0);
			clientEntry.setTaxDue(balance);

			records.add(clientEntry);
		}
		// setFilterByDateList(records);
		// if (selectedTaxAgency == null)
		grid.setRecords(records);
		// else
		// filterlistbyTaxAgency(selectedTaxAgency);

	}

	@Override
	protected void initTransactionNumber() {

		rpcUtilService.getNextTransactionNumber(
				ClientTransaction.TYPE_RECEIVE_TAX,
				new AccounterAsyncCallback<String>() {

					@Override
					public void onException(AccounterException caught) {
						Accounter.showError(messages
								.failedToGetTransactionNumber());
					}

					@Override
					public void onResultSuccess(String result) {
						if (result == null)
							onFailure(null);
						transactionNumber = result;
						transNumber.setValue(result);
					}

				});

	}

	@Override
	public ValidationResult validate() {
		ValidationResult result = new ValidationResult();

		if (AccounterValidator.isInPreventPostingBeforeDate(transactionDate)) {
			result.addError(transactionDate, messages.invalidateDate());
		}
		result.add(mainform.validate());
		if (isInViewMode()) {
			if (grid.getRecords().isEmpty()) {
				result.addError(grid,
						messages.youdonthaveanyfiledVATentriestoselect());
			}
		}

		if (grid == null || grid.getRecords().isEmpty()) {
			result.addError(grid,
					messages.youdonthaveanyfiledVATentriestoselect());
		} else {
			result.add(grid.validateGrid());
		}
		if (!(grid.getRecords().isEmpty())
				&& grid.getSelectedRecords().size() == 0) {
			result.addError(grid, messages.pleaseSelect(messages.fileVAT()));
		}
		if (grid != null && !grid.getRecords().isEmpty()) {

			List<ClientTransactionReceiveVAT> selectedRecords = grid
					.getSelectedRecords();
			for (ClientTransactionReceiveVAT receiveVat : selectedRecords) {

				int status = DecimalUtil.compare(
						receiveVat.getAmountToReceive(), 0.00);
				if (status <= 0) {
					result.addError(grid,
							messages.receiveAmountCannotBeZeroOrLessThanZero());

				}
			}

		}
		if (isInViewMode()) {
			if (!AccounterValidator.isPositiveAmount(totalAmount)) {
				// FIXME Need to Configm Object
				result.addError("TotalAmount", messages.amount());
			}
		}
		ClientAccount bankAccount = depositInAccCombo.getSelectedValue();
		// check if the currency of accounts is valid or not
		if (bankAccount != null) {
			ClientCurrency bankCurrency = getCurrency(bankAccount.getCurrency());
			if (bankCurrency != getBaseCurrency() && bankCurrency != currency) {
				result.addError(depositInAccCombo,
						messages.selectProperBankAccount());
			}
		}
		return result;

	}

	@Override
	public ClientReceiveVAT saveView() {
		ClientReceiveVAT saveView = super.saveView();
		if (saveView != null) {
			updateTransaction();
		}
		return saveView;
	}

	@Override
	public void saveAndUpdateView() {
		updateTransaction();
		saveOrUpdate(transaction);
	}

	@Override
	protected void updateTransaction() {
		super.updateTransaction();
		transaction.setNumber(transactionNumber);
		transaction.setType(ClientTransaction.TYPE_RECEIVE_TAX);

		if (transactionDateItem.getEnteredDate() != null)
			transaction.setDate(transactionDateItem.getEnteredDate().getDate());
		long accountId;
		if (selectedDepositInAccount != null) {
			accountId = selectedDepositInAccount.getID();
		} else {
			accountId = 0;
		}
		transaction.setDepositIn(accountId);
		transaction.setPaymentMethod(paymentMethod);
		if ((paymentMethod.equalsIgnoreCase(messages.cheque()) || paymentMethod
				.equalsIgnoreCase(messages.check()))
				&& checkNoText.getValue() != null
				&& !checkNoText.getValue().trim().isEmpty()) {
			transaction.setCheckNumber(getCheckValue());
		} else {
			transaction.setCheckNumber("");
		}

		// transaction.setIsToBePrinted(isChecked);

		if (billsDue.getValue() != null)
			transaction
					.setReturnsDueOnOrBefore((billsDue.getValue()).getDate());

		if (selectedTaxAgency != null)
			transaction.setVatAgency(selectedTaxAgency.getID());

		transaction.setTotal(totalAmount);
		transaction.setEndingBalance(endingBalance);

		if (currency != null) {
			transaction.setCurrency(currency.getID());
		}
		transaction.setCurrencyFactor(currencyWidget.getCurrencyFactor());

		transaction
				.setClientTransactionReceiveVAT(getTransactionReceiveVATList());

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

	private List<ClientTransactionReceiveVAT> getTransactionReceiveVATList() {

		// List<ClientTransactionPayVAT> payVATList = new
		// ArrayList<ClientTransactionPayVAT>();
		//
		// for (ClientTransactionPayVAT rec : grid.getSelectedRecords()) {
		// // rec.setTransaction(paySalesTax);
		// rec.setVatReturn(vatReturn)setRecords
		// payVATList.add(rec);
		//
		// }

		return grid.getSelectedRecords();
	}

	/*
	 * This method invoked eachtime when there is a change in records and it
	 * updates the noneditable amount fields
	 */
	public void adjustAmountAndEndingBalance(double toBeSetAmount) {
		// List<ClientTransactionPayVAT> selectedRecords = grid
		// .getSelectedRecords();
		// double toBeSetAmount = 0.0;
		// for (ClientTransactionPayVAT rec : selectedRecords) {
		// toBeSetAmount += rec.getAmountToPay();
		// }
		// if (this.transaction == null) {
		amountText.setAmount(toBeSetAmount);
		totalAmount = toBeSetAmount;
		if (selectedDepositInAccount != null) {
			double toBeSetEndingBalance = 0.0;
			// if (selectedDepositInAccount.isIncrease())
			toBeSetEndingBalance = selectedDepositInAccount.getTotalBalance()

					+ DataUtils.getBalance(amountText.getAmount().toString())
							.doubleValue();

			// else
			// toBeSetEndingBalance = selectedDepositInAccount
			// .getTotalBalance()
			//
			// - DataUtils.getBalance(
			// getAmountInBaseCurrency(amountText.getAmount())
			// .toString()).doubleValue();
			endingBalanceText.setAmount(toBeSetEndingBalance);
		}
		// }

	}

	@Override
	public void updateNonEditableItems() {

	}

	@Override
	public List<DynamicForm> getForms() {
		return null;
	}

	@Override
	public void deleteFailed(AccounterException caught) {

	}

	@Override
	public void deleteSuccess(IAccounterCore result) {

	}

	@Override
	public void onEdit() {
		AsyncCallback<Boolean> editCallBack = new AsyncCallback<Boolean>() {

			@Override
			public void onFailure(Throwable caught) {
				if (caught instanceof InvocationException) {
					Accounter.showMessage(messages.sessionExpired());
				} else {
					int errorCode = ((AccounterException) caught)
							.getErrorCode();
					Accounter.showError(AccounterExceptions
							.getErrorString(errorCode));

				}
			}

			@Override
			public void onSuccess(Boolean result) {
				if (result) {
					showWarningDialog();
				}
			}

		};

		AccounterCoreType type = UIUtils.getAccounterCoreType(transaction
				.getType());
		this.rpcDoSerivce.canEdit(type, transaction.id, editCallBack);
	}

	protected void showWarningDialog() {
		Accounter.showWarning(messages.W_116(), AccounterType.WARNING,
				new ErrorDialogHandler() {

					@Override
					public boolean onYesClick() {
						voidTransaction();
						return true;
					}

					private void voidTransaction() {
						AccounterAsyncCallback<Boolean> callback = new AccounterAsyncCallback<Boolean>() {

							@Override
							public void onException(AccounterException caught) {
								Accounter.showError(messages
										.failedtovoidReceiveVAT());

							}

							@Override
							public void onResultSuccess(Boolean result) {
								if (result) {
									enableFormItems();
								} else

									onFailure(new Exception());
							}

						};
						if (isInViewMode()) {
							AccounterCoreType type = UIUtils
									.getAccounterCoreType(transaction.getType());
							rpcDoSerivce.voidTransaction(type, transaction.id,
									callback);
						}

					}

					@Override
					public boolean onNoClick() {

						return true;
					}

					@Override
					public boolean onCancelClick() {

						return true;
					}
				});
	}

	private void enableFormItems() {
		setMode(EditMode.EDIT);
		paymentMethodCombo.setEnabled(!isInViewMode());
		billsDue.setEnabled(!isInViewMode());
		// vatAgencyCombo.setDisabled(isInViewMode())
		depositInAccCombo.setEnabled(!isInViewMode());
		if (paymentMethodCombo.getSelectedValue().equals(messages.cheque())) {
			printCheck.setEnabled(!isInViewMode());
		}
		grid.setEnabled(!isInViewMode());
		grid.setCanEdit(!isInViewMode());
		if (printCheck.getValue()) {
			checkNoText.setEnabled(!isInViewMode());
			checkNoText.setValue(messages.toBePrinted());
		}
		if (isMultiCurrencyEnabled()) {
			currencyWidget.setEnabled(!isInViewMode());
		}
		super.onEdit();

		fillGrid();
		setData(new ClientReceiveVAT());

	}

	@Override
	public void print() {

	}

	@Override
	public void printPreview() {

	}

	@Override
	protected String getViewTitle() {
		return messages.tAXRefund();
	}

	@Override
	protected void refreshTransactionGrid() {

	}

	@Override
	public void setFocus() {
		this.depositInAccCombo.setFocus();

	}

	private void settabIndexes() {
		depositInAccCombo.setTabIndex(1);
		paymentMethodCombo.setTabIndex(2);
		billsDue.setTabIndex(3);
		transactionDateItem.setTabIndex(4);
		transNumber.setTabIndex(5);
		amountText.setTabIndex(6);
		endingBalanceText.setTabIndex(7);
		currencyWidget.setTabIndex(8);
		if (saveAndCloseButton != null)
			saveAndCloseButton.setTabIndex(9);
		if (saveAndNewButton != null)
			saveAndNewButton.setTabIndex(10);
		cancelButton.setTabIndex(11);

	}

	@Override
	public void updateAmountsFromGUI() {
		// TODO Auto-generated method stub

	}

	@Override
	protected boolean canRecur() {
		return false;
	}

	protected void updateDiscountValues() {
		// TODO Auto-generated method stub

	}

	@Override
	protected void classSelected(ClientAccounterClass clientAccounterClass) {

	}

	@Override
	public boolean allowEmptyTransactionItems() {
		return false;
	}
}