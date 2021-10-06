package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Chile extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aisen", "Antofagasta", "Araucania",
				"Atacama", "Bio Bio", "Coquimbo",
				"Libertador General Bernardo O'Higgins", "Los Lagos",
				"Magellanes", "Maule", "Metropolitana", "Tarapaca",
				"Valparaiso" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "CLP";
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
		return "UTC-4:00 America/Santiago";
	}

}
