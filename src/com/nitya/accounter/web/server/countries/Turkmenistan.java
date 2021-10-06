package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Turkmenistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Ahal", "Asgabat", "Balkan", "Dasoguz", "Lebap",
				"Mari" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TMT";
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
		return "UTC+5:00 Asia/Ashgabat";
	}

}
