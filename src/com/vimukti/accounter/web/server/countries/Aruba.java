package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Aruba extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "AWG";
	}

	@Override
	public String[] getStates() {
		return new String[] { "" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/Aruba";
	}

}
