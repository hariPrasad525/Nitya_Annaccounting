package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class FrenchSouthernAndAntarcticLands extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Amsterdam", "Crozet Islands", "Kerguelen" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+5:00 Indian/Kerguelen";
	}

}
