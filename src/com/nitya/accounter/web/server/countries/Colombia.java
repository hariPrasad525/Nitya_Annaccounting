package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Colombia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Amazonas", "Antioquia", "Arauca",
				"Atlantico", "Bogota", "Bolivar", "Boyaca", "Caldas",
				"Caqueta", "Casanare", "Cauca", "Cesar", "Choco", "Cordoba",
				"Cundinamarca", "Guainia", "Guaviare", "Huila", "La Guajira",
				"Magdalena", "Meta", "Narino", "Norte de Santander",
				"Putumayo", "Quindio", "Risaralda", "San Andres y Providencia",
				"Santander", "Sucre", "Tolima", "Valle del Cauca", "Vaupes",
				"Vichada" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "COP";
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
		return "UTC-5:00 America/Bogota";
	}

}
