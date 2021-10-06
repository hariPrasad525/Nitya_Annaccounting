package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Cuba extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "CUP";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Camag�ey", "Ciego de �vila", "Cienfuegos",
				"Ciudad de la Habana", "Granma", "Guant�namo", "Holgu�n",
				"Isla de la Juventud", "La Habana", "Las Tunas", "Matanzas",
				"Pinar del R�o", "Sancti Sp�ritus", "Santiago de Cuba",
				"Villa Clara" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-5:00 America/Havana";
	}

}
