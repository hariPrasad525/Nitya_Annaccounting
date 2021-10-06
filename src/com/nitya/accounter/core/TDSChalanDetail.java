package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.List;

import org.json.JSONException;

import com.nitya.accounter.web.client.Global;
import com.nitya.accounter.web.client.exception.AccounterException;

public class TDSChalanDetail extends Transaction implements
		IAccounterServerCore {

	/**
	 * 
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
	private Account payFrom;
	private FinanceDate fromDate;
	private FinanceDate toDate;

	private String etdsfillingAcknowledgementNo;
	private FinanceDate acknowledgementDate;
	private boolean isFiled;

	private List<TDSTransactionItem> tdsTransactionItems = new ArrayList<TDSTransactionItem>();

	public Double getIncomeTaxAmount() {
		return incomeTaxAmount;
	}

	public void setIncomeTaxAmount(Double incomeTaxAmount) {
		this.incomeTaxAmount = incomeTaxAmount;
	}

	public Double getSurchangePaidAmount() {
		return surchangePaidAmount;
	}

	public void setSurchangePaidAmount(Double surchangePaidAmount) {
		this.surchangePaidAmount = surchangePaidAmount;
	}

	public Double getEducationCessAmount() {
		return educationCessAmount;
	}

	public void setEducationCessAmount(Double educationCessAmount) {
		this.educationCessAmount = educationCessAmount;
	}

	public Double getInterestPaidAmount() {
		return interestPaidAmount;
	}

	public void setInterestPaidAmount(Double interestPaidAmount) {
		this.interestPaidAmount = interestPaidAmount;
	}

	public Double getPenaltyPaidAmount() {
		return penaltyPaidAmount;
	}

	public void setPenaltyPaidAmount(Double penaltyPaidAmount) {
		this.penaltyPaidAmount = penaltyPaidAmount;
	}

	public Double getOtherAmount() {
		return otherAmount;
	}

	public void setOtherAmount(Double otherAmount) {
		this.otherAmount = otherAmount;
	}

	public String getPaymentSection() {
		return paymentSection;
	}

	public void setPaymentSection(String paymentSection) {
		this.paymentSection = paymentSection;
	}

	public Long getBankChalanNumber() {
		return bankChalanNumber;
	}

	public void setBankChalanNumber(Long bankChalanNumber) {
		this.bankChalanNumber = bankChalanNumber;
	}

	public Long getCheckNumber() {
		return checkNumber;
	}

	public void setCheckNumber(Long checkNumber) {
		this.checkNumber = checkNumber;
	}

	public String getBankBsrCode() {
		return bankBsrCode;
	}

	public void setBankBsrCode(String bankBsrCode) {
		this.bankBsrCode = bankBsrCode;
	}

	public boolean isBookEntry() {
		return bookEntry;
	}

	public void setBookEntry(boolean bookEntry) {
		this.bookEntry = bookEntry;
	}

	public Long getDateTaxPaid() {
		return dateTaxPaid;
	}

	public void setDateTaxPaid(Long dateTaxPaid) {
		this.dateTaxPaid = dateTaxPaid;
	}

	public int getChalanPeriod() {
		return chalanPeriod;
	}

	public void setChalanPeriod(int chalanPeriod) {
		this.chalanPeriod = chalanPeriod;
	}

	public Long getChalanSerialNumber() {
		return chalanSerialNumber;
	}

	public void setChalanSerialNumber(Long chalanSerialNumber) {
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

	public List<TDSTransactionItem> getTdsTransactionItems() {
		return tdsTransactionItems;
	}

	public void setTdsTransactionItems(List<TDSTransactionItem> list) {
		this.tdsTransactionItems = list;
	}

	private Account getTDSTaxAgencyAccount() {
		for (TDSTransactionItem item : tdsTransactionItems) {
			if (item.getTransaction() instanceof PayBill) {
				PayBill paybill = (PayBill) item.getTransaction();
				if (paybill.getTdsTaxItem() != null) {
					TAXAgency taxAgency = paybill.getTdsTaxItem()
							.getTaxAgency();
					return taxAgency.getPurchaseLiabilityAccount();
				}
			}
		}
		return null;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		if (getSaveStatus() == STATUS_DRAFT) {
			return;
		}
		// TODO Auto-generated method stub

	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	public Account getPayFrom() {
		return payFrom;
	}

	public void setPayFrom(Account payFrom) {
		this.payFrom = payFrom;
	}

	@Override
	public boolean isPositiveTransaction() {
		return false;
	}

	@Override
	public boolean isDebitTransaction() {
		return true;
	}

	@Override
	public int getTransactionCategory() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public String toString() {
		return AccounterServerConstants.TYPE_TDS_CHALLAN;
	}

	@Override
	public Payee getInvolvedPayee() {
		// TODO Auto-generated method stub
		return null;
	}

	public String getEtdsfillingAcknowledgementNo() {
		return etdsfillingAcknowledgementNo;
	}

	public void setEtdsfillingAcknowledgementNo(
			String etdsfillingAcknowledgementNo) {
		this.etdsfillingAcknowledgementNo = etdsfillingAcknowledgementNo;
	}

	public FinanceDate getAcknowledgementDate() {
		return acknowledgementDate;
	}

	public void setAcknowledgementDate(FinanceDate acknowledgementDate) {
		this.acknowledgementDate = acknowledgementDate;
	}

	public boolean isFiled() {
		return isFiled;
	}

	public void setFiled(boolean isFiled) {
		this.isFiled = isFiled;
	}

	public FinanceDate getFromDate() {
		return fromDate;
	}

	public void setFromDate(FinanceDate fromDate) {
		this.fromDate = fromDate;
	}

	public FinanceDate getToDate() {
		return toDate;
	}

	public void setToDate(FinanceDate toDate) {
		this.toDate = toDate;
	}

	@Override
	public void getEffects(ITransactionEffects e) {
		e.add(getTDSTaxAgencyAccount(), -getTotal());
		e.add(getPayFrom(), getTotal());
	}

	@Override
	public void selfValidate() throws AccounterException {
		if (chalanSerialNumber == 0) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().challanSerialNo());
		}
		if (paymentSection == null || paymentSection.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().natureOfPayment());
		}
		if (bankBsrCode == null || bankBsrCode.trim().isEmpty()) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().bankBSRCode());
		}
		if (payFrom == null) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().payFrom());
		}
		if (chalanPeriod == 0) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().challanPeriod());
		}
		if (formType == 0) {
			throw new AccounterException(AccounterException.ERROR_OBJECT_NULL,
					Global.get().messages().formType());
		}

		if (tdsTransactionItems == null || tdsTransactionItems.isEmpty()) {
			throw new AccounterException(
					AccounterException.ERROR_TRANSACTION_ITEM_NULL, Global
							.get().messages().transaction());
		}
	}

}
