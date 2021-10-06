package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Zambia extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "ZMK";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Central", "Copperbelt", "Eastern", "Luapala",
				"Lusaka", "Northern", "North-Western", "Southern", "Western" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+2:00 Africa/Lusaka";
	}

}
