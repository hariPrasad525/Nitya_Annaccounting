package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.ClientTransaction;

/**
 * 
 * @author vimukti16
 * 
 *         ====>Vendor Payments List view may have the following type of
 *         possible transactions.
 * 
 *         (Vendor Payments, PayBills, Write Check -> Vendor and TaxAgency )
 * 
 *         ====>Payments List view may have the following type of possible
 *         transactions.
 * 
 *         (Customer Refunds , Vendor Payments,Cash Purchases, Credit Card
 *         Charge and all Write Checks)
 */
public class PaymentsList implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long transactionId;

	int type;

	ClientFinanceDate paymentDate;

	String paymentNumber;

	private String checkNumber;

	int status;

	ClientFinanceDate issuedDate;

	String name;

	String paymentMethodName;

	Double amountPaid = 0D;
	boolean isVoided;

	String inFavourOf;

	// boolean isDeleted;

	public boolean isDeleted() {
		return this.status == ClientTransaction.STATUS_DELETED;
	}

	// public void setDeleted(boolean isDeleted) {
	// this.isDeleted = isDeleted;
	// }

	private long currency;

	private int saveStatus;

	public boolean isVoided() {
		return isVoided;
	}

	public void setVoided(boolean isVoided) {
		this.isVoided = isVoided;
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
	 * @return the paymentDate
	 */
	public ClientFinanceDate getPaymentDate() {
		return paymentDate;
	}

	/**
	 * @param paymentDate
	 *            the paymentDate to set
	 */
	public void setPaymentDate(ClientFinanceDate paymentDate) {
		this.paymentDate = paymentDate;
	}

	/**
	 * @return the paymentNumber
	 */
	public String getPaymentNumber() {
		return paymentNumber;
	}

	/**
	 * @param paymentNumber
	 *            the paymentNumber to set
	 */
	public void setPaymentNumber(String paymentNumber) {
		this.paymentNumber = paymentNumber;
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

	/**
	 * @return the issuedDate
	 */
	public ClientFinanceDate getIssuedDate() {
		return issuedDate;
	}

	/**
	 * @param issuedDate
	 *            the issuedDate to set
	 */
	public void setIssuedDate(ClientFinanceDate issuedDate) {
		this.issuedDate = issuedDate;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
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
	 * @return the paymentMethodName
	 */
	public String getPaymentMethodName() {
		return paymentMethodName;
	}

	/**
	 * @param paymentMethodName
	 *            the paymentMethodName to set
	 */
	public void setPaymentMethodName(String paymentMethodName) {
		this.paymentMethodName = paymentMethodName;
	}

	/**
	 * @return the amountPaid
	 */
	public Double getAmountPaid() {
		return amountPaid;
	}

	/**
	 * @param amountPaid
	 *            the amountPaid to set
	 */
	public void setAmountPaid(Double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public long getCurrency() {
		return this.currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(long currency) {
		this.currency = currency;
	}

	public String getInFavourOf() {
		return inFavourOf;
	}

	public void setInFavourOf(String inFavourOf) {
		this.inFavourOf = inFavourOf;
	}

	public int getSaveStatus() {
		return saveStatus;
	}

	public void setSaveStatus(int saveStatus) {
		this.saveStatus = saveStatus;
	}

}
