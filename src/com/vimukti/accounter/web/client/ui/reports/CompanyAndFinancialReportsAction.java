package com.vimukti.accounter.web.client.ui.reports;

import java.util.HashMap;

import com.google.gwt.core.client.GWT;
import com.google.gwt.core.client.RunAsyncCallback;
import com.google.gwt.resources.client.ImageResource;
import com.vimukti.accounter.web.client.Global;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.ExpenseList;
import com.vimukti.accounter.web.client.ui.Accounter;
import com.vimukti.accounter.web.client.ui.HistoryTokens;
import com.vimukti.accounter.web.client.ui.MainFinanceWindow;
import com.vimukti.accounter.web.client.ui.core.Action;
import com.vimukti.accounter.web.client.ui.core.ViewManager;

public class CompanyAndFinancialReportsAction extends Action {

	public static final int TYPE_BALANCE_SHEET = 1;
	public static final int TYPE_CASH_FLOW = 2;
	public static final int TYPE_TRAIL_BALANCE = 3;
	public static final int TYPE_TRANSACTION_DETAIL_BY_ACCOUNT = 4;
	public static final int TYPE_GENERAL_LEDGER = 5;
	public static final int TYPE_EXPENSE = 6;
	public static final int TYPE_AUTOMATIC_TRANSACTIONS = 7;
	public static final int TYPE_REALISED_LOSS_AND_GAIN = 8;
	public static final int TYPE_UNREALISED_LOSS_AND_GAIN = 9;
	public static final int TYPE_EXCHANGE_RATES = 10;
	public static final int TYPE_PROFIT_AND_LOSS = 11;
	public static final int TYPE_RECONCILIATIONS = 12;
	public static final int TYPE_RECONCILIATION_DETAIL_BY_ACCOUNT = 13;
	public static final int TYPE_SALES_LIABILITY = 14;
	private static final int TYPE_TRANSACTION_DETAIL_BY_TAX_ITEM = 15;
	public static final int TYPE_INCOME_BY_CUSTOMER_DETAIL = 16;

	private int type;
	private boolean fromReportsHome;
	private ClientFinanceDate enteredDate;

	private long accountID;

	public CompanyAndFinancialReportsAction(int type) {
		super();
		this.type = type;
		this.catagory = messages.report();
	}

	public CompanyAndFinancialReportsAction(int type, long accountID) {
		this(type);
		this.accountID = accountID;
	}

	@Override
	public void run() {

		GWT.runAsync(new RunAsyncCallback() {

			public void onSuccess() {
				AbstractReportView report = null;

				switch (type) {
				case TYPE_BALANCE_SHEET:
					report = new BalanceSheetReport();
					break;
				case TYPE_CASH_FLOW:
					report = new CashFlowStatementReport();
					break;
				case TYPE_TRAIL_BALANCE:
					report = new TrialBalanceReport();
					break;
				case TYPE_TRANSACTION_DETAIL_BY_ACCOUNT:
					report = new TransactionDetailByAccountReport();
					break;
				case TYPE_GENERAL_LEDGER:
					report = new GeneralLedgerReport();
					break;
				case TYPE_EXPENSE:
					ExpenseReport eReport = new ExpenseReport();
					if (data != null && data instanceof ExpenseList) {
						eReport.setType(((ExpenseList) data)
								.getTransactionType());
					}
					report = eReport;
					break;
				case TYPE_AUTOMATIC_TRANSACTIONS:
					report = new AutomaticTransactionsReport();
					break;
				case TYPE_REALISED_LOSS_AND_GAIN:
					report = new RealisedExchangeLossesAndGainsReport();
					break;
				case TYPE_UNREALISED_LOSS_AND_GAIN:
					UnRealisedExchangeLossesAndGainsReport urlgReport = new UnRealisedExchangeLossesAndGainsReport();
					urlgReport.setExchangeRates((HashMap<Long, Double>) data);
					urlgReport.setEnteredDate(enteredDate);
					report = urlgReport;
					break;
				case TYPE_EXCHANGE_RATES:
					EnterExchangeRatesDialog dialog = new EnterExchangeRatesDialog(
							isFromReportsHome());
					ViewManager.getInstance().showDialog(dialog);
					break;
				case TYPE_PROFIT_AND_LOSS:
					report = new ProfitAndLossReport();
					break;
				case TYPE_RECONCILIATIONS:
					report = new ReconcilationsReport();
					break;
				case TYPE_RECONCILIATION_DETAIL_BY_ACCOUNT:
					report = new RecincilationDetailsByAccountReport(accountID);
					break;
				case TYPE_SALES_LIABILITY:
					report = new SalesTaxLiabilityReport();
					break;
				case TYPE_TRANSACTION_DETAIL_BY_TAX_ITEM:
					report = new TransactionDetailByTaxItemReport();
					break;
				case TYPE_INCOME_BY_CUSTOMER_DETAIL:
					report = new IncomeByCustomerDetailReport();
				}

				if (report != null) {
					MainFinanceWindow.getViewManager().showView(report, data,
							isDependent, CompanyAndFinancialReportsAction.this);
				}
			}

			public void onFailure(Throwable e) {
				Accounter.showError(Global.get().messages()
						.unableToshowtheview());
			}
		});

	}

