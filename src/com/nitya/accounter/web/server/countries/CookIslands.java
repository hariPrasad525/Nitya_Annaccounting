package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class CookIslands extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "NZD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Aitutaki", "Atiu", "Mangaia", "Manihiki",
				"Mauke", "Mitiaro", "Nassau", "Pukapuka", "Rakahanga",
				"Rarotonga", "Tongareva" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-10:00 Pacific/Rarotonga";
	}

}
