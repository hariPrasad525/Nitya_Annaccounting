package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class GambiaThe extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "GMD";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Banjul", "Basse", "Brikama", "Janjanbureh",
				"Kanifing", "Kerewan", "Kuntaur", "Mansakonko" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Africa/Banjul";
	}

}
