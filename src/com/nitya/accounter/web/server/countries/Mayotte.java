package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Mayotte extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "EUR";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Mayotte", "Pamanzi" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+3:00 Indian/Mayotte";
	}

}
