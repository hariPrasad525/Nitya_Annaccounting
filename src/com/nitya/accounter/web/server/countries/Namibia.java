package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Namibia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Caprivi", "Erongo", "Hardap", "Karas", "Kavango",
				"Khomas", "Kunene", "Ohangwena", "Omaheke", "Omusati",
				"Oshana", "Oshikoto", "Otjozondjupa" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "NAD";
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
		return "UTC+1:00 Africa/Windhoek";
	}

}
