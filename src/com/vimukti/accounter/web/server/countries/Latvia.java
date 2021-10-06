package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Latvia extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Aizkraukles", "Aluksnes", "Balvu",
				"Bauskas", "Cesu", "Daugavpils", "Daugavpils pilseta",
				"Dobeles", "Gulbenes", "Jekabspils", "Jelgava", "Jelgavas",
				"Jurmala pilseta", "Kraslavas", "Kuldigas", "Liepaja",
				"Liepajas", "Limbazu", "Ludzas", "Madonas", "Ogres", "Preilu",
				"Rezekne", "Rezeknes", "Riga", "Rigas", "Saldus", "Talsu",
				"Tukuma", "Valkas", "Valmieras", "Ventspils",
				"Ventspils pilseta" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "LVL";
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
		return "UTC+2:00 Europe/Riga";
	}

}
