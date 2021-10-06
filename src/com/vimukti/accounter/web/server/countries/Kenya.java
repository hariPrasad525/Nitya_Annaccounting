package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Kenya extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Central", "Coast", "Eastern",
				"Nairobi", "North Eastern", "Nyanza", "Rift Valley", "Western" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KES";
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
		return "UTC+3:00 Africa/Nairobi";
	}

}
