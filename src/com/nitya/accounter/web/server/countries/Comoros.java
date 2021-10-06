package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Comoros extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "KMF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Mwali", "Ndzouani", "Ngazidja" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+3:00 Indian/Comoro";
	}

}
