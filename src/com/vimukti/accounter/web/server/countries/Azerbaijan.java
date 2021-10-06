package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Azerbaijan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abseron", "Aran", "Baki",
				"Dagliq Sirvan", "Ganja-Gazakh", "Kalbajar-Lachin", "Lankaran",
				"Naxcivan", "Quba-Xacmaz", "Shaki-Zaqatala", "Yuxari Qarabag" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "AZM";
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
	public String getDefaultTimeZone(String timeZone) {
		return "UTC+4:00 Asia/Baku";
	}

}
