package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Guadeloupe extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Basse-Terre", "Grande-Terre",
				"Îles des Saintes", "La Désirade", "Marie-Galante",
				"Saint Barthélemy", "Saint Martin" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/Guadeloupe";
	}

}
