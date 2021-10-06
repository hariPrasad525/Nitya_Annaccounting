package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Oman extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "ad-Dah_iliyah", "al-Batinah",
				"as-Sarqiyah", "az?-Z?ahirah", "Maskat", "Musandam", "Z?ufar" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "OMR";
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
		return "UTC+4:00 Asia/Muscat";
	}

}
