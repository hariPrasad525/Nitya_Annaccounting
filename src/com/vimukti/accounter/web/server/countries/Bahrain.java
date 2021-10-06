package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Bahrain extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Capital Governorate",
				"Central Governorate", "Muharraq Governorate",
				"Northern Governorate", "Southern Governorate" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BHD";
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
		return "UTC+3:00 Asia/Bahrain";
	}

}
