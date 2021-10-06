package com.vimukti.accounter.web.server.util;

import java.io.Serializable;
import java.util.ArrayList;

import com.vimukti.accounter.web.client.util.OrganizationType;

public interface ICountryPreferences extends Serializable, Cloneable {
	String[] getStates();

	String getPreferredCurrency();

	String getDefaultTimeZone(String state);

	OrganizationType[] getOrganizationTypes();

	boolean allowFlexibleFiscalYear();

	String getDefaultFiscalYearStartingMonth();

	String getDefaultDateFormat();

	boolean isVatAvailable();

	boolean isVatIdAndCompanyTaxIdSame();

	boolean isSalesTaxAvailable();

	boolean isSalesTaxIdAndCompanyTaxIdSame();

	String getVATName();

	boolean isServiceTaxAvailable();

	boolean isServiceTaxIdAndCompanyTaxIdSame();

	boolean isTDSAvailable();

	boolean isServiceTaxDeductable();

	String getAmountInwords(double amount);

	ArrayList<String> getCompanyFields();

	ArrayList<String> getCustomerFields();

	ArrayList<String> getVendorFields();

	/**
	 * Returns name of that field. Returns null if that field is not required.
	 * 
	 * @param state
	 * @param taxAgencyType
	 * @param fieldIndex
	 * @return
	 */
	public String[] getTaxAgencyFieldName(String state, int taxAgencyType);
}
