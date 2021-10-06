package com.vimukti.accounter.web.client.core;

public class ClientPayrollTransactionPayTax implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;
	private int version;
	private double originalAmount = 0D;
	private double amountDue = 0D;
	private double payment = 0D;
	private long payRun;
	private String payRunNumber;
	private long date;
	//private long payEmployee;
    private long payTax;

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public String getName() {
		return "PayRunComponent";
	}

	@Override
	public String getDisplayName() {
		return "PayRunComponent";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TRANSACTION_PAYEMPLOYEE;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public double getOriginalAmount() {
		return originalAmount;
	}

	public void setOriginalAmount(double originalAmount) {
		this.originalAmount = originalAmount;
	}

	public double getAmountDue() {
		return amountDue;
	}

	public void setAmountDue(double amountDue) {
		this.amountDue = amountDue;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

	public long getPayRun() {
		return payRun;
	}

	public void setPayRun(long payRun) {
		this.payRun = payRun;
	}

	public String getPayRunNumber() {
		return payRunNumber;
	}

	public void setPayRunNumber(String payRunNumber) {
		this.payRunNumber = payRunNumber;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public long getPayTax() {
		return payTax;
	}

	public void setPaytax(long payTax) {
		this.payTax = payTax;
	}
}
