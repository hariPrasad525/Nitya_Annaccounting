package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class NewCaledonia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "�les", "Nord", "Sud" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XPF";
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
		return "UTC+11:00 Pacific/Noumea";
	}
}
