package com.nitya.accounter.web.client.core.reports;

import com.nitya.accounter.web.client.core.ClientFinanceDate;

public class MISC1099TransactionDetail extends BaseReport {

	private String name;

	private ClientFinanceDate date;

	private int type;

	private String number;

	private String memo;

	private int box1099;

	private String account;

	private double amount;

	private long transactionId;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ClientFinanceDate getDate() {
		return date;
	}

	public void setDate(ClientFinanceDate date) {
		this.date = date;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getMemo() {
		return memo;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public int getBox1099() {
		return box1099;
	}

	public void setBox1099(int box1099) {
		this.box1099 = box1099;
	}

	public String getAccount() {
		return account;
	}

	public void setAccount(String account) {
		this.account = account;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public long getTransactionid() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

}
