package com.vimukti.accounter.web.client.core;

public class ClientCustomerPrePayment extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	long customer;
	long depositIn;

	ClientAddress address;

	double balanceDue = 0d;

	private double customerBalance = 0D;

	private boolean isToBePrinted;

	private String checkNumber;

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	public double getCustomerBalance() {
		return customerBalance;
	}

	public void setCustomerBalance(double customerBalance) {
		this.customerBalance = customerBalance;
	}

	@Override
	public String getName() {
		return Utility.getTransactionName(getType());
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.CUSTOMERPREPAYMENT;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	public long getCustomer() {
		return customer;
	}

	public void setCustomer(long customer) {
		this.customer = customer;
	}

	public long getDepositIn() {
		return depositIn;
	}

	public void setDepositIn(long depositIn) {
		this.depositIn = depositIn;
	}

	public ClientAddress getAddress() {
		return address;
	}

	public void setAddress(ClientAddress address) {
		this.address = address;
	}

	public double getBalanceDue() {
		return balanceDue;
	}

	public void setBalanceDue(double balanceDue) {
		this.balanceDue = balanceDue;
	}

	public void setToBePrinted(boolean isToBePrinted) {
		this.isToBePrinted = isToBePrinted;
	}

	public boolean isToBePrinted() {
		return isToBePrinted;
	}

	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getCheckNumber() {
		return checkNumber;
	}

	public ClientCustomerPrePayment clone() {
		ClientCustomerPrePayment clientCustomerPrePaymentClone = (ClientCustomerPrePayment) this
				.clone();
		clientCustomerPrePaymentClone.address = this.address.clone();
		return clientCustomerPrePaymentClone;
	}

	public String getpaymentMethod() {
		return paymentMethod;
	}

}
