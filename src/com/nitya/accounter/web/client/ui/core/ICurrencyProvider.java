package com.nitya.accounter.web.client.ui.core;

import com.nitya.accounter.web.client.core.ClientCurrency;

public interface ICurrencyProvider {

	/**
	 * This should return the given amount in the currency of the transaction.
	 * If your base currency is USD and Transaction Currency is INR Then passing
	 * 1.0 will return 50.0.
	 * 
	 * @param amount
	 * @return
	 */
//	public Double getAmountInTransactionCurrency(Double amount);

	/**
	 * This should return the given amount in the base currency. If your base
	 * currency is INR and Transaction Currency is USD Then passing 1.0 will
	 * return 50.0.
	 * 
	 * @param amount
	 * @return
	 */
	public Double getAmountInBaseCurrency(Double amount);

	public ClientCurrency getTransactionCurrency();

	public Double getCurrencyFactor();
}
