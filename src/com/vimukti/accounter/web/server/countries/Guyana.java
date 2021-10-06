package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Guyana extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Barima-Waini", "Cuyuni-Mazaruni",
				"Demerara-Mahaica", "East Berbice-Corentyne",
				"Essequibo Islands-West Demerara", "Mahaica-Berbice",
				"Pomeroon-Supenaam", "Upper Demerara-Berbice",
				"Upper Takutu-Upper Essequibo" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "GYD";
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
		return "UTC-4:00 America/Guyana";
	}

}
