package com.vimukti.accounter.web.server.countries;

import com.vimukti.accounter.web.server.util.AbstractCountryPreferences;

public class Haiti extends AbstractCountryPreferences {

	@Override
	public String getPreferredCurrency() {
		return "HTG";
	}

	@Override
	public String[] getStates() {
		return new String[] { "Artibonite", "Centre", "Grand'Anse", "Nord",
				"Nord-Est", "Nord-Ouest", "Ouest", "Sud", "Sud-Est" };
	}

	@Override
	public String getDefaultTimeZone(String state) {
		return "UTC-5:00 America/Port-au-Prince";
	}

}
