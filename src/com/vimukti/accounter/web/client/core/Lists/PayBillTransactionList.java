package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class PayBillTransactionList implements IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long transactionId;

	int type;

	ClientFinanceDate dueDate;

	String vendorName;

	String billNumber;

	Double originalAmount = 0D;

	Double amountDue = 0D;

	ClientFinanceDate discountDate;

	Double cashDiscount = 0D;

	Double credits = 0D;

	Double payment = 0D;

	String paymentMethod;

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
	 * @return the vendor
	 */
	public String getVendorName() {
		return vendorName;
	}

	/**
	 * @param vendor
	 *            the vendor to set
	 */
	public void setVendorName(String vendorName) {
		this.vendorName = vendorName;
	}

	/**
	 * @return the billNumber
	 */
	public String getBillNumber() {
		return billNumber;
	}

	/**
	 * @param billNumber
	 *            the billNumber to set
	 */
	public void setBillNumber(String billNumber) {
		this.billNumber = billNumber;
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
	 * @return the amountDue
	 */
	public Double getAmountDue() {
		return amountDue;
	}

	/**
	 * @param amountDue
	 *            the amountDue to set
	 */
	public void setAmountDue(Double amountDue) {
		this.amountDue = amountDue;
	}

	/**
	 * @return the discountDate
	 */
	public ClientFinanceDate getDiscountDate() {
		return discountDate;
	}

	/**
	 * @param discountDate
	 *            the discountDate to set
	 */
	public void setDiscountDate(ClientFinanceDate discountDate) {
		this.discountDate = discountDate;
	}

	/**
	 * @return the cashDiscount
	 */
	public Double getCashDiscount() {
		return cashDiscount;
	}

	/**
	 * @param cashDiscount
	 *            the cashDiscount to set
	 */
	public void setCashDiscount(Double cashDiscount) {
		this.cashDiscount = cashDiscount;
	}

	/**
	 * @return the credits
	 */
	public Double getCredits() {
		return credits;
	}

	/**
	 * @param credits
	 *            the credits to set
	 */
	public void setCredits(Double credits) {
		this.credits = credits;
	}

	/**
	 * @return the payment
	 */
	public Double getPayment() {
		return payment;
	}

	/**
	 * @param payment
	 *            the payment to set
	 */
	public void setPayment(Double payment) {
		this.payment = payment;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

}
