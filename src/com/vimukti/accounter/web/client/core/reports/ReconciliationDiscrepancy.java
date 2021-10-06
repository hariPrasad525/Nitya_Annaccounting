package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class ReconciliationDiscrepancy extends BaseReport implements
		Serializable, IsSerializable {

	private static final long serialVersionUID = 1L;

	private long transactionId;

	private ClientFinanceDate transactionDate;

	private String transactionNumber;

	private String name;

	private int transactionType;

	private double reconciliedAmount;

	private double transactionAmount;

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public double getReconciliedAmount() {
		return reconciliedAmount;
	}

	public void setReconciliedAmount(double reconciliedAmount) {
		this.reconciliedAmount = reconciliedAmount;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public String getTransactionNumber() {
		return transactionNumber;
	}

	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	public double getTransactionAmount() {
		return transactionAmount;
	}

	public void setTransactionAmount(double transactionAmount) {
		this.transactionAmount = transactionAmount;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

}
