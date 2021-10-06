package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class SaudiArabia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "al-Bah?ah", "al-H?udud-as-Samaliyah",
				"al-Jawf", "al-Madinah", "al-Qasim", "'Asir", "as-Sarqiyah",
				"H?a'il", "Jizan", "Makkah", "Najran", "Riad", "Tabuk" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "SAR";
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
		return "UTC+3:00 Asia/Riyadh";
	}

}
