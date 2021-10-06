package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

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
