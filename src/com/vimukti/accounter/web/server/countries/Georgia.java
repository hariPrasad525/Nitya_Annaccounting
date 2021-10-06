package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Georgia extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "GEL";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Abkhasia", "Ajaria", "Guria", "Imereti",
				"Kakheti", "Kvemo Kartli", "Mtskheta-Mtianeti",
				"Raga-Lechkumi and Kverno Svaneti",
				"Samagrelo and Zemo Svaneti", "Samtskhe-Javakheti",
				"Shida Kartli", "Tiflis" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+4:00 Asia/Tbilisi";
	}

}
