package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Paraguay extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alto Paraguay", "Alto Parana",
				"Amambay", "Asuncion", "Boqueron", "Caaguazu", "Caazapa",
				"Canendiyu", "Central", "Concepcion", "Cordillera", "Guaira",
				"Itapua", "Misiones", "Neembucu", "Paraguari",
				"Presidente Hayes", "San Pedro" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PYG";
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
		return "UTC-4:00 America/Asuncion";
	}

}
