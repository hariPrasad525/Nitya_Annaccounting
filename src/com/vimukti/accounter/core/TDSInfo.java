package com.vimukti.accounter.core;

import java.io.Serializable;

public class TDSInfo implements Serializable{

	private double payment;

	private double percentage;
	@ReffereredObject
	Vendor vendor;

	double orginalBalance = 0.0D;

	double tdsAmount = 0.0D;

	FinanceDate date;

	public Vendor getVendor() {
		return vendor;
	}

	public void setVendor(Vendor vendor) {
		this.vendor = vendor;
	}

	public double getOrginalBalance() {
		return orginalBalance;
	}

	public void setOrginalBalance(double orginalBalance) {
		this.orginalBalance = orginalBalance;
	}

	public double getTdsAmount() {
		return tdsAmount;
	}

	public void setTdsAmount(double tdsAmount) {
		this.tdsAmount = tdsAmount;
	}

	public FinanceDate getDate() {
		return date;
	}

	public void setDate(FinanceDate date) {
		this.date = date;
	}

	public double getPercentage() {
		return percentage;
	}

	public void setPercentage(double percentage) {
		this.percentage = percentage;
	}

	public double getPayment() {
		return payment;
	}

	public void setPayment(double payment) {
		this.payment = payment;
	}

}
