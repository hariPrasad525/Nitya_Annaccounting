package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Tunisia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "al-Kaf", "al-Mahdiyah",
				"al-Munastir", "al-Qasrayn", "al-Qayrawan", "Aryanah", "Bajah",
				"Bin 'Arus", "Binzart", "Jundubah", "Madaniyin", "Manubah",
				"Nabul", "Qabis", "Qafsah", "Qibili", "Safaqis",
				"Sidi Bu Zayd", "Silyanah", "Susah", "Tatawin", "Tawzar",
				"Tunis", "Zagwan" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TND";
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
		return "UTC+1:00 Africa/Tunis";
	}

}
