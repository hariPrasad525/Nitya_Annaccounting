package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class France extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alsace", "Aquitaine", "Auvergne",
				"Basse-Normandie", "Bourgogne", "Bretagne", "Centre",
				"Champagne-Ardenne", "Corse", "Franche-Comte",
				"Haute-Normandie", "Ile-de-France", "Languedoc-Roussillon",
				"Limousin", "Lorraine", "Midi-Pyrenees", "Nord-Pas-de-Calais",
				"Pays-de-la-Loire", "Picardie", "Poitou-Charentes",
				"Provence-Alpes-Cote-d'Azur", "Rhone-Alpes" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "EUR";
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
		return "UTC+1:00 Europe/Paris";
	}

}
