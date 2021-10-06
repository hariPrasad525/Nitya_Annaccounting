package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;

/**
 * @author vimukti16
 * 
 */
public class ReceivePaymentTransactionList implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long transactionId;

	int type;

	ClientFinanceDate dueDate;

	String number;

	Double invoiceAmount = 0D;

	Double amountDue = 0D;

	ClientFinanceDate discountDate;

	Double cashDiscount = 0D;

	Double writeOff = 0D;

	Double appliedCredits = 0D;

	Double payment = 0D;

	public ReceivePaymentTransactionList() {
		// TODO Auto-generated constructor stub
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
	 * @return the invoiceAmount
	 */
	public Double getInvoiceAmount() {
		return invoiceAmount;
	}

	/**
	 * @param invoiceAmount
	 *            the invoiceAmount to set
	 */
	public void setInvoiceAmount(Double invoiceAmount) {
		this.invoiceAmount = invoiceAmount;
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
	 * @return the writeOff
	 */
	public Double getWriteOff() {
		return writeOff;
	}

	/**
	 * @param writeOff
	 *            the writeOff to set
	 */
	public void setWriteOff(Double writeOff) {
		this.writeOff = writeOff;
	}

	/**
	 * @return the appliedCredits
	 */
	public Double getAppliedCredits() {
		return appliedCredits;
	}

	/**
	 * @param appliedCredits
	 *            the appliedCredits to set
	 */
	public void setAppliedCredits(Double appliedCredits) {
		this.appliedCredits = appliedCredits;
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

}
