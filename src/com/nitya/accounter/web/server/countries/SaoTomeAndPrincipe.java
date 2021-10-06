package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class SaoTomeAndPrincipe extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Água Grande", "Cantagalo", "Caué", "Lemba",
				"Lobata", "Mé-Zochi", "Pagué" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "STD";
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
		return "UTC+0:00 Africa/Sao_Tome";
	}
}
