package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class SriLanka extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amparai", "Anuradhapuraya",
				"Badulla", "Colombo", "Galla", "Gampaha", "Hambantota",
				"Kalatura", "Kegalla", "Kilinochchi", "Kurunegala",
				"Mad?akalpuwa", "Maha Nuwara", "Mannarama", "Matale", "Matara",
				"Monaragala", "Nuwara Eliya", "Puttalama", "Ratnapuraya",
				"Tirikunamalaya", "Vavuniyawa", "Yapanaya" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "LKR";
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
		return "UTC+5:30 Asia/Colombo";
	}

}
