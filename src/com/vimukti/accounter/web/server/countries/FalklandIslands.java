package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class FalklandIslands extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "FKP";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Falkland Islands", "South Georgia" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 Atlantic/Stanley";
	}

}
