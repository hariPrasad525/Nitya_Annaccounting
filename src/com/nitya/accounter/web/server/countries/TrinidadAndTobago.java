package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class TrinidadAndTobago extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Arima", "Chaguanas",
				"Couva-Tabaquite-Talparo", "Diego Martín", "Mayaro-Río Claro",
				"Peñal Débé", "Point Fortín", "Port of Spain", "Princes Town",
				"San Fernando", "Sangre Grande", "San Juan-Laventville",
				"Siparia", "Tobago", "Tunapuna-Piarco" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TTD";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.october();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-4:00 America/Port_of_Spain";
	}

}
