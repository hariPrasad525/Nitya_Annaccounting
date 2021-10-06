package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Egypt extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "ad-Daqahliyah", "al-Bah?r-al-Ah?mar",
				"al-Buh?ayrah", "Alexandria", "al-Fayyum", "al-Garbiyah",
				"al-Ismailiyah", "al-Minufiyah", "al-Minya", "al-Qalyubiyah",
				"al-Wadi al-Jadid", "as-Sarqiyah", "Assiut", "Assuan",
				"as-Suways", "Bani Suwayf", "Bur Sa'id", "Dumyat", "Giseh",
				"Kafr-as-Sayh_", "Kairo", "Luxor", "Matruh", "Qina",
				"Samal Sina", "Sawhaj", "South Sinai" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EGP";
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
		return "UTC+2:00 Africa/Cairo";
	}

}
