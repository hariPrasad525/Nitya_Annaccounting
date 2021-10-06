package com.nitya.accounter.core;

import org.json.JSONException;

import com.nitya.accounter.web.client.exception.AccounterException;

/**
 * @author Prasanna Kumar G
 * 
 */
public class EmployeePayHeadComponent implements IAccounterServerCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeePayHeadComponent() {
	}

	private long id;

	private PayHead payHead;

	private double rate;

	private FinanceDate periodEndDate;

	private FinanceDate periodStartDate;

	private EmployeePaymentDetails empPaymentDetails;
	
	private double employeerDeductions;

	public double getEmployeerDeductions() {
		return employeerDeductions;
	}

	public void setEmployeerDeductions(double employeerDeductions) {
		this.employeerDeductions = employeerDeductions;
	}

	private int version;

	/**
	 * @return the payHead
	 */
	public PayHead getPayHead() {
		return payHead;
	}

	/**
	 * @param payHead
	 *            the payHead to set
	 */
	public void setPayHead(PayHead payHead) {
		this.payHead = payHead;
	}

	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	public boolean isEarning() {
		return payHead.isEarning();
	}

	public boolean isDeduction() {
		return payHead.isDeduction();
	}

	@Override
	public boolean canEdit(IAccounterServerCore clientObject,
			boolean goingToBeEdit) throws AccounterException {
		return true;
	}

	@Override
	public void writeAudit(AuditWriter w) throws JSONException {
	}

	public FinanceDate getStartDate() {
		return periodStartDate;
	}

	public FinanceDate getEndDate() {
		return periodEndDate;
	}

	public void setStartEndDates(FinanceDate payPeriodStartDate,
			FinanceDate payPeriodEndDate) {
		this.periodStartDate = payPeriodStartDate;
		this.periodEndDate = payPeriodEndDate;
	}

	public EmployeePaymentDetails getEmpPaymentDetails() {
		return empPaymentDetails;
	}

	public void setEmpPaymentDetails(EmployeePaymentDetails empPaymentDetails) {
		this.empPaymentDetails = empPaymentDetails;
	}

	@Override
	public int getVersion() {
		return this.version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	@Override
	public long getID() {
		return this.id;
	}

	public void setId(long id) {
		this.id = id;
	}

	@Override
	public void selfValidate() throws AccounterException {
		// TODO Auto-generated method stub

	}

	public double getRateToUpdate() {
		double rate = getRate();
		if (!isDeduction()) {
			rate = -1 * rate;
		}
		return rate;
	}
}
