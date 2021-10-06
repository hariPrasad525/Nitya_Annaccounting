package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class MicronesiaFederated extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Chuuk", "Kusaie", "Pohnpei", "Yap" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "USD";
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
		if (state.equals("Chuuk") || state.equals("Yap")) {
			return "UTC+10:00 Pacific/Chuuk";
		} else if (state.equals("Pohnpei")) {
			return "UTC+11:00 Pacific/Pohnpei";
		} else {
			return "UTC+11:00 Pacific/Kosrae";
		}
	}

}
