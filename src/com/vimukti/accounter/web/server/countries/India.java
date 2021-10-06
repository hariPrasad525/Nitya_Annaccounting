package com.vimukti.accounter.web.server.countries;

import java.util.ArrayList;
import java.util.List;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class India extends AbstractCountryPreferences {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Andaman and Nicobar Islands",
				"Andhra Pradesh", "Arunachal Pradesh", "Assam", "Bangla",
				"Bihar", "Chandigarh", "Chhattisgarh",
				"Dadra and Nagar Haveli", "Daman and Diu", "Delhi", "Goa",
				"Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir",
				"Jharkhand", "Karnataka", "Kerala", "Lakshadweep",
				"Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya",
				"Mizoram", "Nagaland", "Orissa", "Pondicherry", "Punjab",
				"Rajasthan", "Sikkim", "Tamil Nadu", "Tripura", "Uttaranchal",
				"Uttar Pradesh" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "INR";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.april();
	}

	@Override
	public boolean isServiceTaxIdAndCompanyTaxIdSame() {
		return false;
	}

	@Override
	public String getDefaultDateFormat() {
		return super.getDefaultDateFormat();
	}

	@Override
	public boolean isVatAvailable() {
		return true;
	}

	@Override
	public boolean isVatIdAndCompanyTaxIdSame() {
		return false;
	}

	@Override
	public boolean isSalesTaxAvailable() {
		return true;
	}

	@Override
	public boolean isSalesTaxIdAndCompanyTaxIdSame() {
		return false;
	}

	@Override
	public String getVATName() {
		return "VAT";
	}

	@Override
	public boolean isServiceTaxAvailable() {
		return true;
	}

	@Override
	public boolean isTDSAvailable() {
		return true;
	}

	@Override
	public boolean isServiceTaxDeductable() {
		return true;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+5:30 Asia/Kolkata";
	}

	@Override
	public ArrayList<String> getCompanyFields() {
		List<String> companyFields = new ArrayList<String>();
		companyFields.add("TIN");
		companyFields.add("TAN");
		companyFields.add("PAN");
		return new ArrayList<String>(companyFields);
	}
}
