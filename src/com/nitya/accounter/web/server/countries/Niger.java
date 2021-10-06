package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Niger extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Agadez", "Diffa", "Dosso", "Maradi",
				"Niamey", "Tahoua", "Tillabery", "Zinder" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "Niger";
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
		return "UTC+1:00 Africa/Niamey";
	}

}
