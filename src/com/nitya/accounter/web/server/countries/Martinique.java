package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Martinique extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Fort-de-France", "La Trinité", "Le Marin",
				"Saint-Pierre" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/Martinique";
	}

}
