package com.nitya.accounter.web.client.ui;

import com.google.gwt.user.client.rpc.IsSerializable;

public class AccountDetail implements IsSerializable {
	private String name;
	private Double amount;

	public AccountDetail() {
		// TODO Auto-generated constructor stub
	}

	public AccountDetail(String accountName, Double accountBalance) {
		this.name = accountName;
		this.amount = accountBalance;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the amount
	 */
	public Double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(Double amount) {
		this.amount = amount;
	}
}
