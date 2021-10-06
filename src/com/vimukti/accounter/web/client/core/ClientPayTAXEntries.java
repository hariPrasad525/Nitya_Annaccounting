package com.vimukti.accounter.web.client.core;

public class ClientPayTAXEntries implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long vatAgency;

	double amount;

	double balance;

	private long vatReturn;

	private long id;

	private int version;

	private ClientFinanceDate taxReturnDate;

	public long getVatAgency() {
		return vatAgency;
	}

	public void setVatAgency(long vatAgency) {
		this.vatAgency = vatAgency;
	}

	public double getAmount() {
		return amount;
	}

	public void setAmount(double amount) {
		this.amount = amount;
	}

	public double getBalance() {
		return balance;
	}

	public void setBalance(double balance) {
		this.balance = balance;
	}


	@Override
	public String getDisplayName() {
		// its not using any where
		return null;
	}

	@Override
	public String getName() {
		return null;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAYVAT;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public void setID(long id) {
		this.id = id;

	}

	public void setVatReturn(long vatReturn) {
		this.vatReturn = vatReturn;
	}

	public long getVatReturn() {
		return vatReturn;
	}

	public ClientPayTAXEntries clone() {
		ClientPayTAXEntries payVATEntries = (ClientPayTAXEntries) this.clone();
		return payVATEntries;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}

	/**
	 * @return the taxReturnDate
	 */
	public ClientFinanceDate getTaxReturnDate() {
		return taxReturnDate;
	}

	/**
	 * @param taxReturnDate
	 *            the taxReturnDate to set
	 */
	public void setTaxReturnDate(ClientFinanceDate taxReturnDate) {
		this.taxReturnDate = taxReturnDate;
	}

}
