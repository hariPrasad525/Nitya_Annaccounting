package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class DominicanRepublic extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Azua", "Baoruco", "Barahona",
				"Dajabon", "Duarte", "Elias Pina", "El Seibo", "Espaillat",
				"Hato Mayor", "Independencia", "La Altagracia", "La Romana",
				"La Vega", "María Trinidad Sanchez", "Monsenor Nouel",
				"Monte Cristi", "Monte Plata", "Pedernales", "Peravia",
				"Puerto Plata", "Salcedo", "Samana", "Sanchez Ramirez",
				"San Cristobal", "San Jose de Ocoa", "San Juan",
				"San Pedro de Macoris", "Santiago", "Santiago Rodriguez",
				"Santo Domingo", "Valverde" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "DOP";
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
		return "UTC-4:00 America/Santo_Domingo";
	}

}
