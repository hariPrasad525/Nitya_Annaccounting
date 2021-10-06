package com.vimukti.accounter.web.client.core;

public class ClientTransactionIssuePayment implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	long date;

	long writeCheck;

	long customerRefund;

	long payBill;

	long customerPrepayment;

	long creditCardCharge;

	long cashPurchase;

	long payVAT;

	long receiveVAT;

	long paySalesTax;

	String number;

	String name;

	String memo;

	double amount = 0D;

	ClientTransaction transaction;

	int recordType;

	int version;

	String paymentMethod;

	private long currency;

	/**
	 * @return the payBill
	 */
	public long getPayBill() {
		return payBill;
	}

	/**
	 * @param payBill
	 *            the payBill to set
	 */
	public void setPayBill(long payBill) {
		this.payBill = payBill;
	}

	/**
	 * @return the creditCardCharge
	 */
	public long getCreditCardCharge() {
		return creditCardCharge;
	}

	/**
	 * @param creditCardCharge
	 *            the creditCardCharge to set
	 */
	public void setCreditCardCharge(long creditCardCharge) {
		this.creditCardCharge = creditCardCharge;
	}

	/**
	 * @return the payVAT
	 */

	public long getPayVAT() {
		return payVAT;
	}

	/**
	 * @param payVAT
	 *            the payVAT to set
	 */
	public void setPayVAT(long payVAT) {
		this.payVAT = payVAT;
	}

	/**
	 * @return the receiveVAT
	 */
	public long getReceiveVAT() {
		return receiveVAT;
	}

	/**
	 * @param receiveVAT
	 *            the receiveVAT to set
	 */
	public void setReceiveVAT(long receiveVAT) {
		this.receiveVAT = receiveVAT;
	}

	/**
	 * @return the cashPurchase
	 */
	public long getCashPurchase() {
		return cashPurchase;
	}

	/**
	 * @param cashPurchase
	 *            the cashPurchase to set
	 */
	public void setCashPurchase(long cashPurchase) {
		this.cashPurchase = cashPurchase;
	}

	/**
	 * @return the paySalesTax
	 */
	public long getPaySalesTax() {
		return paySalesTax;
	}

	/**
	 * @param paySalesTax
	 *            the paySalesTax to set
	 */
	public void setPaySalesTax(long paySalesTax) {
		this.paySalesTax = paySalesTax;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the date
	 */
	public long getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(long date) {
		this.date = date;
	}

	/**
	 * @return the writeCheck
	 */
	public long getWriteCheck() {
		return writeCheck;
	}

	/**
	 * @param writeCheckId
	 *            the writeCheck to set
	 */
	public void setWriteCheck(long writeCheckId) {
		this.writeCheck = writeCheckId;
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
	 * @return the transaction
	 */
	public ClientTransaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transactionId
	 *            the transaction to set
	 */
	public void setTransaction(ClientTransaction transaction) {
		this.transaction = transaction;

	}

	/**
	 * @return the customerRefund
	 */
	public long getCustomerRefund() {
		return customerRefund;
	}

	/**
	 * @param customerRefundId
	 *            the customerRefund to set
	 */
	public void setCustomerRefund(long customerRefundId) {
		this.customerRefund = customerRefundId;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	public void setRecordType(int objectType) {
		this.recordType = objectType;
	}

	public int getRecordType() {
		return recordType;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
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
		// TODO Auto-generated method stub
		return null;
	}

	public void setCustomerPrepayment(long customerPrepayment) {
		this.customerPrepayment = customerPrepayment;
	}

	public long getCustomerPrepayment() {
		return customerPrepayment;
	}

	public ClientTransactionIssuePayment clone() {
		ClientTransactionIssuePayment clientTransactionIssuePaymentClone = (ClientTransactionIssuePayment) this
				.clone();
		clientTransactionIssuePaymentClone.transaction = this.transaction
				.clone();
		return clientTransactionIssuePaymentClone;
	}

	public long getCurrency() {
		return this.currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

}
