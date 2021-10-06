package com.vimukti.accounter.web.client.core;

public class ClientTDSTransactionItem implements IAccounterCore {

	/**
	 * this class contains information of all the transaction happened using any
	 * tax.
	 */
	private static final long serialVersionUID = 1L;
	private long id;

	private long vendor;

	private double totalAmount;

	private double tdsAmount;

	private double surchargeAmount;

	private double eduCess;

	private double totalTax;

	private long transactionDate;

	private double taxRate;

	private long transaction;

	// These fields for filling
	private int deducteeCode;
	private String remark;

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {

	}

	@Override
	public String getName() {
		return "ClientTDSTransactionItem";
	}

	@Override
	public String getDisplayName() {
		return "ClientTDSTransactionItem";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TDSCHALANTRANSACTIONITEM;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public long getVendor() {
		return vendor;
	}

	public void setVendor(long vendor) {
		this.vendor = vendor;
	}

	public double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(double totalAmount) {
		this.totalAmount = totalAmount;
	}

	public long getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(long transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getSurchargeAmount() {
		return surchargeAmount;
	}

	public void setSurchargeAmount(double surchargeAmount) {
		this.surchargeAmount = surchargeAmount;
	}

	public double getEduCess() {
		return eduCess;
	}

	public void setEduCess(double eduCess) {
		this.eduCess = eduCess;
	}

	/**
	 * @return the transactionID
	 */
	public long getTransaction() {
		return transaction;
	}

	/**
	 * @param transactionID
	 *            the transactionID to set
	 */
	public void setTransaction(long transactionID) {
		this.transaction = transactionID;
	}

	public double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public double getTotalTax() {
		return totalTax;
	}

	public void setTotalTax(double totalTax) {
		this.totalTax = totalTax;
	}

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	public int getDeducteeCode() {
		return deducteeCode;
	}

	public void setDeducteeCode(int deducteeCode) {
		this.deducteeCode = deducteeCode;
	}

	public String getRemark() {
		return remark;
	}

	public void setRemark(String remark) {
		this.remark = remark;
	}

}
