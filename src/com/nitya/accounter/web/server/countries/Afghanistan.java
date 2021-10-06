package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Afghanistan extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		return new String[] { "Badah_san", "Badgis", "Baglan", "Balh_",
				"Bamiyan", "Farah", "Faryab", "Gawr", "Gazni", "H_awst",
				"Herat", "Hilmand", "Jawzjan", "Kabul", "Kandahar", "Kapisa",
				"Kunarha", "Kunduz", "Lagman", "Lawgar", "Maydan-Wardak",
				"Nangarhar", "Nimruz", "Nuristan", "Paktika", "Paktiya",
				"Parwan", "Samangan", "Sar-e Pul", "Tah_ar", "Uruzgan", "Zabul" };
	}

	@Override
	public String getPreferredCurrency() {
		return "AFN";
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
		return "UTC+4:30 Asia/Kabul";
	}

}
