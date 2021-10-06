package com.nitya.accounter.web.client.core;

import java.util.HashSet;
import java.util.Set;

public class ClientCustomerRefund extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int NOT_ISSUED = 0;

	public static final int ISSUED = 2;

	// long date;

	long payTo;
	ClientAddress address;

	long payFrom;

	boolean isToBePrinted;

	double endingBalance = 0D;

	boolean isPaid = false;

	String accountsReceivable;

	double payments = 0D;

	double balanceDue = 0D;

	String checkNumber;

	Set<ClientTransactionReceivePayment> transactionReceivePayments = new HashSet<ClientTransactionReceivePayment>();

	/**
	 * @return the address
	 */
	public ClientAddress getAddress() {
		return address;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	/**
	 * @param address
	 *            the address to set
	 */
	public void setAddress(ClientAddress address) {
		this.address = address;
	}

	// @Override
	// public void setDate(long date) {
	// this.date = date;
	// }

	public boolean getIsToBePrinted() {
		return isToBePrinted;
	}

	public void setIsToBePrinted(boolean isToBePrinted) {
		this.isToBePrinted = isToBePrinted;
	}

	public boolean getIsPaid() {
		return isPaid;
	}

	public void setIsPaid(boolean isPaid) {
		this.isPaid = isPaid;
	}

	public double getPayments() {
		return payments;
	}

	public void setPayments(double payments) {
		this.payments = payments;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public void updatePaymentsAndBalanceDue(double amount2) {
		this.payments -= amount2;
		this.balanceDue += amount2;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	public void setPayTo(long id) {
		this.payTo = id;
	}

	public void setPayFrom(long id) {
		this.payFrom = id;
	}

	public long getPayFrom() {
		return this.payFrom;
	}

	public long getPayTo() {
		return this.payTo;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CUSTOMERREFUND;
	}

	public ClientCustomerRefund clone() {
		ClientCustomerRefund clientCustomerRefundClone = (ClientCustomerRefund) this
				.clone();
		clientCustomerRefundClone.address = this.address.clone();
		Set<ClientTransactionReceivePayment> transactionReceivePayments = new HashSet<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment clientTransactionReceivePayment : this.transactionReceivePayments) {
			transactionReceivePayments.add(clientTransactionReceivePayment);
		}
		clientCustomerRefundClone.transactionReceivePayments = transactionReceivePayments;

		return clientCustomerRefundClone;
	}

}
