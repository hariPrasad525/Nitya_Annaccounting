package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Tonga extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Eua", "Ha'apai", "Niuas", "Tongatapu", "Vava'u" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TOP";
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
		return "UTC+13:00 Pacific/Tongatapu";
	}

}
