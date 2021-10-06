package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Syria extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "al-H?asakah", "al-Ladiqiyah", "al-Qunaytirah",
				"ar-Raqqah", "as-Suwayda", "Damaskus", "Dar'a", "Dayr-az-Zawr",
				"H?alab", "H?amah", "H?ims", "Idlib", "Tartus" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SYP";
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
		return "UTC+2:00 Asia/Damascus";
	}

}
