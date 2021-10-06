package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Italy extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Abruzzen", "Apulien", "Basilicata",
				"Calabria", "Campania", "Emilia-Romagna",
				"Friuli-Venezia Giulia", "Lazio", "Ligurien", "Lombardei",
				"Marken", "Molise", "Piemonte", "Sardinien", "Sizilien",
				"Toscana", "Trentino-Alto Adige", "Umbria", "Valle d'Aosta",
				"Veneto" };
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
		return "UTC+1:00 Europe/Rome";
	}

}
