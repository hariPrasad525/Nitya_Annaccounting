package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Ireland extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Carlow", "Cavan", "Clare", "Cork",
				"Donegal", "Dublin", "Galway", "Kerry", "Kildare", "Kilkenny",
				"Laois", "Leitrim", "Limerick", "Longford", "Louth", "Mayo",
				"Meath", "Monaghan", "Offaly", "Roscommon", "Sligo",
				"Tipperary North Riding", "Tipperary South Riding",
				"Waterford", "Westmeath", "Wexford", "Wicklow" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EUR";
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
		return "UTC+0:00 Europe/Dublin";
	}

	@Override
	public boolean isVatAvailable() {
		return true;
	}
}
