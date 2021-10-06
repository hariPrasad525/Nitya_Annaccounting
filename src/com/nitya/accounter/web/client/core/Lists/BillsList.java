package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;

/**
 * 
 * @author vimukti16
 * 
 *         Bills List View may have the following type of Transactions.
 * 
 *         (Vendor Bills, Vendor Credit Memo, Cash Purchase and Credit Card
 *         Charge and Write Check -> Vendor)
 */
public class BillsList implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long transactionId;

	int type;

	ClientFinanceDate dueDate;

	ClientFinanceDate transactionDate;

	String number;

	String vendorName;

	Double originalAmount;

	Double balance;
	boolean isVoided;
	private long currency;
	private int saveStatus;

	int status;

	private int expenseStatus;

	private long payFrom;

	/**
	 * @return the transactionId
	 */
	public long getTransactionId() {
		return transactionId;
	}

	public boolean isVoided() {
		return isVoided;
	}

	public void setVoided(boolean isVoided) {
		this.isVoided = isVoided;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
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
	 * @return the dueDate
	 */
	public ClientFinanceDate getDueDate() {
		return dueDate;
	}

	/**
	 * @param dueDate
	 *            the dueDate to set
	 */
	public void setDueDate(ClientFinanceDate dueDate) {
		this.dueDate = dueDate;
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
	 * @return the vendorName
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * @param vendorName
	 *            the vendorName to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	/**
	 * @return the originalAmount
	 */
	public Double getOriginalAmount() {
		return originalAmount;
	}

	/**
	 * @param originalAmount
	 *            the originalAmount to set
	 */
	public void setOriginalAmount(Double originalAmount) {
		this.originalAmount = originalAmount;
	}

	/**
	 * @return the balance
	 */
	public Double getBalance() {
		return balance;
	}

	/**
	 * @param balance
	 *            the balance to set
	 */
	public void setBalance(Double balance) {
		this.balance = balance;
	}

	/**
	 * @return the status
	 */
	public int getStatus() {
		return status;
	}

	/**
	 * @param status
	 *            the status to set
	 */
	public void setStatus(int status) {
		this.status = status;
	}

	public boolean isDeleted() {
		return this.status == ClientTransaction.STATUS_DELETED;
	}

	/**
	 * 
	 * @return transactionDate
	 */
	public ClientFinanceDate getDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public void setExpenseStatus(int expenseStatus) {
		this.expenseStatus = expenseStatus;
	}

	public int getExpenseStatus() {
		return expenseStatus;
	}

	public void setPayFrom(long payFrom) {
		this.payFrom = payFrom;
	}

	public long getPayFrom() {
		return payFrom;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	public int getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(int saveStatus) {
		this.saveStatus = saveStatus;
	}

}
