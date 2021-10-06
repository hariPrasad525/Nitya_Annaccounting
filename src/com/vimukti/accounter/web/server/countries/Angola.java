package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.client.util.DayAndMonthUtil;
import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Angola extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = new String[] { "Bengo", "Benguela", "Bie", "Cabinda",
				"Cuando-Cubango", "Cuanza-Norte", "Cuanza-Sul", "Cunene",
				"Huambo", "Huila", "Luanda", "Lunda Norte", "Lunda Sul",
				"Malanje", "Moxico", "Namibe", "Uige", "Zaire" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "AOK";
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
		return "UTC+1:00 Africa/Luanda";
	}

}
