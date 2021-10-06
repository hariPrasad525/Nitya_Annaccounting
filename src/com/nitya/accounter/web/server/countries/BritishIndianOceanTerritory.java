package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class BritishIndianOceanTerritory extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "GBR";
		// SCR...
	}

	@Override
	public String[] getStates() {
		return new String[] { "Tortola" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+6:00 Indian/Chagos";
	}

}
