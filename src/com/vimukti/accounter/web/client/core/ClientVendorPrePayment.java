package com.vimukti.accounter.web.client.core;

public class ClientVendorPrePayment extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private String name;
	private boolean isAmountIncludeTDS;
	private double unusedAmount;
	private double tdsTotal;
	private long vendor;
	private ClientAddress address;
	private long payFrom;
	private double endingBalance;
	private String checkNumber;
	private long tdsTaxItem;
	private double vendorBalance;

	@Override
	public String getName() {
		return name;
	}

	@Override
	public String getDisplayName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.VENDORPAYMENT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	/**
	 * @return the isAmountIncludeTDS
	 */
	public boolean isAmountIncludeTDS() {
		return isAmountIncludeTDS;
	}

	/**
	 * @param isAmountIncludeTDS
	 *            the isAmountIncludeTDS to set
	 */
	public void setAmountIncludeTDS(boolean isAmountIncludeTDS) {
		this.isAmountIncludeTDS = isAmountIncludeTDS;
	}

	public void setUnusedAmount(double unusedAmount) {
		this.unusedAmount = unusedAmount;
	}

	public double getUnusedAmount() {
		return unusedAmount;
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

	public long getVendor() {
		return this.vendor;
	}

	public void setVendor(long vendor2) {
		this.vendor = vendor2;

	}

	public ClientAddress getAddress() {
		return this.address;
	}

	public void setAddress(ClientAddress billingAddress) {
		this.address = billingAddress;

	}

	public long getPayFrom() {
		return this.payFrom;
	}

	public void setPayFrom(long payFromAccount) {
		this.payFrom = payFromAccount;
	}

	public void setEndingBalance(double amount) {
		this.endingBalance = amount;

	}

	public double getEndingBalance() {
		return this.endingBalance;
	}

	/**
	 * @return the checkNumber
	 */
	public String getCheckNumber() {
		return checkNumber;
	}

	/**
	 * @param checkNumber
	 *            the checkNumber to set
	 */
	public void setCheckNumber(String checkNumber) {
		this.checkNumber = checkNumber;
	}

	/**
	 * @return the taxItem
	 */
	public long getTdsTaxItem() {
		return tdsTaxItem;
	}

	/**
	 * @param taxItem
	 *            the taxItem to set
	 */
	public void setTdsTaxItem(long taxItem) {
		this.tdsTaxItem = taxItem;
	}

	public void setVendorBalance(double vendorBalance) {
		this.vendorBalance = vendorBalance;
	}

	public double getVendorBalance() {
		return vendorBalance;
	}
}