	@Override
	public ImageResource getBigImage() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ImageResource getSmallImage() {
		return Accounter.getFinanceMenuImages().reports();
	}

	@Override
	public String getHistoryToken() {
		switch (type) {
		case TYPE_BALANCE_SHEET:
			return "balanceSheet";
		case TYPE_CASH_FLOW:
			return "cashFlowReport";
		case TYPE_TRAIL_BALANCE:
			return "trialBalance";
		case TYPE_TRANSACTION_DETAIL_BY_ACCOUNT:
			return "transactionDetailByAccount";
		case TYPE_GENERAL_LEDGER:
			return "generalLedger";
		case TYPE_EXPENSE:
			return "expenseReport";
		case TYPE_AUTOMATIC_TRANSACTIONS:
			return "automaticTransactions";
		case TYPE_REALISED_LOSS_AND_GAIN:
			return "realisedExchangeLossesOrGains";
		case TYPE_UNREALISED_LOSS_AND_GAIN:
			return "unrealisedExchangeLossesAndGains";
		case TYPE_EXCHANGE_RATES:
			return "enterExchangeRatesDialog";
		case TYPE_PROFIT_AND_LOSS:
			return "profitAndLoss";
		case TYPE_RECONCILIATIONS:
			return "ReconciliationReports";
		case TYPE_RECONCILIATION_DETAIL_BY_ACCOUNT:
			return "reconcilationDetailByAccount";
		case TYPE_SALES_LIABILITY:
			return "salesTaxLiability";
		case TYPE_TRANSACTION_DETAIL_BY_TAX_ITEM:
			return "transactionDetailByTaxItem";
		case TYPE_INCOME_BY_CUSTOMER_DETAIL:
			return "incomeByCustomerDetail";
		}
		return null;
	}

	@Override
	public String getHelpToken() {
		switch (type) {
		case TYPE_BALANCE_SHEET:
			return "balance-sheet";
		case TYPE_CASH_FLOW:
			return "cash-flow";
		case TYPE_TRAIL_BALANCE:
			return "trial-balance";
		case TYPE_TRANSACTION_DETAIL_BY_ACCOUNT:
			return "transaction-by-account";
		case TYPE_GENERAL_LEDGER:
			return "gl-report";
		case TYPE_EXPENSE:
			return "expense-report";
		case TYPE_AUTOMATIC_TRANSACTIONS:
			return "automatic-transactions";
		case TYPE_REALISED_LOSS_AND_GAIN:
			return "realisedExchangeLossesOrGains";
		case TYPE_UNREALISED_LOSS_AND_GAIN:
			return "unrealisedExchangeLossesAndGains";
		case TYPE_EXCHANGE_RATES:
			return "enterExchangeRatesDialog";
		case TYPE_PROFIT_AND_LOSS:
			return "profit-loss";
		case TYPE_RECONCILIATIONS:
			return "Reconciliation-Reports";
		case TYPE_RECONCILIATION_DETAIL_BY_ACCOUNT:
			return "Reconcilation-DetailByAccount";
		case TYPE_INCOME_BY_CUSTOMER_DETAIL:
			return HistoryTokens.INCOMEBYCUSTOMERDETAIL;
		}
		return null;
	}

