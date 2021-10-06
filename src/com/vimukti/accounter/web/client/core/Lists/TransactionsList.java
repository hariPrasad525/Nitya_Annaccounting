package com.vimukti.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

/**
 * 
 * Transactions List View may have the following possible type of Transactions.
 * 
 * (Invoices, Cash Sales , Write Checks etc., based on the spent or received
 * amount )
 * 
 */
public class TransactionsList implements IsSerializable, Serializable {
	private static final long serialVersionUID = 1L;

	private long transactionId;

	private int type;

	private ClientFinanceDate date;

	private String number;

	private String customerName;

	private Double spentAmount = 0.0;
	private Double receivedAmount = 0.0;

	private long currency;
	private boolean isSelected;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ClientFinanceDate getDate() {
		return date;
	}

	public void setDate(ClientFinanceDate date) {
		this.date = date;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getCustomerName() {
		return customerName;
	}

	public void setCustomerName(String customerName) {
		this.customerName = customerName;
	}

	public long getCurrency() {
		return currency;
	}

	public void setCurrency(long currency) {
		this.currency = currency;
	}

	public boolean isSelected() {
		return isSelected;
	}

	public void setSelected(boolean isSelected) {
		this.isSelected = isSelected;
	}

	public Double getSpentAmount() {
		return spentAmount;
	}

	public void setSpentAmount(Double spentAmount) {
		this.spentAmount = spentAmount;
	}

	public Double getReceivedAmount() {
		return receivedAmount;
	}

	public void setReceivedAmount(Double receivedAmount) {
		this.receivedAmount = receivedAmount;
	}

}
