package com.vimukti.accounter.web.client.ui.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.core.reports.BaseReport;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class CheckDetailReport extends BaseReport {

	String payeeName;

	int transactionType;

	private long transactionId;

	ClientFinanceDate transactionDate;

	String number;

	private String memo;

	String accountName;

	Double amount;

	String paymentMethod;

	public Double getAmount() {
		return amount;
	}

	public void setAmount(Double amount) {
		this.amount = amount;
	}

	public String getPaymentMethod() {
		return paymentMethod;
	}

	public void setPaymentMethod(String paymentMethod) {
		this.paymentMethod = paymentMethod;
	}

	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	public String getPayeeName() {
		return payeeName;
	}

	public int getTransactionType() {
		return transactionType;
	}

	public String getNumber() {
		return number;
	}

	public String getAccountName() {
		return accountName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public void setAccountName(String accountName) {
		this.accountName = accountName;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public long getTransactionId() {
		return transactionId;
	}

	public void setMemo(String memo) {
		this.memo = memo;
	}

	public String getMemo() {
		return memo;
	}

	public boolean equals(CheckDetailReport ca) {

		if (this.accountName.equals(ca.accountName)
				&& this.payeeName.equals(ca.payeeName)
				&& this.transactionId == ca.transactionId
				&& this.transactionType == ca.transactionType
				&& this.number == ca.number
				&& DecimalUtil.isEquals(this.amount, ca.amount))
			return true;
		return false;

	}

}
