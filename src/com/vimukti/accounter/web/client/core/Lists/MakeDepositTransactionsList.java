package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.ClientTransaction;

public class MakeDepositTransactionsList implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	

	public static final int TYPE_ACCOUNT = 1;

	public static final int TYPE_VENDOR = 2;

	public static final int TYPE_CUSTOMER = 3;

	long transactionId;

	int transactionType;

	ClientFinanceDate date;

	String number;

	String paymentMethod;

	String payeeName;

	String reference;

	double amount;

	String cashAccountId;

	int type;

	// /**
	// * @return the transactionId
	// */
	// public Transaction getTransaction() {
	// return transaction;
	// }
	//
	// /**
	// * @param transactionId the transactionId to set
	// */
	// public void setTransaction(Transaction transaction) {
	// this.transaction = transaction;
	// }

	public MakeDepositTransactionsList(ClientTransaction transaction,
			double amount) {

		this.transactionId = transaction.getID();
		this.transactionType = transaction.getType();
		this.date = new ClientFinanceDate(transaction.getTransactionDate());
		this.number = transaction.getNumber();
		this.amount = amount;

	}

	public MakeDepositTransactionsList() {
	}

	/**
	 * @return the transactionType
	 */
	public Integer getTransactionType() {
		return transactionType;
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

	/**
	 * @param transactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(Integer transactionType) {
		this.transactionType = transactionType;
	}

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
	 * @return the paymentMethod
	 */
	public String getPaymentMethod() {
		return paymentMethod;
	}

	/**
	 * @param paymentMethod
	 *            the paymentMethod to set
	 */
	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	/**
	 * @return the reference
	 */
	public String getReference() {
		return reference;
	}

	/**
	 * @param reference
	 *            the reference to set
	 */
	public void setReference(String reference) {
		this.reference = reference;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}

	/**
	 * @return the cashAccountId
	 */
	public String getCashAccountId() {
		return cashAccountId;
	}

	/**
	 * @param cashAccountId
	 *            the cashAccountId to set
	 */
	public void setCashAccountId(String cashAccountId) {
		this.cashAccountId = cashAccountId;
	}

	/**
	 * @return the type
	 */
	public Integer getType() {
		return type;
	}

	/**
	 * @param type
	 *            the type to set
	 */
	public void setType(Integer type) {
		this.type = type;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

}
