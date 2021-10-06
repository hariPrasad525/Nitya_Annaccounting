package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Guinea extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "GNS";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Basse Guin�e", "Conakry", "Guin�e Foresti�re",
				"Haute Guin�e", "Moyenne Guin�e" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+0:00 Africa/Conakry";
	}

}
