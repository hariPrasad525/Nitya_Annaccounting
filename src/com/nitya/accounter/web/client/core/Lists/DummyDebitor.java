package com.nitya.accounter.web.client.core.Lists;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.nitya.accounter.web.client.core.ClientFinanceDate;
import com.nitya.accounter.web.client.core.reports.BaseReport;

public class DummyDebitor extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	String debitorName;
	double debitdays_in30;
	double debitdays_in60;
	double debitdays_in90;
	double debitdays_inolder;
	double debitdays_incurrent;

	long transactionId;
	ClientFinanceDate dueDate;

	public long getTransactionId() {
		return transactionId;
	}

	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public ClientFinanceDate getDueDate() {
		return dueDate;
	}

	public void setDueDate(ClientFinanceDate dueDate) {
		this.dueDate = dueDate;
	}

	public String getDebitorName() {
		return debitorName;
	}

	public void setDebitorName(String debitorName) {
		this.debitorName = debitorName;
	}

	public double getDebitdays_in30() {
		return debitdays_in30;
	}

	public void setDebitdays_in30(double debitdaysIn30) {
		debitdays_in30 = debitdaysIn30;
	}

	public double getDebitdays_in60() {
		return debitdays_in60;
	}

	public void setDebitdays_in60(double debitdaysIn60) {
		debitdays_in60 = debitdaysIn60;
	}

	public double getDebitdays_in90() {
		return debitdays_in90;
	}

	public void setDebitdays_in90(double debitdaysIn90) {
		debitdays_in90 = debitdaysIn90;
	}

	public double getDebitdays_inolder() {
		return debitdays_inolder;
	}

	public void setDebitdays_inolder(double debitdaysInolder) {
		debitdays_inolder = debitdaysInolder;
	}

	public void setDebitdays_incurrent(double dayscurrentTotal) {
		debitdays_incurrent = dayscurrentTotal;
	}

	public double getDebitdays_incurrent() {
		return debitdays_incurrent;
	}
}
