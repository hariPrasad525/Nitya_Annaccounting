package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Gabon extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Estuaire", "Haut-Ogooué", "Moyen-Ogooué",
				"Ngounié", "Nyanga", "Ogooué-Ivindo", "Ogooué-Lolo",
				"Ogooué-Maritime", "Woleu-Ntem" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Africa/Libreville";
	}

}
