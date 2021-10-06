package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Swaziland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Hhohho", "Lubombo", "Manzini", "Shiselweni" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SZL";
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
		return "UTC+2:00 Africa/Mbabane";
	}

}
