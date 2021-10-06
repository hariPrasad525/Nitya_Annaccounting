package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class PapuaNewGuinea extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Eastern Highlands", "East New Britain",
				"East Sepik", "Enga", "Fly River", "Gulf", "Madang", "Manus",
				"Milne Bay", "Morobe", "National Capital District",
				"New Ireland", "North Solomons", "Oro", "Sandaun", "Simbu",
				"Southern Highlands", "Western Highlands", "West New Britain" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PGK";
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
		return "UTC+10:00 Pacific/Port_Moresby";
	}
}
