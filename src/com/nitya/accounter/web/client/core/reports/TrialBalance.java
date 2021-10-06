package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class TrialBalance extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;
	long accountId;
	String accountName;
	String accountNumber;
	int accountType;
	int cashFlowCategory;
	long parentAccount;
	double creditAmount;
	double debitAmount;
	double amount;
	double cashAtBeginningOfPeriod = 0D;
	int baseType;
	int subBaseType;
	int groupType;
	private transient boolean isIncrease;
	String accountFlow;
	double totalAmount;
	// JOB ,LOCATION,CLASS ID
	private long categoryId;
	// JOB ,LOCATION,CLASS type
	private int categoryType;

	/**
	 * @return the id
	 */
	public long getID() {
		return id;
	}

	/**
	 * @param id
	 *            the id to set
	 */
	public void setID(long id) {
		this.id = id;
	}

	public long getAccountId() {
		return accountId;
	}

	public void setAccountId(long accountId) {
		this.accountId = accountId;
	}

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName
	 *            the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	/**
	 * @return the accountNumber
	 */
	public String getAccountNumber() {
		return accountNumber;
	}

	/**
	 * @param accountNumber
	 *            the accountNumber to set
	 */
	public void setAccountNumber(String accountNumber) {
		this.accountNumber = accountNumber;
	}

	/**
	 * @return the accountType
	 */
	public int getAccountType() {
		return accountType;
	}

	/**
	 * @param accountType
	 *            the accountType to set
	 */
	public void setAccountType(int accountType) {
		this.accountType = accountType;
	}

	/**
	 * @return the cashFlowCategory
	 */
	public int getCashFlowCategory() {
		return cashFlowCategory;
	}

	/**
	 * @param cashFlowCategory
	 *            the cashFlowCategory to set
	 */
	public void setCashFlowCategory(int cashFlowCategory) {
		this.cashFlowCategory = cashFlowCategory;
	}

	/**
	 * @return the parentAccount
	 */
	public long getParentAccount() {
		return parentAccount;
	}

	/**
	 * @param parentAccount
	 *            the parentAccount to set
	 */
	public void setParentAccount(long parentAccount) {
		this.parentAccount = parentAccount;
	}

	/**
	 * @return the creditAmount
	 */
	public double getCreditAmount() {
		return creditAmount;
	}

	/**
	 * @param creditAmount
	 *            the creditAmount to set
	 */
	public void setCreditAmount(double creditAmount) {
		this.creditAmount = creditAmount;
	}

	/**
	 * @return the debitAmount
	 */
	public double getDebitAmount() {
		return debitAmount;
	}

	/**
	 * @param debitAmount
	 *            the debitAmount to set
	 */
	public void setDebitAmount(double debitAmount) {
		this.debitAmount = debitAmount;
	}

	/**
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @return the cashAtBeginningOfPeriod
	 */
	public double getCashAtBeginningOfPeriod() {
		return cashAtBeginningOfPeriod;
	}

	/**
	 * @param cashAtBeginningOfPeriod
	 *            the cashAtBeginningOfPeriod to set
	 */
	public void setCashAtBeginningOfPeriod(double cashAtBeginningOfPeriod) {
		this.cashAtBeginningOfPeriod = cashAtBeginningOfPeriod;
	}

	/**
	 * @return the baseType
	 */
	public int getBaseType() {
		return baseType;
	}

	/**
	 * @return the subBaseType
	 */
	public int getSubBaseType() {
		return subBaseType;
	}

	/**
	 * @return the groupType
	 */
	public int getGroupType() {
		return groupType;
	}

	/**
	 * @param baseType
	 *            the baseType to set
	 */
	public void setBaseType(int baseType) {
		this.baseType = baseType;
	}

	/**
	 * @param subBaseType
	 *            the subBaseType to set
	 */
	public void setSubBaseType(int subBaseType) {
		this.subBaseType = subBaseType;
	}

	/**
	 * @param groupType
	 *            the groupType to set
	 */
	public void setGroupType(int groupType) {
		this.groupType = groupType;
	}

	public void addAmount(double amount2) {
		if (isIncrease) {
			this.amount += amount2;
		} else {
			this.amount -= amount2;
		}
	}

	public void setIsIncrease(boolean increase) {
		this.isIncrease = increase;
	}

	public String getAccountFlow() {
		return accountFlow;
	}

	public void setAccountFlow(String accountFlow) {
		this.accountFlow = accountFlow;
	}

	/**
	 * @return the totalAmount
	 */
	public double getTotalAmount() {
		return totalAmount;
	}

	/**
	 * @param totalAmount
	 *            the totalAmount to set
	 */
	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public boolean equals(TrialBalance tb) {

		if (this.id == (tb.id)
				&& this.accountId == (tb.accountId)
				&& this.accountName.equals(tb.accountName)
				&& this.accountNumber.equals(tb.accountNumber)
				&& this.accountType == tb.accountType
				&& this.cashFlowCategory == tb.cashFlowCategory
				// && this.parentAccount==(tb.parentAccount)
				&& DecimalUtil.isEquals(this.creditAmount, tb.creditAmount)
				&& DecimalUtil.isEquals(this.cashAtBeginningOfPeriod,
						tb.cashAtBeginningOfPeriod)
				&& this.baseType == tb.baseType
				&& this.subBaseType == tb.subBaseType
				&& this.groupType == tb.groupType
				&& DecimalUtil.isEquals(this.amount, tb.amount))
			return true;

		return false;
	}

	public long getCategoryId() {
		return categoryId;
	}

	public void setCategoryId(long categoryId) {
		this.categoryId = categoryId;
	}

	public int getCategoryType() {
		return categoryType;
	}

	public void setCategoryType(int categoryType) {
		this.categoryType = categoryType;
	}
}
