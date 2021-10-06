package com.nitya.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PayHeadSummary extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private long employeeId;

	private String employeeName;

	private double payHeadAmount;

	private long payHead;

	private String payHeadName;
	
	private boolean isDefault = false;
	
	private double employeerDeductions;

	public double getEmployeerDeductions() {
		return employeerDeductions;
	}

	public void setEmployeerDeductions(double employeerDeductions) {
		this.employeerDeductions = employeerDeductions;
	}

	public boolean isDefault() {
		return isDefault;
	}

	public void setDefault(boolean isDefault) {
		this.isDefault = isDefault;
	}

	public long getEmployeeId() {
		return employeeId;
	}

	public void setEmployeeId(long employeeId) {
		this.employeeId = employeeId;
	}

	public String getEmployeeName() {
		return employeeName;
	}

	public void setEmployeeName(String employeeName) {
		this.employeeName = employeeName;
	}

	public double getPayHeadAmount() {
		return payHeadAmount;
	}

	public void setPayHeadAmount(double payHeadAmount) {
		this.payHeadAmount = payHeadAmount;
	}

	public long getPayHead() {
		return payHead;
	}

	public void setPayHead(long payHead) {
		this.payHead = payHead;
	}

	public String getPayHeadName() {
		return payHeadName;
	}

	public void setPayHeadName(String payHeadName) {
		this.payHeadName = payHeadName;
	}
}
