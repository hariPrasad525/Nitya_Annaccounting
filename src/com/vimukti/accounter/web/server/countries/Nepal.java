package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Nepal extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		return null;
	}

	@Override
	public String getPreferredCurrency() {
		return "NPR";
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+5:45 Asia/Kathmandu";
	}
}
