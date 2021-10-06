package com.nitya.accounter.web.client.core;

public class ClientPayBill extends ClientTransaction {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public static final int TYPE_PAYBILL = 1;

	public static final int TYPE_VENDOR_PAYMENT = 2;

	String name;

	String fileAs;

	long payFrom;

	private long billDueOnOrBefore;

	long vendor;
	boolean isToBePrinted;

	double endingBalance;

	private double unusedAmount = 0D;

	private double UnUsedCredits = 0D;

	private double vendorBalance = 0D;

	int payBillType;

	ClientAddress address;

	String checkNumber;

	private long tdsTaxItem;

	private double tdsTotal;

	private boolean isAmountIncludeTDS;

	// ClientTaxCode VATCode;
	//
	// double VATFraction;

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

	public String getFileAs() {
		return fileAs;
	}

	public boolean isToBePrinted() {
		return isToBePrinted;
	}

	public void setToBePrinted(boolean isToBePrinted) {
		this.isToBePrinted = isToBePrinted;
	}

	public void setFileAs(String fileAs) {
		this.fileAs = fileAs;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Override
	public String toString() {
		return "ClientPayBill";
	}

	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		return name;
	}

	public void setPayBillType(int typePaybill) {

		this.payBillType = typePaybill;

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

	public void setPayFrom(ClientAccount payFromAccount) {
		this.payFrom = payFromAccount.getID();
	}

	public void setPayFrom(long payFromAccount) {
		this.payFrom = payFromAccount;
	}

	public void setBillDueOnOrBefore(ClientFinanceDate enteredDate) {
		this.billDueOnOrBefore = enteredDate.getDate();

	}

	public void setVendor(ClientVendor vendor2) {
		this.vendor = vendor2.getID();

	}

	public void setVendor(long vendor2) {
		this.vendor = vendor2;

	}

	public void setEndingBalance(double amount) {
		this.endingBalance = amount;

	}

	public double getEndingBalance() {
		return this.endingBalance;
	}

	public int getPayBillType() {
		return payBillType;

	}

	public long getVendor() {
		return this.vendor;
	}

	public ClientAddress getAddress() {
		return this.address;
	}

	public long getPayFrom() {
		return this.payFrom;
	}

	public void setBillDueOnOrBefore(long billDueOnOrBefore) {
		this.billDueOnOrBefore = billDueOnOrBefore;
	}

	public long getBillDueOnOrBefore() {
		return billDueOnOrBefore;
	}

	public void setVendorBalance(double vendorBalance) {
		this.vendorBalance = vendorBalance;
	}

	public double getVendorBalance() {
		return vendorBalance;
	}

	public void setUnusedAmount(double unusedAmount) {
		this.unusedAmount = unusedAmount;
	}

	public double getUnusedAmount() {
		return unusedAmount;
	}

	public ClientAddress setAddress(ClientAddress billingAddress) {
		return address;

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
		return AccounterCoreType.PAYBILL;
	}

	public double getUnUsedCredits() {
		return UnUsedCredits;
	}

	public void setUnUsedCredits(double UnUsedCredits) {
		this.UnUsedCredits = UnUsedCredits;

	}

	public ClientPayBill clone() {
		ClientPayBill clientPayBillClone = (ClientPayBill) this.clone();
		return clientPayBillClone;
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
}
