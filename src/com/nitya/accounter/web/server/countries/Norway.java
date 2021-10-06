package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Norway extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Akershus", "Aust-Agder", "Buskerud",
				"Finnmark", "Hedmark", "Hordaland", "More og Romsdal",
				"Nordland", "Nord-Trondelag", "Oppland", "Oslo", "Ostfold",
				"Rogaland", "Sogn og Fjordane", "Sor-Trondelag", "Telemark",
				"Troms", "Vest-Agder", "Vestfold" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "NOK";
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
		return "UTC+1:00 Europe/Oslo";
	}

}
