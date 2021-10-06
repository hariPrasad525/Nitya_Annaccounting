package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Spain extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Andalusien", "Aragonien", "Asturien",
				"Balearen", "Baskenland", "Ceuta", "Extremadura", "Galizien",
				"Kanaren", "Kantabrien", "Kastilien-La Mancha",
				"Kastilien-Leon", "Katalonien", "La Rioja", "Madrid",
				"Melilla", "Murcia", "Navarra", "Valencia" };
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

		if (state.equals("Ceuta") || state.equals("Melilla")) {
			return "UTC+1:00 Africa/Ceuta";
		} else {
			return "UTC+1:00 Europe/Madrid";
		}
	}

}
