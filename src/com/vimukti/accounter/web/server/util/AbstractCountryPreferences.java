package com.vimukti.accounter.web.server.util;

import java.util.ArrayList;

import com.vimukti.accounter.web.client.util.OrganizationType;

public abstract class AbstractCountryPreferences implements ICountryPreferences {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public OrganizationType[] getOrganizationTypes() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDefaultDateFormat() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isVatAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isVatIdAndCompanyTaxIdSame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSalesTaxAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isSalesTaxIdAndCompanyTaxIdSame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getVATName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public boolean isServiceTaxAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isServiceTaxIdAndCompanyTaxIdSame() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isTDSAvailable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public boolean isServiceTaxDeductable() {
		// TODO Auto-generated method stub
		return false;
	}

	@Override
	public String getAmountInwords(double amount) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public ArrayList<String> getCompanyFields() {
		return new ArrayList<String>();

	}

	@Override
	public ArrayList<String> getCustomerFields() {
		return new ArrayList<String>();

	}

	@Override
	public ArrayList<String> getVendorFields() {
		return new ArrayList<String>();

	}

	@Override
	public String[] getTaxAgencyFieldName(String state, int taxAgencyType) {
		return null;
	}
}
