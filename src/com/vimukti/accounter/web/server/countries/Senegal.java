package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Senegal extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Dakar", "Diourbel", "Fatick",
				"Kaolack", "Kolda", "Louga", "Matam", "Saint-Louis",
				"Tambacounda", "Thies", "Ziguinchor" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XOF";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.january();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Africa/Dakar";
	}

}
