package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Germany extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Baden-Wurttemberg", "Bayern",
				"Berlin", "Brandenburg", "Bremen", "Hamburg", "Hessen",
				"Mecklenburg-Vorpommern", "Niedersachsen",
				"Nordrhein-Westfalen", "Rheinland-Pfalz", "Saarland",
				"Sachsen", "Sachsen-Anhalt", "Schleswig-Holstein", "Thuringen" };
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
		return "UTC+1:00 Europe/Berlin";
	}

}
