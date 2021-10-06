package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Kyrgyzstan extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "KGS";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Batken", "Biskek", "Celalabad", "Çuy",
				"Issik-Göl", "Narin", "Os", "Talas" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+6:00 Asia/Bishkek";
	}

}
