package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Uzbekistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Andijon", "Buhoro", "Cizah",
				"Fargona", "Horazm", "Kaskadarya", "Korakalpogiston",
				"Namangan", "Navoi", "Samarkand", "Sirdare", "Surhondar",
				"Taschkent" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "UZS";
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
		// Asia/Samarkand west Uzbekistan
		// Asia/Tashkent east Uzbekistan
		// "UTC+5:00 Asia/Samarkand"
		return "UTC+5:00 Asia/Tashkent";
	}

}
