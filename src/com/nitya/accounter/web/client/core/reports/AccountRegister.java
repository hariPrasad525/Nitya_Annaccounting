package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.ui.core.DecimalUtil;

public class AccountRegister implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	ClientFinanceDate date;

	int type;

	String number;

	double amount = 0D;

	String payTo;

	String checkNumber;

	String account;

	String memo;

	double balance = 0D;

	private boolean isVoided;

	long transactionId;

	private long currency;

	private double currencyfactor;

	/**
	 * @return the date
	 */
	public ClientFinanceDate getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(ClientFinanceDate date) {
		this.date = date;
	}

	/**
	 * @return the type
	 */
	public int getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(int type) {
		this.type = type;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
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
	 * @return the payTo
	 */
	public String getPayTo() {
		return payTo;
	}

	/**
	 * @param payTo
	 *            the payTo to set
	 */
	public void setPayTo(String payTo) {
		this.payTo = payTo;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	/**
	 * @param checkNumber
	 *            the checkNumber to set
	 */
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	/**
	 * @return the account
	 */
	public String getAccount() {
		return account;
	}

	/**
	 * @param account
	 *            the account to set
	 */
	public void setAccount(String account) {
		this.account = account;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the transactionId
	 */
	public long getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public boolean equals(AccountRegister ar) {

		if (this.type == ar.type && this.number == ar.number
				&& DecimalUtil.isEquals(this.amount, ar.amount)
				&& this.payTo.equals(ar.payTo)
				&& this.account.equals(ar.account) && this.memo.equals(ar.memo)
				&& DecimalUtil.isEquals(this.balance, ar.balance)
				&& this.transactionId == ar.transactionId)
			return true;
		return false;

	}

	public void setVoided(boolean isVoided) {
		this.isVoided = isVoided;
	}

	public boolean isVoided() {
		return isVoided;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	public double getCurrencyfactor() {
		return currencyfactor;
	}

	public void setCurrencyfactor(double currencyfactor) {
		this.currencyfactor = currencyfactor;
	}

}
