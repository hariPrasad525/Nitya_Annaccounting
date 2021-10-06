package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Argentina extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Buenos Aires", "Catamarca", "Chaco",
				"Chubut", "Córdoba", "Corrientes", "Distrito Federal",
				"Entre Ríos", "Formosa", "Jujuy", "La Pampa", "La Rioja",
				"Mendoza", "Misiones", "Neuquén", "Río Negro", "Salta",
				"San Juan", "San Luis", "Santa Cruz", "Santa Fé",
				"Santiago del Estero", "Tierra del Fuego", "Tucumán" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ARS";
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
		if (state.equals("Buenos Aires")) {
			return "UTC-3:00 America/Argentina/Buenos_Aires";
		} else if (state.equals("Salta")) {
			return "UTC-3:00 America/Argentina/Salta";
		} else if (state.equals("Jujuy")) {
			return "UTC-3:00 America/Argentina/Jujuy";
		} else if (state.equals("Tucumán")) {
			return "UTC-3:00 America/Argentina/Tucuman";
		} else if (state.equals("Chubut") || state.equals("Catamarca")) {
			return "UTC-3:00 America/Argentina/Catamarca";
		} else if (state.equals("La Rioja")) {
			return "UTC-3:00 America/Argentina/La_Rioja";
		} else if (state.equals("San Juan")) {
			return "UTC-3:00 America/Argentina/San_Juan";
		} else if (state.equals("Mendoza")) {
			return "UTC-3:00 America/Argentina/Mendoza";
		} else if (state.equals("San Luis")) {
			return "UTC-4:00 America/Argentina/San_Luis";
		} else if (state.equals("Santa Cruz")) {
			return "UTC-3:00 America/Argentina/Rio_Gallegos";
		} else {
			return "UTC-3:00 America/Argentina/Cordoba";
		}
	}

}
