package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;
import com.vimukti.accounter.web.client.ui.core.DecimalUtil;

public class TransactionDetailByTaxItem extends BaseReport implements
		IsSerializable, Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String payeeName;

	String taxAgencyName;

	String taxItemName;

	double rate;

	int transactionType;

	ClientFinanceDate date;

	String number;

	String memo;

	double taxableAmount = 0D;

	double salesTaxAmount = 0D;

	boolean isVoid;

	long transactionId;

	public String getPayeeName() {
		return payeeName;
	}

	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	/**
	 * @return the taxAgencyName
	 */
	public String getTaxAgencyName() {
		return taxAgencyName;
	}

	/**
	 * @param taxAgencyName
	 *            the taxAgencyName to set
	 */
	public void setTaxAgencyName(String taxAgencyName) {
		this.taxAgencyName = taxAgencyName;
	}

	/**
	 * @return the taxItemName
	 */
	public String getTaxItemName() {
		return taxItemName;
	}

	/**
	 * @param taxItemName
	 *            the taxItemName to set
	 */
	public void setTaxItemName(String taxItemName) {
		this.taxItemName = taxItemName;
	}

	/**
	 * @return the rate
	 */
	public double getRate() {
		return rate;
	}

	/**
	 * @param rate
	 *            the rate to set
	 */
	public void setRate(double rate) {
		this.rate = rate;
	}

	/**
	 * @return the transactionType
	 */
	public int getTransactionType() {
		return transactionType;
	}

	/**
	 * @param transactionType
	 *            the transactionType to set
	 */
	public void setTransactionType(int transactionType) {
		this.transactionType = transactionType;
	}

	/**
	 * @return the date
	 */
	public ClientFinanceDate getDate() {
		return date;
	}

	/**
	 * @param date
	 *            the date to set
	 */
	public void setDate(ClientFinanceDate date) {
		this.date = date;
	}

	/**
	 * @return the number
	 */
	public String getNumber() {
		return number;
	}

	/**
	 * @param number
	 *            the number to set
	 */
	public void setNumber(String number) {
		this.number = number;
	}

	/**
	 * @return the memo
	 */
	public String getMemo() {
		return memo;
	}

	/**
	 * @param memo
	 *            the memo to set
	 */
	public void setMemo(String memo) {
		this.memo = memo;
	}

	/**
	 * @return the taxableAmount
	 */
	public double getTaxableAmount() {
		return taxableAmount;
	}

	/**
	 * @param taxableAmount
	 *            the taxableAmount to set
	 */
	public void setTaxableAmount(double taxableAmount) {
		this.taxableAmount = taxableAmount;
	}

	/**
	 * @return the salesTaxAmount
	 */
	public double getSalesTaxAmount() {
		return salesTaxAmount;
	}

	/**
	 * @param salesTaxAmount
	 *            the salesTaxAmount to set
	 */
	public void setSalesTaxAmount(double salesTaxAmount) {
		this.salesTaxAmount = salesTaxAmount;
	}

	/**
	 * @return the isVoid
	 */
	public boolean getIsVoid() {
		return isVoid;
	}

	/**
	 * @param isVoid
	 *            the isVoid to set
	 */
	public void setIsVoid(boolean isVoid) {
		this.isVoid = isVoid;
	}

	/**
	 * @return the transactionId
	 */
	public long getTransactionId() {
		return transactionId;
	}

	/**
	 * @param transactionId
	 *            the transactionId to set
	 */
	public void setTransactionId(long transactionId) {
		this.transactionId = transactionId;
	}

	public boolean equals(TransactionDetailByTaxItem tt) {

		if (this.taxAgencyName.equals(tt.taxAgencyName)
				&& this.taxItemName.equals(tt.taxItemName)
				&& DecimalUtil.isEquals(this.rate, tt.rate)
				&& this.transactionType == tt.transactionType
				&& this.number == tt.number && this.memo.equals(tt.memo)
				&& DecimalUtil.isEquals(this.taxableAmount, tt.taxableAmount)
				&& DecimalUtil.isEquals(this.salesTaxAmount, tt.salesTaxAmount)
				&& this.isVoid == tt.isVoid
				&& this.transactionId == tt.transactionId)
			return true;
		return false;
	}

}
