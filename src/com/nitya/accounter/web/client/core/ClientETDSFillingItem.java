package com.nitya.accounter.web.client.core;

public class ClientETDSFillingItem implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long deducteeID;

	private int serialNo;
	private String bankBSRCode;
	private long dateTaxDeposited;
	private long chalanSerialNumber;
	private String sectionForPayment;
	private double totalTDSfordeductees;
	private String panOfDeductee;
	private long dateOFpayment;
	private double amountPaid;
	private double tds;
	private double surcharge;
	private double educationCess;
	private double totalTaxDEducted;
	private double totalTaxDeposited;
	private long dateofDeduction;
	private double taxRate;
	private String bookEntry;
	private String grossingUpIndicator;

	private String remark;
	private String companyCode;

	private long id;

	@Override
	public int getVersion() {
		return 0;
	}

	@Override
	public void setVersion(int version) {

	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public String getDisplayName() {
		return "ClientETDSFilling";
	}

	@Override
	public AccounterCoreType getObjectType() {
		return null;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public int getSerialNo() {
		return serialNo;
	}

	public void setSerialNo(int serialNo) {
		this.serialNo = serialNo;
	}

	public String getBankBSRCode() {
		return bankBSRCode;
	}

	public void setBankBSRCode(String bankBSRCode) {
		this.bankBSRCode = bankBSRCode;
	}

	public long getDateTaxDeposited() {
		return dateTaxDeposited;
	}

	public void setDateTaxDeposited(long dateTaxDeposited) {
		this.dateTaxDeposited = dateTaxDeposited;
	}

	public long getChalanSerialNumber() {
		return chalanSerialNumber;
	}

	public void setChalanSerialNumber(long chalanSerialNumber) {
		this.chalanSerialNumber = chalanSerialNumber;
	}

	public String getSectionForPayment() {
		return sectionForPayment;
	}

	public void setSectionForPayment(String sectionForPayment) {
		this.sectionForPayment = sectionForPayment;
	}

	public double getTotalTDSfordeductees() {
		return totalTDSfordeductees;
	}

	public void setTotalTDSfordeductees(double totalTDSfordeductees) {
		this.totalTDSfordeductees = totalTDSfordeductees;
	}

	public long getDateOFpayment() {
		return dateOFpayment;
	}

	public void setDateOFpayment(long dateOFpayment) {
		this.dateOFpayment = dateOFpayment;
	}

	public double getAmountPaid() {
		return amountPaid;
	}

	public void setAmountPaid(double amountPaid) {
		this.amountPaid = amountPaid;
	}

	public double getTds() {
		return tds;
	}

	public void setTds(double tds) {
		this.tds = tds;
	}

	public double getSurcharge() {
		return surcharge;
	}

	public void setSurcharge(double surcharge) {
		this.surcharge = surcharge;
	}

	public double getEducationCess() {
		return educationCess;
	}

	public void setEducationCess(double educationCess) {
		this.educationCess = educationCess;
	}

	public double getTotalTaxDEducted() {
		return totalTaxDEducted;
	}

	public void setTotalTaxDEducted(double totalTaxDEducted) {
		this.totalTaxDEducted = totalTaxDEducted;
	}

	public double getTotalTaxDeposited() {
		return totalTaxDeposited;
	}

	public void setTotalTaxDeposited(double totalTaxDeposited) {
		this.totalTaxDeposited = totalTaxDeposited;
	}

	public long getDateofDeduction() {
		return dateofDeduction;
	}

	public void setDateofDeduction(long dateofDeduction) {
		this.dateofDeduction = dateofDeduction;
	}

	public double getTaxRate() {
		return taxRate;
	}

	public void setTaxRate(double taxRate) {
		this.taxRate = taxRate;
	}

	public String getBookEntry() {
		return bookEntry;
	}

	public void setBookEntry(String bookEntry) {
		this.bookEntry = bookEntry;
	}

	public long getDeducteeID() {
		return deducteeID;
	}

	public void setDeducteeID(long deducteeID) {
		this.deducteeID = deducteeID;
	}

	/**
	 * @return the remark
	 */
	public String getRemark() {
		return remark;
	}

	/**
	 * @param remark
	 *            the remark to set
	 */
	public void setRemark(String remark) {
		this.remark = remark;
	}

	/**
	 * @return the companyCode
	 */
	public String getCompanyCode() {
		return companyCode;
	}

	/**
	 * @param companyCode
	 *            the companyCode to set
	 */
	public void setCompanyCode(String companyCode) {
		this.companyCode = companyCode;
	}

	public String getPanOfDeductee() {
		return panOfDeductee;
	}

	public void setPanOfDeductee(String panOfDeductee) {
		this.panOfDeductee = panOfDeductee;
	}

	public String getGrossingUpIndicator() {
		return grossingUpIndicator;
	}

	public void setGrossingUpIndicator(String grossingUpIndicator) {
		this.grossingUpIndicator = grossingUpIndicator;
	}

}
