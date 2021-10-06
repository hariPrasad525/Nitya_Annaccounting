package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class JobProfitability extends BaseReport implements Serializable,
		IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long transactionId;
	private int transactionType;
	private long customerId;
	private String customerName;
	private long jobId;
	private String name;
	private double costAmount;
	private double revenueAmount;
	private boolean isCost;

	public double getRevenueAmount() {
		return revenueAmount;
	}

	public void setRevenueAmount(double revenueAmount) {
		this.revenueAmount = revenueAmount;
	}

	public double getCostAmount() {
		return costAmount;
	}

	public void setCostAmount(double costAmount) {
		this.costAmount = costAmount;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public long getJobId() {
		return jobId;
	}

	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

	public boolean isCost() {
		return isCost;
	}

	public void setCost(boolean isCost) {
		this.isCost = isCost;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getCustomerId() {
		return customerId;
	}

	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}

}
