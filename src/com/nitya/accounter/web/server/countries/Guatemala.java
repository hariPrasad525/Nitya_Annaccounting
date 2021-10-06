package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Guatemala extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alta Verapaz", "Baja Verapaz",
				"Chimaltenango", "Chiquimula", "El Progreso", "Escuintla",
				"Guatemala", "Huehuetenango", "Izabal", "Jalapa", "Jutiapa",
				"Peten", "Quezaltenango", "Quiche", "Retalhuleu",
				"Sacatepequez", "San Marcos", "Santa Rosa", "Solola",
				"Suchitepéquez", "Totonicapan", "Zacapa" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "GTQ";
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
		return "UTC-6:00 America/Guatemala";
	}

}
