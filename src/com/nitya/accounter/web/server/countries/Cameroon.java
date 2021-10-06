package com.nitya.accounter.web.server.countries;

import com.nitya.accounter.web.server.util.AbstractCountryPreferences;

public class Cameroon extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "XAF";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Adamaoua", "Centre", "Est", "Littoral", "Nord",
				"Nord Extrème", "Nordouest", "Ouest", "Sud", "Sudouest" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC+1:00 Africa/Douala";
	}

}
