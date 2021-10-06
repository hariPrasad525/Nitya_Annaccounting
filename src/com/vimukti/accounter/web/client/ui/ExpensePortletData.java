package com.vimukti.accounter.web.client.ui;

import java.util.ArrayList;
import java.util.List;

import com.google.gwt.user.client.rpc.IsSerializable;

public class ExpensePortletData implements IsSerializable {
	private double cashExpenseTotal;
	private double creditCardExpensesTotal;
	private double allExpensesTotal;
	private List<AccountDetail> accountDetails = new ArrayList<AccountDetail>();

	public ExpensePortletData() {
		// TODO Auto-generated constructor stub
	}

	/**
	 * @return the cashExpenseTotal
	 */
	public double getCashExpenseTotal() {
		return cashExpenseTotal;
	}

	/**
	 * @param cashExpenseTotal
	 *            the cashExpenseTotal to set
	 */
	public void setCashExpenseTotal(double cashExpenseTotal) {
		this.cashExpenseTotal = cashExpenseTotal;
	}

	/**
	 * @return the creditCardExpensesTotal
	 */
	public double getCreditCardExpensesTotal() {
		return creditCardExpensesTotal;
	}

	/**
	 * @param creditCardExpensesTotal
	 *            the creditCardExpensesTotal to set
	 */
	public void setCreditCardExpensesTotal(double creditCardExpensesTotal) {
		this.creditCardExpensesTotal = creditCardExpensesTotal;
	}

	/**
	 * @return the allExpensesTotal
	 */
	public double getAllExpensesTotal() {
		return allExpensesTotal;
	}

	/**
	 * @param allExpensesTotal
	 *            the allExpensesTotal to set
	 */
	public void setAllExpensesTotal(double allExpensesTotal) {
		this.allExpensesTotal = allExpensesTotal;
	}

	/**
	 * @return the accountDetails
	 */
	public List<AccountDetail> getAccountDetails() {
		return accountDetails;
	}

	/**
	 * @param accountDetails
	 *            the accountDetails to set
	 */
	public void setAccountDetails(List<AccountDetail> accountDetails) {
		this.accountDetails = accountDetails;
	}

}
