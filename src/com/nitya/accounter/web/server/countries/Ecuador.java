package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Ecuador extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Azuay", "Bolivar", "Canar", "Carchi",
				"Chimborazo", "Cotopaxi", "El Oro", "Esmeraldas", "Galapagos",
				"Guayas", "Imbabura", "Loja", "Los Rios", "Manabi",
				"Morona Santiago", "Napo", "Orellana", "Pastaza", "Pichincha",
				"Sucumbios", "Tungurahua", "Zamora Chinchipe" };

		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "ECS";
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
		if (state.equals("Galapagos")) {
			return "UTC-6:00 Pacific/Galapagos";
		} else {
			return "UTC-5:00 America/Guayaquil";
		}
	}

}
