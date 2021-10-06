package com.vimukti.accounter.web.client.ui.win8portlets;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class DashboardPortletDataRequest implements IsSerializable {

	private ClientFinanceDate expenseClaims_StartDate;
	private ClientFinanceDate expenseClaims_EndDate;
	private int recentTransactions_Limit;
	private String accountBalances_AccountType;
	private int topCustomers_Limit;
	private int topVendors_Limit;
	private int topItems_Limit;
	private ClientFinanceDate topCustomers_StartDate;
	private ClientFinanceDate topVendors_StartDate;
	private ClientFinanceDate topItems_StartDate;
	private ClientFinanceDate topCustomers_EndDate;
	private ClientFinanceDate topVendors_EndDate;
	private ClientFinanceDate topItems_EndDate;
	private ClientFinanceDate incomeAndExpense_StartDate;
	private ClientFinanceDate incomeBreakdown_StartDate;
	private ClientFinanceDate yearOverYearIncome_StartDate;
	private ClientFinanceDate yearOverYearExpense_StartDate;
	private ClientFinanceDate incomeAndExpense_EndDate;
	private ClientFinanceDate incomeBreakdown_EndDate;
	private ClientFinanceDate yearOverYearIncome_EndDate;
	private ClientFinanceDate yearOverYearExpense_EndDate;
	private long yearOverYearIncome_AccountId;
	private long yearOverYearExpense_AccountId;
	private int yearOverYearIncome_dateRangeType;
	private int yearOverYearExpense_dateRangeType;
	private int incomeAndExpense_dateRangeType;

	public ClientFinanceDate getExpenseClaims_StartDate() {
		return expenseClaims_StartDate;
	}

	public void setExpenseClaims_StartDate(
			ClientFinanceDate expenseClaims_StartDate) {
		this.expenseClaims_StartDate = expenseClaims_StartDate;
	}

	public ClientFinanceDate getExpenseClaims_EndDate() {
		return expenseClaims_EndDate;
	}

	public void setExpenseClaims_EndDate(ClientFinanceDate expenseClaims_EndDate) {
		this.expenseClaims_EndDate = expenseClaims_EndDate;
	}

	public int getRecentTransactions_Limit() {
		return recentTransactions_Limit;
	}

	public void setRecentTransactions_Limit(int recentTransactions_Limit) {
		this.recentTransactions_Limit = recentTransactions_Limit;
	}

	public String getAccountBalances_AccountType() {
		return accountBalances_AccountType;
	}

	public void setAccountBalances_AccountType(
			String accountBalances_AccountType) {
		this.accountBalances_AccountType = accountBalances_AccountType;
	}

	public int getTopCustomers_Limit() {
		return topCustomers_Limit;
	}

	public void setTopCustomers_Limit(int topCustomers_Limit) {
		this.topCustomers_Limit = topCustomers_Limit;
	}

	public int getTopVendors_Limit() {
		return topVendors_Limit;
	}

	public void setTopVendors_Limit(int topVendors_Limit) {
		this.topVendors_Limit = topVendors_Limit;
	}

	public int getTopItems_Limit() {
		return topItems_Limit;
	}

	public void setTopItems_Limit(int topItems_Limit) {
		this.topItems_Limit = topItems_Limit;
	}

	public ClientFinanceDate getTopCustomers_StartDate() {
		return topCustomers_StartDate;
	}

	public void setTopCustomers_StartDate(
			ClientFinanceDate topCustomers_StartDate) {
		this.topCustomers_StartDate = topCustomers_StartDate;
	}

	public ClientFinanceDate getTopVendors_StartDate() {
		return topVendors_StartDate;
	}

	public void setTopVendors_StartDate(ClientFinanceDate topVendors_StartDate) {
		this.topVendors_StartDate = topVendors_StartDate;
	}

	public ClientFinanceDate getTopItems_StartDate() {
		return topItems_StartDate;
	}

	public void setTopItems_StartDate(ClientFinanceDate topItems_StartDate) {
		this.topItems_StartDate = topItems_StartDate;
	}

	public ClientFinanceDate getTopCustomers_EndDate() {
		return topCustomers_EndDate;
	}

	public void setTopCustomers_EndDate(ClientFinanceDate topCustomers_EndDate) {
		this.topCustomers_EndDate = topCustomers_EndDate;
	}

	public ClientFinanceDate getTopItems_EndDate() {
		return topItems_EndDate;
	}

	public void setTopItems_EndDate(ClientFinanceDate topItems_EndDate) {
		this.topItems_EndDate = topItems_EndDate;
	}

	public ClientFinanceDate getTopVendors_EndDate() {
		return topVendors_EndDate;
	}

	public void setTopVendors_EndDate(ClientFinanceDate topVendors_EndDate) {
		this.topVendors_EndDate = topVendors_EndDate;
	}

	public ClientFinanceDate getIncomeAndExpense_StartDate() {
		return incomeAndExpense_StartDate;
	}

	public void setIncomeAndExpense_StartDate(
			ClientFinanceDate incomeAndExpense_StartDate) {
		this.incomeAndExpense_StartDate = incomeAndExpense_StartDate;
	}

	public ClientFinanceDate getIncomeBreakdown_StartDate() {
		return incomeBreakdown_StartDate;
	}

	public void setIncomeBreakdown_StartDate(
			ClientFinanceDate incomeBreakdown_StartDate) {
		this.incomeBreakdown_StartDate = incomeBreakdown_StartDate;
	}

	public ClientFinanceDate getYearOverYearIncome_StartDate() {
		return yearOverYearIncome_StartDate;
	}

	public void setYearOverYearIncome_StartDate(
			ClientFinanceDate yearOverYearIncome_StartDate) {
		this.yearOverYearIncome_StartDate = yearOverYearIncome_StartDate;
	}

	public ClientFinanceDate getYearOverYearExpense_StartDate() {
		return yearOverYearExpense_StartDate;
	}

	public void setYearOverYearExpense_StartDate(
			ClientFinanceDate yearOverYearExpense_StartDate) {
		this.yearOverYearExpense_StartDate = yearOverYearExpense_StartDate;
	}

	public ClientFinanceDate getIncomeAndExpense_EndDate() {
		return incomeAndExpense_EndDate;
	}

	public void setIncomeAndExpense_EndDate(
			ClientFinanceDate incomeAndExpense_EndDate) {
		this.incomeAndExpense_EndDate = incomeAndExpense_EndDate;
	}

	public ClientFinanceDate getIncomeBreakdown_EndDate() {
		return incomeBreakdown_EndDate;
	}

	public void setIncomeBreakdown_EndDate(
			ClientFinanceDate incomeBreakdown_EndDate) {
		this.incomeBreakdown_EndDate = incomeBreakdown_EndDate;
	}

	public ClientFinanceDate getYearOverYearIncome_EndDate() {
		return yearOverYearIncome_EndDate;
	}

	public void setYearOverYearIncome_EndDate(
			ClientFinanceDate yearOverYearIncome_EndDate) {
		this.yearOverYearIncome_EndDate = yearOverYearIncome_EndDate;
	}

	public ClientFinanceDate getYearOverYearExpense_EndDate() {
		return yearOverYearExpense_EndDate;
	}

	public void setYearOverYearExpense_EndDate(
			ClientFinanceDate yearOverYearExpense_EndDate) {
		this.yearOverYearExpense_EndDate = yearOverYearExpense_EndDate;
	}

	public long getYearOverYearIncome_AccountId() {
		return yearOverYearIncome_AccountId;
	}

	public void setYearOverYearIncome_AccountId(
			long yearOverYearIncome_AccountId) {
		this.yearOverYearIncome_AccountId = yearOverYearIncome_AccountId;
	}

	public long getYearOverYearExpense_AccountId() {
		return yearOverYearExpense_AccountId;
	}

	public void setYearOverYearExpense_AccountId(
			long yearOverYearExpense_AccountId) {
		this.yearOverYearExpense_AccountId = yearOverYearExpense_AccountId;
	}

	public int getYearOverYearIncome_dateRangeType() {
		return yearOverYearIncome_dateRangeType;
	}

	public void setYearOverYearIncome_dateRangeType(
			int yearOverYearIncome_dateRangeType) {
		this.yearOverYearIncome_dateRangeType = yearOverYearIncome_dateRangeType;
	}

	public int getYearOverYearExpense_dateRangeType() {
		return yearOverYearExpense_dateRangeType;
	}

	public void setYearOverYearExpense_dateRangeType(
			int yearOverYearExpense_dateRangeType) {
		this.yearOverYearExpense_dateRangeType = yearOverYearExpense_dateRangeType;
	}

	public int getIncomeAndExpense_dateRangeType() {
		return incomeAndExpense_dateRangeType;
	}

	public void setIncomeAndExpense_dateRangeType(
			int incomeAndExpense_dateRangeType) {
		this.incomeAndExpense_dateRangeType = incomeAndExpense_dateRangeType;
	}
}
