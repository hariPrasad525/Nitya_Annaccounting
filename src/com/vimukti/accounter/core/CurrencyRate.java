/**
 * 
 */
package com.vimukti.accounter.core;

/**
 * @author Srikanth.J
 * 
 */
public class CurrencyRate {

	private Currency baseCurrency;
	private Currency foreignCurrency;
	private double byRate;
	private double sellRate;

	public CurrencyRate() {
	}

	public Currency getBaseCurrency() {
		return baseCurrency;
	}

	public void setBaseCurrency(Currency baseCurrency) {
		this.baseCurrency = baseCurrency;
	}

	public Currency getForeignCurrency() {
		return foreignCurrency;
	}

	public void setForeignCurrency(Currency foreignCurrency) {
		this.foreignCurrency = foreignCurrency;
	}

	public double getByRate() {
		return byRate;
	}

	public void setByRate(double byRate) {
		this.byRate = byRate;
	}

	public double getSellRate() {
		return sellRate;
	}

	public void setSellRate(double sellRate) {
		this.sellRate = sellRate;
	}

}
