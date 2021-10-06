package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Chad extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Batha", "Biltine", "Bourkou-Ennedi-Tibesti",
				"Chari-Baguirmi", "Gu�ra", "Kanem", "Lac", "Logone Occidental",
				"Logone Oriental", "Mayo-K�bbi", "Moyen-Chari", "Ouadda�",
				"Salamat", "Tandjil�" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Africa/Ndjamena";
	}

}
