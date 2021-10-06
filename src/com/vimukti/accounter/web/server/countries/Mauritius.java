package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Mauritius extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Black River", "Flacq", "Grand Port",
				"Moka", "Pamplemousses", "Plaines Wilhelm",
				"Riviere du Rempart", "Rodrigues", "Savanne" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MUR";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return null;
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+4:00 Indian/Mauritius";
	}

}
