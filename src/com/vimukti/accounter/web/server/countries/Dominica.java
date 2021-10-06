package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Dominica extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Saint Andrew", "Saint David",
				"Saint George", "Saint John", "Saint Joseph", "Saint Luke",
				"Saint Mark", "Saint Patrick", "Saint Paul", "Saint Peter" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "XCD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.july();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/Dominica";
	}

}
