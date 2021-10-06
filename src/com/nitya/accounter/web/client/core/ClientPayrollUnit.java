package com.nitya.accounter.web.client.core;

public class ClientPayrollUnit implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	/**
	 * Symbol of the Payroll Unit
	 */
	private String symbol;

	/**
	 * Formal Name of the Pay Roll Unit
	 */
	private String formalname;

	/**
	 * Number of Decimal Places for the Unit if you want to use the Unit in
	 * fractions
	 */
	private int noofDecimalPlaces;

	private long id;

	/**
	 * @return the symbol
	 */
	public String getSymbol() {
		return symbol;
	}

	/**
	 * @param symbol
	 *            the symbol to set
	 */
	public void setSymbol(String symbol) {
		this.symbol = symbol;
	}

	/**
	 * @return the formalname
	 */
	public String getFormalname() {
		return formalname;
	}

	/**
	 * @param formalname
	 *            the formalname to set
	 */
	public void setFormalname(String formalname) {
		this.formalname = formalname;
	}

	/**
	 * @return the noofDecimalPlaces
	 */
	public int getNoofDecimalPlaces() {
		return noofDecimalPlaces;
	}

	/**
	 * @param noofDecimalPlaces
	 *            the noofDecimalPlaces to set
	 */
	public void setNoofDecimalPlaces(int noofDecimalPlaces) {
		this.noofDecimalPlaces = noofDecimalPlaces;
	}

	@Override
	public int getVersion() {
		// TODO Auto-generated method stub
		return 0;
	}

	@Override
	public void setVersion(int version) {
		// TODO Auto-generated method stub

	}

	@Override
	public String getName() {
		return this.formalname;
	}

	@Override
	public String getDisplayName() {
		return this.formalname;
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.PAYROLL_UNIT;
	}

	@Override
	public void setID(long id) {
		this.id = id;
	}

	@Override
	public long getID() {
		return this.id;
	}

}
