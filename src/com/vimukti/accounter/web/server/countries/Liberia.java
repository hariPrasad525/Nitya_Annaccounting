package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Liberia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bomi", "Bong", "Grand Bassa",
				"Grand Cape Mount", "Grand Gedeh", "Maryland and Grand Kru",
				"Montserrado", "Nimba" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "LRD";
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
		return "UTC+0:00 Africa/Monrovia";
	}

}
