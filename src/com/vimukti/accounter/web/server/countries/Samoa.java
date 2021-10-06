package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Samoa extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Apia Urban Area", "North West Upolu",
				"Rest of Upolu", "Savaii" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "WST";
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
		return "UTC-11:00 Pacific/Apia";
	}
}
