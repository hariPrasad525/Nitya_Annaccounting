package com.nitya.accounter.web.client.ui.win8portlets;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.ui.ExpensePortletData;
import com.nitya.accounter.web.client.ui.PayeesBySalesPortletData;
import com.nitya.accounter.web.client.ui.YearOverYearPortletData;

public class DashboardPortletsData implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private MoneyComingOrGoingPortletData moneyComingData;
	private MoneyComingOrGoingPortletData moneyGoingData;
	private ExpensePortletData expensePortletData;
	private WhoIOweOrOwesMePorletData whoIowePortletData;
	private WhoIOweOrOwesMePorletData whoOwesMePortletData;
	// private RecentTransactionsPortletData recentTransactionsPortletData;
	// private AccountBalancesPortletData accountBalancesPortletData;
	private PayeesBySalesPortletData customersBySalesPortletData;
	private PayeesBySalesPortletData vendorsByExpensesPortletData;
	private PayeesBySalesPortletData itemsBySalesPortletData;
	private IncomeExpensePortletData incomeExpensePortletData;
	private ExpensePortletData incomePortletData;
	private YearOverYearPortletData yearOverYearIncomePortletData;
	private YearOverYearPortletData yearOverYearExpensePortletData;

	// private AccountBalancesPortletData bankAccountsPortletData;

	public DashboardPortletsData() {
	}

	public MoneyComingOrGoingPortletData getMoneyComingData() {
		return moneyComingData;
	}

	public void setMoneyComingData(MoneyComingOrGoingPortletData moneyComingData) {
		this.moneyComingData = moneyComingData;
	}

	public MoneyComingOrGoingPortletData getMoneyGoingData() {
		return moneyGoingData;
	}

	public void setMoneyGoingData(MoneyComingOrGoingPortletData moneyGoingData) {
		this.moneyGoingData = moneyGoingData;
	}

	public ExpensePortletData getExpensePortletData() {
		return expensePortletData;
	}

	public void setExpensePortletData(ExpensePortletData expensePortletData) {
		this.expensePortletData = expensePortletData;
	}

	public WhoIOweOrOwesMePorletData getWhoIowePortletData() {
		return whoIowePortletData;
	}

	public void setWhoIowePortletData(
			WhoIOweOrOwesMePorletData whoIowePortletData) {
		this.whoIowePortletData = whoIowePortletData;
	}

	public WhoIOweOrOwesMePorletData getWhoOwesMePortletData() {
		return whoOwesMePortletData;
	}

	public void setWhoOwesMePortletData(
			WhoIOweOrOwesMePorletData whoOwesMePortletData) {
		this.whoOwesMePortletData = whoOwesMePortletData;
	}

	// public RecentTransactionsPortletData getRecentTransactionsPortletData() {
	// return recentTransactionsPortletData;
	// }
	//
	// public void setRecentTransactionsPortletData(
	// RecentTransactionsPortletData recentTransactionsPortletData) {
	// this.recentTransactionsPortletData = recentTransactionsPortletData;
	// }
	//
	// public AccountBalancesPortletData getAccountBalancesPortletData() {
	// return accountBalancesPortletData;
	// }
	//
	// public void setAccountBalancesPortletData(
	// AccountBalancesPortletData accountBalancesPortletData) {
	// this.accountBalancesPortletData = accountBalancesPortletData;
	// }

	public PayeesBySalesPortletData getCustomersBySalesPortletData() {
		return customersBySalesPortletData;
	}

	public void setCustomersBySalesPortletData(
			PayeesBySalesPortletData customersBySalesPortletData) {
		this.customersBySalesPortletData = customersBySalesPortletData;
	}

	public PayeesBySalesPortletData getVendorsByExpensesPortletData() {
		return vendorsByExpensesPortletData;
	}

	public void setVendorsByExpensesPortletData(
			PayeesBySalesPortletData vendorsByExpensesPortletData) {
		this.vendorsByExpensesPortletData = vendorsByExpensesPortletData;
	}

	public PayeesBySalesPortletData getItemsBySalesPortletData() {
		return itemsBySalesPortletData;
	}

	public void setItemsBySalesPortletData(
			PayeesBySalesPortletData itemsBySalesPortletData) {
		this.itemsBySalesPortletData = itemsBySalesPortletData;
	}

	public IncomeExpensePortletData getIncomeExpensePortletData() {
		return incomeExpensePortletData;
	}

	public void setIncomeExpensePortletData(
			IncomeExpensePortletData incomeExpensePortletData) {
		this.incomeExpensePortletData = incomeExpensePortletData;
	}

	public ExpensePortletData getIncomePortletData() {
		return incomePortletData;
	}

	public void setIncomePortletData(ExpensePortletData incomePortletData) {
		this.incomePortletData = incomePortletData;
	}

	public YearOverYearPortletData getYearOverYearIncomePortletData() {
		return yearOverYearIncomePortletData;
	}

	public void setYearOverYearIncomePortletData(
			YearOverYearPortletData yearOverYearIncomePortletData) {
		this.yearOverYearIncomePortletData = yearOverYearIncomePortletData;
	}

	public YearOverYearPortletData getYearOverYearExpensePortletData() {
		return yearOverYearExpensePortletData;
	}

	public void setYearOverYearExpensePortletData(
			YearOverYearPortletData yearOverYearExpensePortletData) {
		this.yearOverYearExpensePortletData = yearOverYearExpensePortletData;
	}

	// public AccountBalancesPortletData getBankAccountsPortletData() {
	// return bankAccountsPortletData;
	// }
	//
	// public void setBankAccountsPortletData(
	// AccountBalancesPortletData bankAccountsPortletData) {
	// this.bankAccountsPortletData = bankAccountsPortletData;
	// }
}
