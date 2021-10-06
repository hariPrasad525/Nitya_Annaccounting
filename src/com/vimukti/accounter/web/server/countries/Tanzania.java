package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Tanzania extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Arusha", "Dar es Salaam", "Dodoma",
				"Iringa", "Kagera", "Kigoma", "Kilimanjaro", "Lindi",
				"Manyara", "Mara", "Mbeya", "Morogoro", "Mtwara", "Mwanza",
				"Pwani", "Rukwa", "Ruvuma", "Shinyanga", "Singida", "Tabora",
				"Tanga", "Zanzibar and Pemba" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "TZS";
	}

	@Override
	public boolean allowFlexibleFiscalYear() {
		return true;
	}

	@Override
	public String getDefaultFiscalYearStartingMonth() {
		return DayAndMonthUtil.july();
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+3:00 Africa/Dar_es_Salaam";
	}

}
