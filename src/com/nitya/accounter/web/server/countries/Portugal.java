package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Portugal extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Acores", "Alentejo", "Algarve",
				"Centro", "Lisboa e Vale do Tejo", "Madeira", "Norte" };
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
		if (state.equals("Madeira")) {
			return "UTC+0:00 Atlantic/Madeira";
		} else if (state.equals("Acores")) {
			return "UTC-1:00 Atlantic/Azores";
		} else {
			return "UTC+0:00 Europe/Lisbon";
		}

	}
}
