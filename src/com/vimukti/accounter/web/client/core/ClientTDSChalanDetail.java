package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.ui.Accounter;

public class ClientTDSChalanDetail extends ClientTransaction {

	/**
	 * this class is used to maintain the details of chalan
	 */
	private static final long serialVersionUID = 1L;

	public static final int Form26Q = 1;
	public static final int Form27Q = 2;
	public static final int Form27EQ = 3;

	private double incomeTaxAmount;
	private double surchangePaidAmount;
	private double educationCessAmount;
	private double interestPaidAmount;
	private double penaltyPaidAmount;
	private double otherAmount;

	private String paymentSection;
	private long bankChalanNumber;
	private long checkNumber;
	private String bankBsrCode;

	private boolean bookEntry;
	private long dateTaxPaid;
	private int chalanPeriod;
	private long chalanSerialNumber;
	private int formType;
	private int assesmentYearStart;
	private int assesmentYearEnd;

	private long payFrom;

	private long fromDate;
	private long toDate;

	private String etdsfillingAcknowledgementNo;
	private long acknowledgementDate;
	private boolean isFiled;

	private List<ClientTDSTransactionItem> tdsTransactionItems = new ArrayList<ClientTDSTransactionItem>();

	@Override
	public String getName() {
		return Accounter.getMessages().tdsChallan();
	}

	@Override
	public String getDisplayName() {
		return getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.TDSCHALANDETAIL;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return id;
	}

	public double getIncomeTaxAmount() {
		return incomeTaxAmount;
	}

	public void setIncomeTaxAmount(double incomeTaxAmount) {
		this.incomeTaxAmount = incomeTaxAmount;
	}

	public double getSurchangePaidAmount() {
		return surchangePaidAmount;
	}

	public void setSurchangePaidAmount(double surchangePaidAmount) {
		this.surchangePaidAmount = surchangePaidAmount;
	}

	public double getEducationCessAmount() {
		return educationCessAmount;
	}

	public void setEducationCessAmount(double educationCessAmount) {
		this.educationCessAmount = educationCessAmount;
	}

	public double getInterestPaidAmount() {
		return interestPaidAmount;
	}

	public void setInterestPaidAmount(double interestPaidAmount) {
		this.interestPaidAmount = interestPaidAmount;
	}

	public double getPenaltyPaidAmount() {
		return penaltyPaidAmount;
	}

	public void setPenaltyPaidAmount(double penaltyPaidAmount) {
		this.penaltyPaidAmount = penaltyPaidAmount;
	}

	public double getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(double otherAmount) {
		this.otherAmount = otherAmount;
	}

	public String getPaymentSection() {
		return paymentSection;
	}

	public void setPaymentSection(String paymentSectionSelected) {
		this.paymentSection = paymentSectionSelected;
	}

	public long getBankChalanNumber() {
		return bankChalanNumber;
	}

	public void setBankChalanNumber(long bankChalanNumber) {
		this.bankChalanNumber = bankChalanNumber;
	}

	public long getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(long checkNumber) {
		this.checkNumber = checkNumber;
	}

	public boolean isBookEntry() {
		return bookEntry;
	}

	public void setBookEntry(boolean bookEntry) {
		this.bookEntry = bookEntry;
	}

	/**
	 * @return the bankBsrCode
	 */
	public String getBankBsrCode() {
		return bankBsrCode;
	}

	/**
	 * @param bankBsrCode
	 *            the bankBsrCode to set
	 */
	public void setBankBsrCode(String bankBsrCode) {
		this.bankBsrCode = bankBsrCode;
	}

	public long getDateTaxPaid() {
		return dateTaxPaid;
	}

	public void setDateTaxPaid(long dateTaxPaid) {
		this.dateTaxPaid = dateTaxPaid;
	}

	public int getChalanPeriod() {
		return chalanPeriod;
	}

	public void setChalanPeriod(int chalanPeriod) {
		this.chalanPeriod = chalanPeriod;
	}

	public long getChalanSerialNumber() {
		return chalanSerialNumber;
	}

	public void setChalanSerialNumber(long chalanSerialNumber) {
		this.chalanSerialNumber = chalanSerialNumber;
	}

	public int getFormType() {
		return formType;
	}

	public void setFormType(int formType) {
		this.formType = formType;
	}

	public int getAssesmentYearStart() {
		return assesmentYearStart;
	}

	public void setAssesmentYearStart(int assesmentYearStart) {
		this.assesmentYearStart = assesmentYearStart;
	}

	public int getAssessmentYearEnd() {
		return assesmentYearEnd;
	}

	public void setAssessmentYearEnd(int assessmentYearEnd) {
		this.assesmentYearEnd = assessmentYearEnd;
	}

	public List<ClientTDSTransactionItem> getTdsTransactionItems() {
		return tdsTransactionItems;
	}

	public void setTdsTransactionItems(
			List<ClientTDSTransactionItem> tdsTransactionItems) {
		this.tdsTransactionItems = tdsTransactionItems;
	}

	public long getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(long payFrom) {
		this.payFrom = payFrom;
	}

	public String getEtdsfillingAcknowledgementNo() {
		return etdsfillingAcknowledgementNo;
	}

	public void setEtdsfillingAcknowledgementNo(
			String etdsfillingAcknowledgementNo) {
		this.etdsfillingAcknowledgementNo = etdsfillingAcknowledgementNo;
	}

	public long getAcknowledgementDate() {
		return acknowledgementDate;
	}

	public void setAcknowledgementDate(long acknowledgementDate) {
		this.acknowledgementDate = acknowledgementDate;
	}

	public boolean isFiled() {
		return isFiled;
	}

	public void setFiled(boolean isFiled) {
		this.isFiled = isFiled;
	}

	public long getFromDate() {
		return fromDate;
	}

	public void setFromDate(long fromDate) {
		this.fromDate = fromDate;
	}

	public long getToDate() {
		return toDate;
	}

	public void setToDate(long toDate) {
		this.toDate = toDate;
	}
}
