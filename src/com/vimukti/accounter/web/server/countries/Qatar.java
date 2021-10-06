package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Qatar extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "al-Guwayriyah", "al-H_awr",
				"al-Jumayliyah", "al-Wakrah", "ar-Rayyan", "as-Samal", "Doha",
				"Jariyan al-Batnah", "Musay'id", "Umm Salal" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "QAR";
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
		return "UTC+3:00 Asia/Qatar";
	}

}
