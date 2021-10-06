package com.vimukti.accounter.web.client.core;

import com.google.gwt.user.client.rpc.IsSerializable;

public class PrintCheque implements IsSerializable {
	private String payeeName;
	private double amount;
	private long date;
	private String currencySymbol;

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getDate() {
		return date;
	}

	public void setDate(long date) {
		this.date = date;
	}

	public String getCurrency() {
		return currencySymbol;
	}

	public void setCurrency(String currency) {
		this.currencySymbol = currency;
	}
}
