package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class KoreaNorth extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Chagangdo", "Hamgyongbukto",
				"Hamgyongnamdo", "Hwanghaebukto", "Hwanghaenamdo", "Kangwon",
				"Pyonganbukto", "Pyongannamdo", "Pyongyang", "Rason",
				"Yanggang" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "KPW";
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
		return "UTC+9:00 Asia/Pyongyang";
	}

}
