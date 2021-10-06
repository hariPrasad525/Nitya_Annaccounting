package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Macedonia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Berovo", "Bitola", "Brod", "Debar",
				"Delcevo", "Demir Hisar", "Gevgelija", "Gostivar", "Kavadarci",
				"Kicevo", "Kocani", "Kratovo", "Kriva Palanka", "Krusevo",
				"Kumanovo", "Negotino", "Ohrid", "Prilep", "Probistip",
				"Radovis", "Resen", "Skopje", "Stip", "Struga", "Strumica",
				"Sveti Nikole", "Tetovo", "Valandovo", "Veles", "Vinica" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "MKD";
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
		return "UTC+1:00 Europe/Skopje";
	}

}
