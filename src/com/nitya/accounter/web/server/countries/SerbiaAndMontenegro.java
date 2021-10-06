package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class SerbiaAndMontenegro extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Central Serbia", "Vojvodina" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "RSD";
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
		return "UTC+1:00 Europe/Belgrade";
	}
}
