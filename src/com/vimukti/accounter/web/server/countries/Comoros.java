package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

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
