package com.vimukti.accounter.web.client.core;

import java.io.Serializable;
import java.util.ArrayList;

import com.google.gwt.user.client.rpc.IsSerializable;

public class CountryPreferences implements Serializable, IsSerializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public boolean isServiceTaxAvailable = false;
	private ArrayList<String> customerFields;
	private String preferredCurrency;
	private ArrayList<String> vendorFields;
	private boolean isTDSAvailable;
	private boolean isVatAvailable;
	private boolean isSalesTaxAvailable;
	private ArrayList<String> companyFields;
	private String[] states;
	private String defaultTimeZone;
	private String defaultFiscalYearStartingMonth;

	public ArrayList<String> getCustomerFields() {
		return customerFields;
	}

	public void setCustomerFields(ArrayList<String> customerFields) {
		this.customerFields = customerFields;
	}

	public String getPreferredCurrency() {
		return this.preferredCurrency;
	}

	public void setPreferredCurrency(String preferredCurrency) {
		this.preferredCurrency = preferredCurrency;
	}

	public ArrayList<String> getVendorFields() {
		return this.vendorFields;
	}

	public void setVendorFields(ArrayList<String> vendorFileds) {
		this.vendorFields = vendorFileds;
	}

	public boolean isTDSAvailable() {
		return this.isTDSAvailable;
	}

	public void setTDSAvailable(boolean isTDSAvailable) {
		this.isTDSAvailable = isTDSAvailable;
	}

	public boolean isVatAvailable() {
		return this.isVatAvailable;
	}

	public void setVatAvailable(boolean isVatAvailable) {
		this.isVatAvailable = isVatAvailable;
	}

	public boolean isSalesTaxAvailable() {
		return this.isSalesTaxAvailable;
	}

	public void setSalesTaxAvailable(boolean isSalesTaxAvailable) {
		this.isSalesTaxAvailable = isSalesTaxAvailable;
	}

	public boolean isServiceTaxAvailable() {
		return this.isServiceTaxAvailable;
	}

	public void setServiceTaxAvailable(boolean isServiceTaxAvailable) {
		this.isServiceTaxAvailable = isServiceTaxAvailable;
	}

	public ArrayList<String> getCompanyFields() {
		return this.companyFields;
	}

	public void setCompanyFields(ArrayList<String> companyFields) {
		this.companyFields = companyFields;
	}

	public String[] getStates() {
		return this.states;
	}

	public void setStates(String[] states) {
		this.states = states;
	}

	public String getDefaultTimeZone(String selectedState) {
		return this.defaultTimeZone;
	}

	public void setDefaultTimeZone(String defaultTimeZone) {
		this.defaultTimeZone = defaultTimeZone;
	}

	public String getDefaultFiscalYearStartingMonth() {
		return this.defaultFiscalYearStartingMonth;
	}

	public void setDefaultFiscalYearStartingMonth(
			String defaultFiscalYearStartingMonth) {
		this.defaultFiscalYearStartingMonth = defaultFiscalYearStartingMonth;
	}
}
