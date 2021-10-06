package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PayHeadDetails extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private String employee;

	private String payHead;

	private int payHeadType;

	private long periodEndDate;

	private String transactionNumber;

	private double amount;

	private Long transactionId;

	public String getEmployee() {
		return employee;
	}

	public void setEmployee(String employee) {
		this.employee = employee;
	}

	public long getPeriodEndDate() {
		return periodEndDate;
	}

	public void setPeriodEndDate(long periodEndDate) {
		this.periodEndDate = periodEndDate;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getPayHead() {
		return payHead;
	}

	public void setPayHead(String payHead) {
		this.payHead = payHead;
	}

	public int getPayHeadType() {
		return payHeadType;
	}

	public void setPayHeadType(int payHeadType) {
		this.payHeadType = payHeadType;
	}

	public void setTransactionId(Long transactionId) {
		this.transactionId = transactionId;
	}

	public Long getTransactionId() {
		return transactionId;
	}
}
