package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Australia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Australian Capital Territory",
				"New South Wales", "Northern Territory", "Queensland",
				"South Australia", "Tasmania", "Victoria", "Western Australia" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "AUD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {

		return DayAndMonthUtil.july();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		if (state.equals("Australian Capital Territory")) {
			return "UTC+10:00 Australia/ACT";
		} else if (state.equals("New South Wales")) {
			return "UTC+10:00 Australia/NSW";
		} else if (state.equals("Northern Territory")) {
			return "UTC+9:30 Australia/Darwin";
		} else if (state.equals("Queensland")) {
			return "UTC+10:00 Australia/Queensland";
		} else if (state.equals("South Australia")) {
			return "UTC+9:30 Australia/South";
		} else if (state.equals("Tasmania")) {
			return "UTC+10:00 Australia/Tasmania";
		} else if (state.equals("Victoria")) {
			return "UTC+10:00 Australia/Victoria";
		} else if (state.equals("Western Australia")) {
			return "UTC+8:00 Australia/West";
		}
		return "UTC+10:00 Australia/Sydney";
	}
}
