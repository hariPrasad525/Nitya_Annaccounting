package com.vimukti.accounter.web.client.core.reports;

import com.vimukti.accounter.web.client.core.ClientFinanceDate;

public class RealisedExchangeLossOrGain extends BaseReport {

	private long transaction;

	private int transactionType;

	private ClientFinanceDate transactionDate;

	private String payeeName;

	private String currency;

	private double exchangeRate;

	private double realisedLossOrGain;

	/**
	 * @return the transaction
	 */
	public long getTransaction() {
		return transaction;
	}

	/**
	 * @param transaction
	 *            the transaction to set
	 */
	public void setTransaction(long transaction) {
		this.transaction = transaction;
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
	 * @return the currency
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * @param currency
	 *            the currency to set
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	/**
	 * @return the exchangeRate
	 */
	public double getExchangeRate() {
		return exchangeRate;
	}

	/**
	 * @param exchangeRate
	 *            the exchangeRate to set
	 */
	public void setExchangeRate(double exchangeRate) {
		this.exchangeRate = exchangeRate;
	}

	/**
	 * @return the realisedLossOrGain
	 */
	public double getRealisedLossOrGain() {
		return realisedLossOrGain;
	}

	/**
	 * @param realisedLossOrGain
	 *            the realisedLossOrGain to set
	 */
	public void setRealisedLossOrGain(double realisedLossOrGain) {
		this.realisedLossOrGain = realisedLossOrGain;
	}
}
