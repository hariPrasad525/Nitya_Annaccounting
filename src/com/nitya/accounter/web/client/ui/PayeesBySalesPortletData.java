package com.nitya.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PayeesBySalesPortletData implements IsSerializable {
	private String name;
	private int noOfTrans;
	private double amount;
	private double quantity;
	private long currency;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getNoOfTrans() {
		return noOfTrans;
	}

	public void setNoOfTrans(int noOfTrans) {
		this.noOfTrans = noOfTrans;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getQuantity() {
		return quantity;
	}

	public void setQuantity(double quantity) {
		this.quantity = quantity;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

}
