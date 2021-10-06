package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Botswana extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bobonong", "Boteti", "Chobe",
				"Francistown", "Gaborone", "Ghanzi", "Jwaneng",
				"Kgalagadi North", "Kgalagadi South", "Kgatleng", "Kweneng",
				"Lobatse", "Mahalapye", "Ngamiland", "Ngwaketse", "North East",
				"Okavango", "Orapa", "Selibe Phikwe", "Serowe-Palapye",
				"South East", "Sowa", "Tutume" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {

		return "BWP";
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
		return "UTC+2:00 Africa/Gaborone";
	}

}
