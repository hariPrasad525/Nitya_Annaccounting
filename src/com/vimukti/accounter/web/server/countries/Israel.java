package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Israel extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Hadarom", "Haifa", "Hamerkaz",
				"Haz_afon", "Jerusalem", "Judea and Samaria", "Tel Aviv" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ILS";
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
		return "UTC+2:00 Asia/Jerusalem";
	}

}
