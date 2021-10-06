package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Mauritania extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "MRO";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Adrar", "Assaba", "Brakna", "Ðah_lat Nawadibu",
				"Guidimagha", "Gurgul", "Hud-al-Garbi", "Hud-aš-Šarqi",
				"Inširi", "Nawakšut", "Takant", "Tiris Zammur", "Trarza" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Africa/Nouakchott";
	}

}
