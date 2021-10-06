package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class UnitedArabEmirates extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abu Dhabi", "'Ajman", "al-Fujayrah",
				"aš-Šariqah", "Dubai", "Ra's al-H_aymah", "Umm al-Qaywayn" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "AED";
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
		return "UTC+4:00 Asia/Dubai";
	}

}
