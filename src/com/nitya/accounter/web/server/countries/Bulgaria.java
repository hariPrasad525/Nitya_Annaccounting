package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Bulgaria extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Blagoevgrad", "Burgas", "Dobric",
				"Gabrovo", "Haskovo", "Jambol", "Kardzali", "Kjustendil",
				"Lovec", "Montana", "Pazardzik", "Pernik", "Pleven", "Plovdiv",
				"Razgrad", "Ruse", "Silistra", "Sliven", "Smoljan",
				"Sofija grad", "Sofijska oblast", "Stara Zagora", "Sumen",
				"Targoviste", "Varna", "Veliko Tarnovo", "Vidin", "Vraca" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BGL";
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
		return "UTC+2:00 Europe/Sofia";
	}

}
