package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Mexico extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aguascalientes", "Baja California",
				"Baja California Sur", "Campeche", "Chiapas", "Chihuahua",
				"Coahuila", "Colima", "Distrito Federal", "Durango",
				"Guanajuato", "Guerrero", "Hidalgo", "Jalisco", "Mexico",
				"Michoacan", "Morelos", "Nayarit", "Nuevo Leon", "Oaxaca",
				"Puebla", "Queretaro", "Quintana Roo", "San Luis Potosi",
				"Sinaloa", "Sonora", "Tabasco", "Tamaulipas", "Tlaxcala",
				"Veracruz", "Yucatan", "Zacatecas" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MXP";
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
		if (state.equals("Baja California")) {
			return "UTC-8:00 Mexico/BajaNorte";
		} else if (state.equals("Baja California Sur")
				|| state.equals("Chihuahua") || state.equals("Nayarit")
				|| state.equals("Sinaloa") || state.equals("Sonora")) {
			return "UTC-7:00 Mexico/BajaSur";
		} else {
			return "UTC-6:00 America/Mexico_City";
		}
	}

}
