package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Brunei extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String states[] = new String[] { "Belait", "Brunei-Muara", "Temburong",
				"Tutong" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "BND";
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
		return "UTC+8:00 Asia/Brunei";
	}

}
