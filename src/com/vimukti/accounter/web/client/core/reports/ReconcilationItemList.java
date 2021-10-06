package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class ReconcilationItemList extends BaseReport implements
		IsSerializable, Serializable {
	private static final long serialVersionUID = 1L;

	private long id;

	private String bankAccountName;

	private long transaction;

	/** Transaction Date */
	private ClientFinanceDate transactionDate;

	/** Transaction Number */
	private String transactionNo;

	/** Transaction Type */
	private int transationType;

	/** Credit Amount. Exists if Transaction is MoneyIn */
	private double amount;

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}

	public long getTransaction() {
		return transaction;
	}

	public void setTransaction(long transaction) {
		this.transaction = transaction;
	}

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getTransactionNo() {
		return transactionNo;
	}

	public void setTransactionNo(String transactionNo) {
		this.transactionNo = transactionNo;
	}

	public int getTransationType() {
		return transationType;
	}

	public void setTransationType(int transationType) {
		this.transationType = transationType;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public String getBankAccountName() {
		return bankAccountName;
	}

	public void setBankAccountName(String bankAccountName) {
		this.bankAccountName = bankAccountName;
	}

}
