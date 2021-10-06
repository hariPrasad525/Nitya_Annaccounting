package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Vanuatu extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "VUV";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Malampa", "Penama", "Sanma", "Shefa", "Tafea",
				"Torba" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+11:00 Pacific/Efate";
	}

}
