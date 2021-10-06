/**
 * 
 */
package com.nitya.accounter.web.client.core;


public class ClientCurrency implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	int version;

	long id;

	String name;

	String formalName;

	String symbol;

	/**
	 * Accounts Receivable Account for this Currency
	 */
	private long accountsReceivable;

	/**
	 * Accounts Payable Account for this Currency
	 */
	private long accountsPayable;

	public ClientCurrency(String name, String formalName, String symbol) {
		this.name = name;
		this.formalName = formalName;
		this.symbol = symbol;
	}

	public ClientCurrency() {
		// TODO Auto-generated constructor stub
	}

	/*
	 * String countryName;
	 * 
	 * public String getCountryName() { return countryName; }
	 * 
	 * public void setCountryName(String countryName) { this.countryName =
	 * countryName; }
	 */

	public String getSymbol() {
		return symbol;
	}

	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the version
	 */
	public int getVersion() {
		return version;
	}

	/**
	 * @param version
	 *            the version to set
	 */
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the id
	 */

	/**
	 * @param id
	 *            the id to set
	 */

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the formalName
	 */
	public String getFormalName() {
		return formalName;
	}

	/**
	 * @param formalName
	 *            the formalName to set
	 */
	public void setFormalName(String formalName) {
		this.formalName = formalName;
	}

	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public AccounterCoreType getObjectType() {

		return AccounterCoreType.CURRENCY;
	}

	@Override
	public long getID() {
		return this.id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public ClientCurrency clone() {
		ClientCurrency currency = (ClientCurrency) this.clone();
		return currency;

	}

	@Override
	public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof ClientCurrency) {
			ClientCurrency currency = (ClientCurrency) obj;
			return currency.getFormalName().endsWith(this.formalName)
					&& currency.getName().equals(this.name);
			// && currency.getCountryName().endsWith(this.countryName);
		}
		return false;
	}

	/**
	 * @return the accountsReceivable
	 */
	public long getAccountsReceivable() {
		return accountsReceivable;
	}

	/**
	 * @param accountsReceivable
	 *            the accountsReceivable to set
	 */
	public void setAccountsReceivable(long accountsReceivable) {
		this.accountsReceivable = accountsReceivable;
	}

	/**
	 * @return the accountsPayable
	 */
	public long getAccountsPayable() {
		return accountsPayable;
	}

	/**
	 * @param accountsPayable
	 *            the accountsPayable to set
	 */
	public void setAccountsPayable(long accountsPayable) {
		this.accountsPayable = accountsPayable;
	}
}
