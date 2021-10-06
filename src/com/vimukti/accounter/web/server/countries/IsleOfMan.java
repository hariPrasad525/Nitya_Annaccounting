package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class IsleOfMan extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "IMP";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Castletown", "Douglas", "Laxey", "Onchan",
				"Peel", "Port Erin", "Port Saint Mary", "Ramsey" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Europe/Isle_of_Man";
	}

}
