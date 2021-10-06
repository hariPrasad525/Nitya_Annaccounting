/**
 * 
 */
package com.vimukti.accounter.web.client.core;

import java.util.ArrayList;
import java.util.List;

/**
 * @author vimukti5
 * 
 */
public class ClientBox implements IAccounterCore {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	long id;

	String name;
	double amount;

	int boxNumber;
	String description;

	List<ClientTAXRateCalculation> taxRateCalculations = new ArrayList<ClientTAXRateCalculation>();

	private int version;

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
	 * @return the amount
	 */
	public double getAmount() {
		return amount;
	}

	/**
	 * @param amount
	 *            the amount to set
	 */
	public void setAmount(double amount) {
		this.amount = amount;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	/**
	 * 
	 * @return description
	 */
	public String getDesciption() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}


	/*
	 * (non-Javadoc)
	 * 
	 * @see
	 * com.vimukti.accounter.web.client.core.IAccounterCore#getDisplayName()
	 */
	@Override
	public String getDisplayName() {
		return null;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getName()
	 */
	@Override
	public String getName() {
		return name;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getObjectType()
	 */
	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.VATBOX;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#getID()
	 */
	@Override
	public long getID() {
		return this.id;
	}

	/*
	 * (non-Javadoc)
	 * 
	 * @see com.vimukti.accounter.web.client.core.IAccounterCore#setID(java
	 * .lang.String)
	 */
	@Override
	public void setID(long id) {
		this.id = id;

	}

	public List<ClientTAXRateCalculation> getTaxRateCalculations() {
		return taxRateCalculations;
	}

	public void setTaxRateCalculations(
			List<ClientTAXRateCalculation> taxRateCalculations) {
		this.taxRateCalculations = taxRateCalculations;
	}

	public ClientBox clone() {
		ClientBox clientBox = (ClientBox) this.clone();
		List<ClientTAXRateCalculation> taxRateCalculations = new ArrayList<ClientTAXRateCalculation>();
		for (ClientTAXRateCalculation clientTAXRateCalculation : this.taxRateCalculations) {
			taxRateCalculations.add(clientTAXRateCalculation.clone());
		}
		clientBox.taxRateCalculations = taxRateCalculations;
		return clientBox;

	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}
}
