package com.nitya.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

public class ClientReceivePayment extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_RECEIVEPAYMENT = 1;

	public static final int TYPE_CUSTOMER_PAYMENT = 2;

	long customer;
	ClientAddress address;

	private double amount = 0D;

	double customerBalance = 0D;

	long depositIn;

	double unUsedCredits = 0D;

	double unUsedPayments = 0D;

	double totalCashDiscount = 0D;

	double totalWriteOff = 0D;

	long accountsReceivable;

	double totalAppliedCredits = 0D;

	long creditsAndPayments;

	int receivePaymentType;

	boolean isToBePrinted;

	String checkNumber;

	double endingBalance;

	private double unusedAmount = 0D;

	private double tdsTotal;

	// ClientTaxCode VATCode;
	//
	// double VATFraction;
	//
	//
	//
	//
	// public ClientTaxCode getVATCode() {
	// return VATCode;
	// }
	//
	// public void setVATCode(ClientTaxCode code) {
	// VATCode = code;
	// }
	//
	// public double getVATFraction() {
	// return VATFraction;
	// }
	//
	// public void setVATFraction(double fraction) {
	// VATFraction = fraction;
	// }

	/**
	 * @return the version
	 */
	@Override
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the customer
	 */
	public long getCustomer() {
		return this.customer;
	}

	/**
	 * @param customer
	 *            the customer to set
	 */
	public void setCustomer(long customer) {
		this.customer = customer;
	}

	/**
	 * @return the customerBalance
	 */
	public double getCustomerBalance() {
		return customerBalance;
	}

	/**
	 * @param customerBalance
	 *            the customerBalance to set
	 */
	public void setCustomerBalance(double customerBalance) {
		this.customerBalance = customerBalance;
	}

	/**
	 * @return the depositIn
	 */
	public long getDepositIn() {
		return this.depositIn;
	}

	/**
	 * @param depositIn
	 *            the depositIn to set
	 */
	public void setDepositIn(long depositIn) {
		this.depositIn = depositIn;
	}

	/**
	 * @return the unUsedCredits
	 */
	public double getUnUsedCredits() {
		return unUsedCredits;
	}

	/**
	 * @param unUsedCredits
	 *            the unUsedCredits to set
	 */
	public void setUnUsedCredits(double unUsedCredits) {
		this.unUsedCredits = unUsedCredits;
	}

	/**
	 * @return the unUsedPayments
	 */
	public double getUnUsedPayments() {
		return unUsedPayments;
	}

	/**
	 * @param unUsedPayments
	 *            the unUsedPayments to set
	 */
	public void setUnUsedPayments(double unUsedPayments) {
		this.unUsedPayments = unUsedPayments;
	}

	public double getTotalCashDiscount() {
		return totalCashDiscount;
	}

	public void setTotalCashDiscount(double totalCashDiscount) {
		this.totalCashDiscount = totalCashDiscount;
	}

	public double getTotalWriteOff() {
		return totalWriteOff;
	}

	public void setTotalWriteOff(double totalWriteOff) {
		this.totalWriteOff = totalWriteOff;
	}

	/**
	 * @return the accountsReceivable
	 */
	public long getAccountsReceivable() {
		return accountsReceivable;
	}

	/**
	 * @param accountsReceivable
	 *            the accountsReceivable to set
	 */
	public void setAccountsReceivable(long accountsReceivable) {
		this.accountsReceivable = accountsReceivable;
	}

	/**
	 * @return the totalAppliedCredits
	 */
	public double getTotalAppliedCredits() {
		return totalAppliedCredits;
	}

	/**
	 * @param totalAppliedCredits
	 *            the totalAppliedCredits to set
	 */
	public void setTotalAppliedCredits(double totalAppliedCredits) {
		this.totalAppliedCredits = totalAppliedCredits;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
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
		return AccounterCoreType.RECEIVEPAYMENT;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getAmount() {
		return amount;
	}

	public int getReceivePaymentType() {
		return receivePaymentType;

	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public ClientAddress getAddress() {
		return this.address;
	}

	public void setReceivePaymentType(int typeReceivePayment) {

		this.receivePaymentType = typeReceivePayment;

	}

	public void setCustomer(ClientCustomer customer) {
		this.customer = customer.getID();

	}

	public ClientAddress setAddress(ClientAddress billingAddress) {
		return address;

	}

	public void setDepositIn(ClientAccount depositInAccount) {
		this.depositIn = depositInAccount.getID();
	}

	public void setToBePrinted(Boolean isToBePrinted) {
		this.isToBePrinted = isToBePrinted;

	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;

	}

	public void setEndingBalance(Double toBeSetEndingBalance) {
		this.endingBalance = amount;
	}

	public void setUnusedAmount(Double unusedAmount) {
		this.unusedAmount = unusedAmount;

	}

	public Double getUnusedAmount() {
		return unusedAmount;
	}

	public ClientReceivePayment clone() {
		ClientReceivePayment receivePayment = (ClientReceivePayment) this
				.clone();

		receivePayment.address = this.address.clone();

		List<ClientTransactionIssuePayment> transactionIssuePayments = new ArrayList<ClientTransactionIssuePayment>();
		for (ClientTransactionIssuePayment clientTransactionIssuePayment : this.transactionIssuePayment) {
			transactionIssuePayments.add(clientTransactionIssuePayment.clone());
		}
		receivePayment.transactionIssuePayment = transactionIssuePayments;

		List<ClientTransactionItem> transactionItems = new ArrayList<ClientTransactionItem>();
		for (ClientTransactionItem clientTransactionItem : this.transactionItems) {
			transactionItems.add(clientTransactionItem.clone());
		}
		receivePayment.transactionItems = transactionItems;

		List<ClientTransactionPayBill> transactionPayBills = new ArrayList<ClientTransactionPayBill>();
		for (ClientTransactionPayBill clientTransactionPayBill : this.transactionPayBill) {
			transactionPayBills.add(clientTransactionPayBill.clone());
		}
		receivePayment.transactionPayBill = transactionPayBills;

		List<ClientTransactionPayTAX> transactionPaySalesTaxs = new ArrayList<ClientTransactionPayTAX>();
		for (ClientTransactionPayTAX clientTransactionPaySalesTax : this.transactionPaySalesTax) {
			transactionPaySalesTaxs.add(clientTransactionPaySalesTax.clone());
		}
		receivePayment.transactionPaySalesTax = transactionPaySalesTaxs;

		List<ClientTransactionReceivePayment> transactionReceivePayments = new ArrayList<ClientTransactionReceivePayment>();
		for (ClientTransactionReceivePayment clientTransactionReceivePayment : this.transactionReceivePayment) {
			transactionReceivePayments.add(clientTransactionReceivePayment
					.clone());
		}
		receivePayment.transactionReceivePayment = transactionReceivePayments;

		return receivePayment;
	}

	/**
	 * @return the tdsTotal
	 */
	public double getTdsTotal() {
		return tdsTotal;
	}

	/**
	 * @param tdsTotal
	 *            the tdsTotal to set
	 */
	public void setTdsTotal(double tdsTotal) {
		this.tdsTotal = tdsTotal;
	}

}
