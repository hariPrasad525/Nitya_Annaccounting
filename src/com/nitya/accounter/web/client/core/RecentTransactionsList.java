package com.nitya.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * RECENT TRANSACTIONS
 * 
 * @author Lingarao.R
 * 
 */
public class RecentTransactionsList implements Serializable, IsSerializable {

	private int type;
	private long id;
	private String name;
	private double amount;
	private long currecyId;
	private ClientFinanceDate TransactionDate;
	private int estimateType;

	public String getName() {
		return name;
	}

	public void setID(long id) {
		this.id = id;
	}

	public long getID() {
		return id;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public ClientFinanceDate getTransactionDate() {
		return TransactionDate;
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		TransactionDate = transactionDate;
	}

	public long getCurrecyId() {
		return currecyId;
	}

	public void setCurrecyId(long currecyId) {
		this.currecyId = currecyId;
	}

	public int getEstimateType() {
		return estimateType;
	}

	public void setEstimateType(int estimateType) {
		this.estimateType = estimateType;
	}

}
