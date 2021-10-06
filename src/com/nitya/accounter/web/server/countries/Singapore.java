package com.nitya.accounter.web.server.countries;

import java.util.ArrayList;
import java.util.List;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Singapore extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getPreferredCurrency() {
		return "SGD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.april();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+8:00 Asia/Singapore";
	}

	@Override
	public ArrayList<String> getCompanyFields() {
		List<String> companyFields = new ArrayList<String>();
		companyFields.add("CompanyUEN");
		companyFields.add("GST No");
		return new ArrayList<String>(companyFields);
	}

	@Override
	public ArrayList<String> getCustomerFields() {
		List<String> companyFields = new ArrayList<String>();
		companyFields.add("CompanyUEN");
		companyFields.add("GST No");
		return new ArrayList<String>(companyFields);
	}

	@Override
	public ArrayList<String> getVendorFields() {
		List<String> companyFields = new ArrayList<String>();
		companyFields.add("CompanyUEN");
		companyFields.add("GST No");
		return new ArrayList<String>(companyFields);
	}

}
