/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.io.Serializable;

import com.google.gwt.user.client.rpc.IsSerializable;

/**
 * @author Prasanna Kumar G
 * 
 */
public class ClientCurrencyRate implements Serializable, IsSerializable {

	private ClientCurrency baseCurrency;
	private ClientCurrency foreignCurrency;
	private double byRate;
	private double sellRate;

	/**
	 * @return the baseCurrency
	 */
	public ClientCurrency getBaseCurrency() {
		return baseCurrency;
	}

	/**
	 * @param baseCurrency
	 *            the baseCurrency to set
	 */
	public void setBaseCurrency(ClientCurrency baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	/**
	 * @return the foreignCurrency
	 */
	public ClientCurrency getForeignCurrency() {
		return foreignCurrency;
	}

	/**
	 * @param foreignCurrency
	 *            the foreignCurrency to set
	 */
	public void setForeignCurrency(ClientCurrency foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}

	/**
	 * @return the byRate
	 */
	public double getByRate() {
		return byRate;
	}

	/**
	 * @param byRate
	 *            the byRate to set
	 */
	public void setByRate(double byRate) {
		this.byRate = byRate;
	}

	/**
	 * @return the sellRate
	 */
	public double getSellRate() {
		return sellRate;
	}

	/**
	 * @param sellRate
	 *            the sellRate to set
	 */
	public void setSellRate(double sellRate) {
		this.sellRate = sellRate;
	}
}
