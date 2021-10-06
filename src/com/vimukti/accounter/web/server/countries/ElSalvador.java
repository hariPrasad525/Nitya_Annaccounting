package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class ElSalvador extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Ahuachap�n", "Caba�as",
				"Chalatenango", "Cuscatl�n", "La Libertad", "La Paz",
				"La Uni�n", "Moraz�n", "San Miguel", "San Salvador",
				"Santa Ana", "San Vicente", "Sonsonate", "Usulut�n" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SVC";
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
		return "UTC-6:00 America/El_Salvador";
	}

}
