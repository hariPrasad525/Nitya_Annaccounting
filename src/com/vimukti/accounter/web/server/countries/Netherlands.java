package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Netherlands extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Drenthe", "Flevoland", "Friesland",
				"Gelderland", "Groningen", "Limburg", "Noord-Brabant",
				"Noord-Holland", "Overijssel", "Utrecht", "Zeeland",
				"Zuid-Holland" };
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
		return "UTC+1:00 Europe/Amsterdam";
	}

	@Override
	public boolean isVatAvailable() {
		return true;
	}
}
