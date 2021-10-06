package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class NetherlandsAntilles extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Bonaire", "Curaçao", "Saba", "Sint Eustatius",
				"Sint Maarten" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ANG";
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
		return "UTC-4:00 America/Curacao";
	}
}
