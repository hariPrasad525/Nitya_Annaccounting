package com.vimukti.accounter.web.client.core.reports;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;
import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class VATDetail extends BaseReport implements IsSerializable,
		Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	String boxName;

	String transactionName;

	int transactionType;

	ClientFinanceDate transactionDate;

	String transactionNumber;

	String payeeName;

	double vatRate;

	double netAmount;

	double total;

	int boxNumber;

	long transactionId;

	boolean isPercentage;

	/**
	 * @return the boxName
	 */
	public String getBoxName() {
		return boxName;
	}

	/**
	 * @param boxName
	 *            the boxName to set
	 */
	public void setBoxName(String boxName) {
		this.boxName = boxName;
	}

	/**
	 * @return the boxNumber
	 */
	public int getBoxNumber() {
		return boxNumber;
	}

	/**
	 * @param boxNumber
	 *            the boxNumber to set
	 */
	public void setBoxNumber(int boxNumber) {
		this.boxNumber = boxNumber;
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

	/**
	 * @return the transactionName
	 */
	public String getTransactionName() {
		return transactionName;
	}

	/**
	 * @param transactionName
	 *            the transactionName to set
	 */
	public void setTransactionName(String transactionName) {
		this.transactionName = transactionName;
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
	 * @return the transactionDate
	 */
	public ClientFinanceDate getTransactionDate() {
		return transactionDate;
	}

	/**
	 * @param transactionDate
	 *            the transactionDate to set
	 */
	public void setTransactionDate(ClientFinanceDate transactionDate) {
		this.transactionDate = transactionDate;
	}

	/**
	 * @return the transactionNumber
	 */
	public String getTransactionNumber() {
		return transactionNumber;
	}

	/**
	 * @param transactionNumber
	 *            the transactionNumber to set
	 */
	public void setTransactionNumber(String transactionNumber) {
		this.transactionNumber = transactionNumber;
	}

	/**
	 * @return the payeeName
	 */
	public String getPayeeName() {
		return payeeName;
	}

	/**
	 * @param payeeName
	 *            the payeeName to set
	 */
	public void setPayeeName(String payeeName) {
		this.payeeName = payeeName;
	}

	/**
	 * @return the netAmount
	 */
	public double getNetAmount() {
		return netAmount;
	}

	/**
	 * @param netAmount
	 *            the netAmount to set
	 */
	public void setNetAmount(double netAmount) {
		this.netAmount = netAmount;
	}

	/**
	 * @return the total
	 */
	public double getTotal() {
		return total;
	}

	/**
	 * @param total
	 *            the total to set
	 */
	public void setTotal(double total) {
		this.total = total;
	}

	/**
	 * @return the vatRate
	 */
	public double getVatRate() {
		return vatRate;
	}

	/**
	 * @param vatRate
	 *            the vatRate to set
	 */
	public void setVatRate(double vatRate) {
		this.vatRate = vatRate;
	}

	/**
	 * @return the isPercentage
	 */
	public boolean isPercentage() {
		return isPercentage;
	}

	/**
	 * @param isPercentage
	 *            the isPercentage to set
	 */
	public void setPercentage(boolean isPercentage) {
		this.isPercentage = isPercentage;
	}

}
