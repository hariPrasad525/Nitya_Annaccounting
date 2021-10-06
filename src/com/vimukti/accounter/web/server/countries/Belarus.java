package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Belarus extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Christ Church", "Saint Andrew",
				"Saint George", "Saint James", "Saint John", "Saint Joseph",
				"Saint Lucy", "Saint Michael", "Saint Peter", "Saint Philip",
				"Saint Thomas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BYR";
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
		return "UTC+2:00 Europe/Minsk";
	}

}
