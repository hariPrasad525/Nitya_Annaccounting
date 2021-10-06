package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Romania extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Alba", "Arad", "Arges", "Bacau",
				"Bihor", "Bistrita-Nasaud", "Botosani", "Braila", "Brasov",
				"Bukarest", "Buzau", "Calarasi", "Caras-Severin", "Cluj",
				"Constanta", "Covasna", "Dambovita", "Dolj", "Galati",
				"Giurgiu", "Gorj", "Harghita", "Hunedoara", "Ialomita", "Iasi",
				"Ilfov", "Maramures", "Mehedinti", "Mures", "Neamt", "Olt",
				"Prahova", "Salaj", "Satu Mare", "Sibiu", "Suceava",
				"Teleorman", "Timis", "Tulcea", "Valcea", "Vaslui", "Vrancea" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "RON";
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
		return "UTC+2:00 Europe/Bucharest";
	}

}
