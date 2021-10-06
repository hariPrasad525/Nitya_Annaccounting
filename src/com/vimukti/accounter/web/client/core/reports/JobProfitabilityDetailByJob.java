package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class JobProfitabilityDetailByJob extends BaseReport implements Serializable, IsSerializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private long itemId;
	private long itemType;
	private String itemName;
	private double costAmount;
	private double revenueAmount;
	private boolean isCost;
	private long customerId;
	private long jobId;
	public long getItemId() {
		return itemId;
	}
	public void setItemId(long itemId) {
		this.itemId = itemId;
	}
	public String getItemName() {
		return itemName;
	}
	public void setItemName(String itemName) {
		this.itemName = itemName;
	}
	public double getCostAmount() {
		return costAmount;
	}
	public void setCostAmount(double costAmount) {
		this.costAmount = costAmount;
	}
	public double getRevenueAmount() {
		return revenueAmount;
	}
	public void setRevenueAmount(double revenueAmount) {
		this.revenueAmount = revenueAmount;
	}
	public boolean isCost() {
		return isCost;
	}
	public void setCost(boolean isCost) {
		this.isCost = isCost;
	}
	public long getItemType() {
		return itemType;
	}
	public void setItemType(long itemType) {
		this.itemType = itemType;
	}
	public long getCustomerId() {
		return customerId;
	}
	public void setCustomerId(long customerId) {
		this.customerId = customerId;
	}
	public long getJobId() {
		return jobId;
	}
	public void setJobId(long jobId) {
		this.jobId = jobId;
	}

}
