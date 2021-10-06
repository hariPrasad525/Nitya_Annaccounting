package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Tuvalu extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Funafuti", "Nanumanga", "Nanumea", "Niutao",
				"Nui", "Nukufetau", "Nukulaelae", "Vaitupu" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "AUD";
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
		return "UTC+12:00 Pacific/Funafuti";
	}

}
