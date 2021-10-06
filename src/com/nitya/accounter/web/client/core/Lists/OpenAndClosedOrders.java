package com.nitya.accounter.web.client.core.Lists;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.BaseReport;

public class OpenAndClosedOrders extends BaseReport implements IsSerializable {

	long transactionID;

	long id;

	int transactionType;

	String vendorOrCustomerName;

	ClientFinanceDate transactionDate;

	double amount;

	private String number;

	private Integer status;

	public long getTransactionID() {
		return transactionID;
	}

	public void setTransactionID(long transactionID) {
		this.transactionID = transactionID;
	}

	public long getID() {
		return id;
	}

	public void setID(long id) {
		this.id = id;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public String getVendorOrCustomerName() {
		return vendorOrCustomerName;
	}

	public void setVendorOrCustomerName(String vendorOrCustomerName) {
		this.vendorOrCustomerName = vendorOrCustomerName;
	}

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public String getNumber() {
		return number;
	}

	public void setStatus(Integer status) {
		this.status = status;
	}

	public Integer getStatus() {
		return status;
	}
}
