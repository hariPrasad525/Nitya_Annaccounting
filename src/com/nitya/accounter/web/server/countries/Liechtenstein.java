package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Liechtenstein extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "CHF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Balzers", "Eschen", "Gamprin", "Mauren",
				"Planken", "Ruggell", "Schaan", "Schellenberg", "Triesen",
				"Triesenberg", "Vaduz", };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Europe/Vaduz";
	}

}
