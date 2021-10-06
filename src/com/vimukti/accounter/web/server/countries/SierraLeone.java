package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class SierraLeone extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Eastern", "Northern", "Southern", "Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SLL";
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
		return "UTC+0:00 Africa/Freetown";
	}

}
