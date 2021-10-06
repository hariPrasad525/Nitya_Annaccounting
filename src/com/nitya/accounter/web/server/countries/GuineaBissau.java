package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class GuineaBissau extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "GWP";
		// XAF also..
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bafatá", "Biombo", "Bissau", "Bolama", "Cacheu",
				"Gabú", "Oio", "Quinara", "Tombali" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Africa/Bissau";
	}

}
