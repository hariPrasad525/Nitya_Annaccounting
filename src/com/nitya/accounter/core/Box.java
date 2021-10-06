package com.nitya.accounter.core;

import java.util.ArrayList;
import java.util.List;

/**
 * This class an intermediate class which stores all the static values of the
 * VAT return boxes for both UK and Ireland.
 */

public class Box {

	/**
	 * 
	 */
	private static final long serialVersionUID = 3730671126732195673L;
	long id;
	private Company company;
	int boxNumber;

	String name;
	double amount;

	List<TAXRateCalculation> taxRateCalculations = new ArrayList<TAXRateCalculation>();
	private int version;

	/**
	 * @return the vatRateCalculations
	 */
	public List<TAXRateCalculation> getTaxRateCalculations() {
		return taxRateCalculations;
	}

	/**
	 * @param vatRateCalculations
	 *            the vatRateCalculations to set
	 */
	public void setTaxRateCalculations(
			List<TAXRateCalculation> taxRateCalculations) {
		this.taxRateCalculations = taxRateCalculations;
	}

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

	public long getID() {
		return id;
	}

	public boolean isPositiveBox() {
		return (this.boxNumber == 1);

	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public Company getCompany() {
		return company;
	}

	public void setCompany(Company company) {
		this.company = company;
	}

}
