package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class SaintHelena extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Ascension", "Saint Helena", "Tristan da Cunha" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SHP";
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
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Atlantic/St_Helena";
	}

}
