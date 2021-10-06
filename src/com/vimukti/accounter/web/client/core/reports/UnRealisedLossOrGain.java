package com.vimukti.accounter.web.client.core.reports;

public class UnRealisedLossOrGain extends BaseReport {

	private String accountName;

	private String currency;

	private double foreignBalance;

	private double exchangeRate;

	private double adjustedBalance;

	private double currentBalance;

	private double lossOrGain;

	/**
	 * @return the accountName
	 */
	public String getAccountName() {
		return accountName;
	}

	/**
	 * @param accountName
	 *            the accountName to set
	 */
	public void setAccountName(String accountName) {
		this.accountName = accountName;
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
	 * @return the accountCurrencyBalance
	 */
	public double getForeignBalance() {
		return foreignBalance;
	}

	/**
	 * @param accountCurrencyBalance
	 *            the accountCurrencyBalance to set
	 */
	public void setForeignBalance(double accountCurrencyBalance) {
		this.foreignBalance = accountCurrencyBalance;
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
	 * @return the adjustedBalance
	 */
	public double getAdjustedBalance() {
		return adjustedBalance;
	}

	/**
	 * @param adjustedBalance
	 *            the adjustedBalance to set
	 */
	public void setAdjustedBalance(double adjustedBalance) {
		this.adjustedBalance = adjustedBalance;
	}

	/**
	 * @return the currentBalance
	 */
	public double getCurrentBalance() {
		return currentBalance;
	}

	/**
	 * @param currentBalance
	 *            the currentBalance to set
	 */
	public void setCurrentBalance(double currentBalance) {
		this.currentBalance = currentBalance;
	}

	/**
	 * @return the lossOrGain
	 */
	public double getLossOrGain() {
		return lossOrGain;
	}

	/**
	 * @param lossOrGain
	 *            the lossOrGain to set
	 */
	public void setLossOrGain(double lossOrGain) {
		this.lossOrGain = lossOrGain;
	}
}