	@Override
	public String getText() {
		switch (type) {
		case TYPE_BALANCE_SHEET:
			return messages.balanceSheet();
		case TYPE_CASH_FLOW:
			return messages.cashFlowReport();
		case TYPE_TRAIL_BALANCE:
			return messages.trialBalance();
		case TYPE_TRANSACTION_DETAIL_BY_ACCOUNT:
			return messages.transactionDetailByAccount();
		case TYPE_GENERAL_LEDGER:
			return messages.generalLedgerReport();
		case TYPE_EXPENSE:
			return messages.expenseReport();
		case TYPE_AUTOMATIC_TRANSACTIONS:
			return messages.automaticTransactions();
		case TYPE_REALISED_LOSS_AND_GAIN:
			return messages.realisedExchangeLossesAndGains();
		case TYPE_UNREALISED_LOSS_AND_GAIN:
			return messages.unRealisedExchangeLossesAndGains();
		case TYPE_PROFIT_AND_LOSS:
			return messages.profitAndLoss();
		case TYPE_RECONCILIATIONS:
			return messages.reconciliationsReport();
		case TYPE_RECONCILIATION_DETAIL_BY_ACCOUNT:
			return messages.reconcilationDetailByAccount();
		case TYPE_SALES_LIABILITY:
			return messages.salesTaxLiability();
		case TYPE_TRANSACTION_DETAIL_BY_TAX_ITEM:
			return messages.transactionDetailByTaxItem();
		case TYPE_INCOME_BY_CUSTOMER_DETAIL:
			return messages2.incomeByCustomerDetail(Global.get().Customer());
		}
		return null;
	}

	public void setEnterDate(ClientFinanceDate date) {
		this.enteredDate = date;
	}

	public static CompanyAndFinancialReportsAction transactionDetailByTaxItem() {
		return new CompanyAndFinancialReportsAction(
				TYPE_TRANSACTION_DETAIL_BY_TAX_ITEM);
	}

	public static CompanyAndFinancialReportsAction salesTaxLiability() {
		return new CompanyAndFinancialReportsAction(TYPE_SALES_LIABILITY);
	}

	public static CompanyAndFinancialReportsAction balanceSheet() {
		return new CompanyAndFinancialReportsAction(TYPE_BALANCE_SHEET);
	}

	public static CompanyAndFinancialReportsAction cashFlow() {
		return new CompanyAndFinancialReportsAction(TYPE_CASH_FLOW);
	}

	public static CompanyAndFinancialReportsAction trailBalance() {
		return new CompanyAndFinancialReportsAction(TYPE_TRAIL_BALANCE);
	}

	public static CompanyAndFinancialReportsAction transactionDetailByAccount() {
		return new CompanyAndFinancialReportsAction(
				TYPE_TRANSACTION_DETAIL_BY_ACCOUNT);
	}

	public static CompanyAndFinancialReportsAction generalLedger() {
		return new CompanyAndFinancialReportsAction(TYPE_GENERAL_LEDGER);
	}

	public static CompanyAndFinancialReportsAction expense() {
		return new CompanyAndFinancialReportsAction(TYPE_EXPENSE);
	}

	public static CompanyAndFinancialReportsAction automaticTransactions() {
		return new CompanyAndFinancialReportsAction(TYPE_AUTOMATIC_TRANSACTIONS);
	}

	public static CompanyAndFinancialReportsAction realisedLossAndGrains() {
		return new CompanyAndFinancialReportsAction(TYPE_REALISED_LOSS_AND_GAIN);
	}

	public static CompanyAndFinancialReportsAction unRelealisedLossAndGrains() {
		return new CompanyAndFinancialReportsAction(
				TYPE_UNREALISED_LOSS_AND_GAIN);
	}

	public static CompanyAndFinancialReportsAction exchangeRates() {
		return new CompanyAndFinancialReportsAction(TYPE_EXCHANGE_RATES);
	}

	public static CompanyAndFinancialReportsAction profitAndLoss() {
		return new CompanyAndFinancialReportsAction(TYPE_PROFIT_AND_LOSS);
	}

	public static CompanyAndFinancialReportsAction reconciliations() {
		return new CompanyAndFinancialReportsAction(TYPE_RECONCILIATIONS);
	}

	public static CompanyAndFinancialReportsAction reconciliationByAccount(
			long accountID) {
		return new CompanyAndFinancialReportsAction(
				TYPE_RECONCILIATION_DETAIL_BY_ACCOUNT, accountID);
	}

	public static CompanyAndFinancialReportsAction incomeByCustomer() {
		return new CompanyAndFinancialReportsAction(
				TYPE_INCOME_BY_CUSTOMER_DETAIL);
	}

	public boolean isFromReportsHome() {
		return fromReportsHome;
	}

	public void setFromReportsHome(boolean fromReportsHome) {
		this.fromReportsHome = fromReportsHome;
	}

}
