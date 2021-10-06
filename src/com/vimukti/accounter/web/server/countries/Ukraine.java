package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Ukraine extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "UAH";
		// UAK also..
	}

	@Override
	public String[] getStates() {
		return new String[] { "Central", "Eastern", "Northern", "Western" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+2:00 Europe/Kiev";
	}

}
