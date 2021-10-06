package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class SaintKittsAndNevis extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Christ Church Nichola Town",
				"Saint Anne Sandy Point", "Saint George Basseterre",
				"Saint George Gingerland", "Saint James Windward",
				"Saint John Capesterre", "Saint John Figtree",
				"Saint Mary Cayon", "Saint Paul Capesterre",
				"Saint Paul Charlestown", "Saint Peter Basseterre",
				"Saint Thomas Lowland", "Saint Thomas Middle Island",
				"Trinity Palmetto Point" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XCD";
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
		return "UTC-4:00 America/St_Kitts";
	}

}
