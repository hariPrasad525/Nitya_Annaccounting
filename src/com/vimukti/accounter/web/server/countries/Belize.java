package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Belize extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Belize", "Cayo", "Corozal",
				"Orange Walk", "Stann Creek", "Toledo" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BZD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.april();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-6:00 America/Belize";
	}

}
