package com.nitya.accounter.core;

import java.io.Serializable;

import org.hibernate.CallbackException;
import org.hibernate.Session;
import org.hibernate.classic.Lifecycle;
import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

public class TDSTransactionItem implements IAccounterServerCore, Lifecycle {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long id;

	private Vendor vendor;

	private double totalAmount;

	private double tdsAmount;

	private double surchargeAmount;

	private double eduCess;

	private double totalTax;

	private FinanceDate transactionDate;

	private Transaction transaction;

	private double taxRate;

	private boolean isOnSaveProccessed;

	// These fields for filling
	private int deducteeCode;
	private String remark;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public Double getTotalAmount() {
		return totalAmount;
	}

	public void setTotalAmount(Double totalAmount) {
		this.totalAmount = totalAmount;
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
	public Transaction getTransaction() {
		return transaction;
	}

	/**
	 * @param transactionID
	 *            the transactionID to set
	 */
	public void setTransaction(Transaction transactionID) {
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

	public FinanceDate getTransactiondate() {
		return getTransactionDate();
	}

	public void setTransactiondate(FinanceDate transactiondate) {
		this.setTransactionDate(transactiondate);
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
		// TODO Auto-generated method stub

	}

	public boolean isPositiveTransaction() {
		return this.transaction.isPositiveTransaction();
	}

	public boolean isOnSaveProccessed() {
		return isOnSaveProccessed;
	}

	public void setOnSaveProccessed(boolean isOnSaveProccessed) {
		this.isOnSaveProccessed = isOnSaveProccessed;
	}

	public FinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(FinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	@Override
	public boolean onDelete(Session arg0) throws CallbackException {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public void onLoad(Session arg0, Serializable arg1) {
		// TODO Auto-generated method stub

	}

	@Override
	public boolean onSave(Session arg0) throws CallbackException {
		if (transaction instanceof PayBill) {
			this.taxRate = ((PayBill) transaction).getTdsTaxItem().getTaxRate();
		}
		return false;
	}

	@Override
	public boolean onUpdate(Session arg0) throws CallbackException {
		if (OnUpdateThreadLocal.get()) {
			return false;
		}
		// TODO Auto-generated method stub
		return false;
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

	@Override
	public void selfValidate() {
		// TODO Auto-generated method stub
		
	}
}
