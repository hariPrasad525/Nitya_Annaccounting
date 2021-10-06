package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Jamaica extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Clarendon", "Hanover", "Kingston",
				"Manchester", "Portland", "Saint Andrew", "Saint Ann",
				"Saint Catherine", "Saint Elizabeth", "Saint James",
				"Saint Mary", "Saint Thomas", "Trelawney", "Westmoreland" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "JMD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.april();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-5:00 America/Jamaica";
	}

}
