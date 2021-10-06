package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.client.util.DayAndMonthUtil;
import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Rwanda extends AbstractCountryPreferences {

	@Override
	public String[] getStates() {
		String[] states = { "Butare", "Byumba", "Cyangugu", "Gikongoro",
				"Gisenyi", "Gitarama", "Kibungo", "Kibuye", "Ruhengeri",
				"Ville de Kigali" };
		return states;
	}

	@Override
	public String getPreferredCurrency() {
		return "RWF";
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
		return "UTC+2:00 Africa/Kigali";
	}
}
