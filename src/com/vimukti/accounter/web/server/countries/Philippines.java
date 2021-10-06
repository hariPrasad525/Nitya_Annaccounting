package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Philippines extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bicol", "Cagayan Valley", "Caraga",
				"Central Luzon", "Central Mindanao", "Central Visayas",
				"Cordillera", "Eastern Visayas", "Ilocos", "Muslim Mindanao",
				"National Capital Region", "Northern Mindanao",
				"Southern Mindanao", "Southern Tagalog", "Western Mindanao",
				"Western Visayas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "PHP";
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
		return "UTC+8:00 Asia/Manila";
	}

}
