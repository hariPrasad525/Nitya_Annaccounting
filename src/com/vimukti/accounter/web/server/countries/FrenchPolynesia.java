package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class FrenchPolynesia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Iles du Vent", "Iles sous le Vent",
				"Marquises", "Tuamotu-Gambier", "Tubuai" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XPF";
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
		// TODO
		// Pacific/Tahiti Society Islands
		// Pacific/Marquesas Marquesas Islands
		// Pacific/Gambier Gambier Islands
		return "UTC-10:00 Pacific/Tahiti";
	}

}
