package com.vimukti.accounter.web.client.core;

import java.util.List;

public class ClientLiveTaxRate implements IAccounterCore {
	private static final long serialVersionUID = 1L;

	public static final String FILING_STATUS_SINGLE = "Single";
	public static final String FILING_STATUS_MARRIED = "Married";
	public static final String FILING_STATUS_MARRIED_HEADOFHOUSE = "HeadOfHouse";
	public static final String FILING_STATUS_MARRIED_SEPARATE = "MarriedSeparate";

	public static final String TAX_TYPE_FEDERAL = "Federal";
	public static final String TAX_TYPE_STATE = "State";

	String name = "";
	String taxFilingStatus = "";
	String taxType;
	String country;
	String year;

	long id;
	long standardDeductions;

	List<ClientLiveTaxRateRange> rates;

	private int version;

	public void setName(String name) {
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	@Override
	public long getID() {
		return id;
	}

	@Override
	public int getVersion() {
		return version;
	}

	@Override
	public void setVersion(int version) {
		this.version = version;
	}


	public String getTaxFilingStatus() {
		return taxFilingStatus;
	}

	public void setTaxFilingStatus(String taxFilingStatus) {
		this.taxFilingStatus = taxFilingStatus;
	}

	public String getTaxType() {
		return taxType;
	}

	public void setTaxType(String taxType) {
		this.taxType = taxType;
	}

	public String getCountry() {
		return country;
	}

	public void setCountry(String country) {
		this.country = country;
	}

	public List<ClientLiveTaxRateRange> getRates() {
		return rates;
	}

	public void setRates(List<ClientLiveTaxRateRange> rates) {
		this.rates = rates;
	}
	
	public long getStandardDeductions() {
		return standardDeductions;
	}

	public void setStandardDeductions(long standardDeductions) {
		this.standardDeductions = standardDeductions;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String year) {
		this.year = year;
	}


	@Override
	public String getDisplayName() {
		return this.getName();
	}

	@Override
	public AccounterCoreType getObjectType() {
		return AccounterCoreType.LIVE_TAX_RATE;
	}

	@Override
	public void setID(long id) {
      this.id = id;
	}

}
