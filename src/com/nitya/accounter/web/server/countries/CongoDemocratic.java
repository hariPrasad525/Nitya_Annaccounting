package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class CongoDemocratic extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "CDZ";
		// formerly ZRZ
	}

	@Override
	public String[] getStates() {
		return new String[] { "Bandundu", "Bas-Congo", "Équateur",
				"Haut-Congo", "Kasai-Occidental", "Kasai-Oriental", "Katanga",
				"Kinshasa", "Maniema", "Nord-Kivu", "Sud-Kivu" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		// West
		return "UTC+1:00 Africa/Kinshasa";
		// East
		// "UTC+2:00 Africa/Lubumbashi"
	}

}
