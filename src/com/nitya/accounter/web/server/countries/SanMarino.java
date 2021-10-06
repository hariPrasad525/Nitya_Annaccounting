package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class SanMarino extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Acquaviva", "Borgo Maggiore", "Chiesanuova",
				"Domagnano", "Faetano", "Fiorentino", "Montegiardino",
				"San Marino", "Serravalle" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EUR";
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
		return "UTC+1:00 Europe/San_Marino";
	}
}
