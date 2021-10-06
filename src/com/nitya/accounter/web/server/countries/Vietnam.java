package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Vietnam extends AbstractCountryPreferences {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getStates() {
		String[] states = new String[] { "North Central Coast", "Mekong Delta",
				"Red River Delta", "Northeastern", "Southeastern",
				"South Central Coast", "Northwestern", "Central Highlands" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "VND";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
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
		return super.isSalesTaxAvailable();
	}

	@Override
	public boolean isSalesTaxIdAndCompanyTaxIdSame() {
		return false;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+7:00 Asia/Ho_Chi_Minh";
	}

}
