package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Yemen extends AbstractCountryPreferences {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abyan", "Ad-Dali'", "Aden",
				"Al-Bayda", "Al-Hudaydah", "Al-Jawf", "Al-Mahrah", "Al-Mahwit",
				"Amanah al-'Asmah", "Amran", "Hadramaut", "Hajjah", "Ibb",
				"Lahij", "Ma'rib", "Raymah", "Sa'dah", "San'a", "Ta'izz" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "YER";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return false;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public boolean isVatAvailable() {
		return false;
	}

	@Override
	public boolean isSalesTaxAvailable() {
		return true;
	}

	@Override
	public boolean isServiceTaxAvailable() {
		return false;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+3:00 Asia/Aden";
	}

}
