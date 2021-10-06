package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Kiribati extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "AUD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Abaiang", "Abemana", "Aranuka", "Arorae",
				"Banaba", "Beru", "Butaritari", "Kiritimati", "Kuria",
				"Maiana", "Makin", "Marakei", "Nikunau", "Nonouti", "Onotoa",
				"Phoenix Islands", "Tabiteuea North", "Tabiteuea South",
				"Tabuaeran", "Tamana", "Tarawa North", "Tarawa South",
				"Teraina" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// TODO
		// Pacific/Tarawa Gilbert Islands
		// Pacific/Enderbury Phoenix Islands
		// Pacific/Kiritimati Line Islands
		return "UTC+12:00 Pacific/Tarawa";
	}

}
